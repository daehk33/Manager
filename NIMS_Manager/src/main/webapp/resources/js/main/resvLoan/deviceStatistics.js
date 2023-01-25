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
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "장비별 통계_("+Search.params.startDate+").xlsx";
				else file_name = "장비별 통계_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
		}
	};
	
	/**
	 * @Desc 시스템 사용 이력 관리 테이블
	 */
	const Table = {
		load : function() {
			Search.params.type = "day";
			Search.params.startDate = common.getDateToStr('month');

			return this.draw();
		},
		draw : function () {
			const that = this;

			const table = new Tabulator("#statsTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<div class="group-header" style="display: inline">${count}</div> 건)</span>`;
				},
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				
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
					type: Search.params.type,
					startDate: Search.params.startDate,
					endDate: ''
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
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "장비명", field: "device_nm", widthGrow: 2, hozAlign: "center", headerHozAlign: "center", headerSort: false },
					{ title: "권 수", field: "cnt", widthGrow: 4, hozAlign:"center", headerHozAlign: "center", headerSort: false },
					{ title: "이용인원", field: "user_cnt", widthGrow: 4, hozAlign:"center", headerHozAlign: "center", headerSort: false }
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
		url: '/api/stats/getStatsResvLoanDevice'
	});
});