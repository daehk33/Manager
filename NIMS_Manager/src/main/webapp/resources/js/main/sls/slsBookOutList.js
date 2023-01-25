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
			const table = this.table;
			
			// 배출 시작/중지 제어 버튼
			$('button[role="action"][data-action="control"]').click(function() {
				const selectModal = $('#slsBookOutSelectModal');
				let allData = table.getData();
				
				const input = ParamManager.load('start', JSON.stringify(allData));
				
				selectModal.append(input);
				selectModal.modal('show');
			});
			
			// 취소 버튼 클릭
			$('button[role="action"][data-action="delete"]').click(function() {
				const selected = table.getSelectedData();
				
				if(selected.length == 0) {
					Alert.warning({text: '배출 요청을 취소할 도서를 선택해 주세요.'});
					return;
				}
				else {
					let success = true;
					let rtnMsg = "";
					
					Alert.confirm({title: '배출 요청 취소', text: '선택하신 도서들의 배출 요청을 취소하시겠습니까?<br/>배출 취소는 배출 예정 상태인 도서만 가능합니다.', confirmText: '확인'}, (result) => {
						if(result.isConfirmed) {
							for(bookData of selected) {
								// 배출 도서 중 상태가 배출 예정인 도서만 취소 처리
								if(bookData.status == "0") {
									AjaxUtil.request({
										url: '/api/sls/deleteSLSBookOutInfo',
										async: false,
										data: {
											rec_key: bookData.rec_key,
											device_id: bookData.device_id,
											id: bookData.id
										},
										done: function(data){
											data = data.result;
											
											if(data.CODE != '200') {
												success = false;
												if(rtnMsg=="") rtnMsg += "[" + bookData.book_no + "] " + bookData.title + " - " + data.DESC;
												else rtnMsg += "\n[" + bookData.book_no + "] " + bookData.title + " - " + data.DESC;
											}
										}
									});
								}
							}
							if(success) Alert.success({text: "배출 요청 취소가 완료되었습니다!"}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						}
					});
				}
			});
			
			// 엑셀 내보내기 버튼 클릭
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "배출 도서 목록_("+common.getDateToStr()+").xlsx";
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
				placeholder: TableUtil.getPlaceholder('조회된 배출 도서 목록이 없습니다.'),
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
					
					url += '&bookout_status=0';
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("book_no", {headerFilter: false});
						table.updateColumnDefinition("title", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("book_no", {headerFilter: true});
						table.updateColumnDefinition("title", {headerFilter: true});
					}
					
					return response.result.data;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						},
						width: 30, download: false
					},
					{ title: "도서관명", field: "library_nm", visible: false},
					{ title: "장비명", field: "device_nm", visible: false},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow: 1, headerSort: true, hozAlign: "center", headerHozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "도서명", field: "title", widthGrow: 3, headerSort: true, headerFilterPlaceholder: " " },
					{ title: "저자", field: "author", widthGrow: 1, headerSort: false },
					{ title: "출판사", field: "publisher", widthGrow: 1, headerSort: false },
					{ title: "배출 요청 일자", field: "add_date", widthGrow: 1.5, headerSort: true, hozAlign: "center", headerHozAlign: "center", },
					{
						title: "상태", field: "status", widthGrow: 0.7, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const row = cell.getRow();
							let status = cell.getValue();
							
							result = status == '0' ? '배출 예정' : status == '1' ? '배출' : '배출 취소';
							
							return `<font>${result}</font>`;
						}
					},
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
		url: '/api/sls/getSLSBookOutList',
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		}
	});
});