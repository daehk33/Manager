$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function() {
			this.table = ControlTable.load();
			ControlButton.load();
			
			this.event();
		},
		event: function() {
			const table = this.table;
			
			// 버튼 공통 이벤트
			$('button[role="action"][target="device"]').click(function() {
				const selectedData = table.getSelectedData();
				let params = {};
				
				if(selectedData.length == 0) {
					Alert.warning({text: '제어할 장비를 선택해 주세요.'});
					return;
				}
				else {
					const action = this.dataset.action;
					
					if(action == 'lock'){
						params = {title: '게이트 잠금', text: '게이트를 잠그시겠습니까?', confirmText: '확인'};
						Alert.confirm(params, (result) => {
							if(!result.isConfirmed) return;
							
							console.log('lock!');
						});
					}
					else if(action == 'unlock'){
						params = {title: '게이트 잠금 해제', text: '게이트 잠금을 해제하시겠습니까?', confirmText: '확인'};
						Alert.confirm(params, (result) => {
							if(!result.isConfirmed) return;
							
							console.log('unlock!');
						});
					}
					else if(action == 'restart'){
						params = {title: '장비 재시작', text: '선택하신 장비들을 재시작 하시겠습니까?', confirmText: '확인'};
						let success = true;
						let rtnMsg = "";
						Alert.confirm(params, (result) => {
							if(!result.isConfirmed) return;
							
							for(data of selectedData) {
								let device_id = data.device_id;
								AjaxUtil.request({
									url: '/api/sls/updateSLSStatus',
									async: false,
									data: {
										device_key: data.device_key,
										device_id:data.device_id,
										status_info : '1'
									},
									done: function(data) {
										data = data.result;
										if(data.CODE != '200') {
											success = false;
											if(rtnMsg=="") rtnMsg += "["+device_id+"]" + data.DESC;
											else rtnMsg += "\n["+device_id+"]" + data.DESC;
										}
									}
								});
							}
							if(success)	Alert.success({text: "재시작 요청이 완료되었습니다!<br/>약 1분 이내에 장비가 재시작 됩니다."}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						});
					}
					else if(action == 'syncSetting'){
						let success = true;
						let rtnMsg = "";
						Alert.confirm({title: '정책 적용', text: '정책을 적용하시겠습니까?', confirmText: '확인'}, function(result) {
							if(!result.isConfirmed) {
								return;
							}
							
							for(data of selectedData) {
								AjaxUtil.request({
									url: '/api/device/sendDeviceRule',
									data: {
										device_key: data.device_key
									},
									done: function(data) {
										data = data.result;
										if(data.CODE != '200') {
											success = false;
											if(rtnMsg=="") rtnMsg += "["+device_id+"]" + data.DESC;
											else rtnMsg += "\n["+device_id+"]" + data.DESC;
										}
									}
								});
							}
							if(success)	Alert.success({text: "정책 적용이 완료되었습니다!"}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						});
					}
					else if(action == 'on') {
						let rtnMsg = '[Result]';
						Alert.confirm({title: '장비 부팅', text: '선택하신 장비들의 전원을 켜시겠습니까?', confirmText: '확인'}, (result) => {
							if(!result.isConfirmed) return;
							
							for(data of selectedData) {
								AjaxUtil.request({
									url: '/api/api/wol',
									data: {
										device_key: data.device_key
									},
									done: function(data) {
										data = data.result;
										if(dat.CODE != '200') {
											success = false;
											if(rtnMsg=="") rtnMsg += "["+device_id+"]" + data.DESC;
											else rtnMsg += "\n["+device_id+"]" + data.DESC;
										}
									}
								});
							}
							if(success)	Alert.success({text: "전원 ON 요청이 완료되었습니다!"}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						});
					}
					else if(action == 'off') {
						params = {title: '장비 종료', text: '선택하신 장비들의 전원을 끄시겠습니까?', confirmText: '확인'};
						let success = true;
						let rtnMsg = "";
						Alert.confirm(params, (result) => {
							if(!result.isConfirmed) return;
							
							for(data of selectedData) {
								let device_id = data.device_id;
								AjaxUtil.request({
									url: '/api/sls/updateSLSStatus',
									data: {
										device_key: data.device_key,
										device_id:data.device_id,
										status_info : '2'
									},
									done: function(data) {
										data = data.result;
										if(data.CODE != '200'){
											success = false;
											if(rtnMsg != '') rtnMsg += '\n'; 
											rtnMsg += `[${device_id}] ${data.DESC}`;
										}
									}
								});
							}
							if(success)	Alert.success({text: "전원 OFF 요청이 완료되었습니다!"}, Content.params.onHide);
							else Alert.warning({text: rtnMsg});
						});
					}
				}
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			$('button[role="action"][target="device"]').hide();
		}
	};
	
	const ControlTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			let selectableRows = [];
			
			const table = new Tabulator("#controlTable", {
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
					if(response.result.count == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("model_nm", {headerFilter: false});
						table.updateColumnDefinition("device_id", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("model_nm", {headerFilter: true});
						table.updateColumnDefinition("device_id", {headerFilter: true});
					}
					
					return response.result.data;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						width: 30, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "모델 유형", field: "model_type", headerSort: false, widthGrow: 1.5 },
					{ title: "모델명", field: "model_nm", headerSort: false, widthGrow: 1.5, headerFilterPlaceholder: " "  },
					{ title: "장비 아이디", field: "device_id", headerSort: false, widthGrow: 1, headerFilterPlaceholder: " "  },
					{ title: "요청 상태"	, field: "device_control_status_nm", widthGrow: 1, headerSort: false ,hozAlign: "center", headerHozAlign: "center", },
					{
						title: "장비 상태"	, field: "device_status", widthGrow: 2, headerSort: false, 
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const type = cell.getValue() == '1' ? 'success' : 'danger';
							const text = cell.getValue() == '1' ? '정상' : '오류';
							
							return `<font class="text-${type}" style="font-weight:bold">${text}</font>`;
						},
						hozAlign: "center", headerHozAlign: "center",
					},
					{
						title: "네트워크"	, field: "connect_yn", widthGrow: 2, headerSort: false, 
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							const type = cell.getValue() == 'Y' ? 'success' : 'danger';
							const text = cell.getValue() == 'Y' ? '정상' : '연결 끊김';
							
							return `<font class="text-${type}" style="font-weight:bold">${text}</font>`;
						},
						hozAlign: "center", headerHozAlign: "center",
					},
					{
						title: "정책 사용", field: "config_yn", widthGrow: 2, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() == 'Y' ?
									'<i class="far fa-circle text-success" style="font-weight:bold;"></i>' : '<i class="fa fa-times text-danger mt-1" style="font-size:20px;"></i>';
						},
						hozAlign: "center", headerHozAlign: "center",
					},
				],
				rowSelectionChanged: function(data, rows) {
					const modelKeySet = new Set(data.map(x => x.model_key));
					const modelNameSet = new Set(data.map(x => x.model_nm));
					const statusSet = new Set(data.map(x => x.device_status));
					const configSet = new Set(data.map(x => x.config_yn));
					
					let params = {};
					
					params.modelNameSet = modelNameSet;
					params.statusSet = statusSet;
					params.configSet = configSet;
					
					if(modelKeySet.size > 0) {
						$('button[role="action"][target="device"]').hide();
						
						const modelName = [...modelNameSet];
						
						const restartBtn = ControlButton.find(modelName, 'restart');
						const syncSettingBtn = ControlButton.find(modelName, 'syncSetting');
						const onBtn = ControlButton.find(modelName, 'on');
						const offBtn = ControlButton.find(modelName, 'off');
						
						params.modelName = modelName;
						params.restartBtn = restartBtn;
						params.syncSettingBtn = syncSettingBtn;
						params.onBtn = onBtn;
						params.offBtn = offBtn;
						
						ControlButton.display(params);
					} else {
						$('button[role="action"][target="device"]').hide();
					}
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
	
	const ControlButton = {
		list: [],
		load: function(model) {
			this.draw();
		},
		draw: function() {
			const that = this;
			const btnContainer = $('.card-title-action');
			
			AjaxUtil.request({
				url: '/api/device/getDeviceControlButtonList',
				async: false,
				done: function(data) {
					data = data.result.items;
					
					data.forEach(item => {
						let button = `<button class="btn btn-round btn-primary mr-2" role="action" target="device" data-model="${item.code_value}" data-action="${item.code_value2}">
										<span class="btn-label"><i class="${item.code_value3}"></i></span>${item.code_desc}
									</button>`;
						
						if(item.code_value2 == 'on') {
							button = `<button class="btn btn-round btn-primary mr-2" role="action" target="device" data-model="${item.code_value}" data-action="${item.code_value2}">
										<span class="btn-label">
											<svg class="svg-icon" viewBox="0 0 12.7 12.7">
												<path class="power-circle" d="M8.56 2.43a4.96 4.96 0 012.68 4.6h0a4.96 4.96 0 01-5.14 4.75 4.96 4.96 0 01-4.76-5.14 4.96 4.96 0 012.63-4.18" fill="none" stroke="#fff" stroke-width="1.6827288"/>
												<path class="power-bar" fill="#fff" fill-rule="evenodd" d="M5.45.08h1.72v4.74H5.45z"/>
											</svg>
										</span>${item.code_desc}
									</button>`;
						}
						else if(item.code_value2 == "syncSetting") {
							button = `<button class="btn btn-round btn-primary mr-2" role="action" target="device" data-model="${item.code_value}" data-action="${item.code_value2}">
								<span class="btn-label">
									<svg class="svg-icon" viewBox="0 0 12.7 12.7">
										<path class="arrow" d="M6.35 8.202l2.91-2.381M6.35 8.202L3.44 5.821m2.91-4.498v6.614" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/>
										<path class="holder" d="M1.058 10.224v1.852h10.584v-1.852" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/>
									</svg>
								</span>${item.code_desc}
							</button>`;
						}
						else if(item.code_value2 == "restart") {
							button = `<button class="btn btn-round btn-primary mr-2" role="action" target="device" data-model="${item.code_value}" data-action="${item.code_value2}">
								<span class="btn-label">
									<svg class="svg-icon" viewBox="0 0 12.7 12.7">
										<defs>
											<marker id="a" refX="0" refY="0" orient="auto" overflow="visible">
												<path d="M2.3 0l-3.45 2v-4L2.3 0z" fill-rule="evenodd" stroke="#fff" stroke-width=".4pt" fill="#fff"/>
											</marker>
										</defs>
										<path class="restart-bar" d="M31.36 8.56a17.87 17.87 0 0110.63 16.33h0a17.87 17.87 0 01-17.86 17.87A17.87 17.87 0 016.26 24.89a17.87 17.87 0 014.22-11.53" transform="scale(.26458)" fill="none" stroke="#fff" stroke-width="3.77" stroke-linecap="square" marker-end="url(#a)"/>
									</svg>
								</span>${item.code_desc}
							</button>`;
						}
						else if(item.code_value2 == 'off') {
							button = `<button class="btn btn-round btn-primary mr-2" role="action" target="device" data-model="${item.code_value}" data-action="${item.code_value2}">
								<span class="btn-label">
									<svg class="svg-icon" viewBox="0 0 12.7 12.7">
										<path class="power-circle" d="M8.56 2.43a4.96 4.96 0 012.68 4.6h0a4.96 4.96 0 01-5.14 4.75 4.96 4.96 0 01-4.76-5.14 4.96 4.96 0 012.63-4.18" fill="none" stroke="#fff" stroke-width="1.6827288"/>
										<path class="power-bar" fill="#fff" fill-rule="evenodd" d="M5.45.08h1.72v4.74H5.45z"/>
									</svg>
								</span>${item.code_desc}
							</button>`;
						}
						
						that.list.push({model: item.code_value, action: item.code_value2});
						
						btnContainer.append(button);
					});
				}
			});
		},
		find: function(model, action) {
			const targetBtn = $(`button[role="action"][target="device"][data-action="${action}"]`);
			let resultBtn;
			
			let modelArry = [];
			
			// 공용 버튼 유무 확인
			for(btn of targetBtn) {
				if(btn.dataset.model == "ALL") {
					resultBtn = btn;
				} else {
					modelArry.push(btn.dataset.model );
				}
			}
			
			// 공용 버튼 없을 시
			if(!resultBtn) {
				for(i in modelArry) {
					let results = [];
					
					for(name of model) {
						modelArry[i].includes(name) ? results.push(true) : results.push(false);
					}
					if(results.every(result => result)) {
						return $(targetBtn[i]);
					}
				}
			}
			
			return $(resultBtn);
		},
		display: function(params) {
			// 장비 상태 관련
			if(params.statusSet.size == 1) {
				const statusValue = [...params.statusSet];
				
				if(statusValue == 1) {
					params.restartBtn.show();
					params.offBtn.show();
					
					// 정책 사용 관련
					if(params.configSet.size == 1) {
						const configValue = [...params.configSet];
						
						configValue == 'Y' ? params.syncSettingBtn.show() : params.syncSettingBtn.hide();
					} else {
						params.syncSettingBtn.hide();
					}
				} else {
					// 장비 상태 연결 끊김일 시
					params.onBtn.show();
					params.restartBtn.hide();
					params.offBtn.hide();
				}
			} else {
				$('button[role="action"][target="device"]').hide();
			}
		}
	}
	
	Content.load({
		url: '/api/device/getDeviceInfoList',
		onHide: () => {
			Tabulator.prototype.findTable('#controlTable')[0].setData();
		}
	});
});