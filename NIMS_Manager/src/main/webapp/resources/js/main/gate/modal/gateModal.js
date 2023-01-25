/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load: function(params) {
			this.params = params;
			
			Device.load();
		}
	};
	
	const Device = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="gate"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event: function() {
			const that = this;
			const modal = this.modal;
			const body = modal.find('.modal-content .modal-body');
			let pgType = null;
			
			// 도서관 변경
			body.find('#library_key').change(function() {
				const libKey = this.value;
				
				body.find('#controller_port').html(Select.port({library_key: libKey}));
			});
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				let portList = '<option value="">&nbsp;</option>';
				let libList = '<option value="">&nbsp;</option>';
				
				pgType = $('.param[name="item"]').attr('action');
				
				// 도서관 목록
				body.find('#library_key').html(Select.librarys());
				
				const itemField = body.find('.item-field');
				
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
				
				footer.show();
				footer.find('[role="action"]').hide();
				
				// 선택된 도서관정보가 있을 경우
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != ''){
					body.find('#library_key').val(libKey);
					body.find('#controller_port').html(Select.port({library_key: libKey}));
					common.disableFieldAndLabelInParent(body, 'library_key', true);
				}
				else {
					body.find('#controller_port').html('<option value=""></option>');
					common.disableFieldAndLabelInParent(body, 'library_key', false);
				}
				
				// 추가
				if(pgType == 'insert') {
					title.text('게이트 추가');
					
					footer.find('[role="action"][action="add"]').show();
					footer.find('[role="action"][action="cancel"]').show();
				}
				
				// 수정
				else if(pgType == 'modify') {
					title.text('장비 정보 수정');
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					body.find('#controller_port').html(Select.port({library_key: item['library_key']}));
					
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
				
				body.find('#ip').inputmask({
					alias: "ip",
					placeholder: ' ',
					showMaskOnHover: false,
					showMaskOnFocus: false
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
			const body = modal.find('.modal-content .modal-body');
			const footer = modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				
				const library_key = body.find('#library_key').val();
				const description = body.find('#description').val();
				const controller_port = body.find('#controller_port').val();
				const location = body.find('#location').val();
				const ip = body.find('#ip').val();
				
				const inputValues = [library_key, controller_port, location, description, ip];
				
				if(['add', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '일부 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add') {
					AjaxUtil.request({
						url: '/api/gate/insertGateInfo',
						data: Object.assign({
							library_key: library_key,
							controller_port: controller_port,
							location: location,
							description: description,
							gate_ip: ip
						}),
						modal: modal,
						table: 'gateTable',
						successMessage: '게이트가 성공적으로 추가되었습니다.',
						failMessage: '게이트 추가중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'update') {
					AjaxUtil.request({
						url: '/api/gate/updateGateInfo',
						data: Object.assign({
							rec_key: that.item.gate_key,
							library_key: library_key,
							description: description,
							controller_port: controller_port,
							location: location,
							gate_ip: ip
						}),
						modal: modal,
						table: 'gateTable',
						successMessage: '게이트가 성공적으로 수정되었습니다.',
						failMessage: '게이트 수정중 오류가 발생하였습니다.'
					});
				}
				else if (action == "delete") {
					let confirmParams = {title: '게이트 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
					Alert.confirm(confirmParams, (result) => {
						if(!result.isConfirmed) return;
						
						AjaxUtil.request({
							url: '/api/gate/deleteGateInfo',
							data: Object.assign({
								rec_key: that.item.gate_key,
							}),
							modal: modal,
							table: 'gateTable',
							successMessage: '게이트가 성공적으로 삭제되었습니다.',
							failMessage: '현재 사용중인 게이트는 삭제할 수 없습니다.'
						});
					});
				}
			});
		}
	};
	
	const Select = {
		librarys: function(params) {
			let librarySelect = null;
			this.modal = $('[role="modal"][target="gate"]');
			
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
			let portOptions = '<option value=""></option>';
			const that = this;
			const modal = this.modal;
			const body = modal.find('.modal-content .modal-body');
		
			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getInputList',
				async: false,
				data: params,
				done: function(data) {
					let gateList = data.result.items;
					
					if(gateList == null || gateList.length == 0) {
						portOptions += '<option value="" disabled>등록된 포트가 없습니다.</option>';
					}
					else {
						for(gate of gateList) {
							if(gate.type == 'controller'){
							let device_id = "";
							
							device_id = grade == 0 ? `${gate.port} [${gate.library_nm}]` : gate.port;
							
							portOptions += `<option value="${gate.port}" data-library_key="${gate.library_key}">${gate.port}</option>`;
							}
						}
					}
				}
			});
			
			return portOptions;
		}
	}	
	
	Content.load();
});