<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Code Modal -->
<div class="modal fade" id="codeModal" role="modal" target="code" tabindex="-1"
	aria-labelledby="codeModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="codeModalLabel">Code Detail</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row ta-l">
					<div class="col-lg-6">
						<div class="form-group form-floating-label" style="display: flex">
							<input type="text" class="form-control input-border-bottom item-field" id="code" required>
							<label for="code" class="placeholder">코드</label>
							<div role="msg" target="duplicated"></div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="form-group form-floating-label">
							<input type="text" class="form-control input-border-bottom item-field" id="code_value" required>
							<label for="code_value" class="placeholder">코드 값 1</label>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="form-group form-floating-label">
							<input type="text" class="form-control input-border-bottom item-field" id="code_value2" required>
							<label for="code_value2" class="placeholder">코드 값 2</label>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="form-group form-floating-label">
							<input type="text" class="form-control input-border-bottom item-field" id="code_value3" required>
							<label for="code_value3" class="placeholder">코드 값 3</label>
						</div>
					</div>
					<div class="col-lg-12">
						<div class="form-group form-floating-label">
							<select class="form-control input-border-bottom item-field" id="use_yn" required="">
								<option value="" disabled hidden>&nbsp;</option>
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
							<label for="use_yn" class="placeholder">사용 여부</label>
						</div>
					</div>
					<div class="col-lg-12">
						<div class="form-group form-floating-label">
							<textarea class="form-control input-border-bottom bg-dark-fill-5 item-field" id="code_desc" rows="5" ></textarea>
							<label for="code_desc" class="placeholder">비고</label>
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

<script type="text/javascript" src="/resources/js/main/system/modal/code.js"></script>
