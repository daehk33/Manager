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
		event : function() {
			const table = this.table;
			
			// 등록 버튼 클릭시
			$('button[role="add"]').click(function() {
				const codeGrpModal = $('#codeGrpModal');
				
				const input = ParamManager.load('insert');
				codeGrpModal.append(input);
				
				codeGrpModal.modal('show');
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "코드 그룹 목록_("+common.getDateToStr()+").xlsx";
				
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
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 코드 크룹이 없습니다.'),
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
					{ title: "NO", formatter: "rownum", width: 70, hozAlign:"center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "그룹 아이디", field: "grp_id", widthGrow: 1, headerFilter: 'input', headerSort: true },
					{ title: "그룹명", field: "grp_nm", widthGrow: 2, headerFilter: 'input', headerSort: true },
					{ title: "비고", field: "grp_desc", widthGrow: 2, headerSort: false },
					{ title: "등록일", field: "create_date", widthGrow: 2, headerSort: true },
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
			const codeGrpModal = $('#codeGrpModal');
			let element = row.getElement(), data= row.getData(), width= element.offsetWidth, rowTable, cellContents;
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			codeGrpModal.append(input);
			
			codeGrpModal.modal('show');
			codeGrpModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: '/api/code/getCodeGroupList',
	});
});