$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			
			List.device(); 
			
			this.result();
		},
		result: function() {
			SettingTable.load();
			this.event();
		},
		event: function() {
			const table = this.table;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				$('#filter-field').trigger('change');
				
				List.device();
			});
		}
	};
	
	const SettingTable = {
		device_id: null,
		load: function() {
			this.event();
		},
		event: function() {
			$('#filter-field').change(function() {
				const that = this;
				const container = $('.cabinet-container');
				const leftContainer = container.find('[role="1"]');
				const resvLoanSelect = $('#filter-field');
				const device_id = resvLoanSelect[0].selectedOptions[0].dataset.device_id;
				
				that.device_id = device_id;
				
				AjaxUtil.request({
					type: 'POST',
					url: Content.params.url,
					async: false,
					data: {
						device_key: resvLoanSelect.val(),
					},
					done: function(data) {
						if(data.result.itemCount == 0) {
							let content = $(`<div class="content"><h4>조회된 캐비닛 데이터가 없습니다.</h4></div>`);
							
							container.append(content);
						} else {
							data = data.result.items;
							
							// 컨테이너 초기화
							container.find('.direction-container').empty();
							
							const leftContainer = container.find('[role="1"]');
							const centerContainer = container.find('[role="0"]');
							const rightContainer = container.find('[role="2"]');
							
							const dirNameSet = new Set(data.map(x => x.direction_nm));
							const dirValueSet = new Set(data.map(x => x.direction));
							const cabinetSet = new Set(data.map(x => x.cabinet));
							const dirNameArry = [...dirNameSet];
							const dirValueArry = [...dirValueSet];
							const cabinetArry = [...cabinetSet];
							
							for(i in dirNameArry) {
								const filterData = data.filter(x => x.direction_nm == dirNameArry[i]);
								const targetDir = [...new Set (filterData.map(x => x.direction))][0];
								
								let content = $(`<div class="content-container" data-direction="${targetDir}" data-id="${dirNameArry[i]}"></div>`);
								
								// 모듈 타이틀 추가
								content.append(`<div class="module-title">${dirNameArry[i]}</div>`);
								
								// 캐비닛 컨테이너 생성
								const filterCabinetArry = [...new Set (filterData.map(x => x.cabinet))];
								
								for(cabinet of filterCabinetArry) {
									const cabinetData = filterData.filter(x => x.cabinet == cabinet);
									const cabinetKey = [...new Set (cabinetData.map(x => x.cabinet_key))][0];
									const cabinetType = [...new Set (cabinetData.map(x => x.cabinet_type))][0];
									const cabinetStatus = [...new Set (cabinetData.map(x => x.cabinet_status))][0];
									const subCabinet = [...new Set (cabinetData.map(x => x.sub_cabinet))][0];
									const error = [...new Set (cabinetData.map(x => x.error))][0];
									
									let body = $(`
										<div class="cabinet-body" data-cabinet_key="${cabinetKey}" data-cabinet="${cabinet}" data-sub_cabinet="${subCabinet}" data-cabinet_type=${cabinetType} data-cabinet_status="${cabinetStatus}" data-error="${error}">
											<input type="checkbox" class="cabinet-check"></input>
											<svg viewBox="0 0 8.5 8.5" class="check-svg">
												<path/>
											</svg>
										</div>`);
									
									let tippyContent = $(`
										<div class="book-info">
											<div class="info-title mb-3">투입 도서 정보</div>
										</div>`);
									
									// 캐비닛 데이터 추가
									for(item of cabinetData) {
										let cabinetContent = $(`<div class="content" data-insert_no="${item.insert_no}" data-cabinet_module_key="${item.cabinet_module_key}" data-module_key="${item.module_key}"></div>`);
										
										if(item.title) {
											tippyContent.append(`
												<div class="info-content">
													<div class="content">
														<div class="mb-3">
															<div class="title">
																<i class="fas fa-tag"></i>
																<span class="icon-title">도서명</span>
															</div>
															<div class="text">${item.title}</div>
														</div>
														<div class="mb-3">
															<div class="row">
																<div class="col-lg-4">
																	<div class="title">
																		<i class="fas fa-bookmark"></i>
																		<span class="icon-title">도서 번호</span>
																	</div>
																	<div class="text">${item.book_no}</div>
																</div>
																<div class="col-lg-4">
																	<div class="title">
																		<i class="fas fa-user"></i>
																		<span class="icon-title">저자</span>
																	</div>
																	<div class="text">${item.author}</div>
																</div>
																<div class="col-lg-4">
																	<div class="title">
																		<i class="fas fa-building"></i>
																		<span class="icon-title">출판사</span>
																	</div>
																	<div class="text">${item.publisher}</div>
																</div>
															</div>
														</div>
													</div>
												</div>`);
										}
										
										body.append(cabinetContent);
									}
									
									if(tippyContent.find('.info-content').length > 0) {
										tippy(body[0], {
											content: tippyContent[0],
											arrow: true,
											animation: 'fade',
											allowHTML: true,
											interactive: true,
											maxWidth: "none",
											theme: !theme ? 'white' : theme
										});
									}
									
									content.append(body);
								}
								
								container.find(`.direction-container[role="${targetDir}"]`).append(content);
							}
						}
					 }
				});
				
				$('.cabinet-body').click(function(e) {
					const checkbox = $(e.currentTarget).find('.cabinet-check');
					let checkStatus = checkbox[0]['checked'];
					
					checkbox.prop('checked', !checkStatus);
				});
				
				// 캐비닛 타입 설정 불가 체크 후 unbind
				AjaxUtil.request({
					type: 'POST',
					url: '/api/resvLoan/getResvLoanUnmodifiableCabinetList',
					async: false,
					data: {
						device_key: $('#filter-field').val(),
					},
					done: function(data) {
						data = data.result.items;
						
						for(item of data) {
							let target = $(`.cabinet-body[data-cabinet="${item.cabinet}"]`);
							
							target.unbind('click');
							target.addClass('unmodifiable');
						}
					}
				});
			});
			
			$('[role="type"]').click(function() {
				const cabinetList = $('.cabinet-body .cabinet-check');
				let cabinetArry = [];
				let subCabinetArry = [];
				let cabinet = null;
				let subCabinet = null;
					
				cabinetList.each(i => {
					if(cabinetList[i].checked) {
						
						// 3~5번 캐비닛
						let cabinetKey = $(cabinetList[i].parentElement)[0].dataset.cabinet_key;
						let subCabinetNo = $(cabinetList[i].parentElement)[0].dataset.sub_cabinet;
						
						cabinetArry.push(cabinetKey);
						subCabinetArry.push(subCabinetNo);
						
						// 8~10번 캐비닛
						cabinetKey = $(cabinetList[i + 5].parentElement)[0].dataset.cabinet_key;
						subCabinetNo = $(cabinetList[i + 5].parentElement)[0].dataset.sub_cabinet;
						
						cabinetArry.push(cabinetKey);
						subCabinetArry.push(subCabinetNo);
					}
				});
				
				if(cabinetArry.length == 0) {
					Alert.warning({text: '설정하실 캐비닛을 선택해주세요!'});
				} else {
					cabinet = cabinetArry.join();
					subCabinet = subCabinetArry.join();
					
					AjaxUtil.request({
						type: 'POST',
						url: '/api/resvLoan/updateResvLoanCabinetType',
						async: false,
						data: {
							rec_key: cabinet,
							sub_cabinet: subCabinet,
							device_key: $('#filter-field').val(),
						},
						done: function(data) {
							data = data.result;
							
							if(data.CODE == "200") {
								Alert.success({text: '설정이 완료되었습니다.'}, Content.params.onHide);
							}
							else {
								Alert.danger({text: data.DESC});
								return;
							}
						}
					});
				}
			});
		}
	};
	
	const List = {
		device: function() {
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '8',
				},
				sync: false,
				done: function(data) {
					data = data.result.data;
					
					const resvLoanSelect = $('#filter-field');
					
					resvLoanSelect.empty();
					
					if(data == null || data.length == 0) {
						let option = `<option value="" data-device_id="0" selected disabled>등록된 장비가 없습니다.</option>`;
						
						resvLoanSelect.append(option);
					} else {
						for(item of data) {
							let option = `<option value="${item.rec_key}" data-library_key="${item.library_key}" data-device_id="${item.device_id}">${item.device_nm}</option>`;
							
							resvLoanSelect.append(option);
						}
						
						resvLoanSelect.trigger('change');
					}
				}
			});
		}
	}
	
	Content.load({
		url: '/api/resvLoan/getResvLoanCabinetTotalInfo',
		onHide: () => {
			$('#filter-field').trigger('change');
		}
	});
});