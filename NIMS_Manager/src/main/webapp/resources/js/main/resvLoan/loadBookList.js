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

			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "비치 도서 목록_("+Search.params.startDate+").xlsx";
				else file_name = "비치 도서 목록_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	/**
	 * @Desc 비치 도서 목록 테이블
	 */
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
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
						return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data) {
						return `${value}<span>(도서<span class="group-header">${count}</span>권)</span>`;
					}
				] : function(value, count, data) {
					return `${value}<span>(도서<span class="group-header">${count}</span>권)</span>`;
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
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != "") {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("title", {headerFilter: false});
						table.updateColumnDefinition("book_no", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("title", {headerFilter: true});
						table.updateColumnDefinition("book_no", {headerFilter: true});
					}
					
					return response.result.data;
				},
				
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow: 1, hozAlign: "center", headerHozAlign: "center", headerSort: false, headerFilterPlaceholder: " " },
					{
						title: "도서명", field: "title", widthGrow: 3.5, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "저자", field: "author", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "출판사", field: "publisher", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{ title: "투입일자", field: "insert_date", widthGrow: 1.5, hozAlign: "center", headerHozAlign: "center", headerSort: false },
					{ title: "예약 만료일자", field: "reservation_expire_date", widthGrow: 1.5, hozAlign: "center", headerHozAlign: "center", headerSort: false }
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
		url: '/api/resvLoan/getResvLoanBookList'
	});
});