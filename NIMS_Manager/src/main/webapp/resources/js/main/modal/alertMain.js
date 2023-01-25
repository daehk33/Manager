/**
 * @Desc Alert for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Alert.load();
		}
	};
	
	const Alert = {
		modal : null,
		load : function() {
			const that = this;
			this.modal = $('[role="modal"][target="alert"]');
			
			this.event();
		},
		event : function() {
			const that = this;
			const modal = this.modal;
			
			
			modal.on('show.bs.modal', function(e) {
				const body = modal.find('.modal-content .modal-body');
				const button = $(e.relatedTarget);
				const model_key = button[0].dataset.model_key;
				
				AjaxUtil.request({
					url: '/api/dashboard/getAlarmList',
					method: 'POST',
					data: {
						model_key: model_key
					},
					done: function(data){
						data = data.result;
						
						if(data.itemsCount == 0){
							body.html('<h4 style="text-align: center; margin: 10px 0px;">확인할 알림이 없습니다.</h4>');
							return;
						}
						body.html(`<div role="list"></div>
									<div class="danger-feed-item-action confirm-all">
										<button class="btn btn-success btn-round" role="action" action="confirmAll">
											<span style="padding-right:5px;">일괄 확인</span><i class="fas fa-check"></i>
										</button>
									</div>`);
						
						const container = body.find('[role="list"]');
						container.addClass('danger-feed-list');
						data.items.forEach((item, idx) => {
							item.date = new Date(item.date).toLocaleString();
							
							container.append(`
								<div class="danger-feed-item">
									<div class="danger-feed-item-idx">
										${idx+1}
									</div>
									<div class="danger-feed-item-info">
										<time class="date">
											<i class="fas fa-clock"></i>
											<span class="icon-title">${item.date}</span>
										</time>
										<span class="text">
											<label class="text" style="overflow-wrap: anywhere">${item.message}</label>
										</span>
									</div>
									<div class="danger-feed-item-action" data-id="${item.id}">
										<button class="btn btn-success btn-round" role="action" action="confirm">
											<span style="padding-right:5px;">확인</span><i class="fas fa-check"></i>
										</button>
									</div>
								</div>`);
						});
						
						that.action();
					}
				});
			});
		},
		action: function() {
			const modal = this.modal;
			
			modal.find('[role="action"][action="confirm"]').click(function(e){
				const that = $(this);
				const row = $(this).parent().parent();
				const id = $(this).parent().attr('data-id');
				
				AjaxUtil.request({
					url: '/api/dashboard/deleteAlarmInfo',
					async: false,
					data: {id: id},
					done: function(data){
						if(data.result.CODE == '200'){
							that.attr('class', 'btn btn-round btn-icon');
							that.css({'background-color': '#197e1d', 'border-color': '#197e1d', 'color': 'white'})
							that.html('<i class="fas fa-check"></i>');
							that.prop('disabled', true);
							row.addClass('active');
						}
					}
				});
			});
			
			modal.find('[role="action"][action="confirmAll"]').click(function(e){
				const rows = modal.find('[role="action"][action="confirm"]');
				
				for(target of rows) {
					$(target).trigger('click');
				}
				
				modal.modal('hide');
			});
		}
	};

	Content.load({});
});