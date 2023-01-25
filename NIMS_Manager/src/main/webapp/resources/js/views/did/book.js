$(function(){
	const Content = {
		params: {},
		load: function(params){
			this.params = params;
			
			document.title = '도서검색 페이지';
			
			this.draw();
		},
		draw: function(){
            $('#pills-converter').css({display: 'flex'});
            
            this.event(Content.params);
		},
		event: function(){
			const that = this;
			const table = Table.load();
			
			/**
			 * Resize Event
			 */
			this.resize();
			
			let width = window.innerWidth;
			window.addEventListener('resize', function(e){
				that.resize();
				
				let temp = window.innerWidth;
				if(width > 600 && temp <= 600){
					location.reload();
				}
				if(width <= 600 && temp > 600){
					location.reload();
				}
			});
			
			/**
			 * Search Event
			 */
			const searchContainer = $('.search-container');
			const searchItem = searchContainer.find('select[name="searchItem"]');
			const searchWord = searchContainer.find('input[name="searchWord"]');
			
			const search = function(){
				table.setData();
			}
			
			searchWord.on({
				keyup: function(e){
					if(e.originalEvent.keyCode == 13){
						search();
					}
				}
			});
			
			searchContainer.find('.btn[role="action"]').click(function(e){
				const action = this.dataset.action;
				if(action == 'clear'){
					searchItem.val('keyword');
					
					if(searchWord.val() != ''){
						searchWord.val('');
					}
					
					search();
				}
				else if(action == 'search'){
					search();
				}
			});
		},
		resize: function(){
			const header = $('.header')
			const search = $('.header-search');
			const content = $('.content');
			
			const height = header.outerHeight() + search.outerHeight() + 'px';
			content.css({marginTop: height, height: 'calc(100% - ' + height + ')'});
			
			const contentSummary = $('.content .content-summary');
			const contentItem = $('.content .content-item');
			
			contentItem.css({height: 'calc(100% - ' + contentSummary.outerHeight(true) + 'px)'});
		}
	};
	
	const Table = {
		load : function() {
			return this.draw();
		},
		draw : function () {
			const that = this;
			
			const searchContainer = $('.search-container');
			const searchItem = searchContainer.find('select[name="searchItem"]');
			const searchWord = searchContainer.find('input[name="searchWord"]');
			
			const statusHash = {'':'전체', '0':'대출가능', '1':'대출중'}
			
			const pc = window.innerWidth > 600;
			const mobile = window.innerWidth <= 600;
			
			const table = new Tabulator("#table", {
				renderComplete: function(){
					const val = searchWord.val();
					if(val != ''){
						$('#table').highlight(searchWord.val());
					}
					else {
						$('#table').unhighlight();
					}
				},
				locale: true,
				langs: TableUtil.setDefaults(),
				layout: 'fitColumns',
				placeholder: TableUtil.getPlaceholder('조건에 맞는 서지 정보가 없습니다.'),
				headerFilterPlaceholder: '',
				headerVisible: true,
				pagination : mobile ? false : 'local',
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				paginationButtonCount: 10,
				ajaxFiltering: true,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				ajaxURL: Content.params.url,
				ajaxConfig : ajaxConfig,
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					params = {caller: 'nicom'};
					if(searchWord.val() != ''){
						params[searchItem.val()] = searchWord.val();
					}
					
					url = `${url}?${common.parseParam(params)}`;
					return url;
				},
				ajaxResponse : function(url, params, response) {
					const data = response.result.items || [] ;
					const count = response.result.itemsCount || 0;
					
					/**
					 * 결과 삽입
					 */
					const resultCount = $('#result-count');
					const resultKeyword = $('#result-keyword');
					
					const searchWord = $('input[name="searchWord"]');
					const word = searchWord.val();
					
					Animation.number(resultCount, count);
					
					if(word == ''){
						resultKeyword.addClass('hide');
					}
					else {
						resultKeyword.removeClass('hide');
						resultKeyword.html(word);
					}
					
					/**
					 * 데이터 가공
					 */
					data.forEach(row => {
						row.loan_status_nm = statusHash[row.loan_status] || '';
					})
					return data;
				},
				columnHeaderVertAlign:"middle",
				columns: mobile ? [
					{ title: "순번", formatter: "rownum", field: 'no', hozAlign: "center", vertAlign: 'middle', headerHozAlign: "center", width: 60, headerSort: false},
					{ title: "도서 정보", field: "title", widthGrow: 1, headerHozAlign: "center", headerSort: false
						,formatter: function(cell, fomatterParams, onRendered){
							const data = cell.getData();
							
							const title = data.title || '';
							const author = data.author || '';
							const call_no = data.call_no || '';
							return `
								<div class="book-container">
									<div class="book-title ellipsis">${title}</div>
									<div class="ellipsis">
										<span class="badge badge-default">저자</span> ${author}
									</div>
									<div class="ellipsis">
										<span class="badge badge-default">청구기호</span> ${call_no}
									</div>
								</div>
							`
						}},
				]: [
					{ title: "순번", formatter: "rownum", field: 'no', hozAlign: "center", headerHozAlign: "center", width: 60, headerSort: false},
					{ title: "등록번호", field: "book_no" },
					{ title: "제목", field: "title", widthGrow: 3 },
					{ title: "저자", field: "author", widthGrow: 2 },
					{ title: "발행처", field: "publisher" },
					{ title: "발행연도", field: "publish_year", hozAlign: "center", headerHozAlign: "center" },
					{ title: "소장위치", field: "location", hozAlign: "center", headerHozAlign: "center" },
					{ title: "청구기호", field: "call_no" },
				],
				rowClick: function(e, row) {
					TableUtil.showRowDetail(row, 'receiptModal');
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
		url: '/api/book/getBookList',
	});
});