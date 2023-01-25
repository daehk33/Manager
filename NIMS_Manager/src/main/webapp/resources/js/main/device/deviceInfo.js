$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function() {
			this.table = DeviceTable.load();
			this.event();
		},
		event: function() {
			const table = this.table;
			
			// 등록 버튼 클릭시
			$('[role="add"]').click(function() {
				const deviceModal = $('[id="infoModal"][target="device"]');
				
				const input = ParamManager.load('insert');
				deviceModal.append(input);
				
				deviceModal.modal('show');
			});
			
			// 삭제 버튼 클릭시
			$('[role="delete"]').click(function() {
				let selectedData = table.getSelectedData();
				
				if(selectedData.length == 0) {
					Alert.warning({text: '삭제하실 장비를 선택해 주세요.'});
					return;
				}
				
				let confirmParams = {title: '정말로 삭제하시겠습니까?', text: '장비 삭제 시 해당 장비의 정책도 같이 삭제됩니다.', confirmText: '삭제'}
				Alert.confirm(confirmParams, (result) => {
					if(!result.isConfirmed) {
						return;
					}
					
					let results = [];
					
					selectedData.forEach(item => {
						AjaxUtil.request({
							url: '/api/device/deleteDeviceInfo',
							data: Object.assign({
								rec_key: item.device_key,
							}),
							done: function(data) {
								data = data.result;
								
								if(data.CODE == "200") {
									results.push(true);
								} else {
									results.push(false);
								}
							},
							error: function(error){
								Alert.danger({text: '잠시 후 다시 시도해주세요.'});
							}
						});
					});
					
					if (results.every(result => result)) {
						Alert.success({text: '삭제가 완료되었습니다!'}, Content.params.onHide);
					} else {
						Alert.danger({text: '일부 항목 삭제 처리 중 오류가 발생하였습니다.'});
					}
				});
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "장비 목록_("+common.getDateToStr()+").xlsx";
				Excel.download(target, table, file_name);
			});
		}
	};
	
	const DeviceTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#deviceTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: 'middle',
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(장비<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 장비 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					
					if(model_auth != '') {
						url += `&model_auth=${model_auth}`;
					}
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0){
						$('.card-title-count').hide();
						table.updateColumnDefinition("device_id", {headerFilter: false});
						table.updateColumnDefinition("device_nm", {headerFilter: false});
						table.updateColumnDefinition("device_location", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("device_id", {headerFilter: true});
						table.updateColumnDefinition("device_nm", {headerFilter: true});
						table.updateColumnDefinition("device_location", {headerFilter: true});
					}
					
					return response.result.data;
				},
				/* 컬럼 헤더, 컬럼 가운데 정렬 시 사용
				headerHozAlign: "center",
				cellHozAlign: "center",
				*/
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "도서관명", field: "library_nm", headerSort: false, widthGrow: 2, visible: grade != 0 },
					{ title: "모델명", field: "model_nm", headerSort: false, widthGrow: 2 },
					{ title: "모델 유형", field: "model_type", headerSort: false, widthGrow: 2 },
					{ title: "장비 아이디", field: "device_id", headerSort: false, widthGrow: 3, headerFilterPlaceholder: " " },
					{ title: "장비명", field: "device_nm", headerSort: false, widthGrow: 2, headerFilterPlaceholder: " " },
					{ title: "위치", field: "device_location", headerSort: false, widthGrow: 1.5, headerFilterPlaceholder: " " },
					{
						title: "정책 사용", field: "config_yn", widthGrow: 1.5, headerSort: false, hozAlign: "center", headerHozAlign: "center", cellHozAlign: "center",
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() == 'Y' ? 
									'<i class="far fa-circle text-success" style="font-weight:bold;"></i>' : '<i class="fa fa-times text-danger mt-1" style="font-size:20px;"></i>';
						}
					},
					{ title: "장비 IP", field: "device_ip", widthGrow: 2, headerSort: false },
					{ title: "비고", field: "device_desc", widthGrow: 3, headerSort: false }
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showDeviceDetail(row);
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
		showDeviceDetail : function(row) {
			const deviceModal = $('[id="infoModal"][target="device"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			deviceModal.append(input);
			
			deviceModal.modal('show');
			
			deviceModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	Content.load({
		url: '/api/device/getDeviceInfoList',
		onHide: () => {
			Tabulator.prototype.findTable('#deviceTable')[0].setData();
		}
	});
});