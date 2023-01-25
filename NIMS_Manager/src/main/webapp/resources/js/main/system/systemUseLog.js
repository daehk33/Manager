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
			SearchContainer.load(this);

			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "시스템 사용 이력_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	/**
	 * @Desc 시스템 사용 이력 관리 테이블
	 */
	const Table = {
		load : function() {
			Search.params.startDate =common.getDateToStr('day', -7);
			Search.params.endDate = common.getDateToStr();

			return this.draw();
		},
		draw : function () {
			const that = this;

			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 관리 이력이 없습니다.'),
				
				pagination : "local",
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
				ajaxParams: {
					startDate: Search.params.startDate,
					endDate: Search.params.endDate
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
					
					return response.result.data;
				},
				
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign:"center", headerHozAlign: "center", headerSort: false , download:false},
					{ title: "관리 일시", field: "log_date", widthGrow: 1.5, headerSort: false },
					{ title: "사용자 등급", field: "manager_grade_nm", widthGrow: 1, hozAlign:"center", headerHozAlign: "center", headerSort: false },
					{ title: "소속", field: "library_nm", widthGrow: 1, hozAlign:"center", headerHozAlign: "center", headerSort: false },
					{ title: "사용자 이름", field: "manager_nm", widthGrow: 1, hozAlign:"center", headerHozAlign: "center", headerSort: false },
					{ title: "관리 메뉴", field: "log_menu_nm", widthGrow: 1.5, headerSort: false },
					{ title: "관리 유형", field: "log_type_nm", widthGrow: 1.5, headerSort: false },
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
		},
	};
	
	Content.load({
		url: '/api/log/getLogList'
	});
});