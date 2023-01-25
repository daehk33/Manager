$(function(){
	const Content = {
		params: {},
		load: function(params){
			this.params = params;
			this.result();
		},
		result: function() {
			const tableGrp = TableGrp.load();
			const tableCode = TableCode.load();
			
			this.event();
		},
		event: function() {
			$('button[role="add"]').click(function() {
				const params = TableCode.getParams();
				
				const codeModal = $('#codeModal');
				
				const input = ParamManager.load('insert', params.rec_key);
				codeModal.append(input);
				
				codeModal.modal('show');
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "코드 목록_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	
	const TableGrp = {
		load : function() {
			$('button[role="add"]').hide();
			
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#groupTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				localized:function(locale, lang){
			    	lang.pagination.page_size="";
			    },
				layout: "fitColumns",
				addRowPos : "top",
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 코드 그룹이 없습니다.'),
				
				pagination: false,
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering: true,
				
				ajaxURL : "/api/code/getCodeGroupList",
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
				columns : [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign:"center", headerHozAlign: "center", headerSort: false , download:false },
					{ title:"그룹 아이디", field:"grp_id", widthGrow: 1, headerSort: false },
					{ title:"그룹명", field:"grp_nm", widthGrow: 2, headerSort: false },
				],
				rowClick: function(e, row){
					const cardCodeInfo = $('[role="card"][target="codeInfo"]');
					
					// selected 효과
					const data = row.getData();
					if(row.isSelected()){
						row.deselect();
					}
					else {
						table.getSelectedRows().forEach(row => {
							row.deselect();
						})
						row.select();
					}
					
					const addButton = $('button[role="add"]');
					if(row._row.modules.select.selected){
						TableCode.load(row.getData());
						addButton.show();
					}
					else {
						TableCode.load();
						addButton.hide();
					}
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		},
	};
	
	const TableCode = {
		params: null,
		load : function(params) {
			this.params = params;
			return this.draw(params);
		},
		draw : function(params) {
			this.params = params;
			const that = this;
			
			rowFormatter = this.getRowFormatter();
			
			const table = new Tabulator("#codeTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 코드 목록이 없습니다.'),
				
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering:true,
				
				ajaxURL : "/api/code/getCodeList",
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					if(that.params && that.params.rec_key){
						url += `&grp_key=${that.params.rec_key}`;
					}
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.data;
				},
				
				columns : [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign:"center", headerHozAlign: "center", headerSort: false },
					{ title: "코드", field: "code", widthGrow: 1, headerSort: false },
					{ title: "코드 값 1", field: "code_value", widthGrow: 1.5, headerSort: false },
					{ title: "비고", field: "code_desc", widthGrow: 2, headerSort: false },
					{ title: "등록일", field: "create_date", widthGrow: 1, headerSort: false },
				],
				rowClick: function(e, row){
					$(row.getElement()).addClass('active');
					that.showRowDetail(row);
				},
			    tableBuilt:function(){
			    	$('[role="area"]').show();
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
		getRowFormatter : function(row) {},
		showRowDetail : function(row){
			const codeModal = $('#codeModal');
			let element = row.getElement(), data= row.getData(), width= element.offsetWidth, rowTable, cellContents;
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			codeModal.append(input);
			
			codeModal.modal('show');
			codeModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		},
		getParams : function(){
			return this.params;
		}
	};
	
	Content.load();
})