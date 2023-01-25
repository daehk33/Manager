/**
 * Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load: function(params) {
			this.params = params;
			
			Input.load();
		}
	};
	
	const Input = {
		modal: null,
		item: null,
		results: [],
		load: function() {
			this.modal = $('[role="modal"][target="input"]');
			
			// Initialize modal
			const body = this.modal.find('.modal-content .modal-body');
			
			this.event();
		},
		event: function() {
			const that = this;
			const modal = that.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				const item = JSON.parse($('.param[name="item"]').val());
				that.item = item;
				
				let libList = '<option value="">&nbsp;</option>';
				
				title.text(`${that.item.type.toUpperCase()} 포트 설정`);
			});
			
			modal.on('hide.bs.modal', function(e) {
				const item = $('.param[name="item"]');
				item.remove();
			});
			
			that.action();
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			const footer = modal.find('.modal-footer');
			
			footer.find('[action="save"]').click(function(e) {
				const item = that.item;
				that.results = [];
				
				if(item.portList.length > 0) {
					that.sendRequest(item.portList, 'upsert');
				}
				
				if(item.changeList.length > 0) {
					that.sendRequest(item.changeList, 'upsert');
				}
				
				if(item.deleteList.length > 0) {
					that.sendRequest(item.deleteList, 'delete');
				}
				
				if (that.results.every(result => result)) {
					Alert.success({text: '포트 설정이 완료되었습니다!'}, Content.params.onHide);
				}
				else {
					Alert.warning({text: '일부 항목 처리 중 오류가 발생하였습니다.'}, Content.params.onHide);
				}
			});	
		},
		setData: function(port) {
			const that = this;
			const modal = that.modal;
			
			let data = {
				library_key: that.item.library_key,
				port: port,
				type: that.item.type,
				baudrate: modal.find('#baudrate').val(),
				databit: modal.find('#databit').val(),
				paritybit: modal.find('#paritybity').val(),
				stopbit: modal.find('#stopbit').val()
			};
			
			return data;
		},
		sendRequest: function(list, type) {
			const that = this;
			const url = type == 'upsert' ? '/api/gate/upsertInputInfo' : '/api/gate/deleteInputInfo';
			
			for(port of list) {
				AjaxUtil.request({
					url: url,
					async: false,
					data: that.setData(port),
					done: function(data) {
						data = data.result;
						
						data.CODE == "200" ? that.results.push(true) : that.results.push(false);
					},
					error: function(error) {
						console.log(error);
					}
				});
			}
		}
	};
	
	Content.load({
		onHide: () => {
			Input.modal.modal('hide');
			$('#inputTab div[role="select"] select').trigger('change');
		}
	});
});