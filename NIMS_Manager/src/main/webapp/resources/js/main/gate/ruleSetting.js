$(function() {
	const Content = {
		params : {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function() {
			this.table = SettingTable.load();
			this.event();
		},
		event: function() {
			const table = this.table;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "출입게이트 정책 목록_("+common.getDateToStr()+").xlsx";
				Excel.download(target, table, file_name);
			});
		},
	};
	
	const SettingTable = {
		load: function() {
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#settingTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : null,
				groupHeader: grade == 0 ? [
					function(value, count, data){
						return `${value}<span>(정책<span class="group-header">${count}</span>개)</span>`;
					}
				] : null,
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
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.itemsCount);
					if(response.result.itemsCount == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("rule_name", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("rule_name", {headerFilter: true});
					}
					
					return response.result.items;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title:"정책 내용", field:"rule_name", widthGrow: 2, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "정책 상세 정보", field:"description", widthGrow: 5, headerSort: false },
					{
						title:"항목 1", field:"option01", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 2", field:"option02", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 3", field:"option03", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							let outerText = "";
							cell.getValue() ? outerText = cell.getValue() : outerText = "-";
							
							return outerText;
						}
					},
					{
						title:"항목 4", field:"option04", widthGrow: 1.5, headerSort: false,
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
		url: '/api/gate/getGateRuleInfoList'
	});
});