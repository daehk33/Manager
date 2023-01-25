<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="group" tabindex="-1"
	aria-labelledby="groupModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="groupModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<label for="barcode">BARCODE</label>
								<select class="form-control" id="barcode" name="device_port"  size='4' required="">
									<option value="">&nbsp;</option>
								</select>
							</div>
							<div class="form-group form-floating-label">
								<label for="reader">RF READER</label>
								<select class="form-control" id="reader" name="device_port" size='4' required="">
									<option value="">&nbsp;</option>
								</select>
							</div>
							<div class="form-group form-floating-label">
								<label for="moblie">MOBLIE</label>
								<select class="form-control" id="moblie" name="device_port"  size='4' required="">
									<option value="">&nbsp;</option>
								</select>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label" id="group_lib">
								<select class="form-control input-border-bottom item-field"  id="library_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="library_key" class="placeholder">도서관명</label>
							</div>
							<div class="form-group form-floating-label" id="group_gate">
								<select class="form-control input-border-bottom item-field"  id="gate" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="gate" class="placeholder">게이트 목록</label>
							</div>
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field"  id="type" required="">
									<option value="0" selected>STX+ETX</option>
									<option value="1">ENTER</option>
								</select>
								<label for="type" class="placeholder">타입</label>
							</div>
							<div class="form-group form-floating-label" id="group_command">
								<input type="text"class="form-control input-border-bottom item-field"  id="command" required="">
								<label for="command" class="placeholder">명령어</label>
							</div>
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field"  id="direction" required="">
									<option value="I" selected>I</option>
									<option value="O">O</option>
								</select>
								<label for="direction" class="placeholder">방향 (I: 입구, O:출구)</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="confirm">
					<span class="icon-title">확인</span>
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
				<button type="button" class="btn btn-danger btn-round" role="action" action="dele">
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

<script type="text/javascript" src="/resources/js/main/gate/modal/groupModal.js"></script>
