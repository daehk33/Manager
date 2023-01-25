/**
 * @Desc slsUserLoan for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Modal.load();
		}
	};
	
	const Modal = {
		modal : null,
		load : function() {
			this.modal = $('#slsUserLoanModal');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title')
				const body = modal.find('.modal-content .modal-body');
				
				const param = modal.find('.param');
				const params = JSON.parse(param.val());
				title.html(`<span style="color: #7e44ea">${Masking.name(params.user_name)}</span>님의 대출 목록`)
				that.draw(Object.assign({}, Search.params, {user_id: params.user_id}));
			});
			
			modal.on('hide.bs.modal', function(e) {
				modal.find('.param').remove();
			});
		},
		draw: function(params){
			const table = new Tabulator("#userLoanTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				placeholder: TableUtil.getPlaceholder('대출 목록이 없습니다.'),
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering:true,
				
				ajaxURL : '/api/sls/getSLSUserLoanList',
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				ajaxParams: params,
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
					{ title: "NO", formatter: "rownum", width: 55, hozAlign: "center", headerHozAlign: "center", frozen: true, headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow:2 },
					{ title: "도서명", field: "title", widthGrow:5 },
					{ title: "대출 일자", field: "loan_date", widthGrow:1.8, hozAlign: "center", headerHozAlign: "center" },
					{ title: "반납 일자", field: "return_date", widthGrow:1.8, hozAlign: "center", headerHozAlign: "center" },
				],
				rowClick: function(e, row) {},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			
		}
	};

	Content.load({});
});