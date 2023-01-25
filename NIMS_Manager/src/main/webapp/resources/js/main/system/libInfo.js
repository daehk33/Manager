$(function() {
	const Content = {
		params : {},
		table: null,
		load : function(params) {
			this.params = params;
			this.result();
		},
		result : function() {
			this.table = Table.load();
			this.event();
		},
		event : function() {
			const table = this.table;
			
			// 등록 버튼 클릭시
			$('button[role="add"]').click(function() {
				const libraryModal = $('#libraryModal');
				const input = ParamManager.load('insert');

				libraryModal.append(input);
				libraryModal.modal('show');
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "도서관 목록_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
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
				addRowPos : "top",
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 도서관 목록이 없습니다.'),
				headerFilterPlaceholder: '검색어 입력',
				
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					return url;
				},
				ajaxResponse : function(url, params, response) {
					return response.result.data;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "도서관 아이디", field: "library_id", headerFilter: 'input', headerSort: true },
					{ title: "도서관 이름", field: "library_nm", headerFilter: 'input', headerSort: true },
					{ title: "도서관 위치", field: "location", headerFilter: 'input', headerSort: true },
					{ title: "비고", field: "library_desc", headerSort: true },
					{ title: "등록일", field: "create_date", headerSort: true },
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showRowDetail(row);
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
		showRowDetail : function(row){
			const libraryModal = $('#libraryModal');
			let element = row.getElement(), data= row.getData(), width= element.offsetWidth, rowTable, cellContents;
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			libraryModal.append(input);
			
			libraryModal.modal('show');
			libraryModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: '/api/library/getLibraryList',
	});
});