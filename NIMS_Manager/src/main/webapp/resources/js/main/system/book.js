$(function(){
	const Content = {
		params: {},
		load: function(params){
			this.params = params;
			this.draw();
		},
		draw: function(){
			this.event();
		},
		event: function(){
			const table = Table.load();
			const modal = this.modal;
			
			$('.card .btn[role="action"]').click(function(){
				const action = this.dataset.action;
				const target = this.dataset.target || '';
				
				const bookModal = $('#bookModal');
				if(action == 'export'){
					Excel.download(target, table, '서지정보목록.xlsx');
				}
				else if(action == 'add'){
					bookModal.append(ParamManager.load(action));
					bookModal.modal('show');
					
					bookModal.on('hidden.bs.modal', function(){
						bookModal.find('.param[name="item"]').remove();
					});
				}
				else if(action == 'add_excel'){
					bookModal.append(ParamManager.load(action));
					bookModal.modal('show');
					
					bookModal.on('hidden.bs.modal', function(){
						bookModal.find('.param[name="item"]').remove();
					});
				}
				else if(action == 'delete'){
					const selected = table.getSelectedData();
					if(selected.length == 0){
						Alert.warning({text: '삭제하실 서지 정보를 선택해주세요.'});
						return false;
					}
					
					Alert.confirm({text: '선택한 서지 정보를 모두 삭제하시겠습니까?'}, function(result){
						if(!result.isConfirmed) return;
						
						AjaxUtil.request({
							url: '/api/book/delete',
							data:  Object.assign({}, {
								bookList: selected.map(file => file.rec_key).join(','),
							}),
							table: 'bookTable',
							modal: modal,
							successMessage: '성공적으로 삭제되었습니다',
							failMessage: '삭제 중 오류가 발생했습니다.'
						});
					});
				}
				else if(action == 'deleteAll'){
					Alert.confirm({text: '서지 정보를 전체 삭제하시겠습니까?'}, function(result){
						if(!result.isConfirmed) return;
						
						AjaxUtil.request({
							url: '/api/book/delete',
							data:  Object.assign({}, {
								rewrite: 'Y'
							}),
							table: 'bookTable',
							modal: modal,
							successMessage: '성공적으로 삭제되었습니다',
							failMessage: '삭제할 도서가 없습니다'
						});
					});
					
				}
			});
		}
	};
	
	const Table = {
		load : function() {
			return this.draw();
		},
		draw : function () {
			const that = this;
			
			const loanStatusHash = {"":"전체", "0":"대출가능", "1":"대출중"}
			
			const pc = window.innerWidth > 600;
			const mobile = window.innerWidth <= 600;
			
			
			const table = new Tabulator("#bookTable", {
				locale: true,
				langs: TableUtil.setDefaults(),
				layout: "fitColumns",
				placeholder: TableUtil.getPlaceholder('조건에 맞는 서지 정보가 없습니다.'),
				headerFilterPlaceholder: '',
				headerVisible: true,
				//pagination : 'remote',
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				paginationButtonCount: mobile ? 3 : 10,
				ajaxFiltering: true,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				ajaxURL: Content.params.url,
				ajaxConfig : ajaxConfig,
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					const filters = params.filters;
					params = {};
					
					if(filters != undefined){
						filters.forEach(filter => {
							params[filter.field] = filter.value;
						});
					}
					
					url = `${url}?${common.parseParam(params)}`;
					return url;
				},
				ajaxResponse : function(url, params, response) {
					const data = response.result.items || undefined;
					
					data.forEach(row =>{
						if(row.loan_status == '0') return row.loan_status = '대출가능'
						else if(row.loan_status == '1')  return row.loan_status = '대출중'
					})
					return data;
				},
				columnHeaderVertAlign:"middle",
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						},
						width: 30, download: false, headerSort: false
					},
					{ title: "NO", field: "row_num", hozAlign: "center", headerHozAlign: "center", width: pc ? 70 : 40, headerSort: false, download: true},
					{ title: "등록번호", field: "book_no", widthGrow: 0.8, headerFilterPlaceholder: '숫자(7자리)', headerFilter:"input",  headerFilterParams:{values:true}}, 
					{ title: "서명", field: "title", widthGrow: 1.7, headerFilter:"input"},
					{ title: "저자", field: "author", widthGrow: 1.2, headerFilter:"input", headerFilterParams:{values:true}},
					{ title: "출판사", field: "publisher", widthGrow: 0.8, headerFilter:"input", headerFilterParams:{values:true}},
					{ title: "출판연도", field: "publish_year", widthGrow: 0.7, headerFilterPlaceholder: 'YYYY(연도)', headerFilter:"input", headerFilterParams:{values:true}},
					{ title: "청구기호", field: "call_no", widthGrow: 0.8, headerFilter:"input", headerFilterParams:{values:true}},
					{ title: "위치", field: "location", widthGrow: 0.8, headerFilter:"input", headerFilterParams:{values:true}},
					{ title: "등록일", field: "create_date" },
					{ title: "청구기호출력", field: "print" , headerHozAlign: "center", headerSort: false,  hozAlign: "center",  widthGrow: 0.7, download: false, 
						formatter: function(cell, formatterParams, onRendered) {
							return '<span class="badge badge-default"><i class="fas fa-print"></i><span>';
						},
						cellClick: function(e, cell) {
							e.stopPropagation();
							
							const data = cell.getData();
							ParamManager.show('receiptModal', 'show', data);
						}
					}
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
			const bookModal = $('#bookModal');
			let data = row.getData();
			let type = 'modify';
			const input = ParamManager.load(type, JSON.stringify(data));
			bookModal.append(input);
			
			bookModal.modal('show');
			bookModal.on('hidden.bs.modal', function(){
				bookModal.find('.param[name="item"]').remove();
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		},
	};
	
	Content.load({
		url: '/api/book/getBookList'
	});
});