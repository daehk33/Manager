$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function() {
			this.table = Table.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this);
			
			// 도서관 선택 시
			$('input[name="library"]').change(function() {
				if(!this.value) {
					table.setGroupBy(["library_nm", "device_nm"]);
					
					table.setGroupHeader([
						function(value, count, data, group) {
							let groupCnt = group._group.groupList.length;
							return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
						},
						function(value, count, data) {
							return `${value}<span>(선반<span class="group-header">${count}</span>개)</span>`;
						}
					]);
				}
				else {
					table.setGroupBy("device_nm");
					
					table.setGroupHeader(
						function(value, count, data) {
							return `${value}<span>(선반<span class="group-header">${count}</span>개)</span>`;
					});
				}
					
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e) {
				const target = $(this).attr('target');

				let file_name = "캐비닛 조회_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	/**
	 * @Desc 슬롯 조회 테이블
	 */
	const Table = {
		load: function() {
			return this.draw();
		},
		draw: function () {
			const that = this;
			
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader:grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
						return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data){
						return `${value}<span>(선반<span class="group-header">${count}</span>개)</span>`;
					}
				] : function(value, count, data) {
						return `${value}<span>(선반<span class="group-header">${count}</span>개)</span>`;
				},
				addRowPos: "top",
				history: true,
				movableColumns: true,
				placeholder: TableUtil.getPlaceholder('조회된 캐비닛이 없습니다.'),
				
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering: true,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page": "pageIdx",
					"size": "pageSize",
				},
				ajaxURLGenerator: function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != "") {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("title", {headerFilter: false});
						table.updateColumnDefinition("author", {headerFilter: false});
						table.updateColumnDefinition("publisher", {headerFilter: false});
						table.updateColumnDefinition("user_name", {headerFilter: false});
						table.updateColumnDefinition("user_id", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("title", {headerFilter: true});
						table.updateColumnDefinition("author", {headerFilter: true});
						table.updateColumnDefinition("publisher", {headerFilter: true});
						table.updateColumnDefinition("user_name", {headerFilter: true});
						table.updateColumnDefinition("user_id", {headerFilter: true});
					}
					
					return response.result.data;
				},
				
				columns: [
					{ title: "NO", formatter: "rownum", width: 55, hozAlign: "center", headerHozAlign: "center", frozen: true, headerSort: false , download:false },
					{ title: "서가 위치", field: "direction_nm", widthGrow: 1, headerSort: false, hozAlign: "center", headerHozAlign: "center", frozen: true },
					{ title: "캐비닛 번호", field: "cabinet", widthGrow: 1, hozAlign: "center", headerHozAlign: "center", headerSort: false, frozen: true },
					{ title: "투입 번호", field: "insert_no", widthGrow: 1, hozAlign: "center", headerHozAlign: "center", headerSort: false, frozen: true },
					{
						title: "도서명", field: "title", widthGrow: 3.5, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "저자", field: "author", widthGrow: 1.5, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "출판사", field: "publisher", widthGrow: 1.5, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "예약자 아이디", field: "user_id", widthGrow: 1, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title: "예약자 성함", field: "user_name", widthGrow: 1, headerSort: false, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
				],
				rowClick: function(e, row) {
				},
				downloadComplete: function() {
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
		url: '/api/resvLoan/getResvLoanCabinetList'
	});
});