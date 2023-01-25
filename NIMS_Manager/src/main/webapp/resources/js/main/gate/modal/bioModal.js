/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Security.load();
		}
	};

	const Security = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="security"]');

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
				body.find('#library_key').html(Select.librarys());
				
				body.find('#library_key').change(function() {
					const libKey = this.value;
					
					body.find('#group_no').html(Select.device({library_key: libKey}));
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
					body.find('#group_no').html(Select.device({library_key: libKey}));
					common.disableFieldAndLabelInParent(body, 'library_key', true);
				}
				else {
					body.find('#group_no').html('<option value=""></option>');
					common.disableFieldAndLabelInParent(body, 'library_key', false);
				}

				// 작업 유형 - 추가
				if(pgType == 'insert') {
					title.text('생체인증 추가');

					footer.find('[role="action"][action="confirm"]').show();
					footer.find('[role="action"][action="cancel"]').show();
				}
				// 작업 유형 - 수정
				else if(pgType == 'modify') {
					title.text('생체인증 수정');

					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;

					body.find('#group_no').html(Select.device({library_key: item['library_key']}));

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

				const library_key =$('#bio_lib #library_key').val();
				const group_no = $('#group_no').val();
				const device_sn = $('#device_sn').val();
				const access_token = $('#access_token').val();
				const api_url = $('#api_url').val();

				const inputValues = [library_key, group_no, device_sn, access_token, api_url];

				// 누락된 항목 있는지 확인
				if(['confirm', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}

				if(action == 'confirm') {
					AjaxUtil.request({
						url: '/api/gate/insertSecurityInfo',
						data: {
							library_key: library_key,
							group_no: group_no,
							device_sn: device_sn,
							access_token: access_token,
							api_url: api_url
						},
						modal: modal,
						table: 'bioTable',
						successMessage: '생체인증 등록이 완료되었습니다.',
						failMessage: '생체인증 등록 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'update') {
					AjaxUtil.request({
						url: '/api/gate/updateSecurityInfo',
						data: {
							rec_key: that.item.rec_key,
							group_no: group_no,
							device_sn: device_sn,
							access_token: access_token,
							api_url: api_url
						},
						modal: modal,
						table: 'bioTable',
						successMessage: '생체인증 수정이 완료되었습니다.',
						failMessage: '생체인증 수정 중 오류가 발생하였습니다.'
					});
				}
				else if(action == 'del') {
					let confirmParams = {title: '생체인증 삭제', text: '정말로 삭제하시겠습니까?', confirmText: '삭제'}
					Alert.confirm(confirmParams, (result) => {
						if(result.isConfirmed) {
							AjaxUtil.request({
								url: '/api/gate/deleteSecurityInfo',
								data: {
									rec_key: that.item.rec_key
								},
								modal: modal,
								table: 'bioTable',
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
		device: function(params) {
			let gateOptions = '<option value=""></option>';

			AjaxUtil.request({
				type: 'POST',
				url: '/api/gate/getGroupList',
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

							device_id = grade == 0 ? `${gate.group_no} [${gate.library_nm}]` : gate.group_no;

							gateOptions += `<option value="${gate.rec_key}" data-library_key="${gate.library_key}">${gate.rec_key}</option>`;
						}
					}
				}
			});

			return gateOptions;
		}
	}

	Content.load();
});