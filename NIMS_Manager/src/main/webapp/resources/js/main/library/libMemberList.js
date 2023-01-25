$(function() {
	const Content = {
		params: {},
		table: null,
		load: function(params) {
			this.params = params;
			this.result();
		},
		result: function(){
			this.table = MemberTable.load();
			this.event();
		},
		event: function(){
			const table = this.table;
			
			// 검색 객체 스크립트 로드
			SearchContainer.load(this, [
				{id: 'member_no', name: '회원 번호'}, 
				{id: 'member_nm', name: '회원 이름'}
			]);
			
			// 도서관 선택 시
			$('input[name="library"]').change(function(e){
				table.setData();
			});
			
			// 엑셀 내보내기 버튼 클릭시
			$('button[role="export"]').click(function(){
				const target = $(this).attr('target');
				let file_name = "도서관 회원 목록_("+common.getDateToStr()+").xlsx";
				
				Excel.download(target, table, file_name);
			});
		}
	};
	
	const MemberTable = {
		load: function(){
			return this.draw();
		},
		draw: function(){
			const table = new Tabulator("#listTable", {
				langs: common.setDefaults("tabulator"),
				columnHeaderVertAlign:"middle",
				locale: true,
				layout: "fitColumns",
				groupBy: grade == 0 ? "library_nm" : false,
				groupHeader: function(value, count, data, group){
					return `${value}<span>(회원<span class="group-header">${count}</span>명)</span>`;
				},
				placeholder: TableUtil.getPlaceholder('조회된 회원 목록이 없습니다.'),
				pagination: "local",
				paginationSize: paginationConfig.size,
				paginationSizeSelector: paginationConfig.selector,
				ajaxFiltering: true,
				
				ajaxURL: Content.params.url,
				ajaxConfig: ajaxConfig,
				ajaxLoaderLoading: TableUtil.getLoaderLoading(),
				paginationDataSent: {
					"page" : "pageIdx",
					"size" : "pageSize",
				},
				ajaxURLGenerator: function(url, config, params) {
					url = `${url}?${common.parseParam(params)}`;
					
					if(sessionStorage.getItem('library_key') != '') {
						url += `&library_key=${sessionStorage.getItem('library_key')}`;
					}
					return url;
				},
				ajaxResponse: function(url, params, response) {
					$('[role="msg"][target="count"]').text(response.result.count);
					if(response.result.count == 0) $('.card-title-count').hide();
					else $('.card-title-count').show();
					
					return response.result.data;
				},
				columns: [
					{ title: "회원번호", field: "member_no", headerSort: false },
					{ title: "회원 아이디", field: "member_id", headerSort: false },
					{ title: "회원 이름", field: "member_nm", headerSort: false },
					{ title: "회원 유형", field: "user_class", headerSort: false },
					{ title: "회원 상태", field: "member_class", headerSort: false },
					{ title: "회원 등록일", field: "reg_date", headerSort: false }
				],
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
		url: '/api/library/getLibMemberList'
	});
});