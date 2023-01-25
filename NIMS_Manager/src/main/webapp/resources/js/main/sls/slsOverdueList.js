$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			Data.load();
			this.result();
		},
		result: function(){
			this.table = BookTable.load();
			this.event();
		},
		event: function(){
			const that = this;
			const table = this.table;
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "연체도서 목록_("+common.getDateToStr()+").xlsx";
				Excel.download(target, table, file_name);
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Data.load();
				table.setData();
			});
			
			// 장비 선택 시
			$('#filter-field').change(function() {
				table.setData();
			});
		},
	};
	
	const Data = {
		devices: [],
		load: function(){
			const that = this;
			const deviceSelect = $('#filter-field');
			
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '6',
				},
				async: false,
				done: function(data) {
					data = data.result.data;
					that.devices = data;
					
					deviceSelect.empty();
					
					if(data.length == 0) {
						deviceSelect.append('<option value="-1" data-library_key="-1" data-device_id="-1">장비 없음</option>');
					}
					for(item of data) {
						let option = `<option value="${item.rec_key}" data-library_key="${item.library_key}" data-device_id="${item.device_id}">${item.device_nm}</option>`;
						deviceSelect.append(option); 
					}
					
					deviceSelect.trigger('change');
				}
			});
		},
	};
	
	const BookTable = {
		load: function(){
			return this.draw();
		},
		draw: function(){
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
					function(value, count, data){
						return `${value}<span>(도서<span class="group-header">${count}</span>권)</span>`;
					}
				] : function(value, count, data){
					return `${value}<span>(도서<span class="group-header">${count}</span>권)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('연체 도서 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page": "pageIdx",
					"size": "pageSize",
				},
				ajaxURLGenerator: function(url, config, params) {
					const device_key = $('#filter-field').val();
					
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					url += `&device_key=${device_key}`;
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("book_no", {headerFilter: false});
						table.updateColumnDefinition("title", {headerFilter: false});
						table.updateColumnDefinition("state", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("book_no", {headerFilter: true});
						table.updateColumnDefinition("title", {headerFilter: true});
						table.updateColumnDefinition("state", {headerFilter: "select"});
					}
					
					return response.result.data;
				},
				columns: [
					{ title: "도서관명", field: "library_nm", visible: false },
					{ title: "장비명", field: "device_nm", visible: false },
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow: 1, headerSort: true, hozAlign: "center", headerHozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "도서명", field: "title", widthGrow: 2.5, headerSort: true, headerFilterPlaceholder: " " },
					{ title: "저자", field: "author", widthGrow: 1.2, headerSort: false },
					{ title: "회원번호", field: "user_id", widthGrow: 1.5, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "회원명", field: "user_name", widthGrow: 1, headerSort: false, headerFilterPlaceholder: " ",
						formatter:function(cell){return Masking.name(cell.getValue());}
					},
					{ title: "대여일", field: "loan_date", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center", },
					{ title: "반납 예정일", field: "return_plan_date", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center", },
					{
						title: "연체일", field: "overdue_date", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center",
						formatter: function(cell) {
							return cell.getValue() + "일";
						}
					}
				],
				downloadComplete: function(){
					Swal.close();
				},
				rowClick: function(e, row) {
					row.toggleSelect();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		}
	};
	
	Content.load({
		url: '/api/sls/getSLSOverdueList',
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		}
	});
});