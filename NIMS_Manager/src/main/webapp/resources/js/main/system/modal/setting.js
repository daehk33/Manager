/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			System.load();
		}
	};
	const System = {
		modal: null,
		item: null,
		checkId: false,
		load: function() {
			this.modal = $('[role="modal"][target="system"]');
			
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
				
				pgType = $('.param[name="item"]').attr('action');
				
				let itemField = body.find('.item-field');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				
				// 각 항목별 입력 타입 설정 초기화
				ConfigManager.setFieldType(null, 'option_1');
				ConfigManager.setFieldType(null, 'option_2');
				ConfigManager.setFieldType(null, 'option_3');
				
				// 초기화
				common.disableFieldAndLabel('rule_id', false);
				common.disableFieldAndLabel('rule_nm', false);
				common.disableFieldAndLabel('rule_desc', false);
				duplicated.removeClass();
				
				itemField.each(i => {
					const field = $(itemField[i]);
					
					field.val('');
				});
				
				// 필수 항목 표기
				common.markNecessaryField('rule_id');
				common.markNecessaryField('rule_nm');
				
				// 추가시
				if(pgType == 'insert'){
					title.text('시스템 정책 등록');
				}
				// 수정시
				else if(pgType == 'modify'){
					title.text('시스템 정책 수정');
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					// 각 항목별 입력 타입 설정
					ConfigManager.setFieldType(item.type_1, 'option_1');
					ConfigManager.setFieldType(item.type_2, 'option_2');
					ConfigManager.setFieldType(item.type_3, 'option_3');
					
					// 변경된 input들 Field 변수에 할당
					itemField = body.find('.item-field');
					
					// 초기화
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						
						field.val(item[id]);
					});
					
					common.disableFieldAndLabel('rule_id', true);
					common.disableFieldAndLabel('rule_nm', true);
					common.disableFieldAndLabel('rule_desc', true);
					duplicated.hide();
				}
				
				// 중복 체크
				const rule_id = $('#rule_id');
				
				let oldVal = '';
				rule_id.focusin(function(){
					oldVal = rule_id.val();
				});
				
				rule_id.focusout(function(e) {
					const newVal = rule_id.val();
					if(oldVal == newVal){
						return false;
					}
					
					duplicated.removeClass();
					
					if(newVal == ''){
						that.checkId = false;
						return false;
					}
					
					AjaxUtil.request({
						url: '/api/system/checkSystemRuleDuplicated',
						data: {
							rule_id: newVal
						},
						done: function(data){
							data = data.result;
							
							if(data.CODE == '200'){
								that.checkId = true;
								duplicated.addClass('validate-success animated flipInY');
								rule_id.css({'border-color':'#ebedf2'});
							}
							else if(data.CODE == '201'){
								that.checkId = false;
								duplicated.addClass('validate-fail animated flipInY');
								rule_id.css({'border-color':'var(--red)'});
							}
							else {
								Alert.danger({text: data.DESC});
							}
						}
					});
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
			});
			
			modal.on('shown.bs.modal', function(e) {
				const footer = modal.find('.modal-footer');
				if(pgType=='modify'){
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
				}
				else if(pgType=='insert'){
					footer.find('[role="action"][action="add"]').show();
				}
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]').remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			
			const footer = this.modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function() {
				const action = $(this).attr('action');
				
				const rec_key = $('#rec_key').val();
				const rule_id = $('#rule_id').val();
				const rule_nm = $('#rule_nm').val();
				const rule_desc = $('#rule_desc').val();
				const option_1 = $('#option_1').val();
				const option_2 = $('#option_2').val();
				const option_3 = $('#option_3').val();
				
				const inputValues = [rule_id, rule_nm];
				
				if(['add', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add'){
					if(that.checkId != true){
						Alert.warning({text: '정책 번호 중복 여부를 확인해주세요!'});
						return;
					}
					
					AjaxUtil.request({
						url: '/api/system/insertSystemRule',
						data: {
							rule_id: rule_id,
							rule_nm: rule_nm,
							rule_desc: rule_desc,
							option_1: option_1,
							option_2: option_2,
							option_3: option_3
						},
						done: function(data){
							data = data.result;
							
							that.modal.modal('hide');
							if(data.CODE == '200'){
								Alert.success({text: data.DESC}, Content.params.onHide);
							}
							else {
								Alert.danger({text: data.DESC});
							}
						}
					});
				}
				else if(action == 'update'){
					AjaxUtil.request({
						url: '/api/system/updateSystemRule',
						data: Object.assign({
							rec_key: rec_key,
							option_1: option_1,
							option_2: option_2,
							option_3: option_3
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
				}
				else if(action == 'delete'){
					let confirmParams = {title: '시스템 정책 삭제',
							text: '선택하신 시스템 정책을 삭제하시겠습니까?'};
					
					Alert.confirm(confirmParams, (result) => {
						if(result.isDismissed) {
							return;
						}
						AjaxUtil.request({
							url: '/api/system/deleteSystemRule',
							data: {
								rec_key: that.item.rec_key
							},
							done: function(data){
								data = data.result;
								
								that.modal.modal('hide');
								if(data.CODE == '200'){
									Alert.success({text: data.DESC}, Content.params.onHide);
								}
								else {
									Alert.danger({text: data.DESC});
								}
							}
						});
					});
				}
				else {
					console.error('알수없는 Action : ', action);
				}
			});
		}
	};

	Content.load({
		onHide: () => {
			System.modal.modal('hide');
			location.reload();
		}
	});
});