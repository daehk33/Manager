/**
 * @Desc slsBookOutSelect for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Modal.load();
		}
	};
	
	const Modal = {
		modal : null,
		load : function() {
			this.modal = $('#slsBookOutSelectModal');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			let pgType = null;
			let optionList = null;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const body = modal.find('.modal-content .modal-body');
				
				const param = modal.find('.param');
				const params = JSON.parse(param.val());
				
				const deviceSelect = body.find('#device_key');
				
				AjaxUtil.request({
					url: '/api/device/getDeviceList',
					data: {
						model_key: '6',
					},
					async: false,
					done: function(data) {
						data = data.result.data;
						
						optionList = '<option value=""></option>';
						
						if(data.length == 0) {
							optionList += '<option value="">등록된 장비가 없습니다.</option>';
						}
						else {
							for(item of data) {
								optionList +=
									`<option value="${item.rec_key}" data-device_id=${item.device_id}>
										${item.device_nm}[${item.device_id}]
									</option>`;
							}
						}
					}
				});
				
				deviceSelect.html(optionList);
			});
			
			modal.on('hide.bs.modal', function(e) {
				modal.find('.param').remove();
			});
			
			this.action();
		},
		action: function() {
			const that = this;
			
			const body = this.modal.find('.modal-body');
			const footer = this.modal.find('.modal-footer');
			
			footer.find('[role="action"]').click(function(e) {
				const action = $(this).attr('action');
				
				const deviceSelect = body.find('#device_key');
				const deviceKey = deviceSelect[0].value;
				const deviceId = deviceSelect[0].selectedOptions[0].dataset.device_id;
				
				if(!deviceKey || !deviceId) {
					Alert.warning({text: '장비를 선택해 주세요.'});
				}
				else {
					// 도서 배출 작업 상태 변경
					AjaxUtil.request({
						url: '/api/device/updateBookOutWorkStatus',
						async: false,
						data: {
							rec_key: deviceKey,
							device_id: deviceId,
							book_out_work_status: action == 'start' ? 1 : 0
						},
						done: function(data) {
							data = data.result;
							
							if(data.CODE == "200") Alert.success({text: '요청 처리가 완료되었습니다.'}, Content.params.onHide());
							else Alert.warning({text: '처리 중 오류가 발생하였습니다.'}, Content.params.onHide());
						}
					});
				}
			});
		}
	};

	Content.load({
		onHide: () => {
			Modal.modal.modal('hide');
		}
	});
});