/**
 * @Group 검색 Container 이벤트 관리 객체
 * @Desc 기간별 이력 검색
 */
const SearchContainer = {
	obj: null,
	field: [],
	load: function(obj, field){
		this.obj = obj;
		this.field = field;
		
		// 1주일 검색으로 초기화
		$('input[name="type"][value="week"]').prop('checked', true);
		
		$('input[name="startDate"]').val(common.getDateToStr('day', -7));
		$('input[name="endDate"]').val(common.getDateToStr());
		
		this.event();
	},
	event: function(){
		const Content = this.obj;
		
		const searchButton = $('button[role="search"]');
		const clearButton = $('span[role="clear"]');
		
		const typeButton = $('input[name="type"]');
		const typeWeek = $('input[name="type"][value="week"]');
		const typeMonth = $('input[name="type"][value="month"]');
		
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
		// 날짜 변수 선언
		const today = common.getDateToStr();
		const week = common.getDateToStr('day', -7);
		const month = common.getDateToStr('day', 0, -1);
		
		const startDate = $('input[name="startDate"]');
		const endDate = $('input[name="endDate"]');
		
		const dateField = $('input[type="date"]');
		
		let isSearched = false;
		
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
		
		// 검색 내용 focus 효과
		valueField.focus(function(){
			valueField.parent().addClass('filter-container-focus');
		});
		valueField.blur(function(){
			valueField.parent().removeClass('filter-container-focus');
		});
		
		// field 옵션 추가
		this.field.forEach(item => {
			fieldSelect.append(`<option value="${item.id}">${item.name}</option>`)
		});
		
		// 검색 내용에서 Enter 버튼 클릭시 검색 버튼 클릭으로 trigger
		valueField.keypress(function(e){
			if(e.keyCode == 13){
				searchButton.trigger('click');
			}
		});
		
		// 검색 버튼 클릭시
		searchButton.click(function(){
			isSearched = true;
			Search.load();
		});
		
		// 지우기 버튼 클릭시
		clearButton.click(function(){
			fieldSelect.val('');
			valueField.val('');
			
			if(isSearched){
				Content.table.setData(Content.params.url, {}, ajaxConfig);
				isSearched = false;
			}
		});
		
		// 지우기 버튼 hover 시
		tippy('#filter-clear', {
			content: "검색 지우기",
			inertia: true,
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
	params: {},
	load: function(){
		this.params = {};
		
		this.params.startDate = $('input[name="startDate"]').val();
		this.params.endDate = $('input[name="endDate"]').val();
		
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
		const field = fieldSelect.val();
		const value = valueField.val();
		
		// 검색 내용은 있으나, field 미선택 시 Alert
		if(value && !field) {
			Alert.warning({text: '검색 항목을 선택해주세요!'});
		} else if (value && field) {
			this.params[field] = value;
		}
		
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