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
				
				// 각 항목별 입력 타입 설정 초기화
				ConfigManager.setFieldType(null, 'com');
				ConfigManager.setFieldType(null, 'value1');
				ConfigManager.setFieldType(null, 'value2');
				ConfigManager.setFieldType(null, 'value3');
				ConfigManager.setFieldType(null, 'value4');
				ConfigManager.setFieldType(null, 'value5');
				
				// 각 설정별 value 1~5 명칭 수정
				const value_name = item.value_name;
				
				// 기본 명칭으로 초기화
				for(i = 1; i <= 5; i++) {
					ConfigManager.setFieldName(`항목 ${i}`, true, `value${i}`);
				}
				
				if(value_name == "CONTROLLER") {
					ConfigManager.setFieldName('높낮이 조절 사용 여부 ( 0 : 미사용, 1 : 사용 )', true, 'value1');
					ConfigManager.setFieldType('SELECT {0, 1}', 'value1');
					
					ConfigManager.setFieldName('기본 위치', true, 'value2');
					ConfigManager.setFieldType('SELECT {0, 5, 10, 15, 19}', 'value2');
					
					ConfigManager.setFieldName('자동복귀설정 ( 0 : 자동복귀 사용 안함, 1 : 자동복귀, 2 : 센서자동복귀 )', true, 'value3');
					ConfigManager.setFieldType('SELECT {0, 1, 2}', 'value3');
					
					ConfigManager.setFieldName('S/W 체크 시간', true, 'value4');
					ConfigManager.setFieldType('number', 'value4');
					
					ConfigManager.setFieldName('H/W 체크 시간', true, 'value5');
					ConfigManager.setFieldType('number', 'value5');
				}
				else if (value_name == "PRINTER") {
					ConfigManager.setFieldName('프린트명', true, 'value1');
					
					ConfigManager.setFieldName('feed_cnt', true, 'value2');
					ConfigManager.setFieldType('number', 'value2');
					
					ConfigManager.setFieldName('마스킹 ( 1 : 마스킹 안함, 2 : 두번째, 3 : 세번째 )', true, 'value3');
					ConfigManager.setFieldType('SELECT {1, 2, 3}', 'value3');
					
					ConfigManager.setFieldName('', false, 'value4');
					ConfigManager.setFieldName('', false, 'value5');
				}
				else {
					for(i = 1; i <= 5; i++) {
						ConfigManager.setFieldName('', false, `value${i}`);
					}
				}
				
				// 초기화
				const itemField = body.find('.item-field');
				itemField.each(i => {
					const field = $(itemField[i]);
					
					field.val('');
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
				
				title.text('장비 설정 수정');
				
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					
					field.val(item[id]);
				});
				
				common.disableFieldAndLabel('library_nm', true);
				common.disableFieldAndLabel('device_nm', true);
				common.disableFieldAndLabel('value_name', true);
				
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
				const com = $('#com').val();
				const value1 = $('#value1').val();
				const value2 = $('#value2').val();
				const value3 = $('#value3').val();
				const value4 = $('#value4').val();
				const value5 = $('#value5').val();
				const use_yn = $('#use_yn').val();
				
				AjaxUtil.request({
					url: '/api/loanReturn/updateLoanReturnEquipRuleInfo',
					data: Object.assign({
						rec_key: rec_key,
						com: com,
						value1: value1,
						value2: value2,
						value3: value3,
						value4: value4,
						value5: value5,
						use_yn: use_yn,
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
			Device.modal.modal('hide');
			Tabulator.prototype.findTable('#settingTable')[0].setData();
		}
	});
});