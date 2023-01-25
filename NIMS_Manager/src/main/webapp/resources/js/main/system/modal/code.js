$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Code.load();
		}
	};
	
	/**
	 * @Desc 코드 정보 관리 객체
	 */
	const Code = {
		modal : null,
		item: null,
		checkCode: false,
		load : function() {
			this.modal = $('[role="modal"][target="code"]');
			
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			let pgType = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				pgType = $('.param[name="item"]').attr('action');
				
				const itemField = $('.item-field');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				
				duplicated.hide();
				
				// 초기화
				const item = JSON.parse($('.param[name="item"]').val());
				that.item = item;
				
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				// 필수 항목 표기
				common.markNecessaryField('code');
				common.markNecessaryField('code_value');
				
				// 추가
				if(pgType == 'insert'){
					title.text('코드 등록');
				}
				// 수정
				else if(pgType == 'modify'){
					title.text('코드 수정');
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
				}
				
				const code = $('#code');
				let oldVal = '';
				
				code.css({'border-color':'#ebedf2'});
				
				code.focusin(function(){
					oldVal = code.val();
				});
				
				code.focusout(function(e) {
					const newVal = code.val();
					
					// 변화가 없을 경우 이벤트 중지
					if(newVal == oldVal){
						return;
					}
					// 중복 체크 표시 제거
					duplicated.removeClass();
					
					// 빈 값의 경우 이벤트 중지
					if(newVal == ''){
						that.checkCode = false;
						code.css({'border-color':'#ebedf2'});
						return;
					}
					else if(newVal == that.item.code) {
						return;
					}
					
					const grp_key = pgType == 'insert' ? that.item : that.item.grp_key;
					// 중복 체크
					AjaxUtil.request({
						url: '/api/code/checkCodeDuplicated',
						data: {
							grp_key: grp_key,
							code: newVal
						},
						done: function(data){
							data = data.result;
							
							duplicated.show();
							if(data.CODE == '200'){
								that.checkCode = true;
								duplicated.addClass('validate-success animated flipInY');
								code.css({'border-color':'#ebedf2'});
							}
							else if(data.CODE == '201'){
								that.checkCode = false;
								duplicated.addClass('validate-fail animated flipInY');
								code.css({'border-color':'var(--red)'});
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
				
				if(pgType == 'modify'){
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
				}
				else if(pgType == 'insert'){
					footer.find('[role="action"][action="add"]').show();
				}
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]').remove();
			});
			
			this.action();
		},
		action : function() {
			const that = this;
			const modal = this.modal;
			const alert = Alert;
			
			const footer = this.modal.find('.modal-footer');
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				const code = $('#code').val();
				const code_value = $('#code_value').val();
				const code_value2 = $('#code_value2').val();
				const code_value3 = $('#code_value3').val();
				const use_yn = $('#use_yn').val();
				const code_desc = $('#code_desc').val();
				
				const inputValues = [code, code_value];
				
				if(['add','update'].indexOf(action) > -1 && !inputValues.every(value => value)){
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add'){
					if(that.checkCode != true){
						Alert.warning({text: '코드 사용가능 여부를 확인해주세요!'});
						return;
					}
					
					AjaxUtil.request({
						url: '/api/code/insertCodeInfo',
						data: {
							grp_key: that.item,
							code: code,
							code_value: code_value,
							code_value2: code_value2,
							code_value3: code_value3,
							use_yn: use_yn,
							code_desc: code_desc
						},
						table: 'groupTable',
						modal: modal,
						successMessage: '코드가 성공적으로 추가되었습니다.',
						failMessage: '코드 추가 중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'update'){
					AjaxUtil.request({
						url: '/api/code/updateCodeInfo',
						data: {
							rec_key: that.item.rec_key,
							grp_key: that.item.grp_key,
							code: code,
							code_value: code_value,
							code_value2: code_value2,
							code_value3: code_value3,
							use_yn: use_yn,
							code_desc: code_desc
						},
						table: 'groupTable',
						modal: modal,
						successMessage: '코드가 성공적으로 수정되었습니다.',
						failMessage: '코드 수정 중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'delete'){
					AjaxUtil.request({
						url: '/api/code/deleteCodeInfo',
						data: {
							rec_key: that.item.rec_key
						},
						table: 'groupTable',
						modal: modal,
						successMessage: '코드가 성공적으로 삭제되었습니다.',
						failMessage: '코드 삭제 중 오류가 발생하였습니다.'
					});
				}
				else {
					console.log('알수없는 Action : ',action);
				}
			});
		}
	};
	
	Content.load();
});