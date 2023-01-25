$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Modal.load();
		}
	};
	
	const Modal = {
		modal: null,
		params: {},
		load : function() {
			this.modal = $('#infoModal');
			
			this.draw();
		},
		draw : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				const param = modal.find('.param[name="item"]');
				const params = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				that.params = params;
				
				const form = body.find('#useInfoForm');
				const inputs = body.find('.form-control');
				
				/**
				 * 초기화
				 */
				form.trigger('reset');
				
				inputs.removeClass('is-valid is-invalid');
				inputs.parent().removeClass('is-filled');
				
				/**
				 * update용 초기화 함수 
				 */
				const update = function(){
					// 필드 초기화
					Object.keys(params).forEach(key => {
						const input = modal.find(`.form-control[name="${key}"]`);
						const val = params[key];
						
						// 반납예정일 옵션처리
						if(key == 'return_plan_date'){
							if(val == '') input.val(DateUtil.getDateToStr());
							else input.val(val.split(' ')[0]);
						}
						else {
							input.val(val);
						}
						input.parent().addClass('is-filled');
					});
				};
				
				update();
				
				// 반납예정일 옵션처리
				const type = modal.find('.form-control[name="type"]');
				const returnPlanDate = modal.find('.form-control[name="return_plan_date"]');
				if(type.val() == '3'){
					returnPlanDate.parent().removeClass('hide');
				}
				else {
					returnPlanDate.parent().addClass('hide');
				}
			});
			
			this.event();
			this.action();
		},
		event: function(){
			const that = this;
			const modal = this.modal;
			
			const inputs = modal.find('.form-control');
			
			/**
			 * [Field Validation] 공통
			 */
			inputs.on({
				focus: function(e){
					const obj = $(e.target);
					if(e.target.value == ''){
						obj.removeClass('is-valid');
						obj.removeClass('is-invalid');
					}
				},
				focusout: function(e){
					const obj = $(e.target);
					const value = e.target.value;
					const name = e.target.name;
					
					if(value == ''){
						obj.parent().removeClass('is-filled');
						obj.removeClass('is-valid is-invalid');
						return false;
					}
				}
			});
			
			// 반납예정일 옵션처리
			const type = modal.find('.form-control[name="type"]');
			const returnPlanDate = modal.find('.form-control[name="return_plan_date"]');
			type.change(function(e){
				returnPlanDate.parent().addClass('hide');
				if(e.target.value == '3'){
					returnPlanDate.parent().removeClass('hide');
				}
			});
			
			returnPlanDate.attr('min', DateUtil.getDateToStr());
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			
			modal.find('.btn[role="action"]').click(function(e){
				const action = this.dataset.action;
				const inputs = modal.find('.form-control');
				const returnPlanDate = modal.find('.form-control[name="return_plan_date"]');
				const device_id = $('#filter-field option:selected')[0].dataset.device_id;
				const device_key = $('#filter-field').val();
				
				const param = modal.find('.param[name="item"]');
				const useInfo = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				
				const params = {};
				for(let i=0; i< inputs.length; i++){
					const name = inputs[i].name
					let value = inputs[i].value;
					
					params[name] = value;
				}
				
				params["rec_key"] = useInfo.rec_key;
				params["send_req_yn"] = 'Y';
				
				/*
				 * 반납예정일 format: yyyy-MM-dd week; 2021-12-22 수
				 */
				if(params.type == '3'){
					params['return_plan_date'] = returnPlanDate.val() + ' ' + DateUtil.getWeekToStr(returnPlanDate.val());
				} else {
					params['return_plan_date'] = '';
				}
				
				const table = Tabulator.prototype.findTable("#useInfoTable")[0];
				
				if(Utils.compareMap(Modal.params, params)){
					Alert.warning({text: '변경된 사항이 없습니다!'}, function(){
						modal.modal('hide');
					});
					
					return false;
				}
				
				if(action == 'update'){
					AjaxUtil.request({
						url: '/api/loanReturn/updateLoanReturnBannerSettingInfo',
						data: params,
						done: function(data) {
							data = data.result;
							
							if(data.CODE == "200") {
								BannerManager.sendChanged(device_id, '1');
								Alert.success({title: '설정 완료', text: '설정이 완료되었습니다!'}, Content.params.onHide);
							}
							else {
								Alert.danger({text: data.DESC});
							}
						}
					});
				}
			});
		}
	};

	Content.load({
		onHide: () => {
			Modal.modal.modal('hide');
			Tabulator.prototype.findTable('#infoSettingTable')[0].setData();
		}
	});
});