/**
 * @Desc Manager for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Manager.load();
		}
	};
	
	const Manager = {
		modal : null,
		inputList: [],
		load : function() {
			this.modal = $('[role="modal"][target="managerInfo"]');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const body = modal.find('.modal-content .modal-body');
				
				body.find('#managerID').val(manager_id);
				body.find('#managerName').val(manager_id);
				
				const oldPwd = body.find('#oldPWD[type="password"]');
				const newPwd = body.find('#newPWD[type="password"]');
				const checkPwd = body.find('#checkPWD[type="password"]');
				
				that.inputList = [oldPwd, newPwd, checkPwd];
				
				that.inputList.forEach(input => {
					input.on('blur', function(){
						Validate.isNull(this);
					});
				});
				
				// 신규 비밀번호 확인 Input Event
				newPwd.on('blur', function() {
					Validate.compare(oldPwd[0], newPwd[0], 'same');
					checkPwd.val() != "" && Validate.compare(newPwd[0], checkPwd[0], 'check');
				});
				
				checkPwd.on('blur', function() {
					checkPwd.val() != "" && Validate.compare(newPwd[0], checkPwd[0], 'check');
				});
			});
			
			modal.on('hide.bs.modal', function(e) {
				$('[role="action"][action="cancel"]').trigger('click')
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			
			const oldPwd = modal.find('#oldPWD[type="password"]');
			const newPwd = modal.find('#newPWD[type="password"]');
			const checkPwd = modal.find('#checkPWD[type="password"]');
			
			const footer = modal.find('.modal-footer');
			footer.find('[role="action"]').click(function(){
				const action = $(this).attr('action');
				if (action=="update"){
					let validateResults = [];
					
					for(input of that.inputList) {
						validateResults.push(Validate.isNull(input[0]));
					}
					
					validateResults.push(Validate.compare(newPwd[0], checkPwd[0]), 'check');
					
					// 모든 유효성 검사 결과 값이 true면 return true
					if(!validateResults.every(result => result)) return;
					
					AjaxUtil.request({
						url: '/api/manager/updateManagerPassword',
						data: Object.assign({}, common.params, {
							rec_key: manager_key,
							manager_id: manager_id,
							password: oldPwd.val(),
							new_password: newPwd.val(),
						}),
						done: function(data) {
							data = data.result;
							
							if(data.CODE == "200") {
								Alert.success({title: '비밀번호 변경 완료', 
									text: '비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주시기 바랍니다.'}, ()=>{
									modal.modal('hide');
									location.href = '/logout'
								})
							}
							else if (data.CODE == "420") {
								Action.alert(data.DESC, 'error', 'new');
							}
							else if (data.CODE == "480") {
								Action.alert(data.DESC, 'error', 'old');
							}
							else {
								Action.error({text: data.DESC})
							}
						}
					});
				}
				else if (action=="cancel"){
					for(input of that.inputList) {
						input.parent().removeClass('has-error has-feedback has-success');
						input.val('');
					}
					for(invalidMsg of document.querySelectorAll('#invalidMsg')) {
						invalidMsg.innerHTML = '';
					}
				}
			});
		}
	};

	const Action = {
		alert: function(msg, type, target) {
			const that = this;
			
			const oldPwd = $('[type="password"][id="oldPWD"]')[0];
			const newPwd = $('[type="password"][id="newPWD"]')[0];
			const checkPwd = $('[type="password"][id="checkPWD"]')[0];
			
			let params = {text: msg};
			Alert.find(type, params, () => {
				if(target == 'old') {
					that.animate(oldPwd.parentElement, target);
				}
				else if (target == 'new') {
					that.animate(newPwd.parentElement, target);
				}
				else if (target == 'check') {
					that.animate(checkPwd.parentElement, target);
				}
				else if (target == 'success') {
					location.href = "/manager/changePassword";
				}
			})
		},
		animate: function(target, type) {
			const that = this;
			
			const invalidMsg = target.children.invalidMsg;
			
			target.classList.add('has-error', 'has-feedback', 'animate__animated', 'animate__tada');
			target.addEventListener('animationend', () => {
				target.classList.remove('animate__animated', 'animate__tada');
				
				// 이전 유효성 검사 메시지 삭제
				Validate.message(invalidMsg, 'remove');
				
				Validate.message(invalidMsg, type);
			});
		}
	};
	
	const Validate = {
		isNull: function(target) {
			let invalidMsg = target.parentElement.children.invalidMsg;
			
			if(target.value.length == 0) {
				Action.animate(target.parentElement, 'null');
				return false;
			}
			else {
				this.message(invalidMsg, 'success');
				return true;
			}
		},
		compare: function(mainTarget, checkTarget, type) {
			let invalidMsg = checkTarget.parentElement.children.invalidMsg;
			
			let mainVal = mainTarget.value;
			let checkVal = checkTarget.value;
			
			if(type == 'check') {
				if(mainVal != checkVal) {
					Action.animate(checkTarget.parentElement, 'check');
					return false;
				}
			}
			else if(type == 'same') {
				if(mainVal == checkVal) {
					Action.animate(checkTarget.parentElement, 'new');
					return false;
				}
			}
			
			this.message(invalidMsg, 'success');
			return true;
		},
		message: function(target, type) {
			switch(type) {
				case 'success' :
					target.parentElement.classList.remove('has-error', 'has-feedback');
					target.parentElement.classList.add('has-success');
				case 'remove':
					target.innerText = '';
					break;
				case 'null':
					target.innerText = '비밀번호를 입력해 주세요!';
					break;
				case 'old':
					target.innerText = '비밀번호를 확인해 주세요!';
					break;
				case 'new':
					target.innerText = '기존 비밀번호와 동일한 비밀번호를 입력하셨습니다.';
					break;
				case 'check':
					target.innerText = '비밀번호가 일치하지 않습니다.';
					
					break;
			}
			target.focus();
		}
	};
	Content.load({});
});