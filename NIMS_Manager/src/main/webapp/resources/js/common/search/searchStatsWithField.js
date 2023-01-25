/**
 * @Group 검색 Container 이벤트 관리 객체
 * @Desc 기간별 통계 검색
 */
const SearchContainer = {
	obj: null,
	field: [],
	load: function(obj, field){
		this.obj = obj;
		this.field = field;
		
		// 일별 선택으로 초기화
		Drawer.load('day');
		
		this.event();
	},
	event: function(){
		const searchButton = $('button[role="search"]');
		const typeButton = $('input[name="type"]');
		const clearButton = $('span[role="clear"]');
		
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
		let type = 'day';
		
		$('input[name="type"]').change(function(e){
			type = $(this).val();
			Drawer.load(type);
			Search.load(type);
			
			return false;
		});
		
		$('[role="search"]').click(function(e){
			e.preventDefault();
			Search.load(type);
			
			return false;
		});
		
		// 검색 내용 focus 효과
		valueField.focus(function() {
			valueField.parent().addClass('filter-container-focus');
		});
		valueField.blur(function() {
			valueField.parent().removeClass('filter-container-focus');
		});
		
		// field 옵션 추가
		this.field.forEach(item => {
			fieldSelect.append(`<option value="${item.id}">${item.name}</option>`)
		});
		
		// 검색 내용에서 Enter 버튼 클릭시 검색 버튼 클릭으로 trigger
		valueField.keypress(function(e){
			if(e.keyCode == 13){
				e.preventDefault();
				Search.load(type);
				
				return false;
			}
		});
		
		// 지우기 버튼 클릭시
		clearButton.click(function(){
			fieldSelect.val('');
			valueField.val('');
			
			Search.load(type);
		});
		
		// 지우기 버튼 hover 시
		tippy('#filter-clear', {
			content: "검색 지우기",
			inertia: true,
		});
	}
}

/**
 * @Desc Type에 따른 검색조건 분기
 * @param {type: string [day | month | all ]
 */
const Search = {
	params: {},
	load: function(type){
		this.params = {};
		
		const fieldSelect = $('#filter-field');
		const valueField = $('#filter-value');
		
		const field = fieldSelect.val();
		const value = valueField.val();
		
		this.params.type = type;
		
		const func = {
			day: this.day,
			month: this.month,
			all: this.all
		};
		
		func[type]();
		
		// 검색 내용은 있으나, field 미선택 시 Alert
		if(value && !field) {
			Alert.warning({text: '검색 항목을 선택해주세요!'});
		} else if (value && field) {
			Search.params[field] = value;
		}
		
		this.setData();
	},
	day: function(){
		Search.params.startDate = $('input[name="day"]').val();
	},
	month: function(){
		Search.params.startDate = $('select[name="month"]').val();
	},
	all: function(){
		Search.params.startDate = $('input[name="startDate"]').val();
		Search.params.endDate = $('input[name="endDate"]').val();
	},
	setData: function(){
		let Content = SearchContainer.obj;
		
		if(Content.table != undefined) {
			Content.table.setData(Content.params.url, Search.params, ajaxConfig);
		}else{
			Content.setData(Content.params.url, Search.params, ajaxConfig);
		}
	}
};

/**
 * @Desc Type에 따른 검색창 생성 및 유효성 체크
 * @param {type: string [day | month | all ]} 
 */
const Drawer = {
	load: function(type){
		const content = {
			day: `<input type="month" name="day" class="form-control date-field" />`,
			month: `<select name="month" class="form-control date-field"></select>`,
			all: `<input type="date" name="startDate" class="form-control date-field" />
					<span class="search-label"> ~ </span>
					<input type="date" name="endDate" class="form-control date-field" />`
		}
		const year = Number(common.getDateToStr('year'));
		
		const container = $('[role="type"]');
		container.html(content[type]);
		
		// 최근 10년 검색 추가
		for(let i=year; i>year-10; i--){
			$('select[name="month"]').append(`<option value="${i}">${i} 년</option>`);
		}
		
		this.event(container);
	}, 
	event: function(container){
		// fadeIn animation 추가
		Animation.load(container, 'animated fadeIn');
		
		this.validate(container);
	},
	validate: function(container){
		// 변수 선언
		const day = container.find('input[name="day"]');
		const month = container.find('select[name="month"]');
		const startDate = container.find('input[name="startDate"]');
		const endDate = container.find('input[name="endDate"]');
		
		/**
		 * 지정날짜로 초기화
		 * 일별: 오늘, 월별: 이번달, 기간: 최근 일주일
		 */
		day.val(common.getDateToStr('month'));
		month.val(common.getDateToStr('year'));
		startDate.val(common.getDateToStr('day', -7));
		endDate.val(common.getDateToStr());
		
		// max 제한
		container.find('input[type="date"]').attr('max', common.getDateToStr());
		container.find('input[type="month"]').attr('max', common.getDateToStr('month'));
		
		// 기간별 날짜 제한
		startDate.click(function(){
			const max = endDate.val();
			if(max != '') startDate.attr('max', max);
			return true;
		});
		endDate.click(function(){
			const min = startDate.val();
			if(min != '') endDate.attr('min', min);
			return true;
		});
	}
};
/**
 * @Desc Field 관련 parsing 모음 객체
 * @examples day_1, day_2, day_3, mon_1, mon_2, ....
 */
const Field = {
	type: {
		day: {prefix: 'day', suffix: '일'},
		month: {prefix: 'mon', suffix: '월'},
		all: {prefix: 'cnt', suffix: '합계'}
	},
	getNum: function(e){
		return e.split('_').splice(-1, 1)[0];
	}
}