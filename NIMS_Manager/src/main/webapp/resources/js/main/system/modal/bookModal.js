$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			Modal.load();
		}
	};
	
	const Modal = {
		modal: null,
		type: '',
		check: {
			book_no: false,
			title: false,
			author: false,
			publisher: false,
			publish_year: false,
			get_type: false,
			book_form: false,
			book_price: false,
			loan_status: false
		},
		load : function() {
			this.modal = $('#bookModal');
			
			this.draw();
		},
		draw : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const container = modal.find('.modal-dialog');
				
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				const param = modal.find('.param[name="item"]');
				const params = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				const type = param.attr('action');
				that.params = params;
				that.type = type;
				
				const form = body.find('#bookForm');
				const inputs = body.find('.form-control');
				const buttons = footer.find('.btn');
				
				const addButton = footer.find('.btn[data-action="add"]');
				const addExcelButton = footer.find('.btn[data-action="add_excel"]');
				const updateButton = footer.find('.btn[data-action="update"]');
				const deleteButton = footer.find('.btn[data-action="delete"]');
				
				const addGroup = modal.find('form#bookForm');
				const excelGroup = modal.find('form#fileupload');
				
				/**
				 * 초기화
				 */
				Modal.check = {book_no: false, title: false, author: false, publisher: false, publish_year: false,
								get_type: false, book_form: false, book_price: false, loan_status: false};
				form.trigger('reset');
				buttons.hide();
				addButton.addClass('mr-2');
				updateButton.addClass('mr-2');
				
				inputs.prop('disabled', false);
				inputs.removeClass('is-valid is-invalid');
				inputs.parent().removeClass('is-filled');
				
				/**
				 * update용 초기화 함수 
				 */
				const update = function(){
					// 필드 초기화
					Object.keys(params).forEach(key => {
						const input = modal.find(`.form-control[name="${key}"]`);
						const val = params[key];
						
						if(key == 'loan_status'){
							if(val == '대출가능') input.val('0');
							else if(val == '대출중') input.val('1');
						}
						else {
							input.val(val);
						}
						
						input.parent().addClass('is-filled');
					});
					
					Modal.check.book_no = true;
					Modal.check.title = true;
					Modal.check.author = true;
					Modal.check.publisher = true;
					Modal.check.publish_year = true;
				};
				
				const modalDesc = modal.find('.modal-ext-desc');
				const modalDescContainer = modal.find('.modal-title-desc');
				modalDescContainer.show();
				
				/**
				 * Page 유형 별 데이터 처리
				 */
				if(type == 'add'){
					title.text('서지 정보 추가');
					addButton.show();
					addButton.removeClass('mr-2');
					addGroup.show();
					excelGroup.hide();
					
					modalDesc.html('추가할 서지 정보를 입력해주세요.');
				}
				else if(type == 'add_excel'){
					title.text('엑셀파일로 서지 정보 추가');
					
					addExcelButton.show();
					addButton.removeClass('mr-2');
					addGroup.hide();
					excelGroup.show();
					
					modalDesc.html(`추가할 서지 정보 엑셀파일을 첨부해주세요.<br>
						&nbsp;&nbsp; - xls, xlsx 파일을 올려주세요.<br>
						&nbsp;&nbsp; - 파일에 12개 항목에 해당하는 데이터가 모두 기입되어 있는지 확인해주세요.<br>
						&nbsp;&nbsp; - 항목 : [등록번호, 서명, 저자, 출판사, 출판연도, 청구기호, 위치]<br>
						&nbsp;&nbsp; - 컬럼헤더의 시작위치가 다르다면 같이 넣어주세요!`);
				}
				else if(type == 'modify'){
					title.text('서지 정보 수정');
					updateButton.show();
					deleteButton.show();
					addGroup.show();
					excelGroup.hide();
					
					modal.find('#id').prop('disabled', true);
					modalDesc.html('수정할 항목에 내용을 작성해주세요.');
					
					update();
				}
			});
			
			modal.on('hide.bs.modal', function(e) {
				modal.find('.param[name="item"]').remove();
			})
			
			this.event();
			this.action();
		},
		event: function(){
			const that = this;
			const modal = this.modal;
			
			const inputs = modal.find('.form-control');
			const fieldBookNo = modal.find('input[name="book_no"]');
			const fieldTitle = modal.find('input[name="title"]');
			const fieldAuthor = modal.find('input[name="author"]');
			const fieldPubl = modal.find('input[name="publisher"]');
			const fieldPublYear = modal.find('input[name="publish_year"]');
			const fieldCallNo = modal.find('input[name="call_no"]');
			const fieldGetType = modal.find('input[name="get_type"]');
			const fieldForm = modal.find('input[name="book_form"]');
			const fieldPrice = modal.find('input[name="book_price"]');
			const fieldLoan = modal.find('input[name="loan_status"]');
			
			/**
			 * [Field Validation] 공통
			 */
			inputs.on({
				focus: function(e){
					const that = $(e.target);
					
					if(that[0] == fieldBookNo[0]) that.attr("placeholder", "숫자(7자리)");
					else if(that[0] == fieldPublYear[0]) that.attr("placeholder", "YYYY(연도)");
					else if(that[0] == fieldGetType[0]) that.attr("placeholder", "ex) 구입");
					else if(that[0] == fieldForm[0]) that.attr("placeholder", "ex) 단행본");
					
					if(e.target.value == ''){
						that.removeClass('is-valid');
						that.removeClass('is-invalid');
						if(that[0] == fieldBookNo[0]) that.attr("placeholder", "숫자(7자리)");
						else if(that[0] == fieldPublYear[0]) that.attr("placeholder", "YYYY(연도)");
						else if(that[0] == fieldGetType[0]) that.attr("placeholder", "ex) 구입");
						else if(that[0] == fieldForm[0]) that.attr("placeholder", "ex) 단행본");
					}
				},
				focusout: function(e){
					const that = $(e.target);
					const value = e.target.value;
					
					if(that[0] == fieldBookNo[0]) that.removeAttr("placeholder");
					else if(that[0] == fieldPublYear[0]) that.removeAttr("placeholder");
					else if(that[0] == fieldGetType[0]) that.removeAttr("placeholder");
					else if(that[0] == fieldForm[0]) that.removeAttr("placeholder");
					
					if(value == ''){
						that.parent().removeClass('is-filled');
						that.removeClass('is-valid is-invalid');
						
						return false;
					}
				}
			});
			
			/**
			 * [Field Validation] book_no 중복 및 유효성 체크
			 */
			fieldBookNo.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;
				
				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.book_no = false;
					return false;
				}else if(!ValidationUtil.checkBookNo(value)){
					Modal.check.book_no = false;
					that.addClass('is-invalid');
					return false;
				}else {
					Modal.check.book_no = true;
					that.addClass('is-valid');
				}
				
			});
			
			/**
			 * [Field Validation] title 유효성 체크
			 */
			fieldTitle.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;

				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.title = false;
					return false;
				}else {
					Modal.check.title = true;
					that.addClass('is-valid');
				}
			});
			
			/**
			 * [Field Validation] author 유효성 체크
			 */
			fieldAuthor.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;

				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.author = false;
					return false;
				}else {
					Modal.check.author = true;
					that.addClass('is-valid');
				}
			});
			
			/**
			 * [Field Validation] publisher 유효성 체크
			 */
			fieldPubl.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;

				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.publisher = false;
					return false;
				}else {
					Modal.check.publisher = true;
					that.addClass('is-valid');
				}
			});
			
			/**
			 * [Field Validation] publish_year 유효성 체크
			 */
			fieldPublYear.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;

				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.publish_year = false;
					return false;
				}else if(!ValidationUtil.checkYear(value)){
					Modal.check.publish_year = false;
					that.addClass('is-invalid');
					return false;
				}else {
					Modal.check.publish_year = true;
					that.addClass('is-valid');
				}
			});
			
			/**
			 * [Field Validation] call_no 유효성 체크
			 */
			fieldCallNo.focusout(function(e){
				const that = $(e.target);
				const value = e.target.value;
				
				that.removeClass('is-valid is-invalid');
				if(value == ''){
					Modal.check.call_no = false;
					return false;
				}else {
					Modal.check.call_no = true;
					that.addClass('is-valid');
				}
			});
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			
			modal.find('.btn[role="action"]').click(function(e){
				e.preventDefault();
				
				const action = this.dataset.action;
				const inputs = modal.find('.form-control');
				
				const param = modal.find('.param[name="item"]');
				const manager = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				
				const params = {};
				for(let i=0; i< inputs.length; i++){
					const input = inputs[i];
					const id = inputs[i].id
					let value = inputs[i].value;
					
					if(id == 'is_super'){
						if(value == '0') {
							value = false;
						}
						else value = true;
					}
					params[id] = value;
				}
				
				// 서지 정보 추가
				if(action == 'add'){
					
					if(Modal.check.book_no == false){
						Alert.warning({text: '사용 가능한 등록번호인지 확인해주세요!'});
						return false;
					}
					else if(Modal.check.title == false){
						Alert.warning({text: '사용 가능한 서명인지 확인해주세요!'});
						return false;
					}
					else if(Modal.check.author == false){
						Alert.warning({text: '저자명을 입력해주세요!'});
						return false;
					}
					else if(Modal.check.publisher == false){
						Alert.warning({text: '출판사명을 입력해주세요!'});
						return false;
					}
					else if(Modal.check.publish_year == false){ //select 로 변경
						Alert.warning({text: '출판 연도를 입력해주세요!'});
						return false;
					}
					else if(Modal.check.call_no == false){ 
						Alert.warning({text: '청구 기호를 입력해주세요!'});
						return false;
					}
					
					AjaxUtil.request({
						url: '/api/book/add',
						data: params,
						successMessage: '서지 정보가 성공적으로 등록되었습니다.',
						table: 'bookTable',
						modal: modal,
					});
				}
				// 엑셀로 서지 정보 추가 
				else if(action == 'add_excel'){
					const start_row = modal.find('input[name="start_row"]');
					const fileObj = modal.find('input[name="file"]');
					if(fileObj.length == 0|| fileObj.val() == ''){
						parent = fileObj.parent();
						parent.addClass('has-error');
						Alert.warning({text: '파일을 선택해주세요!'});
						return;
					}
					
					var formData = new FormData();
					formData.append('file', fileObj[0].files[0]);
					formData.append('start_row', start_row.val());
					
					Alert.confirm({text: "첨부하신 파일로 덮어쓰시겠습니까? <br> <span style='font-size: 14px; color:grey'>'아니오' 클릭 시 해당 파일이 기존 정보에 추가로 등록됩니다.</span>"}, function(result){
						if(result.dismiss == "close"){
							return;
						}
						else if(result.isConfirmed){
							formData.append('rewrite', 'Y');
						}
						
						Alert.printLoading({text: '서지정보를 업로드하는 중입니다!<br>잠시만 기다려주십시오.'});
						
						AjaxUtil.request({
							url: '/api/book/excel',
							file: true,
							data: formData,
							table: 'bookTable',
							modal: modal,
							successMessage: '성공적으로 등록되었습니다.',
							complete: function(data){
								fileObj.val('');
							}
						});
					});
				}
				// 서지 정보 수정
				else if(action == 'update'){
					if(Utils.compareMap(params, Modal.params)){
						Alert.warning({text: '수정할 정보가 없습니다!'});
						return false;
					}
					
					if(Modal.check.book_no == false){
						Alert.warning({text: '사용 가능한 등록번호인지 확인해주세요!'});
						return false;
					}
					if(Modal.check.title == false){
						Alert.warning({text: '사용 가능한 서명인지 확인해주세요!'});
						return false;
					}
					else if(Modal.check.author == false){
						Alert.warning({text: '올바른 저자명을 입력해주세요!'});
						return false;
					}
					else if(Modal.check.publisher == false){
						Alert.warning({text: '올바른 출판사명을 입력해주세요!'});
						return false;
					}
					else if(Modal.check.publish_year == false){ //select 로 변경
						Alert.warning({text: '올바른 출판 연도를 입력해주세요!'});
						return false;
					}
					else if(Modal.check.call_no == false){ 
						Alert.warning({text: '올바른 청구 기호를 입력해주세요!'});
						return false;
					}
					
					AjaxUtil.request({
						url: '/api/book/update',
						data: params,
						table: 'bookTable',
						modal: modal,
						successMessage: '성공적으로 수정되었습니다'
					});
				}
				// 서지 정보 삭제
				else if(action == 'delete'){
					Alert.confirm({text: '해당 서지 정보를 삭제하시겠습니까?'}, function(result){
						if(!result.isConfirmed) return;
						AjaxUtil.request({
							url: '/api/book/delete',
							data: {
								rec_key: params.rec_key,
							},
							table: 'bookTable',
							modal: modal,
							successMessage: '성공적으로 삭제되었습니다'
						});
					});
				}
			});
		}
	};

	Content.load({});
});