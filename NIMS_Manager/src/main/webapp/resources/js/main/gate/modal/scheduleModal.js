/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Schedule.load();
		}
	};
	
	const Schedule = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="schedule"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
		
			this.event();
		},
		event: function() {
			const that = this;
			const modal = this.modal;
			let pgType = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				const itemField = body.find('.item-field');
				
				pgType = $('.param[name="item"]').attr('action');
				
				// 도서관 목록
				body.find('#library_key').html(Select.library());
				
				// 제어 목록 생성
				AjaxUtil.request({
					url: '/api/code/getCodeList',
					async: false,
					data: {
						grp_key: '19',
						use_yn: 'Y'
					},
					done: function(data) {
						// 데이터 정렬
						data = data.result.data.sort(function(a, b) {
							// code_value2값이 동일한 경우(0) code_value3으로 비교
							return a.code_value2 - b.code_value2 || a.code_value3 - b.code_value3;
						});
						
						// 제어 유형
						ctrlTypes = new Set(data.map(x => x.code_desc));
						
						let options = '';
						let option = null;
						
						// 제어 유형별 option 생성
						for(type of ctrlTypes) {
							options += `<optgroup label="${type}">`;
							
							for(ctrl of data.filter(x => x.code_desc == type)) {
								option = type != "운용 모드" ? `<option value="${ctrl.code}">${ctrl.code_value}</option>` : `<option value="MODE,${ctrl.code}">${ctrl.code_value}</option>`;
								
								options+= option;
							}
							
							options += '</optgroup>';
						}
						body.find('#operation_type').html(options);
					}
				});
				
				// 초기화
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				// 도서관 선택 표시 여부
				if(grade == 0) {
					body.find('#library_key').parent().show()
				}
				else {
					body.find('#library_key').parent().hide();
					body.find('#library_key').val(manager_library_key);
				}
				
				// 전체 버튼 숨김 처리 (작업 유형에 따라 표시)
				footer.find('[role="action"]').hide();
				
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != '') {
					body.find('#library_key').val(libKey);
					body.find('#equip_key').html(Select.device({library_key: libKey}));
				}
				else {
					body.find('#equip_key').html('<option value=""></option>');
				}
				
				// 작업 유형 - 추가
				if(pgType == 'insert') {
					title.text('스케줄 추가');
					
					footer.find('[role="action"][action="add"]').show();
				}
				// 작업 유형 - 수정
				else if(pgType == 'modify') {
					title.text('스케줄 수정');
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					body.find('#equip_key').html(Select.device({library_key: item['library_key']}));
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
					
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
				}
			});
			
			modal.on('shown.bs.modal', function(e) {
				const body = modal.find('.modal-content .modal-body');
				
				$('#library_key').change(function() {
					let libKey = $('#library_key').val();
					
					body.find('#equip_key').html(Select.device({library_key: libKey}));
				});
				
				// 시간 입력 형식 설정
				body.find('#time').inputmask({
					alias: "datetime",
					inputFormat: "HH:MM",
					outputFormat: "HHMM",
					hourFormat: 24,
					showMaskOnHover: false
				});
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]');
				item.remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			const footer = modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function() {
				const action = $(this).attr('action');
				
				const library_key = $('#library_key').val();
				const equip_key = $('#equip_key').val();
				const date = $('#date').val();
				const time = $('#time').inputmask('unmaskedvalue');
				const operation_type = $('#operation_type').val();
				const operation_type_nm = $('#operation_type option:selected').text();
				const use_yn = $('#use_yn').val();
				
				const inputValues = [library_key, equip_key, date, time, operation_type, use_yn];
				
				// 누락된 항목 있는지 확인
				if(!inputValues.every(value => value)) {
					Alert.warning({text: '일부 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add') {
					AjaxUtil.request({
						url: '/api/gate/insertGateScheduleInfo',
						data: {
							library_key: library_key,
							equip_key: equip_key,
							date: date,
							time: time,
							operation_type: operation_type,
							operation_type_nm: operation_type_nm,
							use_yn: use_yn
						},
						modal: modal,
						table: 'listTable',
						successMessage: '스케줄 등록이 완료되었습니다.',
						failMessage: '스케줄 등록 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'update') {
					AjaxUtil.request({
						url: '/api/gate/updateGateScheduleInfo',
						data: {
							rec_key: that.item.rec_key,
							operation_type: operation_type,
							operation_type_nm: operation_type_nm,
							time: time,
							date: date
						},
						modal: modal,
						table: 'listTable',
						successMessage: '스케줄 수정이 완료되었습니다.',
						failMessage: '스케줄 수정 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'delete') {
					let confirmParams = {title: '스케줄 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제', cancelText: '취소'};
					Alert.confirm(confirmParams, (result) => {
						if(result.isConfirmed) {
							AjaxUtil.request({
								url: '/api/gate/deleteGateScheduleInfo',
								data: {
									rec_key: that.item.rec_key
								},
								modal: modal,
								table: 'listTable',
								successMessage: '스케줄 삭제가 완료되었습니다.',
								failMessage: '스케줄 삭제 중 오류가 발생하였습니다.'
							});
						}
					});
				}
			});
		}
	};
	
	const Select = {
		library: function(params) {
			let librarySelect = null;
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/library/getLibraryList',
				async: false,
				data: Object.assign({}, params, {
					rec_key: manager_library_key
				}),
				done: function(data) {
					let libraryList = data.result.data;
					
					for(lib of libraryList) {
						librarySelect += `<option value="${lib.rec_key}">${lib.library_nm}</option>`;
					}
				}
			});
			
			return librarySelect;
		},
		device: function(params) {
			let gateOptions = '<option vlaue=""></option>';
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getGateList',
				async: false,
				data: params,
				done: function(data) {
					let gateList = data.result.items;
					
					if(gateList == null || gateList.length == 0) {
						gateOptions += '<option value="" disabled>등록된 게이트가 없습니다.</option>';
					}
					else {
						for(gate of gateList) {
							let device_id = "";
							
							device_id = grade == 0 ? `${gate.equip_id} [${gate.library_nm}]` : gate.equip_id;
							
							gateOptions += `<option value="${gate.rec_key}" data-library_key="${gate.library_key}">${gate.equip_id}</option>`;
						}
					}
				}
			});
			
			return gateOptions;
		}
	}
	
	const Validate = {
		configKey : function(e) {
			e.target.value = e.target.value.replace(/[^A-Za-z-_]/ig, '');
		}
	}

	Content.load();
});