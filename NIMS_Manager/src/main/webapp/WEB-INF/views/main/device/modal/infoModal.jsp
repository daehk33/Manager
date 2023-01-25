<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="device" tabindex="-1"
	aria-labelledby="deviceModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="deviceModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<select class="form-control input-border-bottom item-field" id="library_key" required="">
										<option value="">&nbsp;</option>
									</select>
									<label for="library_key" class="placeholder">도서관명</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<select class="form-control input-border-bottom item-field" id="model_key" required="">
										<option value="">&nbsp;</option>
									</select>
									<label for="model_key" class="placeholder">모델명</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="device_id" required="">
									<label for="device_id" class="placeholder">장비 아이디</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="device_nm" required="">
									<label for="device_nm" class="placeholder">장비명</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="device_location" required="">
									<label for="device_location" class="placeholder">위치</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<select class="form-control input-border-bottom item-field" id="config_yn" required="">
										<option value="Y">사용</option>
										<option value="N">미사용</option>
									</select>
									<label for="config_yn" class="placeholder">정책 사용 여부</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="device_ip" required="">
									<label for="device_ip" class="placeholder">장비 IP</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12">
								<div class="form-group form-floating-label">
									<textarea class="form-control input-border-bottom bg-dark-fill-5 item-field" id="device_desc" rows="5" ></textarea>
									<label for="device_desc" class="placeholder">비고</label>
								</div>
							</div>
						</div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="add">
					<i class="fas fa-plus"></i><span class="icon-title">등록</span>
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
				<button type="button" class="btn btn-danger btn-round" role="action" action="delete">
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

<script type="text/javascript" src="/resources/js/main/device/modal/infoModal.js"></script>