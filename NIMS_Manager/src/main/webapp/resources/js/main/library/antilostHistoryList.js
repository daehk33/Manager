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
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				
				let file_name = "분실 방지 이력_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	const Table = {
		load: function(){
			Search.params.startDate = common.getDateToStr('day', -7);
			Search.params.endDate = common.getDateToStr();

			return this.draw();
		},
		draw: function(){
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 && sessionStorage.getItem('library_key') == ''? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<div class="group-header" style="display: inline">${count}</div> 건)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				pagination: "r",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering: true,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxParams: {
					startDate: Search.params.startDate,
					endDate: Search.params.endDate
				},
				ajaxURLGenerator: function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
						
					let items = response.result.data;
					items.forEach(item => {
						item.inoutdiv = item.inoutdiv == '1' ? '입장' : (item.inoutdiv == '0' ? '퇴장' : '-'); 
					});
					
					return response.result.data;
				},
				columns: [
					{ title: "이벤트 일시", field: "event_date", headerSort: false },
					{ title: "이벤트 정보 ", field: "inoutdiv", headerSort: false },
					{ title: "장비 아이디", field: "device_id", headerSort: false },
					{ title: "장비명", field: "device_nm", headerSort: false },
					{ title: "부정 도서 ISBN", field: "book_isbn", headerSort: false },
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
		url: '/api/history/getAntilostHistoryList'
	});
});