$(function() {
	const Content = {
		params: {},
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function(){
			this.table = Table.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this);
			
			// 장비 검색 로드
			Device.load({model_key: '6'});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "반납 이력_("+Search.params.startDate+").xlsx";
				else file_name = "반납 이력_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load({model_key: '6'});
				table.setData();
			});
		}
	};
	
	const Table = {
		load: function(){
			Search.params.type = "day";
			Search.params.startDate = common.getDateToStr('month');
			
			return this.draw();
		},
		draw: function(){
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<div class="group-header" style="display: inline">${count}</div> 건)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
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
				ajaxParams: {
					type: Search.params.type,
					startDate: Search.params.startDate,
					endDate: ''
				},
				ajaxURLGenerator: function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					
					const deviceKey = $('#filter-device #select_device option:selected').val();
					if(deviceKey != '') {
						url += `&device_key=${deviceKey}`;
					}
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("user_id", {headerFilter: false});
						table.updateColumnDefinition("user_name", {headerFilter: false});
						table.updateColumnDefinition("book_no", {headerFilter: false});
						table.updateColumnDefinition("title", {headerFilter: false});
						table.updateColumnDefinition("state", {headerFilter: false});
					}
					else { 
						$('.card-title-count').show();
						table.updateColumnDefinition("user_id", {headerFilter: true});
						table.updateColumnDefinition("user_name", {headerFilter: true});
						table.updateColumnDefinition("book_no", {headerFilter: true});
						table.updateColumnDefinition("title", {headerFilter: true});
						table.updateColumnDefinition("state", {headerFilter: "select"});
					}
					
					return response.result.data;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "회원 아이디", widthGrow: 2, field: "user_id", headerSort: false, headerFilterPlaceholder: " " },
					{
						title: "회원명", widthGrow: 1.5, field: "user_name", headerSort: false, headerFilterPlaceholder: " ",
						formatter: function(cell) {
							return Masking.name(cell.getValue());
						}
					},
					{ title: "등록번호", widthGrow: 2, field: "book_no", headerSort: false, headerFilterPlaceholder: " " }, 
					{ title: "도서명", widthGrow: 3, field: "title", headerSort: false, headerFilterPlaceholder: " " },
					{ title: "대출 일시", widthGrow: 2, field: "loan_date", headerSort: false },
					{ title: "반납 예정 일시", widthGrow: 2, field: "return_plan_date", headerSort: false },
					{
						title: "상태", widthGrow: 1.5, field: "state", headerSort: false, headerFilterParams: {"0": "수거", "1": "반납"}, headerFilterPlaceholder: " ",
						formatter: function(cell) {
							return cell.getValue() == '0'? "수거" : "반납";
						}
					},
					{ title: "반납 일시", widthGrow: 2.5, field: "return_date", headerSort: false },
					{ title: "배출 일시", widthGrow: 2.5, field: "out_date", headerSort: false },
					{ title: "관리구분코드", widthGrow: 2, field: "manage_code", headerSort: false },
					{ title: "자료실명", widthGrow: 2, field: "loc_name", headerSort: false },
					{ title: "자료실 코드", widthGrow: 2, field: "loc_code", headerSort: false },
				],
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
		url: '/api/history/getUnmannedReturnHistoryList'
	});
});