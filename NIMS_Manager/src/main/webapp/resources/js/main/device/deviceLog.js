$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result : function() {
			this.table = Table.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this, false);
			
			// 장비 검색 로드
			Device.load();
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load();
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				
				let file_name = "이벤트 이력_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	/**
	 * @Desc 장애 이력 관리 테이블
	 */
	const Table = {
		load : function() {
			Search.params.startDate =common.getDateToStr('day', -7);
			Search.params.endDate = common.getDateToStr();

			return this.draw();
		},
		draw : function () {
			const that = this;
			let eventTypeList = null;
			
			AjaxUtil.request({
				url: '/api/device/getDeviceEventType',
				done: function(data) {
					data = data.result;
					eventTypeList = data.items;
				}
			});
			
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxParams: {
					type: Search.params.type,
					startDate: Search.params.startDate,
					endDate: ''
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != "") {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					
					const deviceKey = $('#filter-device #select_device option:selected').val();
					if(deviceKey != '') {
						url += `&device_key=${deviceKey}`;
					}
					
					if(model_auth != '') {
						url += `&model_auth=${model_auth}`;
					}
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0){
						$('.card-title-count').hide();
						table.updateColumnDefinition("library_nm", {headerFilter: false});
						table.updateColumnDefinition("event_type", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("library_nm", {headerFilter: true});
						table.updateColumnDefinition("event_type", {headerFilter: "select"});
					}
					
					return response.result.data;
				},
				
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "발생 일시", field: "event_date", widthGrow: 1, headerSort: false },
					{ title: "도서관 명", field: "library_nm", widthGrow: 1, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "발생 장비", field: "device_nm", widthGrow: 1, headerSort: false },
					{
						title: "이벤트 유형", field: "event_type", widthGrow: 0.75, headerSort: false, headerFilterParams: {"2": "시스템 정보", "0": "시스템 장애", "1": "시스템 경고"}, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const typeValue = cell.getValue();
							const typeInfo = eventTypeList.filter(type => type.code == typeValue)[0];
							
							return `<font class="event-status-${typeValue}">${typeInfo.code_value}</font>`;
						}
					},
					{
						title: "이벤트 내용", field: "event_code_nm", widthGrow: 2, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const cellValue = cell.getValue();
							const typeValue = cell._cell.row.data.event_type;
							
							return `<font class="event-status-${typeValue}">${cellValue}</font>`;
						}
					}
				],
				rowClick: function(e, row) {
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		}
	};
	
	Content.load({
		url: '/api/device/getDeviceEventDetailList'
	});
});