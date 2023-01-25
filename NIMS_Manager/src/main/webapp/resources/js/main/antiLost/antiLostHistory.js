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
			Device.load({model_key: '5'});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "대출 및 반납 이력_("+Search.params.startDate+").xlsx";
				else file_name = "대출 및 반납 이력_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load({model_key: '5'});
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
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<div class="group-header" style="display: inline">${count}</div> 건)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				headerFilterPlaceholder: '',
				pagination: "remote",
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
				ajaxSorting: true,
				ajaxFiltering: true,
				ajaxURLGenerator: function(url, config, params) {
					const paramMap = {
						type: params.type,
						startDate: params.startDate,
						endDate: params.endDate,
						pageIdx: params.pageIdx,
						pageSize: params.pageSize,
					};
					
					if(sessionStorage.getItem('library_key') != '') {
						paramMap.library_key = sessionStorage.getItem('library_key')
					}
					
					const deviceKey = $('#filter-device #select_device option:selected').val();
					if(deviceKey != '') {
						paramMap.device_id = Device.itemHash[deviceKey].device_id;
					}
					
					console.log(params.sorters)
					const sorters = params.sorters;
					if(sorters.length > 0){
						paramMap.sort_field = sorters[0].field;
						paramMap.sort_dir = sorters[0].dir;
					}
					
					const filters = params.filters;
					if(filters.length > 0){
						filters.forEach(filter => {
							paramMap[filter.field] = filter.value;
						});
					}
					url = `${url}?${common.parseParam(paramMap)}`;
					return url;
				},
				ajaxResponse: function(url, params, response) {
					console.log(response)
					$('[role="msg"][target="count"]').text(response.result.count);
					
					return response.result;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "발생일시", widthGrow: 1, field: "log_date", headerSort: true },
					{ title: "등록번호", widthGrow: 1, field: "book_no", headerFilter: 'input', headerSort: false },
					{ title: "도서명", widthGrow: 4, field: "book_title", headerFilter: 'input', headerSort: false },
				],
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row);
				}
			});
			
			return table;
		}
	};
	
	Content.load({
		url: '/api/history/getAntilostHistoryList'
	});
});