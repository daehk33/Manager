$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			
			// 카드 전체 화면 보기
			$('.content').addClass('no-fit');
			
			this.event();
		},
		event: function() {
			const table = this.table;
			Data.load();
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Data.load();
			});
			
			$('#filter-field').change(function() {
				const value = this.value;
				
				LoadingUtil.init(SlotTable.container);
				
				if(value == '') {
//					SlotTable.container.html(TableUtil.getPlaceholder('조회된 선반 설정 데이터가 없습니다.'));
					SlotTable.container.html('<div class="content no-content">'+TableUtil.getPlaceholder('조회된 선반 설정 데이터가 없습니다.')+'</div>');
					return;
				}
				
				setTimeout(function(){
					SlotTable.load({
						device_key: value
					});
				}, 500);
			});
		}
	};
	
	const Data = {
		devices: [],
		deviceHash: {},
		load: function(){
			const that = this;
			const deviceSelect = $('#filter-field');
			
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '6',
				},
				done: function(data) {
					data = data.result.data;
					
					deviceSelect.empty();
					if(data.length == 0){
						deviceSelect.html('<option value="">장비 없음</option>')
					}
					for(item of data) {
						that.deviceHash[item.rec_key] = item;
						let option = `<option value="${item.rec_key}">${item.device_nm}</option>`;
						deviceSelect.append(option);
					}
					
					deviceSelect.trigger('change');
				}
			});
		}
	};
	
	const SlotTable = {
		container: $('.slot-container'),
		data: [],
		params: {},
		load: function(params){
			const that = this;
			this.params = params;
			
			AjaxUtil.request({
				url: '/api/sls/getSLSCasInfoList',
				data: params,
				done: function(data){
					data = data.result;
					that.data = data.items;
					that.draw();
				}
			})
		},
		draw: function(){
			const data = this.data;
			const container = this.container;
			const deviceSelect = $('#filter-field');
			
			container.html('');
			if(data.length == 0) {
				container.html('<div class="content"><h4>조회된 선반 설정 데이터가 없습니다.</h4></div>');
				return;
			}
			
			$('[role="msg"][target="count"]').text(data.length);
			
			const drumTrayNameSet = new Set(data.map(x => x.drumtrayname));
			const drumTrayValueSet = new Set(data.map(x => x.drumtrayvalue));
			const drumTrayNameArry = [...drumTrayNameSet];
			const drumTrayValueArry = [...drumTrayValueSet];
			
			const tippyContent = `
				<div role="btn-container">
					<button class="btn mb-2 mt-1" data-action="use-all">전체 사용</button><br/>
					<button class="btn mb-2" data-action="disable-all">전체 불가</button><br/>
					<button class="btn mb-2" data-action="even-use">짝수 사용</button><br/>
					<button class="btn mb-1" data-action="odd-use">홀수 사용</button>
				</div>`;
			
			for(i in drumTrayNameArry) {
				let content = $(`<div class="content" data-id="${drumTrayNameArry[i]}"></div>`);
				
				// 선반 타이틀 추가
				content.append(`<div class="content-title" data-value="${drumTrayValueArry[i]}">${drumTrayNameArry[i]}</div>`);
				
				// 선반 추가
				for(item of data) {
					let body = $(`<div class="content-body" data-rec_key="${item.rec_key}" data-enable_yn="${item.enable_yn}" data-usage="${item.usage}"></div>`);
					
					if(item.drumtrayname == drumTrayNameArry[i]) {
						body.append(`<div class="slot">${item.slot}</div>`);
						if(item.title) {
							body.append(`<div class="title">${item.title}</div>`);
							tippy(body[0], {
								content: `${item.title}`,
								arrow: true,
								animation: 'fade',
								theme: !theme ? 'white' : theme
							});
						}
						content.append(body);
					}
				}
				
				container.append(content);
				container.find('.content-title').hover(function() {
					const value = this.dataset.value;
				});
			}
			
			tippy('.content-title', {
				content: tippyContent,
				allowHTML: true,
				maxWidth: '200px',
				arrow: true,
				animation: 'fade',
				interactive: true,
				interactiveBorder: 5,
				theme: !theme ? 'white' : theme,
						onShown(instance) {
					let value = instance.reference.dataset.value; 
					
					container.find('div[role="btn-container"] button').click(function() {
						const action = this.dataset.action;
						const device_key = deviceSelect.val();
						const drumTrayValue = value
						const device = Data.deviceHash[device_key];
						
						AjaxUtil.request({
							type: 'POST',
							url: '/api/sls/updateSLSCasBatchEnable',
							data: {
								action: action,
								device_key: device_key,
								library_key: device.library_key,
								drumTrayValue: drumTrayValue,
							},
							done: function(data) {
								deviceSelect.trigger('change');
							}
						});
					});
				},
				onHide(instance) {
					container.find('div[role="btn-container"] button').unbind()
				}
			});
			
			container.find('.content-body').click(function() {
				const rec_key = this.dataset.rec_key;
				const enable_yn = this.dataset.enable_yn == 'Y' ? 'N' : 'Y';
				const device_key = deviceSelect.val();
				const device = Data.deviceHash[device_key];
				
				AjaxUtil.request({
					type: 'POST',
					url: '/api/sls/updateSLSCasEnable',
					data: {
						rec_key: rec_key,
						enable_yn: enable_yn,
						device_id: device.device_id,
						device_key: device_key,
						library_key: device.library_key,
					},
					done: function(data) {
						deviceSelect.trigger('change');
					}
				});
			});
		}
	};
	
	Content.load();
});