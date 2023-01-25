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
//			SearchContainer.load(this, [
//				{id: 'title', name: '도서명'},
//				{id: 'book_no', name: '등록번호'}
//			]);

			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "예약 도서 목록.xlsx";
				
				Excel.download(target, table, file_name);
			});
			
			// 도서관 선택 기능 숨김
			$('.sb-lib').hide()
		}
	};
	
	const Table = {
		load : function() {
			return this.draw();
		},
		draw : function () {
			const that = this;

			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				
				pagination:"remote",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering:true,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != "") {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result;
				},
				
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, headerSort: false , download:false },
					{ title: '날짜', columns: [
							{ title: "예약 일자", field: "reservation_date", widthGrow: 1.3 },
							{ title: "만료 일자", field: "reservation_expired_date", widthGrow: 1.3 },
						], hozAlign: "center", headerHozAlign: "center"
					},
					{ title: '이용자 정보', columns: [
							{ title: "아이디", field: "user_id", widthGrow: 1 },
							{ title: "이름", field: "user_name", widthGrow: 1, formatter:function(cell){return Masking.name(cell.getValue());} },
						], hozAlign: "center", headerHozAlign: "center"
					},
					{ title: '도서 정보', columns: [
							{ title: "등록번호", field: "book_no", widthGrow: 1.3 },
							{ title: "제목", field: "title", widthGrow: 3 },
							{ title: "저자", field: "author", widthGrow: 2 },
						], hozAlign: "center", headerHozAlign: "center"
					},
					{ title: '자료실 위치', columns: [
							{ title: "번호", field: "location_no", widthGrow: 1 },
							{ title: "이름", field: "location_name", widthGrow: 2 },
						], hozAlign: "center", headerHozAlign: "center"
					},
					{ title: '상태', field: 'count', hozAlign: "center", headerHozAlign: "center", formatter: function(cell, formatterParams, onRendered){
						let data = cell._cell.row.getData();
						let status_nm = '미비치';
						
						if(data.count > 0) status_nm = '비치';
						return status_nm;
					}, widthGrow: 1},
					
				],
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		},
	};
	
	Content.load({
		url: '/api/resvLoan/getReservedBookList'
	});
});