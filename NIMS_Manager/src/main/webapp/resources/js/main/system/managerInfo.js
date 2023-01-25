$(function() {
	const Content = {
		params : {},
		table: null,
		load : function(params) {
			this.params = params;
			this.admin = {id: manager_id}
			
			this.result();
		},
		result : function() {
			this.table = Table.load();
			this.event();
		},
		event : function() {
			const table = this.table;
			
			// 등록 버튼 클릭시
			$('button[role="add"]').click(function() {
				const managerModal = $('#managerModal');
				
				const input = ParamManager.load('insert');
				managerModal.append(input);
				
				managerModal.modal('show');
			});
			
			// 초기화 버튼 클릭시
			$('button[role="reset"]').click(function() {
				const selectedData = table.getSelectedData();
				if(selectedData.length == 0){
					Alert.warning({text: '비밀번호를 초기화할 관리자를 선택해주세요!'});
					return;
				}
				const idList = selectedData.map(x => x.manager_id).join(', ');
				let confirmParams = {title: '비밀번호 초기화', 
						text: `관리자(${idList})의 비밀번호가 초기 비밀번호로 변경됩니다. 진행하시겠습니까?`, confirmText: '초기화'}
				Alert.confirm(confirmParams, (result) => {
					if(!result.isConfirmed) return;
					
					let results = [];
					
					selectedData.forEach(manager => {
						// 로그인한 사용자보다 초기화하려는 관리자의 등급이 높을 경우
						if(manager.manager_grade < grade){
							results.push(false);
						}
						else {
							AjaxUtil.request({
								url: '/api/manager/resetManagerPassword',
								data: {
									rec_key: manager.rec_key
								},
								done: function(data){
									data = data.result;
									
									data.CODE == '200' ? results.push(true) : results.push(false);
								}
							});
						}
					});
					
					results.every(value => value) ? 
						Alert.success({title: '초기화 완료!', text: '요청하신 관리자들의 비밀번호가 초기화되었습니다!'}) : 
						Alert.warning({title: '경고!', text: '일부 오류와 함께 비밀번호가 초기화되었습니다.'});
				});
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(e){
				const target = $(this).attr('target');
				let file_name = "사용자 목록_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	const Table = {
		load : function() {
			return this.draw();
		},
		draw : function () {
			const that = this;
			
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign: "middle",
				locale: true,
				layout: "fitColumns",
				addRowPos : "top",
				history : true,
				movableColumns : true,
				placeholder: TableUtil.getPlaceholder('조회된 관리자 목록이 없습니다.'),
				pagination : "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				
				ajaxURL : Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent : {
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
				ajaxResponse : function(url, params, response) {
					return response.result.data;
				},
				headerFilterPlaceholder: '검색어 입력',
				columns: [
					{
						formatter: "rowSelection", titleFormatter: "rowSelection", titleFormatterParams: { rowRange: "active" }, 
						hozAlign: "center", widthGrow: 0.5, headerSort: false, headerHozAlign: "center",
						cellClick:function(e, cell) {
							cell._cell.row.component.toggleSelect();
						},
						width: 30, download: false
					},
					{ title: "NO", formatter: "rownum", width: 70, hozAlign: "center", headerHozAlign: "center", headerSort: false , download:false },
					{ title: "사용자 아이디", field: "manager_id", hozAlign: "center", headerHozAlign: "center", headerFilter: true, headerSort: true },
					{ title: "사용자 이름", field: "manager_nm", hozAlign: "center", headerHozAlign: "center", headerFilter: true, headerSort: false },
					{ title: "사용자 등급", field: "manager_grade_nm", hozAlign: "center", headerHozAlign: "center", headerSort: false },
					{ title: "소속", field: "library_nm", hozAlign: "center", headerHozAlign: "center", headerSort: false },
					{ title: "마지막 로그인 일시", field: "login_date", headerSort: false },
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
				}
			});
			
			return table;
		},
		showRowDetail : function(row){
			const managerModal = $('#managerModal');
			let data = row.getData();
			
			const input = ParamManager.load('modify', JSON.stringify(data));
			managerModal.append(input);
			
			managerModal.modal('show');
			managerModal.on('hidden.bs.modal', function(){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			});
		}
	};
	
	Content.load({
		url: '/api/manager/getManagerList'
	});
});