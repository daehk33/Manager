/**
 * [스마트도서관 대시보드] 객체 설명
 * 1. BookStatus - 도서 대출 현황
 * 2. DangerFeed - 장애 알림
 * 3. DeviceList - 장비 목록
 * 4. EventStatus - 장애 현황
 * 5. TodayStatistics - 금일 현황
 * 6. WeekStatistics - 주간 현황
 * 7. Rank - 베스트 도서 / 이용자
 * 8. EventDetail - 이벤트 상세보기
 * 9. Feed - 실시간 모니터링 Feed Item 관리
 * 
 * @returns
 */
$(function() {
	const Content = {
		targetContainer: null,
		params: {},
		charts: {},
		libraryHash: [],
		load: function(params) {
			const that = this;
			that.params = params;
			
			let use_yn = 'Y';
			
			AjaxUtil.request({
				url: '/api/system/getSystemRule',
				async: false,
				data: { rule_id: 'R0002' },
				done: function(data) {
					data = data.result.info;
					
					if(data) {
						use_yn = data.option_1;
					}
				}
			});
			
			Chart.helpers.merge(Chart.defaults.global.plugins.datalabels, {
				display: function(context) {
					// data가 0이 아닐경우에만 값 표시
					let index = context.dataIndex;
					let value = context.dataset.data[index];
					
					return value != 0 ? 'auto' : false;
				},
				font: function(context) {
					let avgSize = Math.round((window.innerWidth + window.innerHeight) / 100);
					let size = Math.round(avgSize / 2);
					size = size > 22 ? 22 : size < 14 ? 14 : size;
					
					return {
						family: "NanumGothic",
						size: size,
						weight: '500'
					};
				}
			});
			
			this.targetContainer = $('.slide-container[role="sls"]');
			this.targetContainer.find('[role="show"]')[0].dataset.model_key = params.model_key;
			
			use_yn == 'Y' && this.result();
		},
		result: function() {
			// 도서 대출 현황
			BookStatus.load();
			
			// 장비 목록
			DeviceList.load();
			
			// 금일 현황
			TodayStatistics.load();
			
			// 주간 현황
			WeekStatistics.load();
			
			// 이벤트 현황
			EventStatus.load();
			
			// 베스트 도서 / 이용자
			Rank.load();
			
			// 장비 이벤트 알림
			DangerFeed.load();
			
			this.event();
		},
		event: function() {
			const reload = function() {
				BookStatus.load();
				DangerFeed.load();
				DeviceList.load();
				EventStatus.load();
				TodayStatistics.load();
				WeekStatistics.load();
				Rank.load();
			};
			
			$('input[name="theme"]').change(function() {
				setTimeout(function(){
					EventStatus.load();
					TodayStatistics.load();
					WeekStatistics.load();
					Rank.load();
				}, 500);
			});
			
			// 도서관 선택 시 
			$('input[name="library"]').change(function() {
				if($('.slide-container[role="sls"]')[0]) {
					reload();
				}
			});
			
			const targetContainer = Content.targetContainer;
			
			targetContainer.find('span[role="refresh"]').click(function() {
				const target = $(this).attr('data-target');
				
				if(target == 'deviceList') {
					DeviceList.load();
				}
				else if(target == 'todayStatistics') {
					TodayStatistics.load();
				}
				else if(target == 'eventStatus') {
					EventStatus.load();
				}
				else if(target == 'weekStatistics') {
					WeekStatistics.load();
				}
				else if(target == 'rankList') {
					Rank.load();
				}
			});
			
			const eventTab = targetContainer.find('#pills-tab-type[role="event"]');
			eventTab.find('[role="type"]').click(function(){
				eventTab.find('.nav-link').removeClass('active');
				$(this).addClass('active');
				
				EventStatus.load();
			});
			
			const rankTab = targetContainer.find('#pills-tab-type[role="rank"]');
			rankTab.find('[role="type"]').click(function(){
				rankTab.find('.nav-link').removeClass('active');
				$(this).addClass('active');
				
				Rank.load();
			});
		}
	};
	
	/**
	 * @Desc 도서 대출 현황
	 */
	const BookStatus = {
		data: [],
		totalContainer: null,
		loanableContainer: null,
		loanContainer: null,
		load: function(){
			const that = this;
			const targetContainer = Content.targetContainer;
			
			that.totalContainer = targetContainer.find('.card[data-card="book-status-total"]');
			that.loanableContainer = targetContainer.find('.card[data-card="book-status-loanable"]');
			that.loanContainer = targetContainer.find('.card[data-card="book-status-loan"]');
			
			AjaxUtil.request({
				url: '/api/dashboard/getBookLoanStatus',
				async: false,
				done: function(data) {
					that.data = data.result.info;
					that.draw();
				}
			});
		},
		draw: function() {
			const that = this;
			const data = that.data;
			
			const totalContainer = that.totalContainer;
			const loanableContainer = that.loanableContainer;
			const loanContainer = that.loanContainer;
			
			const totalCount = totalContainer.find('.card-title[role="count"]');
			const loanableCount = loanableContainer.find('.card-title[role="count"]');
			const loanCount = loanContainer.find('.card-title[role="count"]');
			
			totalCount.text(data.total_book_cnt);
			loanableCount.text(data.total_loanable_cnt);
			loanCount.text(data.total_loan_cnt);
		}
	}
	
	/**
	 * @Desc 장애 알림
	 */
	const DangerFeed = {
		data: [],
		container: null,
		load: function(){
			const that = this;
			const targetContainer = Content.targetContainer;
			
			this.container = targetContainer.find('.card[data-card="danger-feed"]');
			
			AjaxUtil.request({
				url: '/api/dashboard/getAlarmList',
				data: {
					model_key: Content.params.model_key
				},
				done: function(data) {
					that.data = data.result;
					that.draw();
				}
			})
		},
		draw: function(){
			const container = this.container;
			const data = this.data;
			
			const msgCount = container.find('span[role="msg"][target="danger-feed"]'); 
			const icon = container.find('[role="danger-feed-icon"]');
			const content = container.find('.feed-box-content');
			
			content.html('');
			
			if(data.itemsCount != 0){
				icon.addClass('danger-icon');
				msgCount.text(this.data.itemsCount);
				
				let item = data.items[data.itemsCount - 1];
				let type = item.type
				
				content.attr('data-id', item.id);
				content.html(`<div>${Feed.load(type, item, true)}</div>`);
			}
			else {
				msgCount.text('0');
				icon.removeClass('danger-icon');
				content.append(`<div class="no-content">
									<i class="fas fa-exclamation-circle"></i></br>
									<span>최근 장비 이벤트 알림이 없습니다.</span>
								</div>`);
			}
			
			this.event();
		},
		event: function(){
			const that = this;
			const alertModal = $('#alertModal');
			const showButton = this.container.find('span[role="show"]');
			const targetContainer = Content.targetContainer;
			
			// 확대 버튼 hover 시
			tippy('.slide-container[role="sls"] span[role="show"]', {
				content: "알림 전체보기",
				inertia: true,
			});
			
			// 알림 전체보기 클릭시
			showButton.click(function(){
				alertModal.modal('show', showButton);
			});
			
			// feed 개수 클릭시
			targetContainer.find('span[role="msg"][target="danger-feed"]').click(function(){
				showButton.trigger('click');
			});
			
			// 알림 내용 클릭시
			targetContainer.find('.feed-box-content').click(function(){
				if($(this).find('.date').text() == '') return;
				
				let params = {
					date: common.getPlainText($(this).find('.date').text()),
					text: common.getPlainText($(this).find('.text').text()),
					id: $(this).attr('data-id')
				};
				
				EventDetail.load('0', params, true);
			});
		}
	};
	
	$('#alertModal').on('hide.bs.modal', function(e) {
		if($('.slide-container[role="sls"]')[0]) {
			DangerFeed.load();
			Animation.load($('.slide-container[role="sls"] .feed-box-content'), 'animate__animated animate__flipInX');
		}
	});
	
	$('#eventModal').on('hide.bs.modal', function(e) {
		const param = $(this).find('.param');
		
		const action = param.attr('action');
		// 장애 알림 확인일 때만 DangerFeed Reload.
		if(action == 'check') {
			if($('.slide-container[role="sls"]')[0]) {
				DangerFeed.load();
				Animation.load($('.slide-container[role="sls"] .feed-box-content'), 'animate__animated animate__flipInX');
			}
		}
		
		param.remove();
	});
	
	/**
	 * @Desc 장비 목록
	 */
	const DeviceList = {
		data: [],
		container: null,
		load: function() {
			const that = this;
			const targetContainer = Content.targetContainer;
			
			this.container = targetContainer.find('#deviceStatus[role="list"]');
			
			LoadingUtil.init(this.container);
			
			AjaxUtil.request({
				url: '/api/device/getDeviceStatusList',
				data: {
					model_type: Content.params.model_type
				},
				done: function(data){
					that.data = data.result;
					that.draw();
				}
			});
		},
		draw: function() {
			let container = this.container;
			
			container.html('');
			if(this.data.itemsCount == 0) {
				container.css({'height': '100%'})
				container.append(`<div class="no-content">
									<i class="fas fa-exclamation-circle"></i></br>
									<span>등록된 장비가 없습니다.</span>
								</div>`);
				return;
			}
			
			container.css({'height': ''})
			// 모델 타입 분류
			this.data.items.forEach(item => {
				let status = item.status == "정상" ? 'bb-success' : item.status == "연결끊김" ? 'bb-warning' : 'bb-danger';
				container.append(
					`<div class="ds-content border ${status} pt-2" role="info">
						<img src="/resources/img/model/${item.model_id}.png"/>
						<label>${item.device_nm}</label>
						<input type="hidden" name="item_key" value="${item.device_key}" />
					</div>`);
			});
			
			this.event();
		},
		event: function(){
			const that = this;
			const targetContainer = Content.targetContainer;
			
			// 장비 클릭시
			targetContainer.find('.ds-content').click(function(){
				const role = $(this).attr('role')
				if(role == 'info'){
					const itemKey = $(this).find('input[name="item_key"]');
					const param = that.data.items.filter(x => x.device_key == itemKey.val());
					const deviceModal = $('#deviceMainModal');
					deviceModal.append(ParamManager.load('info', JSON.stringify(param[0])));
					deviceModal.modal('show');
				}
			});
		}
	};
	
	/**
	 * @Desc 장비 이벤트 현황
	 */
	const EventStatus = {
		data: {},
		container: null,
		load: function(){
			const that = this;
			const targetContainer = Content.targetContainer;
			
			this.container = targetContainer.find('#chartEventStatus[role="list"]');
			
			LoadingUtil.init(this.container);
			
			const type = targetContainer.find('#pills-tab-type[role="event"] .active[role="type"]').attr('type');
			const params = type == 'today' ? {type: 'day', startDate: common.getDateToStr()} : 
				type == 'week' ? {type: 'day', startDate: common.getDateToStr('day', -7), endDate: common.getDateToStr()} : 
								 {type: 'month', startDate: common.getDateToStr('month')};
			
			AjaxUtil.request({
				url: '/api/dashboard/getEventStatusList',
				data: Object.assign({}, params, {
					model_key: Content.params.model_key
				}),
				done: function(data){
					that.data = data.result;
					that.draw();
				}
			});
		},
		draw: function(){
			const targetContainer = Content.targetContainer;
			let container = this.container;
			
			const items = this.data.items;
			let circles = [];
			let innerWidth = window.innerWidth / 33;
			let total = ChartManager.getTotal(items);
			
			targetContainer.find('span[role="msg"][target="device-event-count"]').html('')
			targetContainer.find('span[role="msg"][target="device-event-count"]').text(total);
			
			container.html('');
			items.forEach(event => {
				container.append(`
					<div class="event-status-container">
						<div class="circle mb-1" id="circles-sls-${event.event_type}"></div>
						<div class="event-status-title">${event.event_type_nm}(${ChartManager.getPercent(event.count, total)})</div>
					</div>`);
				
				const color = ChartManager.getEventTypeColor();
				let eventStatusCircle = Circles.create({
					id: `circles-sls-${event.event_type}`,
					radius: innerWidth > 35 ? innerWidth : 35,
					value: event.count,
					maxValue: total,
					width: innerWidth > 35 ? innerWidth / 4 : 9,
					text: (value) => value + ' 건',
					colors: [theme == 'dark' ? '#3b445e' : '#eaebf2', color[event.event_type]],
					duration: 400,
					wrpClass: 'circles-wrp',
					textClass: 'circles-text',
					valueStrokeClass: 'circles-valueStroke',
					maxValueStrokeClass: 'circles-maxValueStroke',
					styleWrapper: true,
					styleText: true
				});
				
				circles.push(eventStatusCircle);
			});
			
			window.addEventListener('resize', function() {
				for(circle of circles) {
					let innerWidth = window.innerWidth / 33;
					
					if(innerWidth > 35) {
						circle.updateRadius(window.innerWidth / 33)
						circle.updateWidth(innerWidth/4);
					} else {
						circle.updateRadius(35);
						circle.updateWidth(9);
					}
				}
			});
		}
	};
	
	/**
	 * @Desc 금일 현황
	 */
	const TodayStatistics = {
		bookData: [],
		userData: [],
		data: null,
		callCount: 0,
		container: null,
		load: function(){
			const that = this;
			const targetContainer = Content.targetContainer;
			this.container = targetContainer.find('.card-body-statistic[target="today"]');
			
			LoadingUtil.init(this.container);
			that.callCount = 0;
			
			AjaxUtil.request({
				url: '/api/dashboard/getUseLogCntbyBook',
				done: function(data){
					that.bookData = Object.entries(data.result.items[0]).map(item => item[1]);
					that.callCount += 1;
					that.draw();
				}
			});
			
			AjaxUtil.request({
				url: '/api/dashboard/getUseLogCntbyUser',
				done: function(data){
					that.userData = Object.entries(data.result.items[0]).map(item => item[1]);
					that.callCount += 1;
					that.draw();
				}
			});
		},
		draw: function(){
			const that = this;
			const container = this.container;
			
			if(that.callCount < 2){
				return;
			}
			that.data = {
				labels: ['대출', '반납'],
				datasets: [
					{
						label: '권 수',
						data: that.bookData,
						backgroundColor: '#7e44ea',
						borderColor: '#7e44ea',
					},
					{
						label: '이용자 수',
						data: that.userData,
						backgroundColor: '#23cf93',
						borderColor: '#e23cf93',
					}
				]
			};
		
			container.html('<canvas id="todayStatisticsChart"></canvas>');
			
			const todayCtx = document.querySelector('.slide-container[role="sls"] #todayStatisticsChart').getContext('2d');
			const todayChart = new Chart(todayCtx, {
				type: 'bar',
				data: that.data,
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: { display: false },
					tooltips: {
						bodySpacing: 5, mode:"index", intersect: 0,
						xPadding:5, yPadding:5, caretPadding:5
					},
					layout:{
						padding:{top: 20}
					},
					scales: {
						yAxes: [{
							ticks: {
								fontStyle: "500", beginAtZero: true, maxTicksLimit: 7, padding: 15,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
								callback: function(val) {
									return Number.isInteger(val) ? val : null;
								}
							},
							gridLines: {
								drawTicks: false, display: false,
								color: theme == 'dark' ? '3b445e' : '#c7c7c7'
							}
						}],
						xAxes: [{
							ticks: {
								padding: 10, fontStyle: "500",
								fontColor: theme == 'dark' ? "#fff" : "#575962"
							},
							gridLines: {
								zeroLineColor: "transparent",
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
					},
					plugins: {
						datalabels: {
							anchor: 'end', align: 'end', offset: -5,
							formatter: function(value, context) {
								return value.toLocaleString();
							},
							color: function() {
								let color = '';
								theme == 'dark' ? color = 'rgba(255, 255, 255, 1)' : color = 'rgba(0, 0, 0, 1)';
								
								return color;
							}
						},
					}
				},
				plugins: [{
					beforeDraw: function(context) {
						let chartHeight = context.chart.height;
						let size = chartHeight * 3 / 50;
						
						context.scales['y-axis-0'].options.ticks.minor.fontSize = size;
						context.scales['x-axis-0'].options.ticks.minor.fontSize = size;
					}
				}]
			});
		}
	}
	
	/**
	 * @Desc 주간 현황
	 */
	const WeekStatistics = {
		container: null,
		data: [],
		load: function(type) {
			const that = this;
			const targetContainer = Content.targetContainer;
			this.container = targetContainer.find('.card-body-statistic[target="week"]');
			
			LoadingUtil.init(this.container);
			
			this.datasets = [];
			this.labels = [];
			
			// 출입 통계
			AjaxUtil.request({
				url: '/api/dashboard/getUseLogCntbyWeek',
				data: {
					startDate: common.getDateToStr('default', -6),
					endDate: common.getDateToStr(),
				},
				done: function(data){
					let items = data.result.items;
					
					if(items.length == 0) return;
					
					// 컬럼명에 cnt가 포함되는 컬럼들을 리스트로 뽑음
					const cntVariable = Object.entries(items[0]).filter(item => item[0].includes('cnt')).map(item => item[0]);
					
					// 첫번째 data 는 column 정보를 갖고있음 - hash
					const colName = items.splice(0, 1)[0];
					
					// dataArray는 컬럼 수만큼 이차원 배열 생성 
					let dataArray = [];
					cntVariable.forEach(x => dataArray.push([]));
					
					items.forEach(item => {
						that.labels.push(item.label_nm);
						// v는 변수, i는 인덱스
						cntVariable.forEach((v, i) => dataArray[i].push(item[v]));
					});
					
					dataArray.forEach((arr, i) => {
						const colors = ChartManager.getBarChartColor()[i];
						that.datasets.push(that.createDataset(colName[cntVariable[i]], colors[0], colors[1], colors[2], arr));
					});
					
					that.draw();
				},
			});
		},
		draw: function() {
			const that = this;
			const container = this.container;
			
			container.html('<canvas id="weekStatisticsChart"></canvas>');
			
			let ctx = document.querySelector('.slide-container[role="sls"] #weekStatisticsChart').getContext('2d');
			let statisticsChart = new Chart(ctx, {
				type: 'line',
				data: {
					labels: that.labels,
					datasets: that.datasets
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: { display: false },
					tooltips: {
						bodySpacing: 5, mode:"index", intersect: 0,
						xPadding:5, yPadding:5, caretPadding:5
					},
					layout:{
						padding:{top: 20}
					},
					scales: {
						yAxes: [{
							ticks: {
								fontStyle: "500", beginAtZero: true, maxTicksLimit: 7, padding: 15,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
										callback: function(val) {
											return Number.isInteger(val) ? val : null;
										}
							},
							gridLines: {
								drawTicks: false, display: false,
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
						xAxes: [{
							ticks: {
								padding: 10, fontStyle: "500",
								fontColor: theme == 'dark' ? "#fff" : "#575962",
							},
							gridLines: {
								zeroLineColor: "transparent",
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
					},
					legendCallback: function(chart) {
						let text = `<ul class="${chart.id}-legend html-legend">`;
						chart.data.datasets.forEach(item => {
							text += `<li><span style="background-color:${item.legendColor}"></span>${item.label}</li>`;
						});
						text += '</ul>';
						return text;
					},
					plugins: {
						datalabels: {
							anchor: 'end', align: 'end', offset: -5,
							formatter: function(value, context) {
								return value.toLocaleString();
							},
							color: function() {
								let color = '';
								theme == 'dark' ? color = 'rgba(255, 255, 255, 1)' : color = 'rgba(0, 0, 0, 1)';
								
								return color;
							}
						}
					}
				},
				plugins: [{
					beforeDraw: function(context) {
						let chartHeight = context.chart.height;
						let size = chartHeight * 3 / 50;
						
						context.scales['y-axis-0'].options.ticks.minor.fontSize = size;
						context.scales['x-axis-0'].options.ticks.minor.fontSize = size;
					}
				}]
			});
			
			// 통계 Legend 추가
			const targetContainer = Content.targetContainer;
			let legendContainer = targetContainer.find('#chartLegend');
			legendContainer.find('ul').remove();
			legendContainer[0].append($(statisticsChart.generateLegend())[0]);
			
			let legendItems = legendContainer.find('li');
			for(let i = 0; i <  legendItems.length; i ++) {
				legendItems[i].addEventListener('click', legendClickCallback, false);
			}
		},
		createDataset: function(label, bdColor, pointbgColor, bgColor, data) {
			return options = {
				label: label,
				borderColor: bdColor,
				pointBackgroundColor: pointbgColor,
				pointRadius: 3,
				pointStyle: 'rect',
				backgroundColor: bgColor,
				fill: false,
				lineTension : 0,
				borderWidth: 2,
				data: data,
				legendColor: bdColor,
			};
		}
	};
	
	/**
	 * @Desc 베스트 도서 / 이용자
	 */
	const Rank = {
		bookLabels: [],
		bookCnt: [],
		userLabels: [],
		userCnt: [],
		container: null,
		callCount: 0,
		load: function() {
			const that = this;
			const targetContainer = Content.targetContainer;
			this.container = targetContainer.find('#rankChart[role="list"]');
			
			LoadingUtil.init(this.container);
			
			const type = targetContainer.find('#pills-tab-type[role="rank"] .active[role="type"]').attr('type');
			const params = type == 'today' ? {type: 'day', startDate: common.getDateToStr()} : 
				type == 'week' ? {type: 'day', startDate: common.getDateToStr('day', -7), endDate: common.getDateToStr()} : 
								 {type: 'month', startDate: common.getDateToStr('month')};
								 
			// Array 초기화
			that.bookLabels = [];
			that.bookCnt = [];
			that.userLabels = [];
			that.userCnt = [];
			
			// ajax call 횟수
			that.callCount = 0;
			
			// 베스트 도서
			AjaxUtil.request({
				url: '/api/sls/getSLSBookRankList',
				data: Object.assign({
					library_key: sessionStorage.getItem('library_key'),
					limit: 5
				}, params),
				done: function(data) {
					data = Object.entries(data.result.data).map(item => item[1]);
					
					for(item of data) {
						that.bookLabels.push(item.title);
						that.bookCnt.push(item.cnt);
					}
					
					that.callCount += 1;
					that.draw();
				}
			});
			
			// 우수 회원
			AjaxUtil.request({
				url: '/api/sls/getSLSUserRankList',
				data: Object.assign({
					library_key: sessionStorage.getItem('library_key'),
					limit: 5
				}, params),
				done: function(data) {
					data = Object.entries(data.result.data).map(item => item[1]);
					
					for(item of data) {
						that.userLabels.push(item.user_name);
						that.userCnt.push(item.cnt);
					}
					
					that.callCount += 1;
					that.draw();
				}
			});
		},
		draw: function() {
			const that = this;
			const container = this.container;
			let yAxes = [];
			
			if(that.callCount < 2){
				return;
			}
			container.html(`
				<div class="two-block-container">
					<div class="two-block-title" role="bookRankTitle">베스트 도서</div>
					<div role="chart" target="bookRank"><canvas id="bookRankChart" width="150px" height="150px"></canvas></div>
				</div>
				<div class="two-block-container">
					<div class="two-block-title" role="userRankTitle">우수 회원</div>
					<div role="chart" target="userRank"><canvas id="userRankChart" width="150px" height="150px"></canvas></div>
				</div>
			`);
			
			const bookCtx = document.querySelector('.slide-container[role="sls"] #bookRankChart').getContext('2d');
			const bookChart = new Chart(bookCtx, {
				title: {text: '베스트 도서'},
				type: 'horizontalBar',
				data: {
					datasets: [{
						data: that.bookCnt,
						backgroundColor: '#7e44ea',
						borderColor: '#7e44ea'
					}], 
					labels: that.bookLabels
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: { display: false },
					tooltips: {
						bodySpacing: 5, mode:"index", intersect: 0,
						xPadding:5, yPadding:5, caretPadding:5,
						callbacks: {
							title: function(tooltipItem, data) {
								return yAxes[tooltipItem[0].index];
							}
						}
					},
					layout:{
						padding:{top: 10, right: 25}
					},
					scales: {
						yAxes: [{
							ticks: {
								fontStyle: "500", padding: 15,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
								callback: function(value, index, values) {
									yAxes = values;
									
									if(value.length > 5) {
										value = value.substring(0, 3) + "...";
									}
									return value;
								}
							},
							gridLines: {
								drawTicks: false, display: false,
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
						xAxes: [{
							ticks: {
								padding: 10, fontStyle: "500", beginAtZero: true,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
								callback: function(val) {
									return Number.isInteger(val) ? val : null;
								}
							},
							gridLines: {
								zeroLineColor: "transparent",
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
					},
					legendCallback: function(chart) {
						let text = `<ul class="${chart.id}-legend html-legend">`;
						chart.data.datasets.forEach(item => {
							text += `<li><span style="background-color:${item.legendColor}"></span>${item.label}</li>`;
						});
						text += '</ul>';
						return text;
					},
					plugins: {
						datalabels: {
							anchor: 'end', align: 'end', 
							formatter: function(value, context) {
								return value.toLocaleString();
							},
							color: function() {
								let color = '';
								theme == 'dark' ? color = 'rgba(255, 255, 255, 1)' : color = 'rgba(0, 0, 0, 1)';
								
								return color;
							},
							display: true
						}
					}
				},
				plugins: [{
					beforeDraw: function(context) {
						let chartHeight = context.chart.height;
						let size = chartHeight * 3 / 50;
						
						context.scales['y-axis-0'].options.ticks.minor.fontSize = size;
						context.scales['x-axis-0'].options.ticks.minor.fontSize = size;
					}
				}]
			});
			
			const userCtx = document.querySelector('.slide-container[role="sls"] #userRankChart').getContext('2d');
			const userChart = new Chart(userCtx, {
				title: {text: '우수 회원'},
				type: 'horizontalBar',
				data: {
					datasets: [{
						data: that.userCnt,
						backgroundColor: '#23cf93',
						borderColor: '#23cf93'
					}], 
					labels: that.userLabels
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: { display: false },
					tooltips: {
						bodySpacing: 5, mode:"index", intersect: 0,
						xPadding:5, yPadding:5, caretPadding:5
					},
					layout:{
						padding:{top: 10}
					},
					scales: {
						yAxes: [{
							ticks: {
								fontStyle: "500", padding: 15,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
							},
							gridLines: {
								drawTicks: false, display: false,
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
						xAxes: [{
							ticks: {
								padding: 10, fontStyle: "500", beginAtZero: true,
								fontColor: theme == 'dark' ? "#fff" : "#575962",
								callback: function(val) {
									return Number.isInteger(val) ? val : null;
								}
							},
							gridLines: {
								zeroLineColor: "transparent",
								color: theme == 'dark' ? '#3b445e' : '#c7c7c7'
							}
						}],
					},
					legendCallback: function(chart) {
						let text = `<ul class="${chart.id}-legend html-legend">`;
						chart.data.datasets.forEach(item => {
							text += `<li><span style="background-color:${item.legendColor}"></span>${item.label}</li>`;
						});
						text += '</ul>';
						return text;
					},
					plugins: {
						datalabels: {
							anchor: 'end', align: 'end',
							formatter: function(value, context) {
								return value.toLocaleString();
							},
							color: function() {
								let color = '';
								theme == 'dark' ? color = 'rgba(255, 255, 255, 1)' : color = 'rgba(0, 0, 0, 1)';
								
								return color;
							},
							display: true
						}
					}
				},
				plugins: [{
					beforeDraw: function(context) {
						let chartHeight = context.chart.height;
						let size = chartHeight * 3 / 50;
						
						context.scales['y-axis-0'].options.ticks.minor.fontSize = size;
						context.scales['x-axis-0'].options.ticks.minor.fontSize = size;
					}
				}]
			});
		}
	}
	
	Content.load({
		model_key: '6',
		model_type: '4'
	});
});
