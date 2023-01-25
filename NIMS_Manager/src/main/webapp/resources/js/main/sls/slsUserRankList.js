$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function(){
			this.table = BookRankTable.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this);
			
			// 장비 검색 로드
			Device.load({model_key: '6'});
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load({model_key: '6'});
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "";
				
				if(Search.params.type != "all") file_name = "우수 회원 정보_("+Search.params.startDate+").xlsx";
				else file_name = "우수 회원 정보_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";

				Excel.download(target, table, file_name);
			});
		}
	};
	
	const BookRankTable = {
		load: function(){
			Search.params.type = "day";
			Search.params.startDate = common.getDateToStr('month');

			return this.draw();
		},
		draw: function(){
			const table = new Tabulator("#rankTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return value;
				},
				placeholder: TableUtil.getPlaceholder('우수 회원 목록이 없습니다.'),
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				ajaxParams: {
					type: Search.params.type,
					startDate: Search.params.startDate,
					endDate: ''
				},
				paginationDataSent : {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator : function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					
					const deviceKey = $('#filter-device #select_device option:selected').val();
					if(deviceKey != '') {
						url += `&device_key=${deviceKey}`;
					}
					
					return url;
				},
				ajaxResponse : function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					
					if(response.result.count == 0) { 
						$('.card-title-count').hide();
						table.updateColumnDefinition("user_id", {headerFilter: false});
						table.updateColumnDefinition("user_name", {headerFilter: false});
					}
					else { 
						$('.card-title-count').show();
						table.updateColumnDefinition("user_id", {headerFilter: true});
						table.updateColumnDefinition("user_name", {headerFilter: true});
					}
					
					return response.result.data;
				},
				columns: [
					{ title: "NO", formatter: "rownum", width: 55, hozAlign: "center", headerHozAlign: "center", frozen: true, headerSort: false , download:false },
					{ title: "회원번호", field: "user_id", widthGrow:3, headerSort: false, headerFilterPlaceholder: " " },
					{ title: "회원명", field: "user_name", widthGrow:3, headerSort: false, headerFilterPlaceholder: " ",
						formatter:function(cell){return Masking.name(cell.getValue());}
					},
					{ title: "대출횟수", field: "cnt", widthGrow:3 , headerSort: false,
						hozAlign: "center", headerHozAlign: "center",
					},
					{ title: "최근대출일자", field: "recent_loan_date",widthGrow:3, headerSort: false }
				],
				rowClick: function(e, row) {
					const data = row.getData();
					const input = ParamManager.load('show', JSON.stringify(data));
					
					const userLoanModal = $('#slsUserLoanModal');
					userLoanModal.append(input);
					
					userLoanModal.modal('show')
				},
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				}
			});
			
			return table;
		}
	};
	
	Content.load({
		url: '/api/sls/getSLSUserRankList'
	});
});