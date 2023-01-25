$(function() {
	const Content = {
		mode: null,
		sock: null,
		load: function() {
			const that = this;
			
			// 출입게이트 제어 모드 설정 조회
			AjaxUtil.request({
				url: '/api/system/getSystemRule',
				async: false,
				data: { rule_id: 'R0008' },
				done: function(data) {
					data = data.result.info;
					
					if(data) {
						that.mode = data.option_1;
					}
				}
			});
			
			// 제어 모드 설정이 없는 경우 기본 값으로 0 입력
			if(that.mode.length == 0) {
				that.mode = 0;
			}
			
			$('.ctrl-container')[0].dataset.mode = that.mode;
			
			that.result();
		},
		result: function() {
			const that = this;
			
			Gate.load();
			Control.load();
			
			that.event();
		},
		event: function() {
			const that = this;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Gate.load();
			});
		}
	};
	
	const Gate = {
		library_key: null,
		list: [],
		selected: {},
		load: function() {
			const that = this;
			const deviceContent = $('.ctrl[role="device"]');
			
			that.library_key = sessionStorage.getItem('library_key');
			
			let list = [];
			
			// 등록된 게이트 목록 조회
			AjaxUtil.request({
				url: '/api/gate/getGateList',
				async: false,
				data: {
					library_key: that.library_key
				},
				done: function(data) {
					data = data.result;
					
					list = data.items || [];
					
					that.list = list;
				}
			});
			
			// 선택 가능 도서관 체크
			let chkLib = true;
			
			if($('.sb-lib-menu').children().length > 0 ) {
				if(!that.library_key) {
					chkLib = false;
				}
			}
			
			// 장비 Location 배열 생성
			let locArry = [...new Set(list.map(x => chkLib ? 
					{library_key: x.library_key, library_nm: x.library_nm, value: x.location, name: x.location} : 
					{library_key: x.library_key, library_nm: x.library_nm, value: x.location, name: x.location + ' [' + x.library_nm + ']'}
			))];
			
			// 중복 제거
			locArry = locArry.reduce((acc, current) => {
				const x = acc.find(item => item.name === current.name);
				if (!x) {
					return acc.concat([current]);
				} else {
					return acc;
				}
			}, []);
			
			if(locArry.length > 0) {
				deviceContent.empty();
				
				for(item of locArry) {
					// location fieldset 생성
					let locFieldset = $(`
						<fieldset class="ctrl-fieldset" data-target="${item.value}">
							<legend>
								${item.name}
								<div class="btn-round btn-sm btn-light ml-2" data-library_key="${item.library_key}" data-location="${item.value}" role="button" target="ALL">
									전체
								</div>
							</legend>
						</fieldset>`);
					
					let devices = list.filter(x => (x.location == item.value && x.library_key == item.library_key));
					
					for(device of devices) {
						let selectable = `
							<div class="ctrl-item" role="device" data-library_key="${device.library_key}" data-location="${item.value}" data-rec_key="${device.rec_key}" data-description="${device.description}">
								${device.description}
							</div>`;
						
						locFieldset.append(selectable);
					}
					deviceContent.append(locFieldset);
				}
				
				// 데이터가 있는 경우 selectable 생성 및 소켓 연결
				Content.mode == 0 && that.setSelectable();
				Content.sock = Socket.load();
			}
			else {
				deviceContent.empty();
				Alert.warning({text: "현재 도서관에 등록된 출입게이트가 없습니다.<br/><b>[ 장비 운영 환경 > 출입게이트 구성 ]</b>페이지에서<br/> 출입게이트를 등록하신 후 이용해주세요.", time: 10});
				
				Content.sock && Content.sock.disconnect();
			}
			
			that.event();
		},
		event: function() {
			const that = this;
			
			// 장비 우선 모드
			if(Content.mode == 0) {
				// 전체 선택 버튼 클릭
				$('div[role="button"][target="ALL"]').click(function() {
					const dataset = this.dataset;
					const libraryKey = dataset.library_key;
					const location = dataset.location;
					
					const gates = $(`.ctrl-item[data-library_key="${libraryKey}"][data-location="${location}"]`);
					const selected = $(`.ctrl-item.ui-selected[data-library_key="${libraryKey}"][data-location="${location}"]`);
					
					const allCnt = gates.length;
					const selectedCnt = selected.length;
					
					if(allCnt == selectedCnt) {
						gates.removeClass('ui-selected');
						
						for(gate of gates) {
							that.selectManager(gate.dataset, "remove");
						}
					}
					else {
						gates.addClass('ui-selected');
						
						for(gate of gates) {
							that.selectManager(gate.dataset, "add");
						}
					}
				});
			}
			// 제어 우선 모드
			else if(Content.mode == 1) {
				// 전체 버튼 클릭
				$('div[role="button"][target="ALL"]').click(function() {
					let dataset = this.dataset;
					let key = dataset.library_key;
					let location = dataset.location;
					
					const ctrlField = $('.ctrl-field[data-use="true"]');
					
					if(ctrlField.length == 0 ) {
						Alert.warning({text: "상단에서 요청할 제어를 선택하신 후 다시 시도해주세요."}, function() {
							Animation.load($('.ctrl[role="control"]'), 'animate__animated animate__flash');
						});
					}
					else {
						let action = "";
						let actionName = "";
						
						if(ctrlField[0].role == "button") {
							action = ctrlField[0].dataset.action;
						}
						else {
							action = "MODE," + ctrlField.val();
						}
						
						actionName = action == "OPEN" ? "개방" :
									 action == "CLOSE" ? "복귀" :
									 action == "ON" ? "전원 ON" :
									 action == "OFF" ? "전원 OFF" :
									 action == "RESET" ? "초기화" :
									 action == "FORCED_CLOSE" ? "강제 폐쇄" : "운용 모드 변경";
						
						Alert.confirm({title: '출입게이트 제어', text: `정말 <b>[${location}]</b> 전체 출입게이트를 <b>[${actionName}]</b> 하시겠습니까?`}, (result) => {
							if(result.isConfirmed) {
								const gates = $(`.ctrl-item[data-library_key="${key}"][data-location="${location}"]`);
								
								for(gate of gates) {
									let dataset = gate.dataset;
									let data = Socket.createData(dataset, action);
									
									Socket.sendMessage(data);
								}
							}
						});
					}
				});
				
				// 장비 클릭
				$('.ctrl-item[role="device"]').click(function() {
					let dataset = this.dataset;
					
					const ctrlField = $('.ctrl-field[data-use="true"]');
					
					if(ctrlField.length == 0) {
						Alert.warning({text: "상단에서 요청할 제어를 선택하신 후 다시 시도해주세요."}, function() {
							Animation.load($('.ctrl[role="control"]'), 'animate__animated animate__flash');
						});
					}
					else {
						let action = "";
						let actionName = "";
						
						if(ctrlField[0].role == "button") {
							action = ctrlField[0].dataset.action;
						}
						else {
							action = "MODE," + ctrlField.val();
						}
						
						actionName = action == "OPEN" ? "개방" :
							 action == "CLOSE" ? "복귀" :
							 action == "ON" ? "전원 ON" :
							 action == "OFF" ? "전원 OFF" :
							 action == "RESET" ? "초기화" :
							 action == "FORCED_CLOSE" ? "강제 폐쇄" : "운용 모드 변경";
						
						Alert.confirm({title: '출입게이트 제어', text: `정말 <b>[${dataset.location}] [${dataset.description}]</b> 출입게이트를 <b>[${actionName}]</b> 하시겠습니까?`}, (result) => {
							if(result.isConfirmed) {
								let data = Socket.createData(dataset, action);
								
								Socket.sendMessage(data);
							}
						});
					}
				});
			}
		},
		setSelectable: function() {
			const that = this;
			
			$('.ctrl[role="device"]').selectable({
				filter: '.ctrl-item',
				cancel: 'div[role="button"]',
				start: function(e, ui) {
					if(!e.ctrlKey) {
						that.selected = {};
					}
				},
				selecting: function(e, ui) {
					const item = $(ui[Object.keys(ui)[0]]);
					
					that.selectManager(item[0].dataset, "add");
					item.addClass("ui-selected");
				},
				unselecting: function(e, ui) {
					const item = $(ui[Object.keys(ui)[0]]);
					
					that.selectManager(item[0].dataset, "remove");
					item.removeClass("ui-selected");
				}
			});
		},
		selectManager: function(dataset, action) {
			const that = this;
			
			const library_key = dataset.library_key;
			const description = dataset.description;
			
			if(action == "add") {
				that.selected[`${library_key}-${description}`] = dataset;
			}
			else if(action == "remove") {
				delete that.selected[`${library_key}-${description}`];
			}
		}
	}
	
	const Control = {
		mode: null,
		load: function() {
			const that = this;
			that.mode = Content.mode;
			
			const select = $('select[role="select"][data-action="mode"]');
			
			// 출입게이트 운영모드 조회
			AjaxUtil.request({
				url: '/api/code/getCodeList',
				async: false,
				data: {
					grp_key: '19',
					code_desc: '운용 모드'
				},
				done: function(data) {
					data = data.result.data.sort(function(a, b) {
						return a.code - b.code;
					});
					
					for(item of data) {
						let option = `<option value="${item.code}">${item.code_value}</option>`;
						
						select.append(option);
					}
				}
			});
			
			that.result();
		},
		result: function() {
			const that = this;
			
			const modeButton = $('.btn[role="button"][data-action="OK"]');
			
			// 제어 우선 모드인 경우 운용 모드에 버튼 숨김 처리
			if(that.mode == 0) {
				modeButton.show();
			}
			else if(that.mode == 1) {
				modeButton.hide();
			}
			
			that.event();
		},
		event: function() {
			const that = this;
			
			const buttons = $('.ctrl-field[role="button"]');
			const modeButton = $('.btn[role="button"][data-action="OK"]');
			const modeSelect = $('.ctrl-field[role="select"]');
			
			// 장비 우선 모드
			if(that.mode == 0) {
				buttons.click(function(e) {
					const selected = Gate.selected;
					const action = this.dataset.action;
					
					if((Object.keys(selected).length) == 0) {
						Alert.warning({text: '제어할 게이트를 선택하신 후 다시 시도해주세요.'}, function() {
							Animation.load($('.ctrl[role="device"]'), 'animate__animated animate__flash');
						});
					}
					else {
						for(item in selected) {
							let dataset = selected[item];
							let data = Socket.createData(dataset, action);
							
							Socket.sendMessage(data);
						}
					}
				});
				
				modeButton.click(function() {
					const selected = Gate.selected;
					const value = $('.ctrl-field[role="select"][data-action="mode"]').val();
					
					if((Object.keys(selected).length) == 0) {
						Alert.warning({text: '제어할 게이트를 선택하신 후 다시 시도해주세요.'}, function() {
							Animation.load($('.ctrl[role="device"]'), 'animate__animated animate__flash');
						});
					}
					else {
						if(!value) {
							Alert.warning({text: '운용 모드를 선택하신 후 버튼을 눌러주세요!'}, function() {
								Animation.load($('.ctrl-field[role="select"]'), 'animate__animated animate__flash');
							});
						}
						else {
							for(item in selected) {
								let dataset = selected[item];
								let data = Socket.createData(dataset, "MODE," + value);
								
								Socket.sendMessage(data);
							}
						}
					}
				});
			}
			// 제어 우선 모드
			else if(that.mode == 1) {
				buttons.click(function() {
					// 운용 모드 초기화
					modeSelect.val(0);
					modeSelect[0].dataset.use = false;
					
					// 버튼 선택 초기화
					for(button of buttons) {
						button.dataset.use = false;
					}
					
					this.dataset.use = true;
				});
				
				modeSelect.change(function(e) {
					// 버튼 선택 초기화
					for(button of buttons) {
						button.dataset.use = false;
					}
					
					this.dataset.use = true;
				});
			}
		}
	}
	
	const Socket = {
		sock: null,
		load: function() {
			// 기존 연결이 있는경우 끊기
			Content.sock && Content.sock.disconnect();
			
			return this.connect();
		},
		connect: function() {
			const that = this;
			const ws_url = $('input[name="ws_url"]').val();
			
			let sock = new SockJS(`${ws_url}/ws`);
			
			stompClient = Stomp.over(sock);
			stompClient.resoccnet_delay = 5000;
			stompClient.debug = false;
			
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/topic/live/monitoring', function(message) {});
			});
			
			that.sock = stompClient;
			
			return stompClient;
		},
		createData: function(dataset, action) {
			let data = {};
			
			data.EQUIP_ID = dataset.description;
			data.IP = Gate.list.find(i => i.rec_key == dataset.rec_key).equip_ip;
			data.OPERATION_TYPE = action;
			
			return data;
		},
		sendMessage: function(message) {
			const that = this;
			
			that.sock.send("/topic/live/monitoring", {}, JSON.stringify(message));
		}
	}
	
	Content.load();
});