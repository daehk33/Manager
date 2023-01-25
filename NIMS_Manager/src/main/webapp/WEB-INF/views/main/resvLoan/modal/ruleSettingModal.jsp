<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Setting Modal -->
<div class="modal fade" id="settingModal" role="modal" target="device" tabindex="-1"
	aria-labelledby="deviceModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="deviceModalLabel">장비 정책</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<input type="hidden" class="item-field" id="rec_key">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="library_nm" required="">
								<label for="library_nm" class="placeholder">도서관명</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="device_nm" required="">
								<label for="device_nm" class="placeholder">장비 아이디</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<textarea class="form-control input-border-bottom bg-dark-fill-5 item-field" id="description"></textarea>
								<label for="description" class="placeholder">정책 내용</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="manage_id" required="">
								<label for="manage_id" class="placeholder">정책 번호</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="use" required="">
									<option value="" disabled hidden>&nbsp;</option>
									<option value="Y">사용</option>
									<option value="N">미사용</option>
								</select>
								<label for="use" class="placeholder">사용 여부</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="value_1" required="">
								<label for="value_1" class="placeholder">설정 값</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="update">
					<div class="pen-container">
						<span class="pen">
							<span></span>
						</span>
						<i></i>
					</div>
					<span class="icon-title">수정</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/resvLoan/modal/ruleSettingModal.js"></script>
