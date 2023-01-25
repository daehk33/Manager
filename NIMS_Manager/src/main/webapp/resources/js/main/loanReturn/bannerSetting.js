$(function() {
	const Content = {
		params: null,
		imageBanner: null,
		infoBanner: null,
		load: function(params) {
			this.params = params;
			
			this.select();
			
			this.result();
		},
		result: function() {
			this.infoBanner = InfoBanner.load();
			this.imageBanner = ImageBanner.load();
			this.event();
		},
		event: function() {
			$('.btn[role="action"]').click(function(e) {
				const that = this;
				
				let action = $(that).attr("action");
				if (!action) action = that.dataset.action;
				
				const device_key = $('#filter-field').val();
				
				if (device_key == '-1') {
					Alert.warning({text: '등록된 장비가 없는 경우 사용할 수 없습니다.'});
					return;
				}
				
				// 유형 설정
				if(action == 'typeSetting') {
					const type = {'0': '이용 안내', '1': '배너', '2': '사용 안함'};
					let typeValue = '';
					let rec_key = '';
					
					// 현재 설정값 불러오기
					AjaxUtil.request({
						url: '/api/loanReturn/getLoanReturnDIDRuleInfoList',
						data: {
							device_key: device_key
						},
						async: false,
						done: function(data) {
							typeValue = data.value1;
							rec_key = data.rec_key;
						}
					});
					
					Alert.select({
						title: 'DID 유형 설정',
						text: '장비 상단에 들어갈 정보 유형을 선택해주세요.'
					}, function(result) {
						if(result.isConfirmed) {
							const value = result.value;
							
							Alert.confirm({
								title: 'DID 유형 변경 확인', 
								text: `<b>${type[value]}</b> 유형으로 변경하시겠습니까?`,
								confirmClass: 'btn btn-success'
							}, function(result) {
								if(result.isConfirmed) {
									AjaxUtil.request({
										url: '/api/loanReturn/updateLoanReturnDIDRuleInfo',
										async: false,
										data: {
											rec_key: rec_key,
											value1: value,
											send_req_yn: 'Y',
											manager_id: manager_id
										}
									});
								}
							});
						}
					}, type, typeValue)
				}
				else if(action == "syncSetting") {
					const device_id = $('#filter-field option:selected')[0].dataset.device_id;
					const device_key = $('#filter-field').val();
					
					const data = BannerManager.sendChanged(device_id, "1");
					
					if(data.CODE == "200") {
						Alert.success({text: '동기화 요청이 완료되었습니다!'});
					}
					else {
						Alert.danger({text: data.DESC});
					}
				}
				else if(action == "loadSetting") {
					const device_key = $('#filter-field').val();
					
					Alert.confirm({title: '설정 불러오기', text: '장비에 저장된 설정 파일들을 불러오시겠습니까?', confirmClass: 'btn btn-success'}, (result) => {
						if(!result.isConfirmed) {
							return;
						}
						
						AjaxUtil.request({
							url: '/api/loanReturn/loadLoanReturnBannerSettings',
							async: false,
							data: {
								device_key: device_key,
								types: 'image'
							},
							done: function(data) {
								data = data.result;
								
								if(data.CODE == "200") {
									Alert.success({text: '설정 불러오기 요청이 완료되었습니다!'}, Content.params.onHide);
								}
								else {
									Alert.dnager({text: data.DESC});
								}
							},
							fail: function(data){
								Alert.danger({text: '설정 불러오기 중 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.'});
							}
						});
					});
				}
			});
			
			// 장비 선택 시
			$('#filter-field').change(function() {
				Content.infoBanner.setData();
				Content.imageBanner.setData();
			});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Content.select();
				
				Content.infoBanner = InfoBanner.load();
				Content.imageBanner = ImageBanner.load();
				
			});
			
			this.eventInfo();
			this.eventImage();
		},
		eventInfo: function() {
			const that = this;
			const card = $('.card[data-card="info"]');
			
			card.find('.btn[role="action"]').click(function() {
				const table = that.infoBanner;
				
				const action = this.attributes.action.value;
				const tableId = 'infoSettingTable';
				const rec_key = InfoBanner.rec_key;
				const device_key = $('#filter-field').val();
				
				if(action == 'update') {
					const data = {};
					table.getData().forEach(row => {
						data[row.name] = row.value;
					});
					
					data["rec_key"] = rec_key;
					
					const infoModal = $('#infoModal');
					infoModal.append(ParamManager.load(action, JSON.stringify(data)));
					infoModal.modal('show');
					
					infoModal.on('hidden.bs.modal', function() {
						infoModal.find('.param[name="item"]').remove();
					});
				}
			});
		},
		eventImage: function() {
			const that = this;
			const card = $('.card[data-card="image"]');
			
			card.find('.btn[role="action"]').click(function() {
				const table = that.imageBanner;
				
				const action = this.attributes.action.value;
				const tableId = 'imageSettingTable';
				const device_id = $('#filter-field option:selected')[0].dataset.device_id;
				const device_key = $('#filter-field').val();
				
				let data = {"device_key": device_key};
				
				if(action == 'add') {
					const imageModal = $('#imageModal');
					imageModal.append(ParamManager.load(action, JSON.stringify(data)));
					imageModal.modal('show');
					
					imageModal.on('hidden.bs.modal', function() {
						imageModal.find('.param[name="item"]').remove();
					});
				}
				else if (action =="delete" ) {
					const selectedData = table.getSelectedData();
					
					if(selectedData.length == 0) {
						Alert.warning({text: '삭제할 배너 이미지를 선택해 주세요.'});
					}
					else {
						Alert.confirm({title: '배너 삭제', text: '선택하신 배너 이미지들을 삭제하시겠습니까?'}, (result) => {
							if(result.isConfirmed){
								for(data of selectedData) {
									let file_name = data.file_name;
									
									AjaxUtil.request({
										url: '/api/api/sftpService',
										async: false,
										data: {
											action: 'remove',
											type: "image",
											device_key: device_key,
											file_name: file_name
										},
										done: function(data) {
											data = data.result;
											
											if(data.CODE == "200") {
												BannerManager.sendChanged(device_id, '1');
												Alert.success({title: '삭제 완료', text: '배너 이미지가 삭제되었습니다.'}, Content.params.onHide);
											}
											else {
												Alert.danger({text: data.DESC});
											}
										}
									});
								}
							}
						});
					}
				}
			});
		},
		select: function() {
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '1,10',
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
	
	const InfoBanner = {
		rec_key: null,
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#infoSettingTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				placeholder: TableUtil.getPlaceholder('조회된 설정이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.info,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator: function(url, config, params) {
					const device_key = $('#filter-field').val();
					
					url = `${url}?${common.parseParam(params)}`;
					url += `&device_key=${device_key}`;
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					const targets = ["time", "loan_cnt", "type", "return_plan_date", "loan_date"];
					let items = [];
					
					if(response.result.items.length != 0) {
						that.rec_key = response.result.items[0]["rec_key"];
						
						for(target of targets) {
							items.push({'name' : target, 'value' : response.result.items[0][target]});
						}
					}
					
					if(items.length != 0) {
						let type = items.find(item => item.name == "type").value;
						
						for(i in items) {
							if(items[i].name == "returndate_option") {
								items[i] = null;
							}
							else if (type != 3 && items[i].name == "return_plan_date") {
								items[i] = null;
							}
						}
					}
					
					return items;
				},
				columnHeaderVertAlign: "middle",
				columns: [
					{ title: "NO", formatter: "rownum", hozAlign: "center", headerHozAlign: "center", width: 70, download:false, headerSort: false },
					{ title: "속성명", field: "name", widthGrow: 1, formatter: function(cell, formatterParams, onRendered) {
						const hash = {
							time: '이용 시간',
							loan_cnt: '대출 가능 권수',
							loan_date: '대출 기간',
							type: '공휴일 포함 여부 (대출 기간)',
							return_plan_date: '반납 예정일'
						};
						
						let value = cell.getValue();
						if(Object.keys(hash).indexOf(value) > -1){
							value = hash[value];
						}
						return value;
					}},
					{ title: "속성값", field: "value", widthGrow: 1, formatter: function(cell, formatterParams, onRendered) {
						const data = cell._cell.row.getData();
						const typeHash = {
							0: '주말, 공휴일 제외',
							1: '주말, 공휴일 포함',
							2: '공휴일 제외',
							3: '직접 설정'
						};
						let value = cell.getValue();
						if(data.name == 'type' && (Object.keys(typeHash).indexOf(value) > -1)){
							value = typeHash[value];
						}
						
						return value;
					}},
				],
				rowClick: function(e, row) {
				},
				downloadComplete: function(){
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		}
	}
	
	const ImageBanner = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#imageSettingTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				placeholder: TableUtil.getPlaceholder('조회된 배너 이미지가 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url.image,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator: function(url, config, params) {
					const rec_key = $('#filter-field').val();
					
					url = `${url}?${common.parseParam(params)}`;
					url += `&rec_key=${rec_key}`;
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					return response.result.items;
				},
				columnHeaderVertAlign: "middle",
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, hozAlign: "center", widthGrow: 0.5, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						},
						width: 30, download: false, headerSort: false
					},
					{ 
						title: "미리보기", field: "file_path", widthGrow: 0.7, headerSort: false, headerTooltip: true, 
						formatter: function(cell, formatterParams, onRendered) {
							const value = cell.getValue();
							
							$(cell._cell.element).addClass("tabulator-img-cell");
							
							return `<img src="${value}">`;
						}
					},
					{ title: "파일명", field: "file_name", headerTooltip: true, widthGrow: 0.7 },
					{ title: "크기", field: "file_size", headerTooltip: true, widthGrow: 0.7 },
					{ title: "수정 일시", field: "edit_date", headerTooltip: true, widthGrow: 1 }
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showRowDetail(row);
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				},
				renderComplete: function() {
					Content.imageBanner = table;
				}
			});
			
			return table;
		},
		showRowDetail : function(row){
			const imageModal = $('#imageModal');
			
			let data = row.getData();
			data["device_key"] = $('#filter-field').val();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			imageModal.append(input);
			
			imageModal.modal('show');
			imageModal.on('hidden.bs.modal', function(){
				imageModal.find('.param[name="item"]').remove();
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: {
			image: '/api/loanReturn/getLoanReturnBannerImageList',
			info: '/api/loanReturn/getLoanReturnBannerSettingInfo'
		},
		onHide: () => {
			Tabulator.prototype.findTable('#infoSettingTable')[0].setData();
			Tabulator.prototype.findTable('#imageSettingTable')[0].setData();
		}
	});
});