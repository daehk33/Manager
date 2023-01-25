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
		type: '',
		load : function() {
			this.modal = $('#receiptModal');
			
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
				const type = param.attr('action');
				that.params = params;
				that.type = type;
				
				/**
				 * Footer 옵션 - mobile의 경우에는 안보이도록
				 */
				const mobile = window.innerWidth <= 600;
				footer.show();
				if(mobile){
					footer.hide();
				}
				
				/**
				 * 영수증 내용
				 */
				let location = params.location || '';
				if(location != ''){
					location = `<b>소장위치</b>: ${location}<br>`;
				}
				
				body.html(`
					<div class="book-receipt-wrapper" id="receipt">
						<div class="book-receipt-container">
							<div class="book-receipt-title">
								<img src="/resources/img/favicon/logo.png" alt="wrapkit" height="50px" width="134px" class="brand-logo">
							</div><br>
							<div class="book-receipt-title">[ 도서 영수증 ]</div><br>
							<p>
								<b>서명</b>: ${params.title}<br>
								<b>저자</b>: ${params.author}<br>
								<b>등록번호</b>: ${params.book_no}<br>
								<b>청구기호</b>: ${params.call_no}<br>
								${location}
							</p>
							<div style="margin-top: 1rem">
								<img id="barcode" width="220px" />
							</div>
						</div>
					</div>
				`);
				
				JsBarcode('#barcode', params.book_no, {
					lineColor: '#333',
					width:4,
					height:80,
					displayValue: false
				})
			});
			
			modal.on('hide.bs.modal', function(e) {
				modal.find('.param[name="item"]').remove();
			})
			
			this.event();
			this.action();
		},
		event: function(){
			const that = this;
			const modal = this.modal;
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			
			modal.find('.btn[role="action"]').click(function(e){
				const action = this.dataset.action;
				const param = Modal.params;
				
				// 서지 정보 추가
				if(action == 'print'){
					const style = `
						body {
							font-size: 0.9rem;
						}
						.book-receipt-container {
							width: 250px;
							border: solid 1px #eee;
							padding: 2rem 1rem;
							text-align: center;
							display: flex;
							flex-direction: column;
							height: 350px;
							justify-content: center;
						}
						.book-receipt-title {
							font-size: 1.1rem;
							font-weight: 600;
						}
						.book-receipt-container p {
							text-align: left !important;
							margin: 0px 0.5rem;
							width: 100%;
						}
						#barcode {
							width: 100%;
						}
					`;
					
					PrintUtil.print({
						id: 'receipt',
						title: '도서 영수증',
						style: style,
						height: 500,
						width: 320
					});
				}
			});
		}
	};

	Content.load({});
});