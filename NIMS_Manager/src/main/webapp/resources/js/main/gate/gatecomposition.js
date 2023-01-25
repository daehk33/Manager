$(function() {
	const Content = {
		params: {},
		gateTable: null,
		groupTable: null,
		bioTable: null,
		libList: null,
		buttonTable: null,
		load: function(params) {
			const that = this;
			that.params = params;
			
			// 버튼 전체 숨김 처리
			$('.card-title-action').children().hide();
			
			// 도서관 목록 불러오기
			AjaxUtil.request({
				url: '/api/library/getLibraryList',
				async: false,
				done: function(data) {
					data = data.result.data;
					
					if(data.length > 0) libList = '';
					
					data.forEach(lib => {
						libList += `<option value="${lib.rec_key}">${lib.library_nm}</option>`;
					});
					
					that.libList = libList;
				}
			});
			
			// 각 탭 도서관 목록 불러오기
			const librarySelect = $('.card[role="gateComposition"] .tab-pane div[role="select"] select');
			
			librarySelect.empty();
			librarySelect.append(Content.libList);
			
			// 탭 유형 불러오기
			const tabList = $('.card .nav-tabs[role="tablist"]');
			tabList.empty();
			
			AjaxUtil.request({
				url: '/api/code/getCodeList',
				async: false,
				data: {
					grp_key: '21',
					use_yn: 'Y'
				},
				done: function(data) {
					data = data.result.data.sort(function(a, b) {
						return a.rec_key - b.rec_key;
					});
					
					for(let tab of data) {
						let tabButton = `
							<button class="nav-link" data-target="${tab.code_value}" role="tab">
								${tab.code}
							</button>`;
						
						tabList.append(tabButton);
					}
					
					// 첫번째 탭 활성화
					const first = $(tabList.children()[0]);
					const target = first[0].dataset.target;
					first.addClass('active');
					
					$(`.tab-pane[data-target="${target}"][role="tabpanel"]`).addClass("active show");
				}
			});
			
			that.result();
		},
		result: function() {
			const that = this;
			
			// 입력장치 분류
			InputTab.load();
			
			// 게이트 추가
			that.gateTable = GateTable.load();
			
			// 그룹 추가
			that.groupTable = GroupTable.load();
			
			// 서버 설정
			ServerTab.load();
			
			// 게이트 버튼
			that.buttonTable = buttonTable.load();
			
			// 소방 설비
			FireTab.load();
			
			// 생체 인증
			that.bioTable =  BioTable.load();
			
			that.event();
		},
		event: function() {
			const that = this;
			
			const gateTable = this.gateTable;
			const groupTable = this.groupTable;
			const bioTable = this.bioTable;
			const buttonTable = this.buttonTable;
			
			// 탭 클릭 이벤트
			$('.nav-link[role="tab"]').click(function() {
				const target = this.dataset.target;
				
				// 각 탭 관련 버튼들 선언
				const titleAction = $('.card-title-action');
				const buttons = titleAction.children();
				const inputBtns = titleAction.find('[data-target="input"]');
				const gateBtns = titleAction.find('[data-target="gate"]');
				const groupBtns = titleAction.find('[data-target="group"]');
				const serverBtns = titleAction.find('[data-target="server"]');
				const buttonBtns = titleAction.find('[data-target="gateButton"]');
				const fireBtns = titleAction.find('[data-target="fireTab"]');
				const bioBtns = titleAction.find('[data-target="bio"]');
				
				// 전체 tab에서 active 클래스 제거
				$('.nav-link[role="tab"]').removeClass('active');
				
				// 현재 클릭한 tab에 active 클래스 추가
				$(this).addClass('active');
				
				// 현재 선택한 tab 관련 tab-pane 표시
				$('.tab-pane.fade').removeClass('active show');
				$(`.tab-pane.fade[data-target="${target}"]`).addClass('active show');
				
				// 버튼 전체 숨김 처리
				buttons.hide();
				if (target == 'inputDevices') {
					inputBtns.show();
				}
				else if (target == 'gateAdd') {
					gateBtns.show();
				}
				else if (target =='groupAdd') {
					groupBtns.show();
				}
				else if (target =='serverSetting') {
					serverBtns.show();
				}
				else if (target =='gateButton') {
					buttonBtns.show();
				}
				else if (target =='fireSetting') {
					fireBtns.show();
				}
				else if (target =='bioSecurity') {
					bioBtns.show();
				}
			});
			
			// 첫번째 탭 클릭 트리거
			$('.nav-link[role="tab"]:first-child').trigger("click");
			
			// card-title 버튼 클릭시
			$('.card-title-action [role="action"]').click(function() {
				const action = this.dataset.action;
				const target = this.dataset.target;
				
				// 입력장치 관련 버튼
				if(target == 'input') {
					if(action == 'add') {
						let confirmParam = {
							title: '시리얼 포트 추가', 
							text: '추가된 포트는 <b>[ 적용 ]</b> 버튼을 클릭해야 저장됩니다.<br/>포트명 예시) <b>COM0</b>', 
							input: 'text', 
							confirmText: '추가', 
							cancelText: '취소', 
							confirmClass: 'btn btn-primary'
						};
						
						Alert.confirm(confirmParam, function(result) {
							if(result.isConfirmed) {
								const portName = result.value;
								const container = $('.contain[data-target="list"]');
								
								let newPort = `
									<div id="draggable" class="draggable">
										<div class="el" id="el" data-port="${portName}" data-origin_type="list" data-target_type="">${portName}</div>
									</div>`;
								
								container.append(newPort);
								InputTab.setDraggable();
								
								Alert.success({title: '포트 추가가 완료되었습니다.', text: '추가하신 포트는 입력장치에 포트를 드래그 후 <br/><b>[ 적용 ]</b> 버튼을 클릭해야 저장됩니다.'});
							}
						}, false, function(port) {
							const reg_name = /^(COM)[0-9]{1,2}$/;
							
							AjaxUtil.request({
								url: '/api/gate/checkPortDuplicated',
								async: false,
								data: {
									port: port,
									library_key: $('#inputTab .div[role="select"] select').val()
								},
								done: function(data) {
									data = data.result;
									
									if(data.CODE == "200") {
										// 시리얼 포트명 유효성 검사
										if(!reg_name.test(port)) {
											Swal.showValidationMessage(`[ ${port} ] 은(는) 잘못된 포트명 입니다. 다시 입력해주세요!`);
										}
										// 이미 추가된 포트인지 확인
										else if($('#inputTab .contain').find(`.el[data-port="${port}"]`).length > 0) {
											Swal.showValidationMessage('입력하신 포트는 이미 등록된 포트입니다.');
										}
										else {
											return data;
										}
									}
									else {
										Swal.showValidationMessage('입력하신 포트는 이미 등록된 포트입니다.');
									}
								},
								error: function(e) {
									Swal.showValidationMessage(`요청 실패: ${e}`);
								}
							});
						});
					}
					else if(action == 'delete') {
						const portList = $('#inputTab .el.ui-selected');
						
						if(portList.length > 0) {
							Alert.confirm({text: '정말로 선택하신 포트들을 삭제하시겠습니까?', confirmText: '삭제', cancelText: '취소'}, function(result) {
								if(result.isConfirmed) {
									let results = [];
									
									for(port of portList) {
										AjaxUtil.request({
											url: '/api/gate/deleteInputInfo',
											async: false,
											data: {
												library_key: $('#inputTab div[role="select"] select').val(),
												port: port.dataset.port
											},
											done: function(data) {
												data = data.result;
												
												data.CODE == "200" ? results.push(true) : results.push(false);
											},
											error: function(error) {
												console.log(error);
											}
										});
									}
									
									if (results.every(result => result)) {
										Alert.success({text: '포트 삭제가 완료되었습니다!'}, Content.params.inputOnHide);
									}
									else {
										Alert.warning({text: '일부 항목 삭제 중 오류가 발생하였습니다.'}, Content.params.inputOnHide);
									}
								}
							})
						}
						else {
							Alert.warning({text: '삭제할 포트를 선택하신 후 다시 시도해주세요.'});
						}
					}
				}
				// 게이트 관련 버튼
				else if(target == 'gate') {
					// 추가 버튼 클릭
					if(action == 'add') {
						const deviceModal = $('[id="infoModal"][target="gate"]');
						
						const input = ParamManager.load('insert');
						deviceModal.append(input);
						
						deviceModal.modal('show');
					}
					// 삭제 버튼 클릭
					else if(action == 'delete') {
						let selectedData = gateTable.getSelectedData();
						
						if(selectedData.length == 0) {
							Alert.warning({text: '삭제하실 게이트를 선택해 주세요.'});
							return;
						}
						
						let confirmParams = {title: '게이트 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
						Alert.confirm(confirmParams, (result) => {
							if(!result.isConfirmed) {
								return;
							}
							
							let results = [];
							
							selectedData.forEach(item => {
								AjaxUtil.request({
									url: '/api/gate/deleteGateInfo',
									data: Object.assign({
										rec_key: item.gate_key,
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
							
							AjaxUtil.request({
								url: '/api/gate/deleteCommandInfo',
								data: Object.assign({
									rec_key: item.rec_key,
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
							
							if (results.every(result => result)) {
								Alert.success({text: '게이트가 성공적으로 삭제되었습니다.'}, Content.params.onHide);
							} else {
								Alert.danger({text: '일부 항목 삭제 처리 중 오류가 발생하였습니다.'});
							}
						});
					}
				}
				// 그룹 관련 버튼
				else if(target == 'group') {
					// 추가 버튼 클릭
					if(action == 'add') {
						const gateModal = $('[id="infoModal"][target="group"]');
						
						const input = ParamManager.load('insert');
						gateModal.append(input);
						
						gateModal.modal('show');
					}
					else if(action == 'setting') {
						const gateModal = $('[id="infoModal"][target="command"]');
						
						const input = ParamManager.load('insert');
						gateModal.append(input);
						
						gateModal.modal('show');
					}
					// 삭제 버튼 클릭
					else if(action == 'delete') {
						let selectedData = groupTable.getSelectedData();
						
						if(selectedData.length == 0) {
							Alert.warning({text: '삭제하실 그룹을 선택해 주세요.'});
							return;
						}
						
						let confirmParams = {title: '그룹 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
						Alert.confirm(confirmParams, (result) => {
							if(!result.isConfirmed) {
								return;
							}
							
							let results = [];
							
							selectedData.forEach(item => {
								AjaxUtil.request({
									url: '/api/gate/deleteGroupInfo',
									data: Object.assign({
										rec_key: item.rec_key,
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
					}
				}
				
				else if(target == 'server') {
					// 추가 버튼 클릭
					if(action =='submit') {
						// 서버 설정 적용
						const library_key = $('#serverTab div[role="select"] select').val();
						const type = $('#servertype').val();
						const server_ip = $('#server_ip').val();
						const port = $('#port').val();
						const data_source = $('#data_source').val();
						const user_id = $('#user_id').val();
						const pwd = $('#pwd').val();
						const use_smart = $('#use_smart').val();
						const smart_ip = $('#smart_ip').val();
						const smart_port = $('#smart_port').val();
						const smart_data_source = $('#smart_data_source').val();
						const smart_user_id = $('#smart_user_id').val();
						const smart_pwd = $('#smart_pwd').val();
						const gate_ip = $('#gate_ip').val();
						const gate_port = $('#gate_port').val();
						const gate_data_source = $('#gate_data_source').val();
						const gate_user_id = $('#gate_user_id').val();
						const gate_pwd = $('#gate_pwd').val();
						const position = $('#position').val();
						const manage_code = $('#manage_code').val();
						
						const inputValues = [library_key, type, server_ip, port, data_source, user_id, pwd, use_smart, smart_ip,
							smart_port, smart_data_source, smart_user_id, smart_pwd, gate_ip, gate_port, gate_data_source, gate_user_id,
							gate_pwd, position, manage_code];
						
						AjaxUtil.request({
							url: '/api/gate/upsertGateDBConn',
							data: Object.assign({
								library_key: library_key,
								type: type,
								server_ip: server_ip,
								port: port,
								data_source: data_source,
								user_id: user_id,
								pwd: pwd,
								use_smart: use_smart,
								smart_ip: smart_ip,
								smart_port: smart_port,
								smart_data_source: smart_data_source,
								smart_user_id: smart_user_id,
								smart_pwd: smart_pwd,
								gate_ip: gate_ip,
								gate_port: gate_port,
								gate_data_source: gate_data_source,
								gate_user_id: gate_user_id,
								gate_pwd: gate_pwd,
								position: position,
								manage_code: manage_code
							}),
							async: false,
							successMessage: '서버 설정이 적용되었습니다.',
							failMessage: '서버 설정중 오류가 발생하였습니다.'
						});
					}
				}
				
				// 게이트 버튼 관련 버튼
				else if(target == 'gateButton') {
					// 추가 버튼 클릭
					if(action =='add') {
						const gateModal = $('[id="infoModal"][target="gateButton"]');

						const input = ParamManager.load('insert');
						gateModal.append(input);

						gateModal.modal('show');
					}
					// 삭제 버튼 클릭
					else if(action == 'delete') {
						let selectedData = buttonTable.getSelectedData();

						if(selectedData.length == 0) {
							Alert.warning({text: '삭제하실 그룹번호를 선택해 주세요.'});
							return;
						}

						let confirmParams = {title: '게이트버튼 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
						Alert.confirm(confirmParams, (result) => {
							if(!result.isConfirmed) {
								return;
							}

							let results = [];

							selectedData.forEach(item => {
								AjaxUtil.request({
									url: '/api/gate/deleteBtnInfo',
									data: Object.assign({
										rec_key: item.rec_key,
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
					}
				}
				
				// 생체 인증 관련 버튼
				else if(target == 'bio') {
					// 추가 버튼 클릭
					if(action =='add') {
						const gateModal = $('[id="infoModal"][target="security"]');
						
						const input = ParamManager.load('insert');
						gateModal.append(input);
						
						gateModal.modal('show');
					}
					// 명령어 설정 버튼
					else if(action == 'setting') {
						const gateModal = $('[id="infoModal"][target="bioCommand"]');
						
						const input = ParamManager.load('insert');
						gateModal.append(input);
						
						gateModal.modal('show');
					}
					// 삭제 버튼 클릭
					else if(action == 'delete') {
						let selectedData = bioTable.getSelectedData();
						
						if(selectedData.length == 0) {
							Alert.warning({text: '삭제하실 그룹번호를 선택해 주세요.'});
							return;
						}
						
						let confirmParams = {title: '생체인증 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
						Alert.confirm(confirmParams, (result) => {
							if(!result.isConfirmed) {
								return;
							}
							
							let results = [];
							
							selectedData.forEach(item => {
								AjaxUtil.request({
									url: '/api/gate/deleteSecurityInfo',
									data: Object.assign({
										rec_key: item.rec_key,
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
					}
				}
				
				else if(target == 'fireTab') {
					// 추가 버튼 클릭
					if(action =='submit') {
						const action = this.dataset.action;
						if(action == "submit") {
							const name_id = $('#fireChild').children('div').children('input ,select');
							
							for(item of name_id) {
								let rule_name = item.id;
								let rule_value = item.value;
								let category = item.dataset.category;
								let library_key = $('#fireTab div[role="select"] select').val();
								let inputValues = [library_key, category, rule_name, rule_value];
								
								AjaxUtil.request({
									url: '/api/gate/upsertCfgRuleInfo',
									async: false,
									data: {
										library_key: library_key,
										rule_name: rule_name,
										category: category,
										rule_value: rule_value,
									},
									successMessage: '소방 설비 설정이 적용되었습니다.',
									failMessage: '소방 설비 설정중 오류가 발생하였습니다.'
								});
							}
						}
					}
				}
			});
			
			// 도서관 선택
			$('input[name="library"]').change(function(){
				InputTab.load();
				gateTable.setData();
				groupTable.setData();
				ServerTab.load();
				FireTab.load();
				bioTable.setData();
				buttonTable.setData();
			});
			
			// 입력장치 분류 도서관 선택
			$('#inputTab div[role="select"] select').change(function() {
				InputTab.load();
			});
			
			// 서버 설정 분류 도서관 선택
			$('#serverTab div[role="select"] select').change(function() {
				ServerTab.load();
			});
			
			// 서버 설정 분류 도서관 선택
			$('#fireTab div[role="select"] select').change(function() {
				FireTab.load();
			});
		}
	};
	
	const InputTab = {
		selected: {},
		load: function() {
			const that = this;
			const librarySelect = $('#inputTab div[role="select"] select');
			const portList = $('.contain[data-target="list"]');
			let library_key = sessionStorage.getItem("library_key");
			let serverYn = "N";
			
			// 도서관 선택 표시 여부
			if(!library_key) {
				library_key = librarySelect.val();
				librarySelect.show();
			}
			else {
				librarySelect.val(library_key);
				librarySelect.hide();
			}
			
			that.library_key = library_key;
			
			// 시리얼 포트 목록 불러오기
			portList.empty();
			
			AjaxUtil.request({
				url: '/api/library/checkLibServer',
				async: false,
				data: {
					rec_key: library_key
				},
				done: function(data) {
					serverYn = data.server_yn;
				}
			});
			
			// 선택한 도서관이 서버로 사용되는 경우 API 호출
			if(serverYn == "Y") {
				AjaxUtil.request({
					url: '/api/api/serialService',
					async: false,
					done: function(data) {
						data = data.result;
						
						for(item of data) {
							let list = `<div class="draggable" draggable="true">
								<div class="el" id="el" data-port="${item}">${item}</div>
								</div>`;
							
							portList.append(list);
						}
					},
					error: function(error) {
						console.log(error);
					}
				});
			}
			
			// 입력장치 분류 목록 불러오기
			$('.contain[role="one"]').empty();
			
			AjaxUtil.request({
				url: '/api/gate/getInputList',
				async: false,
				data: {
					library_key: that.library_key
				},
				done: function(data) {
					data = data.result.items;
					
					if(data.length > 0) {
						data.forEach(item => {
							let draggable = `
								<div id="draggable" class="draggable">
									<div class="el" id="el" data-port="${item.port}" data-origin_type="${item.type}" data-target_type="" data-is_changed="false">${item.port}</div>
								</div>`;
							
							// 이미 등록되어있는 포트는 해당 입력장치에 할당
							$(`.contain[data-target="${item.type}"]`).append(draggable);
							$(`.contain[data-target="list"] [data-port="${item.port}"]`).parent().remove();
						});
					}
				},
				error: function(error) {
					console.log(error);
				}
			});
			
			that.event();
		},
		event: function() {
			const that = this;
			
			// 적용 버튼 클릭
			$('#inputTab .btn[role="action"]').click(function() {
				const action = this.dataset.action;
				const inputModal = $('[id="infoModal"][target="input"]');
				
				// Modal쪽으로 넘겨줄 데이터
				let param = {};
				
				param.type = action;
				param.library_key = $('#inputTab div[role="select"] select').val();
				
				// 적용 대상 입력장치에 위치한 기존 포트 목록
				const portList = $(`.contain[data-target="${action}"] .el[data-is_changed="false"]`);
				param.portList = that.setList(portList);
				
				// 적용 대상 입력장치에 위치한 포트 중 변경 사항이 있는 포트 목록
				const changeList = $(`.contain[data-target="${action}"] .el[data-is_changed="true"]`);
				param.changeList = that.setList(changeList);
				
				// 적용 대상 입력장치에 포함되있던 포트 중 시리얼 포트 목록에 위치한 포트 목록
				const delList = $(`.contain[data-target="list"] .el[data-is_changed="true"][data-origin_type="${action}"]`);
				param.deleteList = that.setList(delList);
				
				let input = ParamManager.load('insert', JSON.stringify(param));
				
				inputModal.append(input);
				inputModal.modal('show');
			});
			
			that.setDraggable();
		},
		setDraggable: function() {
			const that = this;
			let isRevert = true;
			let item = null;
			let currentDrop = null;
			
			// Droppable 설정
			$('#inputTab .contain').droppable({
				accept: '.draggable',
				drop: function(e, ui) {
					const targetDrop = e.target;
					const itemData = $(item).find('.el');
					const targetType = targetDrop.dataset.target;
					
					// 포트를 Drop한 위치가 drop전 위치와 다른 경우
					if(currentDrop[0].dataset.target != targetType) {
						targetDrop.append(item);
						
						isRevert = false;
					}
					else {
						isRevert = true;
					}
					
					// 포트의 초기 위치와 drop한 위치가 다른 경우
					if(itemData[0].dataset.origin_type != targetType) {
						itemData[0].dataset.is_changed = true;
						itemData[0].dataset.target_type = targetType;
					}
					else {
						itemData[0].dataset.is_changed = false;
						itemData[0].dataset.target_type = '';
					}
				}
			});
			
			// Draggable 설정
			$('#inputTab .draggable').draggable({
				cursor: 'grab',
				zIndex: 1,
				opacity: 0.5,
				scroll: false,
				create: function(e, ui) {
					const target = $(e.target).find('.el');
					
					$(target).click(function(event) {
						let item = $(this);
						let port = item[0].dataset.port;
						
						if(item.is('.ui-selected')) {
							if(event.ctrlKey) {
								item.removeClass('ui-selected');
								
								delete that.selected[port];
							}
							else {
								// 선택된 목록 초기화
								that.selected = {};
								
								if($('#inputTab .draggable').children('.draggable .el.ui-selected').length > 1) {
									item.parent().siblings().each(function() {
										$(this).find('.el').removeClass('ui-selected');
									});
									
									that.selected[port] = port;
								}
								else {
									item.removeClass('ui-selected');
								}
							}
						}
						else {
							item.addClass('ui-selected');
							that.selected[port] = port;
							
							if(!event.ctrlKey) {
								that.selected = {};
								that.selected[port] = port;
								
								item.parent().siblings().each(function() {
									$(this).find('.el').removeClass('ui-selected');
								});
							}
							else {
								that.selected[port] = port;
							}
						}
					});
				},
				revert: function(e, ui) {
					// Drop 가능한 영역 외에 drop하는경우 false return
					if(e) {
						return isRevert;
					}
					else {
						return true;
					}
				},
				helper: function() {
					let clone = $(this).clone();
					
					return clone;
				},
				start: function(e, ui) {
					// Drag 대상 및 Drag 시작 지점 설정
					item = e.currentTarget;
					currentDrop = $(item).parent();
					
					let selectable = $(item).find('.el');
					let port = selectable[0].dataset.port;
					
					if(!selectable.is('.ui-selected')) {
						that.selected = {};
						
						selectable.addClass('ui-selected');
						that.selected[port] = port;
					}
				}
			});
			
			// Selectable 설정
			$('#inputTab .contain').selectable({
				filter: ".el",
				start: function(e, ui) {
					if(!e.ctrlKey) {
						that.selected = {};
					}
				},
				selecting: function(e, ui) {
					let item = $(ui[Object.keys(ui)[0]]);
					let port = item[0].dataset.port;
					
					item.addClass("ui-selected");
					that.selected[port] = port;
				},
				unselecting: function(e, ui) {
					let item = $(ui[Object.keys(ui)[0]]);
					let port = item[0].dataset.port;
					
					item.removeClass("ui-selected");
					delete that.selected[port];
				},
				stop: function(e, ui) {
					console.log(that.selected);
				}
			});
		},
		setList: function(list) {
			let arry = [];
			
			for(item of list) {
				arry.push(item.dataset.port);
			}
			
			return arry;
		}
	};
	
	const GateTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#gateTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: 'middle',
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 장비 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.port,
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
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.itemsCount);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.items;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" },
						hozAlign: "center", widthGrow: 0.3, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "장비 아이디", field: "description", headerSort: false},
					{ title: "포트", field: "controller_port", headerSort: false},
					{ title: "층", field: "location", headerSort: false},
					{ title: "장비 IP", field: "ip", headerSort: false }
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
			const gateModal = $('[id="infoModal"][target="gate"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			gateModal.append(input);
			
			gateModal.modal('show');
			
			gateModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	const GroupTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#groupTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: 'middle',
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 장비 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.gp,
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
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="groupcount"]').text(response.result.itemsCount);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.items;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" },
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "포트번호", field: "device_port", headerSort: false, widthGrow: 2, },
					{ title: "게이트번호", field: "gate", headerSort: false, widthGrow: 2 },
					{ title: "명령어", field: "command", headerSort: false, widthGrow: 2 },
					{ title: "방향", field: "direction", headerSort: false, widthGrow: 2 },
					{ title: "설명", field: "description", headerSort: false, widthGrow: 2 },
					
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
			const groupModal = $('[id="infoModal"][target="group"]');
			let data = row.getData();
			
			const groupinput = ParamManager.load('modify', JSON.stringify(data));
			groupModal.append(groupinput);
			
			groupModal.modal('show');
			
			groupModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	const ServerTab = {
		load: function() {
			const that = this;
			const librarySelect = $('#serverTab div[role="select"] select');
			let library_key = sessionStorage.getItem("library_key");
			that.library_key = library_key;
			
			// 도서관 선택 표시 여부
			if(!library_key) {
				library_key = librarySelect.val();
				librarySelect.show();
			}
			else {
				librarySelect.val(library_key);
				librarySelect.hide();
			}
			
			that.library_key = library_key;
			
			$('#serverTab .contain').find('input,select').each(function() {
				$(this).val("");
			});
			
			// LAS 목록 추가
			let lasList = "";
			
			AjaxUtil.request({
				url: '/api/code/getCodeList',
				async: false,
				data: {
					grp_key: '20',
					use_yn: 'Y'
				},
				done: function(data) {
					data = data.result.data;
					
					lasList = data;
				}
			});
			
			for(let las of lasList) {
				let option = `<option value="${las.code_value}">${las.code_value}</option>`
				
				$('#servertype').append(option);
			}
			
			// 서버설정 불러오기
			AjaxUtil.request({
				url: '/api/gate/getGateDBConnInfo',
				async: false,
				data: {
					library_key: that.library_key
				},
				done: function(data) {
					data = data.result.items;
					
					if(data.length > 0)
					data.forEach(dbc => {
						$('#servertype').val(`${dbc.type}`);
						$('#server_ip').val(`${dbc.server_ip}`);
						$('#port').val(`${dbc.port}`);
						$('#data_source').val(`${dbc.data_source}`);
						$('#user_id').val(`${dbc.user_id}`);
						$('#pwd').val(`${dbc.pwd}`);
						$('#gate_ip').val(`${dbc.gate_ip}`);
						$('#gate_port').val(`${dbc.gate_port}`);
						$('#gate_user_id').val(`${dbc.gate_user_id}`);
						$('#gate_data_source').val(`${dbc.gate_data_source}`);
						$('#gate_pwd').val(`${dbc.gate_pwd}`);
						$('#smart_ip').val(`${dbc.smart_ip}`);
						$('#smart_port').val(`${dbc.smart_port}`);
						$('#smart_data_source').val(`${dbc.smart_data_source}`);
						$('#smart_user_id').val(`${dbc.smart_user_id}`);
						$('#smart_pwd').val(`${dbc.smart_pwd}`);
						$('#position').val(`${dbc.position}`);
						$('#use_smart').val(`${dbc.use_smart}`);
						$('#manage_code').val(`${dbc.manage_code}`);
					});
				},
				error: function(error) {
					console.log(error);
				}
			});
			
			// IP input mask 설정
			$('#server_ip').inputmask({
				alias: "ip",
				placeholder: ' ',
				showMaskOnHover: false,
				showMaskOnFocus: false
			});
			$('#smart_ip').inputmask({
				alias: "ip",
				placeholder: ' ',
				showMaskOnHover: false,
				showMaskOnFocus: false
			});
			$('#gate_ip').inputmask({
				alias: "ip",
				placeholder: ' ',
				showMaskOnHover: false,
				showMaskOnFocus: false
			});
			
		}
	};
	
	const buttonTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#buttonTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: 'middle',
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 장비 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.button,
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
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="groupcount"]').text(response.result.itemsCount);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.items;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" },
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "버튼이름", field: "btn_name", headerSort: false, widthGrow: 2, },
					{ title: "게이트번호", field: "gate", headerSort: false, widthGrow: 2 },
					{ title: "명령어", field: "command", headerSort: false, widthGrow: 2 },
					{ title: "방향", field: "io_type", headerSort: false, widthGrow: 2 },
					{ title: "open", field: "open", headerSort: false, widthGrow: 2 },
					{ title: "close", field: "close", headerSort: false, widthGrow: 2 },
					{ title: "on", field: "on", headerSort: false, widthGrow: 2 },
					{ title: "off", field: "off", headerSort: false, widthGrow: 2 },
					{ title: "reset", field: "reset", headerSort: false, widthGrow: 2 },
					{ title: "forced_close", field: "forced_close", headerSort: false, widthGrow: 2 },
					
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
			const buttonModal = $('[id="infoModal"][target="gateButton"]');
			let data = row.getData();
			
			const buttoninput = ParamManager.load('modify', JSON.stringify(data));
			buttonModal.append(buttoninput);
			
			buttonModal.modal('show');
			
			buttonModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	const FireTab = {
		load: function() {
			const that = this;
			const librarySelect = $('#fireTab div[role="select"] select');
			let library_key = sessionStorage.getItem("library_key");
			that.library_key = library_key;
			// 도서관 선택 표시 여부
			if(!library_key) {
				library_key = librarySelect.val();
				librarySelect.show();
			}
			else {
				librarySelect.val(library_key);
				librarySelect.hide();
			}
			
			that.library_key = library_key;
			
			//초기화
			$('#fireChild').find('input,select').each(function() {
				$(this).val("");
			});
			
			// 소방설비설정 불러오기
			AjaxUtil.request({
				type: 'POST',
				data: {
					library_key: that.library_key
				},
				url: '/api/gate/getCfgRuleInfo',
				async: false,
				done: function(data) {
					data = data.result.items;
					if(data.length > 0 )
					data.forEach(fire => {
						if(fire.rule_name == 'OPEN_MODE'){
							$('#OPEN_MODE').val(`${fire.rule_value}`);
						}
						else if(fire.rule_name == 'RETURN_MODE'){
							$('#RETURN_MODE').val(`${fire.rule_value}`);
						}
						else if(fire.rule_name == 'PORT_NAME' ){
							$('#PORT_NAME').val(`${fire.rule_value}`);
						}
						else if(fire.category == 'contact' && fire.rule_name == 'CONTROL'){
							$('#CONTROL').val(`${fire.rule_value}`);
						}
						else if(fire.category == 'contact' && fire.rule_name == 'USE'){
							$('#USE').val(`${fire.rule_value}`);
						}
						
					});
					
				},
				error: function(error) {
					console.log(error);
				}
			});
			
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			that.event();
		},
		event: function() {
			
			$('#PORT_NAME, #OPEN_MODE, #RETURN_MODE').on('change',function() {
				const PORT_NAME = document.querySelector('#PORT_NAME');
				const OPEN_MODE = document.querySelector('#OPEN_MODE');
				const RETURN_MODE = document.querySelector('#RETURN_MODE');
				
				const portvalue = $('#PORT_NAME').val();
				const openvalue = $('#OPEN_MODE').val();
				const returnvalue = $('#RETURN_MODE').val();
				
				PORT_NAME.value = portvalue;
				OPEN_MODE.value = openvalue;
				RETURN_MODE.value = returnvalue;
			});
			
		}
	};
	
	const BioTable = {
		load: function() {
			return this.draw();
		},
		draw : function() {
			const that = this;
			
			const table = new Tabulator("#bioTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: 'middle',
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 장비 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.bio,
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
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.itemsCount);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.items;
				},
				
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" },
						hozAlign: "center", widthGrow: 0.3, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "그룹번호", field: "group_no", headerSort: false, widthGrow: 2, },
					{ title: "Device Serial Number", field: "device_sn", headerSort: false, widthGrow: 2, },
					{ title: "Token	", field: "access_token", headerSort: false, widthGrow: 2 },
					{ title: "API_URL", field: "api_url", headerSort: false, widthGrow: 2 },
					
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
			const gateModal = $('[id="infoModal"][target="security"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			gateModal.append(input);
			
			gateModal.modal('show');
			
			gateModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	Content.load({
		url: {
			port: '/api/gate/getGateInfoList',
			gp: '/api/gate/getGroupList',
			bio: '/api/gate/getSecurityList',
			button: '/api/gate/getBtnList',
		},
		onHide: () => {
			Tabulator.prototype.findTable('#gateTable')[0].setData();
			Tabulator.prototype.findTable('#groupTable')[0].setData();
			Tabulator.prototype.findTable('#bioTable')[0].setData();
			Tabulator.prototype.findTable('#buttonTable')[0].setData();
		},
		inputOnHide: () => {
			$('#inputTab div[role="select"] select').trigger('change');
		}
	});
});
