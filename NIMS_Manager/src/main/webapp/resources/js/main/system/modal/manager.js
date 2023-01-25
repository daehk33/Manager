/**
 * @Desc Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		admin: null,
		load : function(params) {
			this.params = params;
			this.admin = {rec_key: manager_key, id: manager_id, grade: grade};
			
			Manager.load();
		}
	};
	const Manager = {
		modal : null,
		item: null,
		checkId: false,
		load : function() {
			this.modal = $('[role="modal"][target="manager"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			const form = body.find('.row div form');
			
			// 모델 권한 관련 체크박스 생성
			const container = $('<div class="form-check"></div>');
			const content = $('<div class="selectgroup selectgroup-pills">');
			
			AjaxUtil.request({
				url: '/api/model/getModelList',
				async: false,
				done: function(data) {
					data = data.result.data;
					
					if(data.length > 0) {
						container.append('<label class="form-label">장비 권한</div>');
						
						data.forEach(model => {
							content.append(`<label class="selectgroup-item">
												<input type="checkbox" name="value" value="${model.rec_key}" class="selectgroup-input">
												<span class="selectgroup-button">${model.model_desc}</span>
											</label>`);
						});
						container.append(content);
						form.append(container);
					}
				}
			});
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			pgType = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				const modelForm = body.find('.row div form .form-check');
				
				pgType = $('.param[name="item"]').attr('action');
				
				// 등급 정보
				AjaxUtil.request({
					type: 'POST',
					url: '/api/code/getCodeList',
					async: false,
					data: {
						grp_key: '2'
					},
					done: function(data) {
						let typeList = data.result.data;
						let typeSelect = '';
						for(type of typeList) {
							typeSelect += `<option value="${type.code}">${type.code_value}</option>`;
						}
						body.find('#manager_grade').html(typeSelect);
					}
				});
				
				// 도서관 목록
				AjaxUtil.request({
					type: 'POST',
					url: '/api/library/getLibraryList',
					async: false,
					done: function(data) {
						let libraryList = data.result.data;
						let librarySelect = '';
						for(lib of libraryList) {
							librarySelect += `<option value="${lib.rec_key}">${lib.library_nm}</option>`;
						}
						body.find('#library_key').html(librarySelect);
					}
				}); 
				
				const itemField = body.find('.item-field');
				const modelList = $('.form-check .selectgroup-input');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				const passwordField = body.find('#password').parent();
				
				// 초기화
				footer.show();
				duplicated.show();
				passwordField.show();
				
				common.disableFieldAndLabel('manager_id', false);
				common.disableFieldAndLabel('manager_nm', false);
				common.disableFieldAndLabel('manager_grade', false);
				
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				modelList.each(i => {
					modelList[i].checked = false;
				});
				
				// 필수 항목 표기
				common.markNecessaryField('manager_id');
				common.markNecessaryField('manager_nm');
				common.markNecessaryField('manager_grade');
				common.markNecessaryField('library_key');
				common.markNecessaryField('password');
				
				// 추가
				if(pgType == 'insert'){
					title.text('사용자 등록');
					if(Content.admin.grade > 0){
						body.find('#manager_grade option[value="0"]').hide();
					}
					
					modelForm.hide();
				}
				// 수정
				else if(pgType == 'modify'){
					title.text('사용자 수정');
					
					common.disableFieldAndLabel('manager_id', true);
					
					duplicated.hide();
					passwordField.hide();
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
					
					let modelList = $('.form-check .selectgroup-input');
					
					if(item["model_auth"]) {
						let itemArry = item["model_auth"].split(",").map(i=>i.trim());
						itemArry.forEach(i => {
							$(`.form-check .selectgroup-input[value="${i}"]`)[0].checked = true;
						});
					}
					
					// 로그인한 사용자가 수정하고자하려는 관리자보다 권한이 낮을 경우 수정 불가
					if(item.manager_grade < Content.admin.grade) {
						footer.hide();
						common.disableFieldAndLabel('manager_nm', true);
						common.disableFieldAndLabel('manager_grade', true);
						title.append('<h5 style="color: red;">권한이 부족하여 정보를 수정할 수 없습니다.</h5>')
					}
					
					// 수정 대상 등급이 시스템 관리자일 경우 modelForm 숨김 처리
					if(item.manager_grade == 0) {
						modelForm.hide();
					}else {
						modelForm.show();
					}
					
					// 관리자가 아니라면 등급, 장비 권한, 소속 도서관 수정 불가
					if(Content.admin.grade > 0){
						common.disableFieldAndLabel('manager_grade', true);
						common.disableFieldAndLabel('library_key', true);
						modelForm.hide();
					}
				}
				
				// 사용자 권한 체크
				body.find('#manager_grade').change(function(e) {
					if ($('#manager_grade').val() != 0){
						$('[role="libShow"]').show();
						Content.admin.grade == 0 && modelForm.show();
					}
					else {
						$('[role="libShow"]').hide();
						modelForm.hide();
					}
				});
				
				const manager_id = $('#manager_id');
				
				let oldVal = '';
				manager_id.focusin(function(){
					oldVal = manager_id.val();
				});
				
				manager_id.focusout(function(e) {
					// 변화가 없을 경우 이벤트 중지
					const newVal = manager_id.val();
					if(newVal == oldVal){
						return false;
					}
					
					// 중복 체크 표시 제거
					duplicated.removeClass();
					
					// 빈 값의 경우 이벤트 중지
					if(newVal == ''){
						that.checkId = false;
						return false;
					}
					
					// 중복 체크
					AjaxUtil.request({
						url: '/api/manager/checkManagerDuplicated',
						data: {
							manager_id: newVal,
						},
						done: function(data){
							data = data.result;
							if (data.CODE == '200'){
								that.checkId = true;
								duplicated.addClass('validate-success animated flipInY');
							}
							else if (data.CODE == '201'){
								duplicated.addClass('validate-fail animated flipInY');
							}
							else {
								Alert.danger({text: data.DESC});
							}
						}
					});
				});

				footer.find('[role="action"]').hide();
				body.find('[role="libShow"]').hide();
			});
			
			modal.on('shown.bs.modal', function(e) {
				const footer = modal.find('.modal-footer');
				const body = modal.find('.modal-body');
				
				if(pgType=='insert'){
					footer.find('[role="action"][action="add"]').show();
				}
				else if(pgType=='modify'){
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
					if(body.find('#manager_grade').val() != 0){
						body.find('[role="libShow"]').show();
					}
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
			
			const footer = modal.find('.modal-footer');
			const body = modal.find('.modal-body');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				const manager_id = $('#manager_id').val();
				const manager_nm = $('#manager_nm').val();
				const password = $('#password').val();
				const manager_grade = $('#manager_grade option:selected');
				const library = body.find('#library_key option:selected');
				let model_auth = '';
				
				const inputValues = [manager_id, manager_nm, manager_grade.val()];
				if(manager_grade.val() != 0){
					const modelList = $('.form-check .selectgroup-input');
					let checkedModel = [];
					
					modelList.each(i => {
						modelList[i].checked && checkedModel.push(modelList[i].value);
					});
					
					model_auth = checkedModel.join();
					
					inputValues.push(library.val());
				}
				if(['add', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}

				if(action == 'add'){
					if(that.checkId != true){
						Alert.warning({text: '아이디 사용 가능 여부를 확인해주세요!'});
						return;
					}
					
					/** TO DO
					 *  초기 패스워드 지정 부여인 경우 비밀번호 항목 제거 
					 */
					AjaxUtil.request({
						url: '/api/manager/insertManagerInfo',
						library: false,
						data: {
							manager_id: manager_id,
							manager_nm: manager_nm,
							password: password,
							manager_grade: manager_grade.val(),
							model_auth: model_auth,
							library_key: library.val()
						},
						table: 'listTable',
						modal: modal,
						successMessage: '사용자가 성공적으로 추가되었습니다.',
						failMessage: '사용자 추가 중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'update'){
					AjaxUtil.request({
						url: '/api/manager/updateManagerInfo',
						library: false,
						data: {
							rec_key: that.item.rec_key,
							manager_id: that.item.manager_id,
							manager_nm: manager_nm,
							manager_grade: manager_grade.val(),
							model_auth: model_auth,
							library_key: library.val()
						},
						table: 'listTable',
						modal: modal,
						successMessage: '사용자가 성공적으로 수정되었습니다.',
						failMessage: '사용자 수정 중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'delete'){
					let confirmParams = {confirmText: '삭제', title: '사용자 삭제', 
							text: '사용자를 삭제하면 해당 사용자로 더이상 로그인할 수 없으며, 시스템 일부 정보가 올바르게 표기되지 않을 수 있습니다. 사용자를 삭제하시겠습니까?'};
					
					Alert.confirm(confirmParams, (result) => {
						// 취소 버튼 클릭시
						if(result.isDismissed) return;
						
						// 로그인한 계정일시
						if(Content.admin.rec_key == that.item.rec_key){
							Alert.warning({text: '현재 로그인한 계정은 삭제할 수 없습니다!'});
							return;
						}
						
						// 삭제
						AjaxUtil.request({
							url: '/api/manager/deleteManagerInfo',
							data: {
								rec_key: that.item.rec_key
							},
							table: 'listTable',
							modal: modal,
							successMessage: '사용자가 성공적으로 삭제되었습니다.',
							failMessage: '사용자 삭제 중 오류가 발생하였습니다.'
						});
						
					});
				}
				else {
					console.log('알수없는 Action : ',action);
				}
			});
		}
	};
	
	Content.load({
	});
});