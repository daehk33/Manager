/**
 * @Desc Library for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Library.load();
		}
	};
	
	const Library = {
		modal : null,
		item: null,
		checkId: false,
		load : function() {
			this.modal = $('[role="modal"][target="library"]');

			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			// Modal Type - insert, delete
			let pgType = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				pgType = $('.param[name="item"]').attr('action');
				
				const itemField = body.find('.item-field');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				
				// 초기화
				common.disableFieldAndLabel('library_id', false);
				duplicated.show();
				
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				// 필수 항목 표기
				common.markNecessaryField('library_id');
				common.markNecessaryField('library_nm');
				common.markNecessaryField('location');
				
				// 추가
				if(pgType == 'insert'){
					title.text('도서관 등록');
				}
				// 수정
				else if(pgType == 'modify'){
					title.text('도서관 수정');
					
					common.disableFieldAndLabel('library_id', true);
					duplicated.hide();
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
				}
				
				const library_id = $('#library_id');
				
				let oldVal = '';
				library_id.focusin(function(){
					oldVal = library_id.val();
				});
				
				library_id.focusout(function() {
					const newVal = library_id.val();
					
					// 변화가 없을 경우 이벤트 중지
					if(oldVal == newVal) {
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
						url: '/api/library/checkLibraryDuplicated',
						library: false,
						data: {
							library_id: newVal,
						},
						done: function(data){
							data = data.result;
							if(data.CODE == '200'){
								that.checkId = true;
								duplicated.addClass('validate-success animated flipInY');
								library_id.css({'border-color':'#ebedf2'});
							}
							else if(data.CODE == '201'){
								that.checkId = false;
								duplicated.addClass('validate-fail animated flipInY');
								library_id.css({'border-color':'var(--red)'});
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
		action: function() {
			const that = this;
			
			const footer = this.modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				const library_id = $('#library_id').val();
				const library_nm = $('#library_nm').val();
				const library_loc = $('#location').val();
				const library_desc = $('#library_desc').val();
				
				const inputValues = [library_id, library_nm, library_loc];
				
				if(['add', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)){
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add'){
					if(that.checkId != true){
						Alert.warning({text: '아이디 사용 가능 여부를 확인해주세요!'});
						return;
					}
					AjaxUtil.request({
						url: '/api/library/insertLibraryInfo',
						library: false,
						data: {
							library_id: library_id,
							library_nm: library_nm,
							location: library_loc,
							library_desc: library_desc
						},
						done: function(data){
							data = data.result;

							that.modal.modal('hide');
							if(data.CODE == '200'){
								Alert.success({text: data.DESC}, Content.params.onHide);
							} 
							else {
								Alert.danger({text: data.DESC})
							}
						}
					});
				}
				else if (action == 'update'){
					AjaxUtil.request({
						url: '/api/library/updateLibraryInfo',
						library: false,
						data: {
							rec_key: that.item.rec_key,
							library_id: that.item.library_id,
							library_nm: library_nm,
							location: library_loc,
							library_desc: library_desc
						},
						done: function(data){
							data = data.result;

							that.modal.modal('hide');
							if(data.CODE == '200'){
								Alert.success({text: data.DESC}, Content.params.onHide);
							} 
							else {
								Alert.danger({text: data.DESC})
							}
						}
					});
				}
				else if (action == 'delete'){
					let confirmParams = {title: '도서관 정보 삭제', 
							text: '장비에 등록된 정보는 삭제할 수 없으며, 정보를 삭제하면 시스템의 일부 정보가 제대로 보여지지 않을 수 있습니다. 정말로 삭제하시겠습니까?'};
					
					Alert.confirm(confirmParams, (result) => {
						if(result.isDismissed) return;
						AjaxUtil.request({
							url: '/api/library/deleteLibraryInfo',
							library: false,
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
									Alert.warning({title: '삭제 불가', text: '장비에 등록된 도서관 정보는 삭제할 수 없습니다.'});
								}
							}
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
		onHide: () => {
			Tabulator.prototype.findTable('#listTable')[0].setData();
		},
	});
});