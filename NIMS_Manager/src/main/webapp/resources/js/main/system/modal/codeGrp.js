/**
 * @Desc Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			CodeGrp.load();
		}
	};
	
	/**
	 * @Desc 코드 그룹 관리 객체
	 */
	const CodeGrp = {
		modal : null,
		item: null,
		checkId: false,
		load : function() {
			this.modal = $('[role="modal"][target="codeGrp"]');
			
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			let pgType = '';
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				pgType = $('.param[name="item"]').attr('action');
				
				const title = header.find('.modal-title');
				let content = null;

				const itemField = body.find('.item-field');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				
				// 초기화
				common.disableFieldAndLabel('grp_id', false);
				duplicated.show();
				
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					field.val('');
				});
				
				// 필수 항목 표기
				common.markNecessaryField('grp_id');
				common.markNecessaryField('grp_nm');
				
				// 추가
				if(pgType == 'insert'){
					title.text('코드그룹 정보 등록');
				}
				// 수정
				else if(pgType == 'modify'){
					title.text('코드그룹 정보 수정');
					
					common.disableFieldAndLabel('grp_id', true);
					duplicated.hide();
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
					
				}
				
				// 아이디 중복 체크
				const grp_id = $('#grp_id');
				
				let oldVal = '';
				grp_id.focusin(function(){
					oldVal = grp_id.val();
				});
				
				grp_id.focusout(function(e) {
					// 변화가 없을 경우 이벤트 중지
					const newVal = grp_id.val();
					if(oldVal == newVal){
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
						url: '/api/code/checkCodeGrpDuplicated',
						data: {
							grp_id: newVal
						},
						done: function(data){
							data = data.result;
							
							if(data.CODE == '200'){
								that.checkId = true;
								duplicated.addClass('validate-success animated flipInY');
								grp_id.css({'border-color':'#ebedf2'});
							}
							else if(data.CODE == '201'){
								that.checkId = false;
								duplicated.addClass('validate-fail animated flipInY');
								grp_id.css({'border-color':'var(--red)'});
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
				if(pgType=='insert'){
					footer.find('[role="action"][action="add"]').show();
				}
				else if(pgType=='modify'){
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
				}
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]').remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			const footer = this.modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				
				const grp_id = $('#grp_id').val();
				const grp_nm = $('#grp_nm').val();
				const grp_desc = $('#grp_desc').val();
				
				const inputValues = [grp_id, grp_nm];
				if(['add','update'].indexOf(action) > -1 && !inputValues.every(value => value)){
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				// 추가
				if(action == 'add'){
					if(that.checkId != true){
						Alert.warning({text: '아이디 사용 가능 여부를 확인해주세요!'});
						return;
					}

					AjaxUtil.request({
						url: '/api/code/insertCodeGroupInfo',
						data: {
							grp_id: grp_id,
							grp_nm: grp_nm,
							grp_desc: grp_desc,
						},
						table: 'listTable',
						modal: modal,
						successMessage: '코드 그룹이 성공적으로 추가되었습니다.',
						failMessage: '코드 그룹 생성중 오류가 발생하였습니다.'
					});
				}
				// 수정
				else if (action == 'update'){
					AjaxUtil.request({
						url: '/api/code/updateCodeGroupInfo',
						data: {
							rec_key: that.item.rec_key,
							grp_id: that.item.grp_id,
							grp_nm: grp_nm,
							grp_desc: grp_desc
						},
						table: 'listTable',
						modal: modal,
						successMessage: '코드 그룹이 성공적으로 수정되었습니다.',
						failMessage: '코드 그룹 수정중 오류가 발생하였습니다.'
					});
				}
				// 삭제
				else if (action == 'delete'){
					AjaxUtil.request({
						url: '/api/code/deleteCodeGroupInfo',
						data: {
							rec_key: that.item.rec_key
						},
						table: 'listTable',
						modal: modal,
						successMessage: '코드 그룹이 성공적으로 삭제되었습니다.',
						failMessage: '그룹에 해당하는 코드 목록이 있어 삭제할 수 없습니다.'
					});
				}
				else {
					console.log('알수없는 Action : ',action);
				}
			});
		}
	};
	
	Content.load({
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		}
	});
});