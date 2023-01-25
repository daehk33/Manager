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
			
			// 등록 버튼 클릭시
			$('button[role="add"]').click(function() {
				const settingModal = $('#settingModal');
				
				const input = ParamManager.load('insert');
				settingModal.append(input);
				
				settingModal.modal('show');
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "시스템 정책 목록_("+common.getDateToStr()+").xlsx";
				
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
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				placeholder: TableUtil.getPlaceholder('조회된 정책 목록이 없습니다.'),
				headerFilterPlaceholder: '검색어 입력',
				
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
					if(response.result.itemsCount == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.items;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "정책 번호", field:"rule_id", widthGrow: 1, headerFilter: 'input', headerSort: true },
					{ title: "정책 내용", field:"rule_nm", widthGrow: 2, headerFilter: 'input', headerSort: true },
					{ title: "정책 상세 정보", field: "rule_desc", widthGrow: 4, headerSort: false },
					{
						title:"항목 1", field:"option_1", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title:"항목 2", field:"option_2", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
						}
					},
					{
						title:"항목 3", field:"option_3", widthGrow: 1.5, headerSort: false,
						formatter: "tickCross", formatter: function(cell, formatterParams, onRendered) {
							return cell.getValue() || "-";
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
			const settingModal = $('[id="settingModal"][target="system"]');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			
			settingModal.append(input);
			settingModal.modal('show');
			settingModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: '/api/system/getSystemRuleList'
	});
});