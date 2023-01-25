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
			// 도서 배출 버튼 클릭
			$('button[role="action"][data-action="pickingOut"]').click(function() {
				const selectedData = table.getSelectedData();
				
				if(selectedData.length == 0) {
					Alert.warning({text: '배출할 도서를 선택해 주세요.'});
					return;
				}
				else {
					let success = true;
					let rtnMsg = "";
					
					Alert.confirm({title: '도서 배출', text: '선택하신 도서들을 배출하시겠습니까?<br/>도서 배출은 비치된 도서만 가능합니다.', confirmText: '배출'}, (result) => {
						if(result.isConfirmed){
							for(bookData of selectedData) {
								// 예약, 대출, 배출 예정 도서 제외
								if(bookData.state == 1 && bookData.status != 0) {
									AjaxUtil.request({
										url: '/api/sls/insertSLSBookOutInfo',
										async: false,
										data: {
											book_key: bookData.book_key,
											device_id: bookData.device_id,
											rfid: bookData.rfid,
											book_no: bookData.book_no,
											status: 0
										},
										done: function(data) {
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
							
							if(success) Alert.success({text: "도서 배출 요청이 완료되었습니다!<br/>진행, 취소 등의 작업은 <b>[배출 도서 관리]</b> 페이지에서 진행하실 수 있습니다."}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						}
					});
				}
			});
			
			// 도서 정보 동기화 버튼 클릭
			$('button[role="action"][data-action="syncInfo"]').click(function(e) {
				AjaxUtil.request({
					url: '/api/device/syncDeviceBookList',
					done: function(data) {
						Alert.success({text: '동기화 요청이 처리되었습니다.'}, Content.params.onHide);
					},
					fail: function(e) {
						Alert.danger({text: "동기화에 실패하였습니다."});
					}
				});
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "비치도서 목록_("+common.getDateToStr()+").xlsx";
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
				columnHeaderVertAlign: 'middle',
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
				placeholder: TableUtil.getPlaceholder('조회된 도서 목록이 없습니다.'),
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
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						},
						width: 30, download: false
					},
					{ title: "도서관명", field: "library_nm", visible: false },
					{ title: "장비명", field: "device_nm", visible: false },
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "등록번호", field: "book_no", widthGrow: 1, headerSort: true, hozAlign: "center", headerHozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "도서명", field: "title", widthGrow: 2.5, headerSort: true, headerFilterPlaceholder: " " },
					{ title: "저자", field: "author", widthGrow: 1.2, headerSort: false },
					{ title: "투입일자", field: "insert_date", widthGrow: 1.5, headerSort: true, hozAlign: "center", headerHozAlign: "center", },
					{ title: "최근 대여일", field: "loan_date", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center", },
					{ title: "최근 반납일", field: "return_date", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center", },
					{ title: "대출건수", field: "loan_cnt", widthGrow: 1, headerSort: true },
					{
						title: "상태", field: "state", widthGrow: 0.7, headerSort: false, headerFilterParams: {"0": "대출", "1": "비치", "2": "예약"}, headerFilterPlaceholder: " ",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const row = cell.getRow();
							
							let bookOutStatus = row.getData()["status"];
							let state = cell.getValue();
							
							result = state == '0' ? '대출중' : state == '1' ? '비치' : '예약중';
							
							if(state == '1' && bookOutStatus == '0') {
								result = '배출예정';
							}
							
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
		url: '/api/device/getDeviceBookList',
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		}
	});
});