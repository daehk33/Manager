/**
 * @Group 검색 Container 이벤트 관리 객체
 * @Desc 통계 검색
 */
const SearchContainer = {
	obj: null,
	load: function(obj, showTab){
		this.obj = obj;
		
		if(showTab == true){
			$('#pills-tab-without-border').css({'display': 'inline-flex'});
		}
		
		// 일별 선택으로 초기화
		Drawer.load('day');
		
		this.event();
	},
	event: function(){
		const searchButton = $('button[role="search"]');
		const typeButton = $('input[name="type"]');
		
		let type = 'day';
		typeButton.change(function(e){
			type = $(this).val();
			Drawer.load(type);
			Search.load(type);
			
			return false;
		});
		
		searchButton.click(function(e){
			e.preventDefault();
			Search.load(type);
			
			return false;
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
		this.params.type = type;
		
		const func = {
			day: this.day,
			month: this.month,
			all: this.all
		};
		
		func[type]();
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
		}
		else {
			Content.setData && Content.setData(Content.params.url, Search.params, ajaxConfig);
			
			const tableArr = Object.entries(Content).filter(x => x[0].toLowerCase().includes('table')).map(x => x[0]);
			tableArr.forEach(table => {
				Content[table].setData(Content.params.url, Search.params, ajaxConfig);
			})
		}
	}
};

/**
 * @Desc Type에 따른 검색창 생성 및 유효성 체크
 * @param {type: string [day | month | all ]} 
 */
const Drawer = {
	type: null,
	load: function(type){
		this.type = type;
		
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
		const that = this;
		
		// fadeIn animation 추가
		Animation.load(container, 'animated fadeIn');
		
		// 엔터 입력시
		$('input[name="day"], input[name="month"], input[name="startDate"], input[name="endDate"]').on('keypress', function(e) {
			let key = e.key || e.keyCode;
			
			if (key === 'Enter' || key === 13) {
				Search.load(that.type);
			}
		});
		
		// 년도 선택 시
		$('select[name="month"]').change(function (e) {
			Search.load(that.type);
		});
		
		this.validate(container);
	},
	validate: function(container){
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
 * @Desc 현재 페이지의 장비 모델에 따라 선택할 수 있는 장비 목록 호출
 * 
 */
const Device = {
	items: null,
	itemHash: [],
	load: function(params) {
		const that = this;
		params = params || {};
		
		const deviceContainer = $('#filter-device');
		deviceContainer.removeClass('hide');
		
		AjaxUtil.request({
			type: 'POST',
			url: '/api/device/getDeviceInfoList',
			data: {
				model_key: params.model_key || ''
			},
			done: function(data) {
				that.items = data.result.data;
				that.draw();
			}
		});
		
		this.event();
	},
	draw: function() {
		const that = this;
		const deviceSelect = $('#filter-device #select_device');
		
		deviceSelect.html('<option value="">전체</option>');
		that.items.forEach(item => {
			deviceSelect.append(`<option value="${item.device_key}">${item.device_nm} [ ${item.device_id} ]</option>`);
			that.itemHash[item.device_key] = item;
		});
	},
	event: function(){
		const deviceSelect = $('#filter-device #select_device');
		const searchButton = $('button[role="search"]');
		
		deviceSelect.change(function() {
			searchButton.trigger('click');
		});
	}
}

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