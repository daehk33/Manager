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
		
		const cabinetStatusSelect = $('#cabinet-status ');
		const directionSelect = $('#filter-direction');
		const cabinetInput = $('#filter-cabinet');
		const insertNoInput = $('#filter-insert-no');
		
		// 검색 여부
		let isSearched = false;
		
		cabinetInput.keypress(function(e){
			e.keyCode == 13 && searchButton.trigger('click');
		});
		
		// 캐비닛 상태 선택 시
		cabinetStatusSelect.change(function() {
			searchButton.trigger('click');
		});
		
		// 서가 위치 선택 시
		directionSelect.change(function() {
			searchButton.trigger('click');
		});
		
		// 검색 버튼 클릭시
		searchButton.click(function(){
			const cabinetStatus = cabinetStatusSelect.val();
			const direction = directionSelect.val();
			const cabinet = cabinetInput.val();
			const insertNo= insertNoInput.val();
			
			isSearched = true;
			
			let params = {};
			// 적재 상태
			if(cabinetStatus) {
				params['cabinet_status'] = cabinetStatus;
			}
			// 서가 위치
			if(direction) {
				params['direction'] = direction;
			}
			// 캐비닛 번호
			if(cabinet) {
				params['cabinet'] = cabinet;
			}
			// 투입 번호
			if(insertNo) {
				params['insert_no'] = insertNo;
			}
			
			Content.table.setData(Content.params.url, params, ajaxConfig);
		});
		
		// 지우기 버튼 클릭시
		clearButton.click(function(){
			cabinetStatusSelect[0].checked = true;
			directionSelect.val('');
			cabinetInput.val('');
			insertNoInput.val('');
			
			if(isSearched){
				Content.table.setData(Content.params.url, {}, ajaxConfig);
				isSearched = false;
			}
		});
	}
}
