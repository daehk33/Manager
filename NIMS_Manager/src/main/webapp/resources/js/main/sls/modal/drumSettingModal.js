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
				
				let libList = List.library({}, true);
				body.find('#library_key').html(libList);
				
				common.disableFieldAndLabel('library_key', false);
				common.disableFieldAndLabel('device_key', false);
				
				// 초기화
				const itemField = body.find('.item-field');
				itemField.each(i => {
					const field = $(itemField[i]);
					field.val('');
				});
				
				footer.show();
				footer.find('[role="action"]').hide();
				
				// 선택된 도서관정보가 있을 경우
				const libKey = sessionStorage.getItem('library_key');
				if(libKey != ''){
					body.find('#library_key').val(libKey);
					common.disableFieldAndLabel('library_key', true);
					body.find('#device_key').html(List.device({library_key: libKey}, true));
				}
				
				title.text('서가 설정 수정');
				
				const item = JSON.parse($('.param[name="item"]').val());
				that.item = item;
				
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					field.val(item[id]);
				});
				
				common.disableFieldAndLabel('library_key', true);
				common.disableFieldAndLabel('device_key', true);
				
				footer.find('[role="action"][action="update"]').show();
				footer.find('[role="action"][action="delete"]').show();
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
			
				const use = $('#use').val();
				const volume = $('#volume').val();
				const grade = $('#grade').val();
				const use_smartlib = $('#use_smartlib').val();
				const use_reserve = $('#use_reserve').val();
				const use_other_loc_return = $('#use_other_loc_return').val();
				const use_other_lib_return = $('#use_other_lib_return').val();
				
				const inputValues = [use, volume, grade, use_smartlib, use_reserve, use_other_loc_return, use_other_lib_return];
				
				if(!inputValues.every(value => value)) {
					Alert.warning({text: '일부 설정 값이 입력되지 않았습니다.'});
					return;
				}
				
				AjaxUtil.request({
					type: 'POST',
					url: '/api/device/updateDrumRule',
					data: Object.assign({
						rec_key: that.item.rec_key,
						device_key: that.item.device_key,
						cas_type: grade,
						drum: that.item.posi,
						tray: that.item.step,
						use: use,
						volume: volume,
						grade: grade,
						use_smartlib: use_smartlib,
						use_reserve: use_reserve,
						use_other_loc_return: use_other_loc_return,
						use_other_lib_return: use_other_lib_return,
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
				data: {
					library_key: params.library_key
				},
				done: function(data) {
					items = data.result.data;
				}
			});
			
			if(option_yn){
				let optionList = '<option value=""></option>';
				
				if(items == null || items.length == 0) {
					optionList += '<option value="">등록된 장비가 없습니다.</option>';
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
	
	const Validate = {
		configKey : function(e) {
			e.target.value = e.target.value.replace(/[^A-Za-z-_]/ig, '');
		}
	}

	Content.load({
		onHide: () => {
			Device.modal.modal('hide');
			Tabulator.prototype.findTable('#settingTable')[0].setData('/api/device/getDrumRuleList', {}, ajaxConfig);
		}
	});
});