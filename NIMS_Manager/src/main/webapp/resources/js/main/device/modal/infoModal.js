/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load: function(params) {
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
			let pgType = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				let libList = '<option value="">&nbsp;</option>';
				let modelList = '<option vlaue="">&nbsp;</option>';
				
				pgType = $('.param[name="item"]').attr('action');
				
				AjaxUtil.request({
					url: '/api/library/getLibraryList',
					async: false,
					done: function(data) {
						data = data.result.data;
						if(data.length > 0) libList = '';
						data.forEach(lib => {
							libList += `<option value="${lib.rec_key}">${lib.library_nm}</option>`;
						});
						body.find('#library_key').html(libList);
					}
				});
				
				AjaxUtil.request({
					url: '/api/model/getModelList',
					async: false,
					done: function(data) {
						data = data.result.data;
						if(data.length > 0) modelList = '';
						data.forEach(model => {
							modelList += `<option value="${model.rec_key}">${model.model_nm} (${model.model_type_nm})</option>`;
						});
						body.find('#model_key').html(modelList);
					}
				});
				
				const itemField = body.find('.item-field');
				
				// 초기화
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
				
				// 필수 항목 표기
				common.markNecessaryField('library_key');
				common.markNecessaryField('model_key');
				common.markNecessaryField('config_yn');
				common.markNecessaryField('device_id');
				common.markNecessaryField('device_nm');
				common.markNecessaryField('device_location');
				common.markNecessaryField('device_ip');
				
				// 선택된 도서관정보가 있을 경우
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != ''){
					body.find('#library_key').val(libKey);
					common.disableFieldAndLabel('library_key', true);
				}
				else {
					common.disableFieldAndLabel('library_key', false);
				}
				// 추가
				if(pgType == 'insert') {
					title.text('장비 정보 추가');
					
					footer.find('[role="action"][action="add"]').show();
				}
				// 수정
				else if(pgType == 'modify') {
					title.text('장비 정보 수정');
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
					
					footer.find('[role="action"][action="update"]').show();
					footer.find('[role="action"][action="delete"]').show();
				}
			});
			
			modal.on('shown.bs.modal', function(e) {
				$('#device_ip').inputmask({
					alias: "ip",
					placeholder: ' ',
					showMaskOnHover: false,
					showMaskOnFocus: false
				});
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]');
				item.remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			const footer = modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				
				const library_key = $('#library_key option:selected').val();
				const model_key = $('#model_key option:selected').val();
				const device_nm = $('#device_nm').val();
				const device_id = $('#device_id').val();
				const device_location = $('#device_location').val();
				const config_yn = $('#config_yn option:selected').val();
				const device_ip = $('#device_ip').val();
				const device_desc = $('#device_desc').val();
				
				const inputValues = [library_key, model_key, device_nm, device_id, device_location, config_yn, device_ip];
				
				if(['add', 'update'].indexOf(action) > -1 && !inputValues.every(value => value)) {
					Alert.warning({text: '필수 항목이 누락되었습니다.'});
					return;
				}
				
				if(action == 'add') {
					AjaxUtil.request({
						url: '/api/device/insertDeviceInfo',
						data: Object.assign({
							library_key: library_key,
							model_key: model_key,
							device_id: device_id,
							device_nm: device_nm,
							device_ip: device_ip,
							device_desc: device_desc,
							device_location: device_location,
							device_status: '0',
							connect_yn: 'N',
							config_yn: config_yn,
							did_yn: 'N',
						}),
						modal: modal,
						table: 'deviceTable',
						successMessage: '장비가 성공적으로 등록되었습니다.',
						failMessage: '장비 등록중 오류가 발생하였습니다.'
					});
				}
				else if (action == 'update') {
					AjaxUtil.request({
						url: '/api/device/updateDeviceInfo',
						data: Object.assign({
							rec_key: that.item.device_key,
							library_key: library_key,
							model_key: model_key,
							device_id: device_id,
							device_nm: device_nm,
							device_ip: device_ip,
							device_desc: device_desc,
							device_status: '0',
							connect_yn: 'N',
							config_yn: config_yn,
							did_yn: 'N',
						}),
						modal: modal,
						table: 'deviceTable',
						successMessage: '장비가 성공적으로 수정되었습니다.',
						failMessage: '장비 수정중 오류가 발생하였습니다.'
					});
				}
				else if (action == "delete") {
					let confirmParams = {title: '장비 삭제', text: '정말로 삭제하시겠습니까? 장비 삭제 시 해당 장비의 정책도 같이 삭제됩니다.', confirmText: '삭제'}
					Alert.confirm(confirmParams, (result) => {
						if(!result.isConfirmed) return;
						
						AjaxUtil.request({
							url: '/api/device/deleteDeviceInfo',
							data: Object.assign({
								rec_key: that.item.device_key,
							}),
							modal: modal,
							table: 'deviceTable',
							successMessage: '장비가 성공적으로 삭제되었습니다.',
							failMessage: '현재 사용중인 장비는 삭제할 수 없습니다.'
						});
					});
				}
			});
		}
	};
  
	Content.load();
});