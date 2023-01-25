/**
 * @Desc Template for Modal Function
 */

$(function() {
	const Content = {
		params: null,
		/**
		 * @Desc 페이지의 정보 초기화 메소드, 주로 전달된 매개변수들을 가공하는 데 사용. 
		 */
		load : function(params) {
			this.params = params;
			
			Template.load();
		}
	};
	
	const Template = {
		modal : null,
		/**
		 * @Desc 주로 Ajax를 통해 불러온 데이터를 저장하거나 객체 변수 초기화에 사용.
		 */
		load : function() {
			this.modal = $('[role="modal"][target="template"]');
			
			this.event();
		},
		/**
		 * @Desc Modal 생명주기 관련 Event
		 */
		event : function() {
			const that = this;
			const modal = this.modal;
			
			/**
			 * [Modal Event] show.bs.modal
			 * @Desc 팝업 생성 이벤트 발생 직후
			 * @Use 초기화에 사용
			 */ 
			modal.on('show.bs.modal', function(e) {
				/**
				 * [Modal Element] header
				 * @Desc 팝업창의 제목 - header안의 xxxModalLabel로 제목 변경 가능
				 */
				const header = modal.find('.modal-header');
				/**
				 * [Modal Element] body
				 * @Desc 팝업창의 내용
				 */
				const body = modal.find('.modal-content .modal-body');
				/**
				 * [Modal Element] footer
				 * @Desc 팝업창에서 필요한 작업 - 주로 submit이나 close의 경우, action의 경우 아래와 같이 다룰수 있음. 
				 */
				const footer = modal.find('.modal-footer');
				footer.find('[role="action"]').click(function(){
					const action = $(this).attr('action');
					if (action == 'close'){
						that.modal.modal('hide');
					}
				});
			});
			
			/**
			 * [Modal Event] shown.bs.modal
			 * @Desc 팝업이 생성되고 CSS가 완전히 적용된 후
			 * @Use 주로 화면이 완전히 그려진 후 필요한 작업(지도 API가 대표적인 사례) 
			 */
			modal.on('shown.bs.modal', function(e) {
				console.log(e);
			});
			
			/**
			 * [Modal Event] hide.bs.modal
			 * @Desc 팝업이 사라진 직후
			 * @Use 초기화 작업에서 직접 생성된 element들을 정리(garbage collector 용도)
			 */ 
			modal.on('hide.bs.modal', function(e) {
				console.log(e);
			});
			
			this.action();
		},
		/**
		 * [Modal Action Event] 
		 * @Desc action으로 정의한 이유는 이미 Modal 상태가 event로 정의되었기 때문. 
		 * footer의 action 이벤트가 길어질 경우, 가독성을 위해 빼는 용도.
		 */
		action: function() {
			// Action Event
		}
	};

	Content.load({});
});