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
				ConfigManager.setFieldType(null, 'value1');
				ConfigManager.setFieldType(null, 'value2');
				ConfigManager.setFieldType(null, 'value3');
				ConfigManager.setFieldType(null, 'value5');
				
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
				common.disableFieldAndLabel('value_name', true);
				common.disableFieldAndLabel('code', true);
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
				const value1 = $('#value1').val();
				const value2 = $('#value2').val();
				const value3 = $('#value3').val();
				const value5 = $('#value5').val();
				
				const inputValues = [code];
				
				if(!inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				AjaxUtil.request({
					url: '/api/loanReturn/updateLoanReturnRuleInfo',
					data: Object.assign({
						rec_key: rec_key,
						value1: value1,
						value2: value2,
						value3: value3,
						value5: value5,
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
			const table = Tabulator.prototype.findTable('#settingTable')[0];
			let currentPage = table.getPage();
			
			Device.modal.modal('hide');
			table.setData('/api/loanReturn/getLoanReturnRuleInfoList', {}, ajaxConfig).then(function() {
				table.setPage(currentPage);
			});
		}
	});
});