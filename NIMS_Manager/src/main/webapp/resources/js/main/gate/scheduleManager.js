$(function() {
	const Content = {
		table: null,
		params: {},
		load: function(params) {
			const that = this;
			that.params = params;
			
			that.result();
		},
		result: function() {
			const that = this;
			
			that.table = Table.load();
			
			that.event();
		},
		event: function() {
			const that = this;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function() {
				that.table.rowManager.clearData();
				Table.load();
			});
			
			// 등록 버튼 클릭 시
			$('button[role="add"]').click(function() {
				const scheduleModal = $('#scheduleModal');
				
				const input = ParamManager.load('insert');
				scheduleModal.append(input);
				
				scheduleModal.modal('show');
			});
			
			// 삭제 버튼 클릭 시
			$('button[role="delete"]').click(function() {
				const selectedData = that.table.getSelectedData(); 
				
				if(selectedData.length == 0) {
					Alert.warning({text: '삭제하실 스케줄을 선택해 주세요.'});
					return;
				}
				
				let confirmParams = {title: '스케줄 삭제', text: '정말로 선택하신 스케줄들을 삭제하시겠습니까?', confirmText: '삭제', cancelText: '취소'};
				Alert.confirm(confirmParams, (result) => {
					if(result.isConfirmed) {
						let results = [];
						
						selectedData.forEach(item => {
							AjaxUtil.request({
								url: '/api/gate/deleteGateScheduleInfo',
								data: {
									rec_key: item.rec_key
								},
								done: function(data) {
									data = data.result;
									
									if(data.CODE == "200") {
										results.push(true);
									} else {
										results.push(false);
									}
								},
								error: function(error) {
									Alert.danger({text: '잠시 후 다시 시도해주세요.'});
								}
							});
						});
						
						if(results.every(result => result)) {
							Alert.success({text: '삭제가 완료되었습니다!'}, Content.params.onHide);
						} else {
							Alert.danger({text: '일부 항목이 삭제 처리 중 오류가 발생하였습니다.'}, Content.params.onHide);
						}
					}
				});
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e) {
				const target = $(this).attr('target');
				const file_name  = "출입게이트 스케줄 목록.xlsx";
				
				Excel.download(target, that.table, file_name);
			});
		}
	};
	
	const Table = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "equip_id"] : "equip_id",
				groupHeader:grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
						return `${value}<span>(게이트<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data){
						return `${value}<span>(스케줄<span class="group-header">${count}</span>개)</span>`;
					}
				] : function(value, count, data) {
						return `${value}<span>(스케줄<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('등록된 스케줄이 없습니다.'),
				
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSend: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.itemsCount);
					if(response.result.itemsCount == 0) {
						table.updateColumnDefinition("equip_id", {headerFilter: false});
					}
					else {
						table.updateColumnDefinition("equip_id", {headerFilter: true});
					}
					
					console.log(response.result.items);
					
					return response.result.items;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						width: 30, download: false
					},
					{
						title: "요일", widthGrow: 1, field: "date", headerSort: false,
						formatter: function(cell, formatterParams, onRendered) {
							const date = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
							const value = cell.getValue();
							
							let result = value != "ALL" ? date[value] : '매일';
							
							return result;
						}
					},
					{
						title: "시간", widthGrow: 1, field: "time", headerSort: false,
						formatter: function(cell, formatterParams, onRendered) {
							const value = cell.getValue();
							let result = value.substring(0,2) + ':' + value.substring(2,4);
							
							return result;
						}
					},
					{ title: "설정 내용", widthGrow: 2.5, field: "operation_type_nm", headerSort: false },
					{
						title: "사용 여부", widthGrow: 1, field: "use_yn", headerSort: false,
						formatter: function(cell, formatParams, onRendered) {
							let result = cell.getValue() == 'Y' ? '사용' : '미사용';
							
							return result 
						}
					},
					{ title: "등록일", widthGrow: 2, field: "create_date", headerSort: false }
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showDetail(row);
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			$('.tabulator-paginator > label').remove();
			
			return table;
		},
		showDetail : function(row) {
			const scheduleModal = $('#scheduleModal[target="schedule"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			scheduleModal.append(input);
			scheduleModal.modal('show');
			scheduleModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: '/api/gate/getGateScheduleList',
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		}
	});
});