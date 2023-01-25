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
		
		const withEmptySlotCheck = $('#empty-slot');
		const drumTraySelect = $('#filter-drumTray');
		const slot = $('#filter-slot');
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
		// 검색 내용 focus 효과
		valueField.focus(function(){
			valueField.parent().addClass('filter-container-focus');
		});
		valueField.blur(function(){
			valueField.parent().removeClass('filter-container-focus');
		});
		
		// 서가 위치 옵션 추가
		AjaxUtil.request({
			type: 'POST',
			url: '/api/sls/getSLSDrumTrayList',
			async: false,
			done: function(data) {
				data = data.result.items;
				
				data.forEach(item => {
					drumTraySelect.append(`<option value="${item.value}">${item.name}</option>`);
				});
			}
		});
		
		// 검색 여부
		let isSearched = false;
		
		// 선반 번호 입력 시
		slot.keypress(function(e){
			e.keyCode == 13 && searchButton.trigger('click');
		});
		
		// 적재 상태 선택 시
		withEmptySlotCheck.change(function() {
			searchButton.trigger('click');
		});
		
		// 서가 위치 선택 시
		drumTraySelect.change(function() {
			searchButton.trigger('click');
		});
		
		// 검색 버튼 클릭시
		searchButton.click(function(){
			const withEmptySlot = withEmptySlotCheck.val();
			const drumTray = drumTraySelect.val();
			const slotValue = slot.val();
			
			isSearched = true;
			
			let params = {};
			
			// 서가 위치
			if(drumTray) {
				params['drumTray'] = drumTray;
			}
			// 선반 번호
			if(slotValue) {
				params['slot'] = slotValue;
			}
			// 적재 상태
			if(withEmptySlot) {
				params['withEmptySlot'] = withEmptySlot;
			}
			
			Content.table.setData(Content.params.url, params, ajaxConfig);
		});
		
		// 지우기 버튼 클릭시
		clearButton.click(function(){
			withEmptySlotCheck[0].checked = true;
			drumTraySelect.val('');
			slot.val('');
			
			if(isSearched){
				Content.table.setData(Content.params.url, {}, ajaxConfig);
				isSearched = false;
			}
		});
	}
}
