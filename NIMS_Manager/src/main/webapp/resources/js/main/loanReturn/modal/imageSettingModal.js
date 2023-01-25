$(function() {
	const Content = {
		params: null,
		load : function(params) {
			this.params = params;
			
			// FilePond 플러그인 등록
			FilePond.registerPlugin(FilePondPluginFileValidateType, FilePondPluginImagePreview);
			
			Modal.load();
		}
	};
	
	const Modal = {
		modal: null,
		params: null,
		filepond: null,
		type: null,
		load : function() {
			this.modal = $('#imageModal');
			
			this.draw();
		},
		draw : function() {
			const that = this;
			const modal = this.modal;
			
			modal.on('show.bs.modal', function(e) {
				const header = modal.find('.modal-header');
				const title = header.find('.modal-title');
				const body = modal.find('.modal-content .modal-body');
				const footer = modal.find('.modal-footer');
				
				const param = modal.find('.param[name="item"]');
				const params = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				const type = param.attr('action');
				that.params = params;
				that.type = type;
				
				modal.find('.role-option').hide();
				if(type == 'add') {
					title.html('배너 추가');
					modal.find('.role-add').show();
					
					that.filepond = that.uploader();
				}
				else if(type == 'modify') {
					title.html('배너 미리보기');
					modal.find('.role-modify').show();
					
					const bannerName = modal.find('span[role="data"][data-target="banner"]');
					
					let name = params.file_name.split('/');
					name = name[name.length -1];
					
					bannerName.text(name);
					
					// 이미지 불러오기
					const image = modal.find('.param[name="banner"]');
					image.attr('src', params.file_path);
				}
			});
			
			modal.on('hide.bs.modal', function(e){
				if(that.type == 'add'){
					const table = Tabulator.prototype.findTable("#imageSettingTable")[0];
					
					that.filepond.destroy();
					table.setData();
				}
			});
			
			this.action();
		},
		uploader: function() {
			const that = this;
			const modal = this.modal;
			const target = modal.find('#fileUploader')[0];
			const device_key =  that.params.device_key
			
			const pond = FilePond.create(target, {
				// Common options
				instantUpload: false,
				labelIdle: `파일을 이곳에 올리거나, <span class="filepond--label-action"><b>여기</b></span>를 클릭하여 파일을 추가해주세요.`,
				labelFileProcessing: '업로드 진행중',
				labelFileProcessingComplete: '업로드 완료',
				labelTapToCancel: '취소',
				labelTapToRetry: '재시도',
				labelTapToUndo: '되돌리기',
				
				// Server options
				server: {
					url: '/api/loanReturn/saveBannerImage',
					process: {
						headers: {
							'device_id': $('#filter-field option:selected')[0].dataset.device_id
						},
						onload: function(response) {
							AjaxUtil.request({
								url: '/api/api/sftpService',
								data: {
									action: "upload",
									type: "image",
									device_key: device_key
								},
								done: function(data) {
									BannerManager.sendChanged($('#filter-field option:selected')[0].dataset.device_id, '1');
								}
							});
						}
					}
				},
				
				// File validate type options
				acceptedFileTypes: ['image/*'],
				labelFileTypeNotAllowed: '잘못된 유형의 파일',
				fileValidateTypeLabelExpectedTypes: '허용된 파일 유형 {allTypes}',
				
				// Image preview options
				imagePreviewMaxHeight: 125
			});
			
			return pond;
		},
		action: function() {
			const that = this;
			const modal = this.modal;
			
			modal.find('.btn[role="action"]').click(function(e){
				const action = this.dataset.action;
				const inputs = modal.find('.form-control');
				
				const param = modal.find('.param[name="item"]');
				const banner = [undefined, ''].indexOf(param.val()) > -1 ? {} : JSON.parse(param.val());
				
				const table = Tabulator.prototype.findTable("#bannerTable")[0];
				
				const device_id = $('#filter-field option:selected')[0].dataset.device_id;
				const device_key = banner.device_key;
				
				if(action == 'delete'){
					Alert.confirm({text: '현재 페이지에서 배너를 삭제하면 대출반납기에서도 더이상 볼수 없습니다. 그래도 삭제하시겠습니까?'}, function(result){
						if(!result.isConfirmed) return;
						const file_name = banner["file_name"];
						
						// 삭제 ajax
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
					});
				}
				else if(action == 'batch_upload'){
					const pond = that.filepond;
					const totalCnt = pond.getFiles().length;
					
					if(totalCnt > 0) {
						pond.processFiles();
					}
				}
			});
		}
	};
	
	Content.load({
		onHide: () => {
			Modal.modal.modal('hide');
			Tabulator.prototype.findTable('#imageSettingTable')[0].setData();
		}
	});
});