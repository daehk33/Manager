$(function() {
	const Content = {
		params: {},
		data: {},
		listTable: null,
		chartTable: null,
		load: function(params) {
			this.params = params;
			
			Chart.helpers.merge(Chart.defaults.global.plugins.datalabels, {
				display: function(context) {
					// data가 0이 아닐경우에만 값 표시
					let index = context.dataIndex;
					let value = context.dataset.data[index];
					
					return value != 0 ? true: false;
				}
			});
			
			this.result();
		},
		result: function(){
			this.event();
		},
		event: function(){
			// 검색 객체 스크립트 로드
			SearchContainer.load(this, true);
			
			// 장비 검색 로드
			Device.load({model_key: '8'});
			
			// 테이블 로드
			const listTable = UseStatsTable.load();
			const chartTable = UseStatsChart.load();
			
			this.listTable = listTable;
			this.chartTable = chartTable;
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(){
				Device.load({model_key: '8'});
				listTable.setData();
				chartTable.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');

				let file_name  = "";
				
				if(Search.params.type != "all") file_name = "요일별 통계_("+Search.params.startDate+").xlsx";
				else file_name = "요일별 통계_("+Search.params.startDate+"-"+Search.params.endDate+").xlsx";
				
				Excel.download(target, listTable, file_name);
			});
		},
	};
	
	const UseStatsTable = {
		load: function(){
			Search.params.type = "day";
			Search.params.startDate = common.getDateToStr('month');

			return this.draw();
		},
		draw: function(){
			const that = this;

			const table = new Tabulator("#statsTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return value;
				},
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),

				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering:true,
				
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
					return response.result.data;
				},
				columns: [
					{ title: "요일", field: "date_nm", widthGrow: 2, headerSort: false,
						hozAlign: "center", headerHozAlign: "center", topCalc:() => "합계"
					},
					{
						title:"대출 정보", hozAlign: "center", headerHozAlign: "center", 
						columns: [
							{ title: "건수", field: "loan_cnt", widthGrow: 4, headerSort: false, hozAlign: "center", headerHozAlign: "center", topCalc:"sum"  },
							{ title: "이용자 수", field: "loan_user_cnt", widthGrow: 4, headerSort: false, hozAlign: "center", headerHozAlign: "center" }
						]
					}
				],
				downloadComplete: function(){
					Swal.close();
				},
				cellMouseEnter: (e, cell, row) => {
					TableUtil.cellMouseEnter(e, cell, row)
				},
			});
			
			return table;
		},
	};
	
	const UseStatsChart = {
		load: function(){
			return this.draw();
		},
		draw: function() {
			const that = this;
			
			const table = new Tabulator("#statsChart", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group) {
					return value;
				},
				addRowPos: "top",
				history: true,
				movableColumns: true,
				placeholder: TableUtil.getPlaceholder('조회된 이력이 없습니다.'),
				
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering:true,
				
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
					const items = response.result.data;
					const libraryInfo = TableUtil.groupBy(items, 'library_key');
					
					let data = [];
					Object.entries(libraryInfo).forEach(info => {
						const col = {library_key: info[0]};
						
						col.label = info[1].map(library => {
							return library.date_nm;
						});
						
						col.loan_info  = [[], []];
						
						info[1].forEach(library => {
							col.loan_info[0].push(library.loan_cnt);
							col.loan_info[1].push(library.loan_user_cnt);
						});
						
						col.library_nm = info[1][0].library_nm;
						
						data.push(col);
					});
					
					return data;
				},
				
				columns: [
					{
						title: "대출 정보", field: 'loan_info', hozAlign: "center", headerSort: false, headerHozAlign: "center",
						formatter:function(cell, formatterParams, onRendered){
							const data = cell._cell.row.getData();
							const cellElement = `<canvas name="loanStats" library="${data.library_key}" height="490px"><canvas>`;
							onRendered(function(){
								let ctx = document.querySelector(`canvas[name="loanStats"][library="${data.library_key}"]`).getContext('2d');
								let chartData = {
									datasets: [{
										label: '건수',
										data: data.loan_info[0],
										backgroundColor: '#23cf93',
										borderColor: '#23cf93'
									},{
										label: '이용자 수',
										data: data.loan_info[1],
										backgroundColor: '#7e44ea',
										borderColor: '#7e44ea'
									}],
									labels: data.label,
								};
								
								that.createChart(ctx, chartData)
							});
							
							return cellElement;
						}
					}
				]
			});
			
			return table;
		},
		createChart: function(ctx, data){
			ctx && new Chart(ctx, {
				type: 'bar',
				data: data,
				options: {
					responsive: true, 
					maintainAspectRatio: false,
					plugins: {
						title: { display: false },
						datalabels: {
							align: 'end',
							anchor: 'end',
							borderRadius: 4,
							backgroundColor: function(context) {
								return context.dataset.backgroundColor;
							},
							formatter: function(value, context) {
								return value.toLocaleString();
							},
							color: 'white'
						}
					},
					tooltips: {
						bodySpacing: 5, mode: 'index', intersect: 0,
						xPadding:5, yPadding:5, caretPadding:5
					},
					layout:{
						padding: { top:15 }
					},
					legend: {
						labels: {
							fontColor: theme == 'dark' ? 'rgba(255, 255, 255, 1)' : 'rgba(0, 0, 0, 1)'
						}
					},
					scales: {
						yAxes: [{
							ticks: {
								beginAtZero: true, maxTicksLimit: 6, padding: 15,
								callback: function(val) {
									return Number.isInteger(val) ? val : null;
								},
							},
							gridLines: {drawTicks: false, display: false}
						}],
						xAxes: [{
							gridLines: {zeroLineColor: "transparent"},
							ticks: {padding: 10, fontStyle: "500", maxTicksLimit: 10}
						}],
					},
				},
			});
		}
	};
	
	Content.load({
		url: '/api/stats/getStatsResvLoanDate'
	});
});