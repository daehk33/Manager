/**
 * @Desc Dashboard Device for Modal Function
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
		modal : null,
		load : function() {
			this.modal = $('[role="modal"][target="deviceMain"]');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const body = modal.find('.modal-content .modal-body');
				const itemField = body.find('.item-field');
				const params = JSON.parse(modal.find('.param').val());
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					if(id == 'model_id'){
						field.attr('src', '');
					}
					field.text('');
				});
				
				itemField.each(i => {
					const field = $(itemField[i]);
					const id = field.attr('id');
					if(id == 'model_id'){
						field.attr('src', `/resources/img/model/${params.model_id}.png`);
					}
					if(id == 'device_status'){
						params[id] = params[id] == 1 ? "ON" : "OFF";
					}
					if(id == 'connect_yn') {
						params[id] = params[id] == 'Y' ? "연결" : "연결끊김";
					}
					field.text(params[id]);
				});
				
			});
			
			modal.on('hide.bs.modal', function(e) {
				$('.param').remove();
			});
			
			this.action();
		},
		action: function() {
			// Action Event
		}
	};

	Content.load({});
});