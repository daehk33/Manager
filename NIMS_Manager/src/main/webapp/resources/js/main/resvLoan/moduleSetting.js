$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function() {
			this.table = SettingTable.load();
			this.event();
		},
		event: function() {
			const table = this.table;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			// 등록 버튼 클릭시
			$('button[role="add"]').click(function() {
				const moduleSettingModal= $('#settingModal');
				
				const input = ParamManager.load('insert');
				moduleSettingModal.append(input);
				
				moduleSettingModal.modal('show');
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "모듈 설정 목록_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		},
	};
	
	const SettingTable = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#settingTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
						return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data){
						return `${value}<span>(설정<span class="group-header">${count}</span>개)</span>`;
					}
				] : function(value, count, data){
					return `${value}<span>(설정<span class="group-header">${count}</span>개)</span>`;
				},
				pplaceholder: TableUtil.getPlaceholder('조회된 서가 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.length);
					if(response.result.length == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("module_id", {headerFilter: false});
						table.updateColumnDefinition("cabinet_no", {headerFilter: false});
						table.updateColumnDefinition("direction_nm", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("module_id", {headerFilter: true});
						table.updateColumnDefinition("cabinet_no", {headerFilter: true});
						table.updateColumnDefinition("direction_nm", {headerFilter: "select"});
					}
					return response.result.items;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "모듈 아이디", field: "module_id", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "캐비닛 시작 번호", field: "cabinet_no", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "모듈 위치", field: "direction_nm", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["중앙", "좌측", "우측"], headerFilterPlaceholder: " " },
					{ title: "행", field: "column_cnt", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center" },
					{ title: "열", field: "row_cnt", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center" },
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showSettingDetail(row);
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		},
		showSettingDetail : function(row) {
			const settingModal = $('[id="settingModal"][target="moduleSetting"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			
			settingModal.append(input);
			settingModal.modal('show');
			settingModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	Content.load({
		url: '/api/resvLoan/getResvLoanModuleInfoList'
	});
});