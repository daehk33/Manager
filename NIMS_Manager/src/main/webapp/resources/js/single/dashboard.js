$(function () {
	const Content = {
		params: null,
		load: function(params) {
			this.params = params;
			
			this.result();
		},
		result: function() {
			this.event();
		},
		event: function() {
			const btnBack = $('.btns.btn-back');
			
			btnBack.click(function() {
				location.href = '/main';
			});
			
			$("body").mousemove(function(e){
				if(e.pageY <= 10) {
					btnBack[0].dataset.show = true;
				}
				
				if(btnBack[0].dataset.show && e.pageY > 140) {
					btnBack[0].dataset.show = false;
				}
			});
		}
	};
	
	Content.load();
});