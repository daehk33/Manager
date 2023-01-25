/**
 * @Desc Dashboard Event for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			Event.load();
		}
	};
	
	const Event = {
		modal : null,
		params: {},
		load : function() {
			this.modal = $('[role="modal"][target="event"]');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				const params = JSON.parse($('.param').val());
				that.params = params;
				
				if($('.param').attr('action') == 'info') footer.hide();
				else footer.show();
				
				header.find('.badge').css({'background': params.labelColor});
				header.find('.badge').text(params.labelName);
				
				body.find('.date').text(params.date);
				body.find('.text').text(params.text);
			});
			
			modal.on('shown.bs.modal', function(e) {
				const footer = modal.find('.modal-footer');
				const params = that.params;
				
				footer.find('[role="action"]').click(function(){
					const action = $(this).attr('action');
					if(action == 'confirm'){
						AjaxUtil.request({
							url: '/api/dashboard/deleteAlarmInfo',
							data: {id: params.id},
							done: function(data){
								if(data.result.CODE == '200'){
									modal.modal('hide');
								}
							}
						});
					}
					
					modal.attr('data-changed', 'true');
				})
			});
		},
	};

	Content.load({});
});