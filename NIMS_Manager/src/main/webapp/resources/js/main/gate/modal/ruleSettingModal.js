/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Device.load();
		}
	};
	const Device = {
		modal: null,
		item: null,
		load: function() {
			this.modal = $('[role="modal"][target="device"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event: function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				const item = JSON.parse($('.param[name="item"]').val());
				that.item = item;
				
				// 각 항목별 입력 타입 설정
				ConfigManager.setFieldType(item.type01, 'option01');
				ConfigManager.setFieldType(item.type02, 'option02');
				ConfigManager.setFieldType(item.type03, 'option03');
				ConfigManager.setFieldType(item.type04, 'option04');
				
				// 초기화
				const itemField = body.find('.item-field');
				itemField.each(i => {
					const field = $(itemField[i]);
					
					field.val('');
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
				
				title.text('운영 정책 수정');
				
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					
					field.val(item[id]);
				});
				
				common.disableFieldAndLabel('library_nm', true);
				common.disableFieldAndLabel('device_nm', true);
				common.disableFieldAndLabel('rule_id', true);
				common.disableFieldAndLabel('rule_name', true);
				common.disableFieldAndLabel('description', true);
				
				footer.find('[role="action"][action="update"]').show();
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]');
				item.remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			
			const footer = this.modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function() {
				const action = $(this).attr('action');
				
				const rec_key = $('#rec_key').val();
				const option01 = $('#option01').val();
				const option02 = $('#option02').val();
				const option03 = $('#option03').val();
				const option04 = $('#option04').val();
				
				const inputValues = [rec_key];
				
				if(!inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				AjaxUtil.request({
					url: '/api/gate/updateGateRuleInfo',
					data: Object.assign({
						rec_key: rec_key,
						option01: option01,
						option02: option02,
						option03: option03,
						option04: option04,
						send_req_yn: 'Y'
					}),
					done: function(data) {
						data = data.result;
						
						if(data.CODE == "200") {
							Alert.success({text: '수정이 완료되었습니다.'}, Content.params.onHide);
						} 
						else {
							Alert.danger({text: data.DESC});
							return;
						}
					}
				});
			});
		}
	};
	
	const Validate = {
		configKey : function(e) {
			e.target.value = e.target.value.replace(/[^A-Za-z-_]/ig, '');
		}
	}

	Content.load({
		onHide: () => {
			let currentPage = Tabulator.prototype.findTable('#settingTable')[0].getPage();
			
			Device.modal.modal('hide');
			Tabulator.prototype.findTable('#settingTable')[0].setData('/api/gate/getGateRuleInfoList', {}, ajaxConfig).then(function() {
				Tabulator.prototype.findTable('#settingTable')[0].setPage(currentPage);
			});
		}
	});
});