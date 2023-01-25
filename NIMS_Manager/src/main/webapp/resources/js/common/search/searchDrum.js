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
		
		const posiStepSelect = $('#filter-posiStep');
		
		// 서가 위치 옵션 추가
		AjaxUtil.request({
			type: 'POST',
			url: '/api/sls/getSLSDrumTrayList',
			async: false,
			done: function(data) {
				data = data.result.items;
				
				data.forEach(item => {
					posiStepSelect.append(`<option value="${item.value}">${item.name}</option>`);
				});
			}
		});
		
		// 검색 여부
		let isSearched = false;
		
		// 서가 위치 선택 시
		posiStepSelect.change(function() {
			const posiStep = posiStepSelect.val();
			let params = {};
			
			isSearched = true;
			
			// 서가 위치
			if(posiStep) {
				params['posiStep'] = posiStep;
			}
			
			Content.table.setData(Content.params.url, params, ajaxConfig);
		});
	}
}
