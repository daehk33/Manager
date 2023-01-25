/**
 * @Group 검색 Container 이벤트 관리 객체
 * @Desc 기간별 이력 검색
 */
const SearchContainer = {
	obj: null,
	load: function(obj){
		this.obj = obj;
		
		// 1주일 검색으로 초기화
		$('input[name="type"][value="week"]').prop('checked', true);
		
		$('input[name="startDate"]').val(common.getDateToStr('day', -7));
		$('input[name="endDate"]').val(common.getDateToStr());
		
		this.event();
	},
	event: function(){
		const searchButton = $('button[role="search"]');
		
		const typeButton = $('input[name="type"]');
		const typeWeek = $('input[name="type"][value="week"]');
		const typeMonth = $('input[name="type"][value="month"]');
		
		// 날짜 변수 선언
		const today = common.getDateToStr();
		const week = common.getDateToStr('day', -7);
		const month = common.getDateToStr('day', 0, -1);
		
		const startDate = $('input[name="startDate"]');
		const endDate = $('input[name="endDate"]');
		
		const dateField = $('input[type="date"]'); 
		// 날짜 제한
		dateField.attr('max', today);
		dateField.change(function(){
			typeButton.prop('checked', false);
			if(endDate.val() != today) return;
			
			if(startDate.val() == week){
				typeWeek.prop('checked', true);
			}
			else if(startDate.val() == month){
				typeMonth.prop('checked', true);
			}
		});
		
		// 빠른 검색 버튼 클릭시
		typeButton.click(function(){
			const val = $(this).val();
			
			endDate.val(today);
			// 최근 일주일
			if(val == 'week'){
				startDate.val(week);
			}
			// 최근 한달
			else if(val == 'month'){
				startDate.val(month);
			}
			searchButton.trigger('click');
		});
		
		// 검색 버튼 클릭시
		searchButton.click(function(){
			Search.load();
		});
		
		// 엔터 클릭시
		$('input[name="startDate"], input[name="endDate"]').on('keypress', function(e) {
			let key = e.key || e.keyCode;
			
			if (key === 'Enter' || key === 13) {
				Search.load();
			}
		});
		
		// 도서관 선택 시
		$('input[name="library"]').change(function(){
			SearchContainer.obj.table.redraw();
		});
	}
}
/**
 * @Group 기간별 로그 검색
 * @Desc 검색 관리 객체
 */
const Search = {
	params: {startDate: '', endDate: ''},
	load: function(){
		this.params.startDate = $('input[name="startDate"]').val();
		this.params.endDate = $('input[name="endDate"]').val();
		
		if(this.validate()){
			this.submit();
		}
	},
	validate: function(){
		if(this.params.startDate == '' || this.params.endDate == ''){
			Alert.warning({text: '기한을 선택해주세요!'});
			return false;
		}
		return true;
	},
	submit: function(){
		let Content = SearchContainer.obj;
		Content.table.setData(Content.params.url, this.params, ajaxConfig);
	}
};