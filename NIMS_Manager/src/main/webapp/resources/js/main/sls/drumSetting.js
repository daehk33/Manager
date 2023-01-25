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
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this);
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "서가 설정 목록_("+common.getDateToStr()+").xlsx";
				
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
				groupBy: grade == 0 ? ["library_nm", "device_nm"] : "device_nm",
				groupHeader: grade == 0 ? [
					function(value, count, data, group){
						let groupCnt = group._group.groupList.length;
						return `${value}<span>(장비<span class="group-header">${groupCnt}</span>개)</span>`;
					},
					function(value, count, data){
						return `${value}<span>(설정<span class="group-header">${count}</span>개)</span>`;
					}
				] : function(value, count, data){
					return `${value}<span>(설정<span class="group-header">${count}</span>개)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 서가 목록이 없습니다.'),
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
					$('[role="msg"][target="count"]').text(response.result.length);
					if(response.result.length == 0) {
						$('.card-title-count').hide();
						table.updateColumnDefinition("use", {headerFilter: false});
						table.updateColumnDefinition("volume", {headerFilter: false});
						table.updateColumnDefinition("grade", {headerFilter: false});
						table.updateColumnDefinition("use_smartlib", {headerFilter: false});
						table.updateColumnDefinition("use_reserve", {headerFilter: false});
						table.updateColumnDefinition("use_other_loc_return", {headerFilter: false});
						table.updateColumnDefinition("use_other_lib_return", {headerFilter: false});
					}
					else {
						$('.card-title-count').show();
						table.updateColumnDefinition("use", {headerFilter: "select"});
						table.updateColumnDefinition("volume", {headerFilter: true});
						table.updateColumnDefinition("grade", {headerFilter: "select"});
						table.updateColumnDefinition("use_smartlib", {headerFilter: "select"});
						table.updateColumnDefinition("use_reserve", {headerFilter: "select"});
						table.updateColumnDefinition("use_other_loc_return", {headerFilter: "select"});
						table.updateColumnDefinition("use_other_lib_return", {headerFilter: "select"});
					}
					return response.result.items;
				},
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						}, widthGrow: 0.25, download: false
					},
					{ title: "NO", field: "no", width: 70, headerSort: false, headerHozAlign: "center", hozAlign: "center" },
					{ title: "서가 번호", field: "posistep", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center" },
					{ title: "사용 여부", field: "use", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["Y", "N"], headerFilterPlaceholder: " " },
					{ title: "서가당 선반 수", field: "volume", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterPlaceholder: " " },
					{ title: "선반 타입", field: "grade", widthGrow: 1, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["B", "C"], headerFilterPlaceholder: " " },
					{ title: "스마트도서관 사용 여부", field: "use_smartlib", widthGrow: 2, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["Y", "N"], headerFilterPlaceholder: " "  },
					{ title: "예약 사용 여부", field: "use_reserve", widthGrow: 2, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["Y", "N"], headerFilterPlaceholder: " "  },
					{ title: "자관 도서 반납 허용", field: "use_other_loc_return", widthGrow: 2, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["Y", "N"], headerFilterPlaceholder: " "  },
					{ title: "타관 도서 반납 허용", field: "use_other_lib_return", widthGrow: 2, headerSort: false, headerHozAlign: "center", hozAlign: "center", headerFilterParams: ["Y", "N"], headerFilterPlaceholder: " "  }
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
		url: '/api/device/getDrumRuleList'
	});
});