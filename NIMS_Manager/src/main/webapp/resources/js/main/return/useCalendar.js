$(function() {
	const Content = {
		params: {},
		calendar: null,
		load: function(params) {
			this.params = params;
			
			this.result();
		},
		result: function(){
			this.event();
		},
		event: function(){
			const that = this;
			
			// 장비 선택 옵션 로드
			that.select();
			
			// 테이블 로드
			that.calendar = dateStats.load();
			
			// 도서관 선택 시
			$('input[name="library"]').change(function() {
				that.calendar.destroy();
				
				// 장비 선택 옵션 로드
				that.select();
				
				// 달력 로드
				that.calendar = dateStats.load();
			});
			
			// 장비 선택 시
			$('#filter-field').change(function() {
				that.calendar.destroy();
				
				that.calendar = dateStats.load();
			});
		},
		select: function() {
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '3,11',
				},
				async: false,
				done: function(data) {
					data = data.result.data;
					
					const select = $('#filter-field');
					select.empty();
					
					if(data.length == 0) {
						select.append('<option value="-1" data-library_key="-1" data-device_id="-1">장비 없음</option>');
					}
					
					for(item of data) {
						let option = `<option value="${item.rec_key}" data-library_key="${item.library_key}" data-device_id="${item.device_id}">${item.device_nm}</option>`;
						
						select.append(option); 
					}
					
					
					select.trigger('change');
				}
			});
		}
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
								data = data.result.data;
								
								let result = [];
								
								for(item of data) {
									if(item.return_cnt > 0 && item.return_user > 0) {
										result.push({
											"title": `${item.return_cnt}권,${item.return_user}명,return`,
											"start": item.return_date,
											"displayOrder": 1
										});
									}
								}
								
								success(result);
							}
						});
					}
				}],
				eventContent: function(args, createElement) {
					const titles = args.event._def.title.split(',');
					
					let bookTitle = `<span class="calendar-title ta-c"><i class="fas fa-book icon" role="${titles[2]}"></i><div class="stat">${titles[0]}</div></span>`;
					let userTitle = `<span class="calendar-title ta-c"><i class="fas fa-user icon" role="${titles[2]}"></i><div class="stat">${titles[1]}</div></span>`;
					
					return {
						html: bookTitle + userTitle
					};
				},
				eventDisplay: 'list-item',
				height: 'calc(80vh - 89px)',
				locale: 'ko'
			});
			
			calendar.render();
			
			return calendar;
		},
		dateToStr: function(date) {
			date.setMonth(date.getMonth()-1);
			date = date.toISOString().split('T')[0];
			
			return date.split('-').slice(0, 2).join('-');
		}
	};
	
	Content.load({
		url: '/api/stats/getStatsReturnCalendar'
	});
});