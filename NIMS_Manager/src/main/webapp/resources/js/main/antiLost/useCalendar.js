$(function() {
	const Content = {
		params: {},
		calendar: null,
		load: function(params) {
			this.params = params;
			
			this.draw();
		},
		draw: function(){
			const params = {model_key: '5'};
			
			// 장비 검색 로드
			Data.load(params);
			
			// 테이블 로드
			this.calendar = dateStats.load();
			
			this.event();
		},
		event: function(){
			const that = this;
			const params = {model_key: '5'};
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Data.load(params);
				that.calendar.destroy();
				that.calendar = dateStats.load();
			});
			
			// 장비 선택 시
			$('#filter-field').change(function() {
				that.calendar.destroy();
				that.calendar = dateStats.load();
			});
		},
	};
	
	const dateStats = {
		load: function(){
			return this.draw();
		},
		draw: function(){
			const that = this;
			const library_key = $('#filter-field option:selected')[0].dataset.library_key;
			const device_key = $('#filter-field').val();
			
			let calendarContainer = $('.calendar')[0];
			let calendar = new FullCalendar.Calendar(calendarContainer, {
				eventOrder: 'displayOrder',
				eventSources: [{
					events: function(info, success, fail) {
						AjaxUtil.request({
							url: Content.params.url,
							async: false,
							data: {
								startDate: that.dateToStr(info.end),
								library_key: library_key,
								device_key: device_key,
								type: 'day'
							},
							done: function(data) {
								data = data.result.data || [];
								
								let result = [];
								for(item of data) {
									result.push({
										"title": `${item.cnt}회, ${item.book_cnt}권`,
										"start": item.date,
										"displayOrder": 1
									});
								}
								
								success(result);
							}
						});
					}
				}],
				eventContent: function(args, createElement) {
					const titles = args.event._def.title.split(',');
					let element = `<span class="calendar-title ta-c">
										<i class="fas fa-bell icon text-warning"></i>
										<div class="stat">${titles[0]}</div>
									</span>
									<span class="calendar-title ta-c">
										<i class="fas fa-book icon text-danger"></i>
										<div class="stat">${titles[1]}</div>
									</span>`;
					
					return {
						html: element
					};
				},
				eventDisplay: 'list-item',
				height: 'calc(80vh - 89px)',
				locale: 'ko'
			});
			
			calendar.render();
			
			that.event();
			
			return calendar;
		},
		event: function(){
			const buttonContainer = $('.fc-today-button').parent();
			
			const legendContent = $(`
					<div class="legendContainer" role="calendar" position="sub">
						<ul class="html-legend">
							<li>
								<i class="fas fa-bell icon text-warning"></i> 카운트 횟수
							</li>
							<li>
								<i class="fas fa-book icon text-danger"></i> 부정도서 권수
							</li>
						</ul>
					</div>`);
			
			if(buttonContainer.find('.legendContainer').length == 0) {
				buttonContainer.prepend(legendContent);
			}
		},
		dateToStr: function(date) {
			date.setMonth(date.getMonth()-1);
			date = date.toISOString().split('T')[0];
			
			return date.split('-').slice(0, 2).join('-');
		}
	};
	
	const Data = {
		devices: [],
		load: function(params){
			const that = this;
			const deviceSelect = $('#filter-field');
			
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: params,
				async: false,
				done: function(data) {
					data = data.result.data;
					that.devices = data;
					
					deviceSelect.empty();
					
					if(data.length == 0) {
						deviceSelect.append('<option value="-1" data-library_key="-1" data-device_id="-1">장비 없음</option>');
					}
					for(item of data) {
						let option = `<option value="${item.rec_key}" data-library_key="${item.library_key}" data-device_id="${item.device_id}">${item.device_nm}</option>`;
						deviceSelect.append(option); 
					}
					
					deviceSelect.trigger('change');
				}
			});
		},
	};

	Content.load({
		url: '/api/stats/getStatsAntiLostDay'
	});
});