$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function(){
			this.table = BookRankTable.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this);
			
			// 장비 검색 로드
			Device.load({model_key: '6'});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load({model_key: '6'});
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "인기 대출 도서_("+Search.params.startDate+").xlsx";
				else file_name = "인기 대출 도서_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	
	const BookRankTable = {
		load: function(){
			Search.params.type = "day";
			Search.params.startDate = common.getDateToStr('month');
			
			return this.draw();
		},
		draw: function(){
			const table = new Tabulator("#rankTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return value;
				},
				placeholder: TableUtil.getPlaceholder('인기 대출 도서 목록이 없습니다.'),
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				ajaxParams: {
					type: Search.params.type,
					startDate: Search.params.startDate,
					endDate: ''
				},
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
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
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					
					if(response.result.count == 0) { 
						$('.card-title-count').hide();
						table.updateColumnDefinition("book_no", {headerFilter: false});
						table.updateColumnDefinition("title", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("book_no", {headerFilter: true});
						table.updateColumnDefinition("title", {headerFilter: true});
					}
					
					return response.result.data;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 55, hozAlign: "center", headerHozAlign: "center", frozen: false, headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow: 1, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "도서명", field: "title", widthGrow: 5, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "저자", field: "author", widthGrow: 2.5, headerSort: false },
					{ title: "대출횟수", field: "cnt", hozAlign: "center", headerHozAlign: "center", widthGrow: 1, }
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
		url: '/api/sls/getSLSBookRankList'
	});
});