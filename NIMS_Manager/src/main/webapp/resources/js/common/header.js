$(function() {
	const Content = {
		load: function() {
			const headerContainer = $('[role="header"][type="container"]');
			const pageTitleContainer = $('.page-title-container');
			
			AjaxUtil.request({
				type: 'POST',
				url: '/api/system/getSystemRule',
				async: false,
				data: {
					rule_id : 'R0001'
				},
				done: function(data) {
					data = data.result.info;
					
					const timerContainer = headerContainer.find('div[role="expired"]'); 
					const timer = headerContainer.find('#timer');
					let sessionInvalidateTime = data.option_1 == 'Y' ? data.option_2 : 0;
					
					timer.text(Timer.getLongToStr(sessionInvalidateTime));
					timer.addClass('header-timer');
					
					Timer.setTimer('timer', sessionInvalidateTime, 'login');
				}
			});
			
			
			// 사용자명 설정
			headerContainer.find('[role="manager"][type="name"]').text(manager_nm);
			headerContainer.find('[class="header-profile"]').html(`<b>${manager_nm}</b> 님<span class="caret"></span>`);
			
			// 시스템 관리자의 경우
			if(grade == 0) {
				// 도서관 Select Box 생성
				const selector = 
					`<div class="navbar-nav topbar-nav">
						<div class="sb-lib">
							<div class="select">
								<span role="title">전체 도서관</span>
								<span class="caret"></span>
							</div>
							<input type="hidden" name="library"/>
							<ul class="sb-lib-menu">
							</ul>
						</div>
					</div>`;
				
				headerContainer.prepend(selector);
				pageTitleContainer.addClass('hide');
				
				AjaxUtil.request({
					type: 'POST',
					url: '/api/library/getLibraryList',
					async: false,
					done: function(data) {
						data = data.result.data;
						const selectContainer = headerContainer.find('[class="sb-lib-menu"]');
						let selectContent = '';
						
						if(data.length > 1) {
							selectContent = '<li data-lib="">전체 도서관</li>';
							
							for(lib of data) {
								selectContent += `<li data-lib="${lib.rec_key}">${lib.library_nm}</li>`;
							}
							
							selectContainer.append(selectContent);
							
							let libraryHash = {};
							data.forEach(lib => {libraryHash[lib.rec_key] = lib.library_nm});
							
							if(sessionStorage.getItem('library_key') != ''){
								const lib_nm = libraryHash[sessionStorage.getItem('library_key')];
								
								$('.sb-lib').find('span[role="title"]').text(lib_nm);
							}
						}
						else {
							const lib = data[0];
							
							$('.sb-lib').hide();
							$('.page-title').text(lib.library_nm);
							pageTitleContainer.removeClass('hide');
						}
					}
				});
			}
			// 시스템 관리자가 아닐 경우
			else {
				if($('.page-title').length != 0) {
					$('.page-title').text(sessionStorage.getItem('library_nm'));
					pageTitleContainer.removeClass('hide');
				}
			}
			
			this.event();
		},
		event: function() {
			// Select Box 이벤트
			const sbLib = $('.sb-lib');
			sbLib.click(function() {
				$(this).attr('tabindex', 1).focus();
				$(this).toggleClass('active');
				$(this).find('.sb-lib-menu').slideToggle(200);
			});
			sbLib.focusout(function() {
				$(this).removeClass('active');
				$(this).find('.sb-lib-menu').slideUp(200);
			});
			$('.sb-lib .sb-lib-menu li').click(function() {
				$(this).parents('.sb-lib').find('span[role="title"]').text($(this).text());
				$(this).parents('.sb-lib').find('input').attr('value', $(this)[0].dataset.lib).trigger('change');
			});
			
			$('input[name="library"]').change(function() {
				const library_key = this.value;
				// 페이지 제목 변경
				if($('.page-title').length != 0) 
					$('.page-title').text($('.sb-lib > .select').text());
				
				sessionStorage.setItem('library_key', library_key);
			});
			
			$('.card-title[data-toggle="collapse"]').click(function(){
				if(!$(this).hasClass('collapsed')) $('.tabulator').addClass('expand');
				else $('.tabulator').removeClass('expand');
			});
			
			$('[role="manager"][type="modal"]').click(function(){
				$('#managerMainModal').modal('show')
			});
			
			$('.btn[role="extention"]').click(function() {
				Timer.resetTimer();
			});
		}
	};
	
	Content.load();
});