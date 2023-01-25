$(function () {
	const Content = {
		load: function() {
			const that = this;
			
			const form = $('[role="form"]');
			form.find('[name="managerID"]').focus();
			
			$('button[role="action"]').on('click', function(e) {
				e.preventDefault();
				
				const action = this.dataset.action;
				if(action == 'login'){
					that.validate();
				}
			});
			
			form.keypress(function(event) {
				const key = event.keyCode ? event.keyCode : event.which;
				switch (key) {
				case 13:
					that.validate();
					break;
				}
			}).bind(this);
			
			$('input').keyup(function(e){
				const id = this.id;
				const target = $(`[role="msg"][target="${id}"]`);
				
				if(e.originalEvent.getModifierState && e.originalEvent.getModifierState("CapsLock")){
					target.html('<div style="margin-top: 5px; color: red; font-size: .8rem; font-weight: bold;">Caps Lock이 켜져 있습니다</div>');
				}
				else {
					target.html('');
				}
			});
			
			$('input').focusout(function(e){
				const id = $(this).attr('id');
				const target = $(`[role="msg"][target="${id}"]`);
				target.html('');
			});
			
			$('.form-floating-label .form-control').keyup(function(){
				if(this.value !== '') {
					$(this).addClass('filled');
				} else {
					$(this).removeClass('filled');
				}
			});
		},
		
		validate: function() {
			const that = this;
			
			const form = $('[role="form"]');
			const managerID = form.find('[name="managerID"]');
			const managerPWD = form.find('[name="managerPWD"]');
			
			let msg = '';
			let target = '';
			
			if(managerID.val() == '' && managerPWD.val() == '') {
				msg = '아이디와 비밀번호를 입력해 주세요.';
                target = 'id';
            }
            else if(managerID.val() == '' && managerPWD.val() != '') {
            	msg = '아이디를 입력해 주세요.';
                target = 'id';
            } 
            else if(managerID.val() != '' && managerPWD.val() == '') {
            	msg = '비밀번호를 입력해 주세요.';
            	target = 'pwd';
            }
            else {
            	that.submit();
            }
			
			if(msg) {
				that.alert(msg, 'warning', target);
			}
		},
		
		alert: function(msg, type, target) {
			const that = this;
			
			const form = $('[role="form"]');
			const managerID = form.find('[name="managerID"]');
			const managerPWD = form.find('[name="managerPWD"]');
			
			Alert.find(type, {text: msg}, () => {
				if(target == 'id') {
					managerID.focus();
					that.animate(managerID[0]);
				}
				else if (target == 'pwd') {
					managerPWD.focus();
					that.animate(managerPWD[0]);
				}
			});
		},
		
		submit: function() {
			const that = this;
			
			const form = $('#loginForm');
			const loading = $('[role="loading"]');
			
			const managerID = form.find('[name="managerID"]').val();
			const managerPWD = form.find('[name="managerPWD"]').val();
			
			// RSA 암호화 처리
	    	const publicKeyModulus = $('input[name="publicKeyModulus"]').val();
	    	const publicKeyExponent = $('input[name="publicKeyExponent"]').val();
	    	
	    	const crypt = new RSAKey();
	    	crypt.setPublic(publicKeyModulus, publicKeyExponent);
	    	
	    	const securedId = crypt.encrypt(managerID);
	    	const securedPassword = crypt.encrypt(managerPWD);
	    	
	    	const timeout = 500;
	    	AjaxUtil.request({
                type: 'POST',
                url: '/login/loginCheck',
                data: Object.assign({}, common.params, {
                	manager_id: securedId,
                   	password: securedPassword,
                }),
                beforeSend: function() {
                	form.addClass('hide');
        			loading.removeClass('hide');
                },
                done: function(data) {
                	data = data.result;
                	
                	if(data.CODE == '220' && data.DATA["initial_password"] == managerPWD) {
                		Alert.info({title: '비밀번호 변경 안내', text: '현재 초기 비밀번호를 사용 중 입니다!<br>패스워드를 변경해주시기 바랍니다.'}, () => {
                			
                			setTimeout(function(){
                				location.href = '/main';
                			}, timeout);
                		})
                	}
                	else if(data.CODE == '220') {
                		setTimeout(function(){
            				location.href = '/main';
            			}, timeout);
                	}
                	else {
                		Alert.warning({title: '알림', text: data.DESC.split('.').join('.<br>')}, () => {
                			location.reload();
                		});
                	}
                },
            });
		},
		
		animate: function(target) {
			target.classList.add('animate__animated', 'animate__jello');
			
			target.addEventListener('animationend', () => {
				target.classList.remove('animate__animated', 'animate__jello');
			});
		}
	};
	
	Content.load();
});