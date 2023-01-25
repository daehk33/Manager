/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Command.load();
		}
	};
	
	const Command = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="command"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event: function() {
			const that = this;
			const modal = that.modal;
			const body = modal.find('.modal-content .modal-body');
			let pgType = null;
			
			const checks = body.find('[role="command"] input[type="checkbox"]');
			
			body.find('#command_yn').change(function() {
				const value = this.value;
				
				value == 'Y' ? checks.attr('disabled', false) : checks.attr('disabled', true);
			});
			
			checks.click(function() {
				const check = this;
				
				check.dataset.value = check.checked ? 'Y' : 'N';
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
					
					if(field.is('.form-control')) {
						field.val('');
					}
					else {
						field[0].checked = false;
					}
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
				
				// 선택된 도서관정보가 있을 경우
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != ''){
					body.find('#library_key').val(libKey);
					common.disableFieldAndLabelInParent(body, 'library_key', true);
				}
				else {
					body.find('#controller_port').html('<option value=""></option>');
					common.disableFieldAndLabelInParent(body, 'library_key', false);
				}
				
				// 추가
				if(pgType == 'insert') {
					title.text('명령어 설정');
					
					footer.find('[role="action"][action="save"]').show();
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
			const body = modal.find('.modal-content .modal-body');
			const footer = modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const library_key = body.find('#library_key').val();
				const itemField = body.find('.item-field');
				
				if(library_key) {
					for(item of itemField) {
						let dataset = item.dataset;
						
						// dataset에 카테고리 값이 있는지 체크
						if(dataset.category) {
							if(item.value == "" && item.dataset.value == null) {
								Alert.warning({text: '사용 유무를 선택하신 후 다시 시도해주세요.'});
								return;
							}
							
							AjaxUtil.request({
								url: '/api/gate/upsertCfgRuleInfo',
								async: false,
								data: {
									library_key: library_key,
									category: dataset.category,
									rule_name: item.id,
									rule_value: item.value || item.dataset.value
								},
								error: function(error) {
									Alert.danger({text: '명령어 설정 중 오류가 발생하였습니다. <br/>잠시 후 다시 시도해주세요.'});
								}
							});
						}
					}
					
					Alert.success({text: '설정이 완료되었습니다.'}, Content.params.onHide);
				}
				else {
					Alert.warning({text: '도서관을 선택하신 후 다시 시도해주세요.'});
					return;
				}
			});	
		}
	};
	
	const Select = {
		librarys: function(params) {
			let librarySelect = null;
			this.modal = $('[role="modal"][target="command"]');
			
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
		}
	}
	
	Content.load({
		onHide: () => {
			Command.modal.modal('hide');
		}
	});
});