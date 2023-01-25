$(function() {
	const Content = {
		params: {},
		sources: [],
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
			
			that.select();
			
			// 도서관 선택 시
			$('input[name="library"]').change(function() {
				// 기존 달력 제거
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
			
			$('.card .btn[role="action"]').click(function() {
				const action = this.dataset.action;
				const library_key = $('#filter-field option:selected')[0].dataset.library_key;
				const device_key = $('#filter-field').val();
				
				if(action == 'useSetting') {
					let val = '';
					
					AjaxUtil.request({
						url: '/api/loanReturn/getLoanReturnHolidayRule',
						async: false,
						data: {
							library_key: library_key,
							device_key: device_key
						},
						done: function(data) {
							val = data.value1;
						}
					});
					
					const type = {'Y': '사용', 'N': '사용안함'};
					Alert.select ({
						title: '휴관일 사용 설정',
						text: '휴관일 설정을 선택해주세요.'
					}, function(result) {
						if(!result.isConfirmed) return;
						
						const value = result.value;
						let text = `휴관일 설정을 <b>${type[value]}</b>으로 변경하시겠습니까?`;
						if(value == 'N') {
							text += `<br/><font class="warning-text">변경 시, 기존에 설정한 휴관일 설정들이 모두 무시됩니다.</font>`;
						}
						
						Alert.confirm({title: '휴관일 사용 설정 확인', text: text, confirmClass: 'btn btn-success'}, function(result) {
							if(result.isConfirmed) {
								AjaxUtil.request({
									url: '/api/loanReturn/updateLoanReturnHolidayRule',
									async: false,
									data: {
										manager_id: manager_id,
										library_key: library_key,
										device_key: device_key,
										value1: value
									},
									done: function(data) {
										data = data.result;
										
										if(data.CODE == '200') {
											Alert.success({text: '휴관일 설정 변경이 완료되었습니다!'});
										}
										else {
											Alert.dnager({text: data.DESC});
										}
									},
									error: function(data) {
										Alert.danger({text: '휴관일 설정 변경 중 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.'});
									}
								});
							}
						});
					}, type, val);
				}
				else if(action == "saveSetting") {
					const result = [];
					
					const origin = Content.sources.filter(e => e.display == 'background').map(e => e.start);
					const after = that.calendar.getEvents().filter(e => e._def.ui.display == 'background').map(e => e.startStr);
					
					const addList = after.filter(e => !origin.includes(e));
					const delList = origin.filter(e => !after.includes(e));
					
					// 휴관일 추가
					DateUtil.getDaysToHash(addList).forEach(params => {
						AjaxUtil.request({
							url: '/api/loanReturn/upsertLoanReturnHolidayInfo',
							async: false,
							data: Object.assign(params, {
								library_key: library_key,
								device_key: device_key,
								send_req_yn: 'Y'
							}),
							done: function(data) {
								data = data.result;
								
								if(data.CODE == '200') {
									result.push(true);
								} else {
									result.push(false);
								}
							}
						});
					});
					
					// 휴관일 삭제
					DateUtil.getDaysToHash(delList).forEach(params => {
						AjaxUtil.request({
							url: '/api/loanReturn/deleteLoanReturnHolidayInfo',
							async: false,
							data: Object.assign(params, {
								library_key: library_key,
								device_key: device_key,
								send_req_yn: 'Y'
							}),
							done: function(data) {
								data = data.result;
								
								if(data.CODE == '200') {
									result.push(true);
								} else {
									result.push(false);
								}
							}
						});
					});
					
					if(delList.length == 0 && addList.length == 0) {
						Alert.warning({text: '추가하거나 삭제할 휴관일을 선택해 주세요.'});
					}
					else {
						const success = result.filter(e => e == true);
						const fail = result.filter(e => e == false);
						
						Alert.success({text: '휴관일 설정이 완료되었습니다!'}, function() {
							// 기존 달력 제거
							that.calendar.destroy();
							
							// 달력 로드
							that.calendar = dateStats.load();
						});
					}
				}
			});
			
			// 테이블 로드
			that.calendar = dateStats.load();
		},
		select: function() {
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '1,9,10',
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
		load: function(options){
			return this.draw(options);
		},
		draw: function(options){
			const that = this;
			const library_key = $('#filter-field option:selected')[0].dataset.library_key;
			const device_key = $('#filter-field').val();
			
			if(options == undefined) {
				options = {};
			}
			
			let sources = [];
			
			AjaxUtil.request({
				url: '/api/loanReturn/getLoanReturnHolidayList',
				async: false,
				data: {
					library_key: library_key,
					device_key: device_key
				},
				done: function(data) {
					data = data.items;
					
					// 초기화
					sources = [];
					
					for(item of data) {
						sources.push(that.createHolidayEvent(item.date));
					}
					
					Content.sources = sources;
				}
			});
			
			let calendarContainer = $('.calendar')[0];
			let calendar = new FullCalendar.Calendar(calendarContainer, {
				eventOrder: 'displayOrder',
				eventSources: [{
					events: function(info, success, fail) {
						sources = sources.filter(e => e.display == 'background');
						
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
								
								for(item of data) {
									if(item.loan_cnt > 0 && item.loan_user > 0) {
										sources.push({
											"title": `${item.loan_cnt}권,${item.loan_user}명,loan`,
											"start": item.log_date,
											"displayOrder": 1
										});
									}
									
									if(item.return_cnt > 0 && item.return_user > 0) {
										sources.push({
											"title": `${item.return_cnt}권,${item.return_user}명,return`,
											"start": item.log_date,
											"displayOrder": 2
										});
									}
								}
								
								success(sources);
							}
						});
					}
				}],
				eventContent: function(args, createElement) {
					const titles = args.event._def.title.split(',');
					
					if(args.event._def.title == "휴관일") {
						return {
							html: `<sapn class="calendar-title ta-c">${titles[0]}</span>`
						};
					}
					
					let bookTitle = `<span class="calendar-title ta-c"><i class="fas fa-book icon" role="${titles[2]}"></i><div class="stat">${titles[0]}</div></span>`;
					let userTitle = `<span class="calendar-title ta-c"><i class="fas fa-user icon" role="${titles[2]}"></i><div class="stat">${titles[1]}</div></span>`;
					
					return {
						html: bookTitle + userTitle
					};
				},
				eventDisplay: 'list-item',
				selectable: true,
				select: function(info) {
					const days = DateUtil.getDays(info.startStr, info.endStr);
					days.forEach(date => {
						calendar.addEvent(that.createHolidayEvent(date));
					});
				},
				selectOverlap: function(event) {
					if(event._def.ui.display == 'background') {
						event.remove();
					}
					else {
						return true;
					}
				},
				height: 'calc(80vh - 89px)',
				locale: 'ko'
			});
			
			calendar.render();
			
			that.event();
			
			return calendar;
		},
		event: function() {
			const buttonContainer = $('.fc-today-button').parent();
			
			const legendContent = $(`
					<div class="legendContainer" role="calendar" position="sub">
						<ul class="html-legend">
							<li>
								<span aria-type="loan"></span> 대출
							</li>
							<li>
								<span aria-type="return"></span> 반납
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
		},
		createHolidayEvent: function(date) {
			return {
				title: '휴관일',
				start: date,
				backgroundColor: theme == 'dark' ? 'rgb(201 10 10 / 40%)' : 'rgb(201 10 10 / 13%)',
				display: 'background'
			};
		}
	};
	
	Content.load({
		url: '/api/stats/getStatsLoanReturnCalendar'
	});
});