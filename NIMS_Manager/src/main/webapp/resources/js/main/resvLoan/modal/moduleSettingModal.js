/**
 * @Desc Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			ModuleSetting.load();
		}
	};
	
	/**
	 * @Desc 모듈 설정 관리 객체
	 */
	const ModuleSetting = {
		modal : null,
		item: null,
		checkId: false,
		load : function() {
			this.modal = $('[role="modal"][target="moduleSetting"]');
			
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			let pgType = '';
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				const duplicated = body.find('[role="msg"][target="duplicated"]');
				let cabinetRow = body.find('.row[role="cabinet"]');
				let directionSelect = body.find('#direction');
				let centerOpt = directionSelect[0][1];
				
				pgType = $('.param[name="item"]').attr('action');
				
				let libList = List.library({}, true);
				body.find('#library_key').html(libList);
				
				let deviceList = List.device({}, true);
				body.find('#device_key').html(deviceList);
				
				common.disableFieldAndLabel('library_key', true);
				common.disableFieldAndLabel('device_key', true);
				common.disableFieldAndLabel('module_id', true);
				common.disableFieldAndLabel('direction', true);
				common.disableFieldAndLabel('cabinet_no', true);
				common.disableFieldAndLabel('column_cnt', true);
				common.disableFieldAndLabel('row_cnt', true);
				duplicated.removeClass();
				
				// 초기화
				const itemField = body.find('.item-field');
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					field.val('');
				});
				
				// 필수 항목 표기
				common.markNecessaryField('library_key');
				common.markNecessaryField('device_key');
				common.markNecessaryField('module_id');
				common.markNecessaryField('direction');
				
				// 선택된 도서관정보가 있을 경우
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != ''){
					body.find('#library_key').val(libKey);
					common.disableFieldAndLabel('library_key', true);
					body.find('#device_key').html(List.device({library_key: libKey}, true));
				}
				
				// 추가
				if(pgType == 'insert'){
					title.text('모듈 등록');
					
					common.disableFieldAndLabel('library_key', false);
					common.disableFieldAndLabel('device_key', false);
					common.disableFieldAndLabel('module_id', false);
					common.disableFieldAndLabel('direction', false);
					
					cabinetRow.hide();
					duplicated.show();
					centerOpt.hidden = true;
				}
				// 삭제
				else {
					title.text('모듈 삭제');
					
					cabinetRow.show();
					duplicated.hide();
					centerOpt.hidden = false;
					
					const item = JSON.parse($('.param[name="item"]').val());
					that.item = item;
					
					let deviceList = List.device(item, true);
					body.find('#device_key').html(deviceList);
					
					itemField.each(i => {
						const field = $(itemField[i]);
						const id = field.attr('id');
						field.val(item[id]);
					});
				}
				
				const moduleInput = $('#module_id');
				
				// 아이디 중복 체크
				moduleInput.focusout(function(e) {
					const library_key = $('#library_key').val();
					const device_key = $('#device_key').val();
					const module_id = $('#module_id').val();
					
					// 중복 체크 표시 제거
					duplicated.removeClass();
					
					// 빈 값의 경우 이벤트 중지
					if(module_id == '') {
						that.checkId = false;
						return false;
					}
					
					if(library_key == '') {
						that.checkId = false;
						return false;
					}
					
					if(device_key == '') {
						that.checkId = false;
						return false;
					}
					
					// 중복 체크
					AjaxUtil.request({
						url: '/api/resvLoan/checkModuleDuplicated',
						data: {
							library_key: library_key,
							device_key: device_key,
							module_id: module_id
						},
						done: function(data){
							data = data.result;
							
							if(data.CODE == '200'){
								that.checkId = true;
								duplicated.addClass('validate-success animated flipInY');
								moduleInput.css({'border-color':'#ebedf2'});
							}
							else if(data.CODE == '201'){
								that.checkId = false;
								duplicated.addClass('validate-fail animated flipInY');
								moduleInput.css({'border-color':'var(--red)'});
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
				$('#library_key').change(function() {
					const body = modal.find('.modal-content .modal-body');
					
					// 선택한 도서관 키 값
					let libKey = $('#library_key option:selected').val();
					
					let deviceList = List.device({library_key: libKey}, true);
					body.find('#device_key').html(deviceList);
				});
				
				const footer = modal.find('.modal-footer');
				if(pgType=='insert'){
					footer.find('[role="action"][action="add"]').show();
				}
				else if(pgType=='modify'){
					footer.find('[role="action"][action="delete"]').show();
				}
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
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				
				const device_key = $('#device_key').val();
				const module_id = $('#module_id').val();
				const direction = $('#direction').val();
				
				const inputValues = [library_key, device_key, module_id, direction];
				if(action == "add" && !inputValues.every(value => value)){
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
						url: '/api/resvLoan/insertResvLoanModule',
						data: {
							device_key: device_key,
							module_id: module_id,
							direction: direction
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
				// 삭제
				else if (action == 'delete'){
					AjaxUtil.request({
						url: '/api/resvLoan/deleteResvLoanModule',
						data: {
							device_key: device_key,
							module_id: module_id,
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
				else {
					console.log('알수없는 Action : ',action);
				}
			});
		}
	};
	
	const List = {
		library : function(params, option_yn) {
			let items = '';
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/library/getLibraryList',
				async: false,
				done: function(data) {
					items = data.result.data;
				}
			});
			
			if(option_yn){
				let optionList = '<option value=""></option>';
				
				items.forEach(lib => {
					optionList += `<option value="${lib.rec_key}">${lib.library_nm}</option>`;
				});
				return optionList;
			}
			
			return items;
		},
		device : function(params, option_yn) {
			let items = '';
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/device/getDeviceInfoList',
				async: false,
				data: {
					model_key: 8
				},
				done: function(data) {
					items = data.result.data;
				}
			});
			
			if(option_yn){
				let optionList = '<option value=""></option>';
				
				if(items == null || items.length == 0) {
					optionList += '<option value="" disabled>등록된 장비가 없습니다.</option>';
				}
				else {
					items.forEach(device => {
						optionList += 
							`<option value="${device.device_key}" data-model="${device.model_nm}">
								${device.device_id}
							</option>`;
					})
				}
				return optionList;
			}
			
			return items;
		}
	}
	
	Content.load({
		onHide: () => {
			Tabulator.prototype.findTable('#settingTable')[0].setData();
		}
	});
});