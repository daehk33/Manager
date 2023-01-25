<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="gateButton" tabindex="-1"
	aria-labelledby="portModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="buttonModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="form-group form-floating-label" id="button_lib">
						<select class="form-control input-border-bottom item-field" id="library_key" required="">
							<option value="">&nbsp;</option>
						</select>
						<label for="library_key" class="placeholder">도서관명</label>
					</div>
					<div class="form-group form-floating-label">
						<select class="form-control input-border-bottom item-field" id="btn_name" required="">
							<option value="">&nbsp;</option>
						</select>
						<label for="btn_name" class="placeholder">버튼이름</label>
					</div>
					<div class="form-group form-floating-label">
						<select class="form-control input-border-bottom item-field" id="command" required="">
							<option value="">&nbsp;</option>
						</select>
						<label for="command" class="placeholder">명령어</label>
					</div>
					<div class="form-group form-floating-label">
						<select class="form-control input-border-bottom item-field"  id="io_type" required="">
							<option value="I" selected>I</option>
							<option value="O">O</option>
						</select>
						<label for="io_type" class="placeholder">방향 (I: 입구, O:출구)</label>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="open">
								<label for="open" class="placeholder">open</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="close">
								<label for="close" class="placeholder">close</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="on">
								<label for="on" class="placeholder">on</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="off">
								<label for="off" class="placeholder">off</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="reset">
								<label for="reset" class="placeholder">reset</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="forced_close">
								<label for="forced_close" class="placeholder">forced_close</label>
							</div>
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
</div>

<script type="text/javascript" src="/resources/js/main/gate/modal/buttonModal.js"></script>
				