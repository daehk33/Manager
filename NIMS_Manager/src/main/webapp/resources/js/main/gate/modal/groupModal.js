/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Group.load();
		}
	};
	
	const Group = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="group"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event: function() {
			const that = this;
			const modal = that.modal;
			const body = modal.find('.modal-content .modal-body');
			
			let pgType = null;
			
			body.find('#library_key').change(function() {
				const libKey = this.value;
				
				body.find('#barcode').html(Select.port({library_key: libKey, type: 'barcode'}));
				body.find('#reader').html(Select.port({library_key: libKey, type: 'reader'}));
				body.find('#moblie').html(Select.port({library_key: libKey, type: 'mobile'}));
				body.find('#gate').html(Select.gate({library_key: libKey}));
			});
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				const itemField = body.find('.item-field');
				
				pgType = $('.param[name="item"]').attr('action');
				
				// 도서관 목록
				body.find('#library_key').html(Select.librarys());
				
				// 초기화
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
				
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
					body.find('#group_lib #library_key').val(libKey);
					body.find('#barcode').html(Select.port({library_key: libKey, type: 'barcode'}));
					body.find('#reader').html(Select.port({library_key: libKey, type: 'reader'}));
					body.find('#moblie').html(Select.port({library_key: libKey, type: 'mobile'}));
					body.find('#gate').html(Select.gate({library_key: libKey}));
					common.disableFieldAndLabelInParent(body, 'library_key', true);
				}
				else {
					body.find('#reader').empty()
					body.find('#barcode').empty()
					body.find('#moblie').empty()
					body.find('#group_gate #gate').empty()
					common.disableFieldAndLabelInParent(body, 'library_key', false);
				}
				
				// 추가
				if(pgType == 'insert') {
					title.text('그룹 추가');
					
					footer.find('[role="action"][action="confirm"]').show();
					footer.find('[role="action"][action="cancel"]').show();
				}
				
				// 수정
				else if(pgType == 'modify') {
					title.text('장비 정보 수정');
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					body.find('#barcode').html(Select.barcode({library_key: item['library_key']}));
					body.find('#reader').html(Select.reader({library_key: item['library_key']}));
					body.find('#moblie').html(Select.moblie({library_key: item['library_key']}));
					body.find('#group_gate #gate').html(Select.gates({library_key: item['library_key']}));
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
					
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="dele"]').show();
				}
			});

			modal.on('shown.bs.modal', function(e) {
				const body = modal.find('.modal-content .modal-body');
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

			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				const chk_arr = new Array();
				const valueArray = new Array();

				$('select[name=device_port] option:selected').each(function(index) {
					var num = $(this).attr('value');
					valueArray.push(num);
				});

				const type = $('#type').val();
				const device_port = valueArray.join();
				const gate = $('#group_gate #gate').val();
				const command = $('#group_command #command').val();
				const direction = $('#direction').val();
				const library_key = $('#group_lib #library_key').val();

				const inputValues = [device_port, gate, command, direction, library_key];
				
				if(['confirm', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'confirm') {
					AjaxUtil.request({
						async : false,
						url: '/api/gate/insertGroupInfo',
						data: Object.assign({
							device_port: device_port,
							gate: gate,
							command: command,
							direction: direction,
							library_key: library_key,
						}),
						modal: modal,
						table: 'groupTable',
						successMessage: '그룹이 성공적으로 추가되었습니다.',
						failMessage: '그룹 추가중 오류가 발생하였습니다.'
					});
					AjaxUtil.request({
						async : false,
						url: '/api/gate/insertCommandInfo',
						data: Object.assign({
							type: type,
							library_key: library_key,
							gate: gate,
						}),
						modal: modal,
						table: 'groupTable',
					});
				}
				else if (action == 'update') {
					AjaxUtil.request({
						async : false,
						url: '/api/gate/updateGroupInfo',
						data: Object.assign({
							rec_key: that.item.rec_key,
							device_port: device_port,
							gate: gate,
							command: command,
							direction: direction,
							library_key: library_key,
						}),
						modal: modal,
						table: 'groupTable',
						successMessage: '그룹이 성공적으로 수정되었습니다.',
						failMessage: '그룹 수정중 오류가 발생하였습니다.'
					});
					AjaxUtil.request({
						async : false,
						url: '/api/gate/updateCommandInfo',
						data: Object.assign({
							rec_key: that.item.rec_key,
							type: type,
							library_key: library_key,
							gate: gate,
						}),
						modal: modal,
						table: 'groupTable',
					});

				}
				else if (action == "dele") {
					let confirmParams = {title: '그룹 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
					Alert.confirm(confirmParams, (result) => {
						if(!result.isConfirmed) return;
						
						AjaxUtil.request({
							url: '/api/gate/deleteGroupInfo',
							data: Object.assign({
								rec_key: that.item.rec_key,
							}),
							modal: modal,
							table: 'groupTable',
							successMessage: '그룹이 성공적으로 삭제되었습니다.',
							failMessage: '현재 사용중인 그룹은 삭제할 수 없습니다.'
						});
						
						AjaxUtil.request({
							url: '/api/gate/deleteCommandInfo',
							data: Object.assign({
								rec_key: that.item.rec_key,
							}),
							modal: modal,
							table: 'groupTable',
						});
						
					});
				}
			});	
		}

	};
	const Select = {
		librarys: function(params) {
			let librarySelect = null;
			this.modal = $('[role="modal"][target="group"]');
			
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
		port: function(params) {
			const that = this;
			const modal = this.modal;
			const body = modal.find('.modal-content .modal-body');
			
			let options = '';
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getInputList',
				async: false,
				data: params,
				done: function(data) {
					let portList = data.result.items;
					
					if(portList == null || portList.length == 0) {
						options += '<option value="" disabled>등록된 포트가 없습니다.</option>';
					}
					else {
						for(port of portList) {
							options += `<option value="${port.port}" data-library_key="${port.library_key}">${port.port}</option>`;
						}
					}
				}
			});
			
			return options;
		},
		gate: function(params) {
			let options = '';
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getGateInfoList',
				async: false,
				data: params,
				done: function(data) {
					let gateList = data.result.items;
					
					if(gateList == null || gateList.length == 0) {
						options += '<option value="" disabled>등록된 게이트가 없습니다.</option>';
					}
					else {
						for(gate of gateList) {
							options += `<option value="${gate.gate_key}" data-library_key="${gate.library_key}">${gate.description}</option>`;
						}
					}
				}
			});
			
			return options;
		}
	}
	
	Content.load();
});