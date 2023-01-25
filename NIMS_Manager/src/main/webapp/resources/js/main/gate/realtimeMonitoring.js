$(function() {
	const Content = {
		sock: null,
		table: null,
		params: {},
		load: function(params) {
			const that = this;
			that.params = params;
			
			// 시스템 관리자 등급인 경우 전체 제어 버튼 추가
			if(grade == 0) {
				const btns = `
					<button class="btn btn-round btn-danger ml-2" role="ALLOPEN" target="gate">
						<i class="fas fa-door-open"></i><span class="icon-title">전체 개방</span>
					</button>
					<button class="btn btn-round btn-warning ml-2" role="ALLCLOSE" target="gate">
							<i class="fas fa-door-closed"></i><span class="icon-title">전체 복귀</span>
					</button>
				`;
				
				$('.card-title-action').append(btns);
			}
			
			that.result();
		},
		result: function() {
			const that = this;
			
			that.table = Table.load();
			Gate.load();
			
			that.event();
		},
		event: function() {
			const that = this;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				that.table.rowManager.clearData();
				Gate.load();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				const file_name  = "실시간 모니터링 이력.xlsx";
				
				Excel.download(target, that.table, file_name);
			});
			
			// 게이트 제어 버튼 클릭 시
			$('.btn[target="gate"]').click(function() {
				const role = this.role;
				const device = $('.tab-pane.active .selectgroup-input:checked');
				const count = $('.tab-pane.active .selectgroup-input').length;
				const locName = $('.nav-link.active[role="tab"]')[0].dataset.target;
				
				// 장비 유무 체크
				if(count > 0) {
					const isAllLocation = role.includes("ALL");
					const isAllDevice = device[0].dataset.rec_key == 0;
					const deviceName = device.parent().find('.selectgroup-button')[0].innerText;
					let msg = null;
					let roleName = null;
					
					// 요청명 설정
					roleName = role.includes("OPEN") ? "개방" : role.includes("CLOSE") ? "복귀" : "1회 개방";
					
					// 경고 메시지 설정
					msg = isAllLocation ? `정말 전체 출입게이트를` :
						isAllDevice ? `정말 <b>[${locName}]</b>에 전체 출입게이트를` :
							`정말 <b>[${locName}]</b>에 <b>[${deviceName}]</b> 출입게이트를`;
					msg += ` ${roleName}하시겠습니까?`;
					
					Alert.confirm({title: '제어 요청', text: msg}, (result) => {
						if(result.isConfirmed) {
							const dataset = device[0].dataset;
							
							// 웹소켓에 전송할 데이터
							let data = {};
							
							// 전체 장비 제어 요청 처리
							if(isAllLocation) {
								data.EQUIP_ID = 'TOTAL';
								data.IP = '127.0.0.1';
								data.OPERATION_TYPE = role;
								
								// 게이트 제어 요청 웹소켓에 전송
								Socket.sendMessage(data);
							}
							else {
								// 층 전체 장비 제어 요청 처리
								if(isAllDevice) {
									let devices = $('.tab-pane.active .selectgroup-item .selectgroup-input');
									
									// 현재 선택된 층의 전체 장비 목록
									devices = devices.filter(x => devices[x].dataset.rec_key != 0);
									
									// 각 장비별 요청 생성하여 전송
									for(target of devices) {
										let dataset = target.dataset;
										
										data.EQUIP_ID = dataset.description;
										data.IP = Gate.list.find(i => i.rec_key == dataset.rec_key).ip;
										data.OPERATION_TYPE = role;
										
										Socket.sendMessage(data);
									}
								}
								// 단일 장비 제어 요청 처리
								else {
									let dataset = device[0].dataset;
									
									data.EQUIP_ID = dataset.description;
									data.IP = Gate.list.find(i => i.rec_key == dataset.rec_key).ip;
									data.OPERATION_TYPE = role;
									
									Socket.sendMessage(data);
								}
							}
						}
					});
				}
				else {
					Alert.warning({text: "현재 도서관에 등록된 출입게이트가 없습니다.<br/>출입게이트를 등록하신 후 이용해주세요.", time: 10});
				}
			});
		}
	};
	
	const Gate = {
		library_key: null,
		list: [],
		load: function() {
			const that = this;
			const tabList = $('.card .nav-tabs[role="tablist"]');
			const tabContent = $('.card .tab-content[role="content"]');
			
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
			let chkLib = null;
			
			if($('.sb-lib-menu').children().length > 0 ) {
				if(!that.library_key) {
					chkLib = false;
				}
				else {
					chkLib = true;
				}
			}
			else {
				chkLib = true;
			}
			
			// 장비 Location 배열 생성
			let locArry = [...new Set(list.map(x => chkLib ? 
					{value: x.location, name: x.location} : 
					{value: x.location, name: x.location + ' [' + x.library_nm + ']'}
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
				tabList.empty();
				tabContent.find('.tab-pane').remove();
				$('.tab-action').show();
				
				let i = 1;
				
				for(item of locArry) {
					// location tabs 생성
					let locButton = `
						<button class="nav-link" data-target="${item.value}" role="tab">
							${item.name}
						</button>`;
					
					tabList.append(locButton);
					
					// 각 location별 장비 버튼 생성
					let panel = $(`<div class="tab-pane fade" data-target="${item.value}" role="tabpanel"></div>`);
					let radioGroup = $(`
						<div class="selectgroup selectgroup-secondary selectgroup-pills" role="equip">
							<label class="selectgroup-item">
								<input type="radio" name="${i}-equip" class="selectgroup-input" data-rec_key="0" checked>
								<span class="selectgroup-button">전체</span>
							</label>
						</div>`);
					
					let devices = list.filter(x => x.location == item.value);
					
					for(device of devices) {
						let button = `
							<label class="selectgroup-item">
								<input type="radio" name="${i}-equip" class="selectgroup-input" data-rec_key="${device.rec_key}" data-description="${device.description}">
								<span class="selectgroup-button">${device.description}</span>
							</label>`;
						
						radioGroup.append(button);
					}
					panel.append(radioGroup);
					tabContent.append(panel);
					
					i += 1;
				}
				
				const first = $(tabList.children()[0]);
				const target = first[0].dataset.target;
				first.addClass('active');
				
				$(`.tab-pane[data-target="${target}"][role="tabpanel"]`).addClass("active show");
				
				// 데이터가 있는 경우 소켓 연결
				Content.sock = Socket.load();
			}
			else {
				tabList.empty();
				tabContent.find('.tab-pane').remove();
				$('.tab-action').hide();
				
				Alert.warning({text: "현재 도서관에 등록된 출입게이트가 없습니다.<br/><b>[ 장비 운영 환경 > 출입게이트 구성 ]</b>페이지에서<br/> 출입게이트를 등록하신 후 이용해주세요.", time: 10});
				
				Content.sock && Content.sock.disconnect();
			}
			
			this.event();
		},
		event: function() {
			const that = this;
			
			// Location 선택 시
			$('.nav-link[role="tab"]').click(function() {
				const target = this.dataset.target;
				
				$('.nav-link[role="tab"]').removeClass('active');
				
				$(this).addClass('active');
				
				// 선택한 Location에 위치한 장비 목록 표시
				$('.tab-pane.fade').removeClass('active show');
				$(`.tab-pane.fade[data-target="${target}"]`).addClass('active show');
				
				Content.table.clearData();
			});
			
			// Device 선택 시
			$('.tab-content .selectgroup-input').click(function() {
				const description = this.dataset.description;
				
				// 이전 기록 제거 후 필터 적용
				Content.table.clearData();
				
				if(description == "전체") {
					Content.table.clearFilter();
				}
				else {
					Content.table.clearFilter();
					Content.table.setFilter("description", "=", description);
				}
			});
		}
	}
	
	const Table = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				locale: true,
				layout: "fitColumns",
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download: false },
					{ title: "출입일", widthGrow: 3, field: "date", headerSort: false },
					{ title: "출입 시간", widthGrow: 2, field: "time", headerSort: false, headerFilterPlaceholder: " "},
					{ title: "소속", widthGrow: 2, field: "position", headerSort: false, headerFilterPlaceholder: " "},
					{ title: "신분", widthGrow: 2.5, field: "dept", headerSort: false },
					{ title: "학번/직번", widthGrow: 1.5, field: "id", headerSort: false },
					{ title: "이용자 ID", widthGrow: 1.5, field: "user_id", headerSort: false, editor: "select", editorParams: {"입실": "입실", "퇴실": "퇴실"}, headerFilterParams: {"입실": "입실", "퇴실": "퇴실"}, headerFilterPlaceholder: " "},
					{ title: "성명", widthGrow: 1.5, field: "name", headerSort: false, formatter:function(cell){return Masking.name(cell.getValue());}},
					{ title: "상태", widthGrow: 1.5, field: "status", headerSort: false },
					{ title: "장비", widthGrow: 1.5, field: "equip_id", headerSort: false },
					{ title: "출입불가 사유", widthGrow: 1.5, field: "error", headerSort: false },
				],
				downloadComplete: function(){
					Swal.close();
				},
			});
			
			$('.tabulator-paginator > label').remove();
			
			return table;
		}
	};
	
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
//			stompClient.debug = false;
			
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/topic/live/monitoring', function(message) {
					const data = JSON.parse(message.body);
					
					// 로그 관련 메시지인지 체크
					if(data.date && data.time && data.status) {
						const location = $('.nav-link.active')[0].dataset.target;
						const key = $('.tab-pane.active input[type="radio"]:checked')[0].dataset.rec_key;
						
						let msgLocation = Gate.list.find(i => i.description == data.EQUIP_ID).location;
						
						// 현재 선택된 위치 체크
						if(location == msgLocation) {
							// 현재 선택된 장비 체크 후 메시지 전달
							if(key == '0') {
								that.createMessage(data);
							} 
							else if(key == data.device_no) {
								that.createMessage(data);
							}
						}
					}
				});
			});
			
			that.sock = stompClient;
			
			return stompClient;
		},
		createMessage: function(item) {
			const data = {
				date: item.date,
				time: item.time,
				position: item.position,
				dept: item.dept,
				id: item.id,
				user_id: item.user_id,
				name: item.name,
				status: item.status,
				equip_id: item.equip_id,
				error: item.error
			}
			
			Content.table.addData([data], true);
		},
		sendMessage: function(message) {
			const that = this;
			
			that.sock.send("/topic/live/monitoring", {}, JSON.stringify(message));
		}
	}
	
	Content.load();
});