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
			Device.load({model_key: '3,11'});
			
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
				Device.load({model_key: '3,11'});
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
						table.updateColumnDefinition("book_name", {headerFilter: false});
						table.updateColumnDefinition("book_no", {headerFilter: false});
						table.updateColumnDefinition("st_name", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("book_name", {headerFilter: true});
						table.updateColumnDefinition("book_no", {headerFilter: true});
						table.updateColumnDefinition("st_name", {headerFilter: true});
					}
					
					return response.result.data;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "도서명", widthGrow: 4, field: "book_name", headerSort: false, headerFilterPlaceholder: " " },
					{ title: "등록번호", widthGrow: 4, field: "book_no", headerSort: false, headerFilterPlaceholder: " " },
					{ title: "대출일", widthGrow: 3, field: "book_loan_date", headerSort: false },
					{ title: "반납 예정일", widthGrow: 3, field: "book_return_date", headerSort: false },
					{ title: "반납일", widthGrow: 3, field: "book_state_date", headerSort: false },
					{ title: "회원 아이디", widthGrow: 2, field: "id_no", headerSort: false },
					{
						title: "회원명", widthGrow: 2, field: "st_name", headerSort: false, headerFilterPlaceholder: " ",
						formatter:function(cell){return Masking.name(cell.getValue());}
					},
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
		url: '/api/history/getReturnHistoryList'
	});
});