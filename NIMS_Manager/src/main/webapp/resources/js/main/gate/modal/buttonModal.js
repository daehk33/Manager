/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			button.load();
		}
	};

	const button = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="gateButton"]');

			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');

			this.event();
		},
		event: function() {
			const that = this;
			const modal = this.modal;
			const body = modal.find('.modal-content .modal-body');
			let pgType = null;

			body.find('#library_key').change(function() {
				const libKey = this.value;
				
				body.find('#btn_name').html(Select.gates({library_key: libKey}));
				body.find('#command').html(Select.command({library_key: libKey}));
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
					body.find('#btn_name').html(Select.gates({library_key: libKey}));
					body.find('#command').html(Select.command({library_key: libKey}));
					common.disableFieldAndLabelInParent(body, 'library_key', true);
				}
				else {
					body.find('#btn_name').html('<option value=""></option>');
					body.find('#command').html('<option value=""></option>');
					common.disableFieldAndLabelInParent(body, 'library_key', false);
				}

				// 작업 유형 - 추가
				if(pgType == 'insert') {
					title.text('게이트 버튼 추가');

					footer.find('[role="action"][action="confirm"]').show();
					footer.find('[role="action"][action="cancel"]').show();
				}
				// 작업 유형 - 수정
				else if(pgType == 'modify') {
					title.text('게이트 버튼 수정');

					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;

					body.find('#btn_name').html(Select.gates({library_key: item['library_key']}));
					body.find('#command').html(Select.command({library_key: item['library_key']}));

					itemField.each(i => {

						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});

					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="del"]').show();
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

			footer.find('[role="action"]').click(function() {
				const action = $(this).attr('action');

				const btn_name = $('#btn_name').val();
				const gate = $('#btn_name > option:selected').attr("value2");
				const enable = "1";
				const short_cut = "0";
				const command = $('#command').val();
				const library_key = $('#button_lib #library_key').val();
				const io_type = $('#io_type').val();
				const open = $('#open').val();
				const close = $('#close').val();
				const on = $('#on').val();
				const off = $('#off').val();
				const reset = $('#reset').val();
				const forced_close = $('#forced_close').val();

				const inputValues = [library_key, btn_name, gate, enable, short_cut, open, close, on, off, reset, forced_close, io_type, command];
				
				if(['confirm', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				if(action == 'confirm') {
					AjaxUtil.request({
						url: '/api/gate/insertBtnInfo',
						async: false,
						data: {
							library_key: library_key,
							btn_name: btn_name,
							gate: gate,
							enable: enable,
							short_cut: short_cut,
							open: open,
							close: close,
							on: on,
							off: off,
							reset: reset,
							forced_close: forced_close,
							io_type: io_type,
							command: command,
						},
						modal: modal,
						table: 'buttonTable',
						successMessage: '게이트버튼 등록이 완료되었습니다.',
						failMessage: '게이트버튼 등록 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'update') {
					AjaxUtil.request({
						url: '/api/gate/updateBtnInfo',
						async: false,
						data: {
							rec_key: that.item.rec_key,
							library_key: library_key,
							btn_name: btn_name,
							gate: gate,
							enable: enable,
							short_cut: short_cut,
							open: open,
							close: close,
							on: on,
							off: off,
							reset: reset,
							forced_close: forced_close,
							io_type: io_type,
							command: command,
						},
						modal: modal,
						table: 'buttonTable',
						successMessage: '게이트버튼 수정이 완료되었습니다.',
						failMessage: '게이트버튼 수정 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'del') {
					let confirmParams = {title: '생체인증 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
					Alert.confirm(confirmParams, (result) => {
						if(result.isConfirmed) {
							AjaxUtil.request({
								url: '/api/gate/deleteBtnInfo',
								data: {
									rec_key: that.item.rec_key
								},
								modal: modal,
								table: 'buttonTable',
								successMessage: '생체인증 삭제가 완료되었습니다.',
								failMessage: '생체인증 삭제 중 오류가 발생하였습니다.'
							});
						}
					});
				}
			});
		}
	};
	
	const Select = {
		librarys: function(params) {
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
		gates: function(params) {
			let gateOptions = '<option value=""></option>';

			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getGateInfoList',
				async: false,
				data: params,
				done: function(data) {
					let gateList = data.result.items;

					if(gateList == null || gateList.length == 0) {
						gateOptions += '<option value="" disabled>등록된 장비가 없습니다.</option>';
					}
					else {
						for(gate of gateList) {
							let device_id = "";

							device_id = grade == 0 ? `${gate.gate_key} [${gate.library_nm}]` : gate.gate_key;

							gateOptions += `<option value="${gate.description}" value2="${gate.gate_key}" data-library_key="${gate.library_key}">${gate.description}</option>`;
						}
					}
				}
			});

			return gateOptions;
		},
		command: function(params) {
			let commandOptions = '<option value=""></option>';

			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getGroupList',
				async: false,
				data: params,
				done: function(data) {
					let commandList = data.result.items;

					if(commandList == null || commandList.length == 0) {
						commandOptions += '<option value="" disabled>등록된 명령어가 없습니다.</option>';
					}
					else {
						for(gate of commandList) {
							let device_id = "";

							device_id = grade == 0 ? `${gate.gate_key} [${gate.library_nm}]` : gate.gate_key;

							commandOptions += `<option value="${gate.command}" data-library_key="${gate.library_key}">${gate.command}</option>`;
						}
					}
				}
			});

			return commandOptions;
		},

	}

	Content.load();
});