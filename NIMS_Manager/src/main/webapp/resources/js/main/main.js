/**
 * [대시보드] 객체 설명
 * 1. Loading - 로딩중
 * 2. Live - 실시간 모니터링
 * 3. Sock - 웹 소켓 구독 현황 관리
 * 4. EventDetail - 이벤트 상세보기
 * 5. Feed - 실시간 모니터링 Feed Item 관리
 * 
 * @returns
 */
$(function() {
	const Content = {
		params: {},
		charts: {},
		activeDevices: null,
		load: function(params) {
			const that = this;
			that.params = params;
			
			// 도서관 정보
			this.libraryHash = {};
			$('.sb-lib-menu').children().each(i => {
				const that = $('.sb-lib-menu').children()[i];
				this.libraryHash[that.dataset.lib] = that.innerText;
			});
			
			// 미사용 장비 슬라이드 숨김 처리
			AjaxUtil.request({
				url: '/api/system/getSystemRuleList',
				async: false,
				data: { rule_nm: '사용여부' },
				done: function(data) {
					data = data.result;
					
					if(data.items) {
						that.activeDevices = [...new Set((data.items.filter(x => x.option_1 == 'Y')).map(x => x.option_2))]
						
						const targetList = data.items.filter(x => x.option_1 == 'N');
						const targetSet = new Set(targetList.map(x => x.option_2));
						const targetArry = [...targetSet];
						
						for(device of targetArry) {
							$(`.slide [role="${device}"]`).remove();
						}
					}
				}
			});
			
			if(grade > 0) {
				AjaxUtil.request({
					url: '/api/model/getModelTypeList',
					async: false,
					data: {
						manager_grade: grade, 
						model_auth: model_auth
					},
					done: function(data) {
						data = data.result;
						
						if(data.data) {
							for(item of data.data) {
								$(`.slide [role="${item.code_desc}"]`).remove();
							}
						}
					}
				});
			}
			
			// 슬라이드 생성
			$('.slide').slick({
				slide: 'div',
				speed: 750,
				autoplay: true,
				autoplaySpeed: 2500,
				fade: true,
				zIndex: 0
			});
			
			this.result();
		},
		result: function() {
			// 실시간 모니터링
			Live.load();
			
			// 웹소켓 설정
			Sock.load();
			
			this.event();
		},
		event: function() {
			const reload = function() {
				Live.load();
				Sock.load();
			}
			
			$('[role="profile"]').parent().attr("aria-expanded", "true");
			
			// 도서관 선택 시 
			$('input[name="library"]').change(function() {
				reload();
			});
			
			$('[role="refresh"][data-target="monitoring"]').click(function() {
				Live.load();
			});
			
			// slick AutoPlay 비활성화
			$('.modal').on('shown.bs.modal', function() {
				$('.slide').slick('slickPause');
			});
			
			// slick AutoPlay 활성화
			$('.modal').on('hidden.bs.modal', function() {
				$('.slide').slick('slickPlay');
			});
			
			// slick-list 크기 변동 시 slick refresh
			const resizeObserver = new ResizeObserver(function(entries) {
				$('.slide').slick('setPosition');
			});
			
			resizeObserver.observe($('.slick-list')[0]);
		}
	};
	
	/**
	 * @Desc 실시간 모니터링
	 */
	const Live = {
		data: [],
		container: null,
		load: function() {
			const that = this;
			
			that.container = $('.activity-feed');
			LoadingUtil.init(that.container);
			
			AjaxUtil.request({
				url: '/api/dashboard/getLiveHistoryList',
				data: {
					grade: grade,
					model_auth: model_auth
				},
				done: function(data){
					that.data = data.result;
					that.draw();
				}
			});
		},
		draw: function() {
			const that = this;
			
			that.container.html('');
			if(that.data) {
				that.data.items.forEach(item => {
					that.container.append(Feed.load(item.type, item));
				});
			}
			
			that.event();
		},
		event: function() {
			Feed.event();
		}
	}
	
	/**
	 * @Desc 웹소켓
	 */
	const Sock = {
		load: function() {
			this.connect();
		},
		connect: function() {
			const that = this;
			const ws_url = $('input[name="ws_url"]').val();
			
			let sock = new SockJS(`${ws_url}/ws`);
			
			stompClient = Stomp.over(sock);
			stompClient.reconnect_delay = 5000;
			stompClient.debug = false;
			
			stompClient.connect({}, function(frame) {
				const activeDevices = Content.activeDevices;
				
				if(activeDevices.includes("gate") || activeDevices.includes("antiLost")) {
					// 출입 통계
					stompClient.subscribe('/topic/live/enter', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				if(activeDevices.includes("loanReturn") || activeDevices.includes("resvLoan")) {
					// 대출
					stompClient.subscribe('/topic/live/loan', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				if(activeDevices.includes("return") || activeDevices.includes("loanReturn")) {
					// 반납
					stompClient.subscribe('/topic/live/return', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				if(activeDevices.includes("loanReturn")) {
					// 대출반납기
					stompClient.subscribe('/topic/live/loanReturn', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				if(activeDevices.includes("sls")) {
					// 스마트 도서관
					stompClient.subscribe('/topic/live/smart', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				if(activeDevices.includes("resvLoan")) {
					// 예약대출기
					stompClient.subscribe('/topic/live/resvLoan', function(message) {
						let data = JSON.parse(message.body);
						that.createMessage(data);
					});
				}
				
				// 장비 이벤트
				stompClient.subscribe('/topic/live/event', function(message) {
					let data = JSON.parse(message.body);
					that.createMessage(data);
					
					if(sessionStorage.getItem('library_key') == '' || sessionStorage.getItem('library_key') == data.library_key) {
						EventStatus.load();
//						EventAnalysis.load();
						
						if(data.type == '0'){
							DangerFeed.load();
						}
					}
				});
				
			}, function() {
				
			});
		},
		createMessage: function(data) {
			const container = $('.activity-feed');
			const containerParent = container.parent();
			
			// 선택된 해당 도서관에 관련된 WebSocket 정보만 받아옴.
			const library_key = sessionStorage.getItem('library_key'); 
			if(library_key != '' && library_key != data.library_key) {
				return;
			}
			
			container.prepend(Feed.load(data.type, data));
			
			// 긴급 알림 사항
			if(data.type == '0'){
				DangerFeed.load();
				Animation.load($('.feed-box-content'), 'animate__animated animate__lightSpeedInRight');
				Animation.load($('[role="danger-feed-icon"]'), 'animate__animated animate__tada animate__repeat-2 alert-danger-icon');
			}
			
			this.event();
		},
		event: function() {
			Feed.event();
		}
	};
	
	Content.load();
});

/**
 * @Desc 이벤트 상세 화면
 */
const EventDetail = {
	params: {},
	load: function(type, params, check){
		this.params = params;
		
		let statusList = null;
		
		AjaxUtil.request({
			url: '/api/dashboard/getEventStatusList',
			async: false,
			data: {
				type: 'month',
				startDate: common.getDateToStr('month')
			},
			done: function(data){
				statusList = data.result.items;
			}
		});
		
		const current = statusList.filter(item => item.event_type == type)[0];
		
		this.params.labelName = current.event_type_nm;
		this.params.labelColor = ChartManager.getEventTypeColor()[type];
		
		this.draw(check);
	},
	draw: function(check){
		const eventModal = $('#eventModal');
		let type = check == true ? 'check' : 'info';
		
		eventModal.append(ParamManager.load(type, JSON.stringify(this.params)));
		eventModal.modal('show');
	},
};

/**
 * @Desc 실시간 모니터링 FeedItem 관리
 */
const Feed = {
	load: function(type, item, child){
		// danger 이면 텍스트 빨간색 
		let style = type == '0' ? 'danger-color' : (type == '1' ? 'warning-color' : 'success-color');
		
		// 도서관 정보
		const libraryHash = [];
		
		const menu = $('.sb-lib-menu');
		let libCnt = menu.children().length;
		if(libCnt > 0) {
			menu.children().each(function(){
				libraryHash[this.dataset.lib] = this.innerText;
			});
		} 
		else {
			const name = $('.page-title')[0].innerText;
			libraryHash[1] = name;
		}
		
		// 도서관 텍스트
		let libraryText = '';
		let libKey = sessionStorage.getItem('library_key') || '';
		if(libKey == '' && libCnt > 0){
			libraryText = `<label class="skip-text lib-text">[관리도서관] ${libraryHash[item.library_key]}</label>`;
		}
		
		const itemType = item.type || '2';
		const itemMessage = item.message || '';
		const itemDate = new Date(item.date).toLocaleString();
		
		const container =
			`<li class="feed-item feed-item-type-${itemType}" type="${itemType}">
				<time class="date skip-text ${style}">
					<i class="fas fa-clock"></i>
					<span class="icon-title">${itemDate}</span>
				</time>
				<span class="text">
					<label class="skip-text ${style}">${itemMessage}</label>
					${libraryText}
				</span>
			</li>`;
		
		if(child) return $(container).html();
		return container;
	},
	event: function(){
		$('.feed-item').click(function(){
			const type = $(this).attr('type');
			let params = {
				date: common.getPlainText($(this).find('.date').text()), 
				text: common.getPlainText($(this).find('.text').text()),
			};
			
			EventDetail.load(type, params);
		});
	}
};