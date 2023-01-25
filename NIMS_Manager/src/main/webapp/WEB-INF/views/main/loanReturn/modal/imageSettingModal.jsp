<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Setting Modal -->
<div class="modal fade" id="imageModal" role="modal" tabindex="-1"
	aria-labelledby="imageModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="bannerModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body uploader-modal">
				<div class="mb-3 role-option role-modify">
					<span class="badge badge-secondary">배너명</span>
					<span class="icon-title" role="data" data-target="banner"></span>
				</div>
				<img src="" class="param img-float role-option role-modify" name="banner"
					onerror="this.src='/resources/banner/img/banner_noimage.png';">
				<form action ="/api/loanReturn/saveBannerImage" method="post" enctype="multipart/form-data" class="role-option role-add">
					<input type="file" id="fileUploader" multiple/>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-round btn-success role-option role-add" role="action" data-action="batch_upload">
					<i class="fas fa-check"></i><span class="icon-title">일괄 등록</span>
				</button>
				<button class="btn btn-round btn-danger btn-trash role-option role-modify" role="action" data-action="delete">
					<span class="trash">
						<span></span><i></i>
					</span>
					<span class="icon-title">삭제</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/loanReturn/modal/imageSettingModal.js"></script>
