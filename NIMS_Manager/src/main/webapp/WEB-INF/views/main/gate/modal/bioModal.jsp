<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="security" tabindex="-1"
	aria-labelledby="portModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="bioModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="form-group form-floating-label" id="bio_lib">
						<select class="form-control input-border-bottom item-field" id="library_key" required="">
							<option value="">&nbsp;</option>
						</select>
						<label for="library_key" class="placeholder">도서관명</label>
					</div>
					<div class="form-group form-floating-label">
						<select class="form-control input-border-bottom item-field" id="group_no" required="">
							<option value="">&nbsp;</option>
						</select>
						<label for="group_no" class="placeholder">그룹</label>
					</div>
					<div class="form-group form-floating-label">
						<input type="text" class="form-control input-border-bottom item-field" id="device_sn">
						<label for="device_sn" class="placeholder">Device_SN</label>
					</div>
					<div class="form-group form-floating-label">
						<input type="text" class="form-control input-border-bottom item-field" id="access_token">
						<label for="access_token" class="placeholder">Token</label>
					</div>
					<div class="form-group form-floating-label">
						<input type="text" class="form-control input-border-bottom item-field" id="api_url">
						<label for="api_url" class="placeholder">API_URL</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="confirm">
					<span class="icon-title">확인</span>
				</button>
				<button type="button" class="btn btn-danger btn-round" role="action" action="cancel" data-dismiss="modal" aria-label="Close">
					<span class="icon-title">취소</span>
				</button>
				<button type="button" class="btn btn-primary btn-round" role="action" action="update">
					<div class="pen-container">
						<span class="pen">
							<span></span>
						</span>
						<i></i>
					</div>
					<span class="icon-title">수정</span>
				</button>
				<button type="button" class="btn btn-danger btn-round" role="action" action="del">
					<span class="trash">
						<span></span>
						<i></i>
					</span>
					<span class="icon-title">삭제</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/gate/modal/bioModal.js"></script>
				