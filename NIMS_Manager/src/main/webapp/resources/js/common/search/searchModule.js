/**
 * @Group 검색 Container 이벤트 관리 객체
 * @Desc 슬롯 조회 검색
 */
const SearchContainer = {
	obj: null,
	field: [],
	load: function(obj, field){
		this.obj = obj;
		this.field = field;
		
		this.event();
	},
	event: function(){
		const Content = this.obj;
		
		const searchButton = $('button[role="search"]');
		const clearButton = $('span[role="clear"]');
		
		const directionSelect = $('#filter-direction');
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
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
		
		// 검색 여부
		let isSearched = false;
		
		// 검색 내용에서 Enter 버튼 클릭시 검색 버튼 클릭으로 trigger
		valueField.keypress(function(e){
			e.keyCode == 13 && searchButton.trigger('click');
		});
		
		// 서가 위치 선택 시
		directionSelect.change(function() {
			searchButton.trigger('click');
		});
		
		// 검색 버튼 클릭시
		searchButton.click(function(){
			const direction = directionSelect.val();
			const field = fieldSelect.val();
			const value = valueField.val();
			
			isSearched = true;
			
			let params = {};
			
			// 검색 항목
			if(value) {
				if(field) {
					params[field] = value;
				}
				else {
					Alert.warning({text: '검색 항목을 선택해주세요!'});
				}
			}
			// 서가 위치
			if(direction) {
				params['direction'] = direction;
			}
			
			Content.table.setData(Content.params.url, params, ajaxConfig);
		});
		
		// 지우기 버튼 클릭시
		clearButton.click(function(){
			directionSelect.val('');
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
	}
}
