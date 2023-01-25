$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			this.select();
			this.result();
		},
		result: function() {
			this.table = SettingTable.load();
			this.event();
		},
		event: function() {
			const that = this;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				that.select();
				that.table.setData();
			});
			
			// 장비 선택 시
			$('#filter-field').change(function() {
				that.table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "장비 정책 목록_("+common.getDateToStr()+").xlsx";
				Excel.download(target, that.table, file_name);
			});
		},
		select: function() {
			AjaxUtil.request({
				url: '/api/device/getDeviceList',
				data: {
					model_key: '6',
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
	
	const SettingTable = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#settingTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
					return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data){
						return `${value}<span>(정책<span class="group-header">${count}</span>개)</span>`;
					}
				] : function(value, count, data){
					return `${value}<span>(정책<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 정책 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					const device_key = $('#filter-field').val();
					
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					url += `&device_key=${device_key}`;
					
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.itemsCount);
					if(response.result.itemsCount == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("value_name", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("value_name", {headerFilter: true});
					}
					
					return response.result.items;
				},
				columns: [
					{ title: "NO", field:"id", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title:"정책 내용", field:"value_name", widthGrow: 2, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "정책 상세 정보", field:"description", widthGrow: 5, headerSort: false },
					{
						title:"항목 1", field:"opt01", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 2", field:"opt02", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 3", field:"opt03", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 4", field:"opt04", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					}
				],
				rowClick: function(e, row) {
					$(row.getElement()).addClass('active');
					that.showSettingDetail(row);
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		},
		showSettingDetail : function(row) {
			const settingModal = $('[id="settingModal"][target="device"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			
			settingModal.append(input);
			settingModal.modal('show');
			settingModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			})
		}
	};
	
	Content.load({
		url: '/api/device/getDeviceRuleList'
	});
});