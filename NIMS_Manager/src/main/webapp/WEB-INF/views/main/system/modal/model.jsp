<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Model Modal -->
<div class="modal fade" id="modelModal" role="modal" target="model" tabindex="-1"
	aria-labelledby="modelModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="modelModalLabel">model Modal</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row ta-l">
					<div class="col-lg-12">
						<div>
							<div class="form-group form-floating-label" style="display: flex">
								<input type="text" class="form-control input-border-bottom item-field" id="model_id" required>
								<label for="model_id" class="placeholder">모델 아이디</label>
								<div role="msg" target="duplicated"></div>
							</div>
						</div>
						<div>
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="model_nm" required>
								<label for="model_nm" class="placeholder">모델 명</label>
							</div>
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="model_type" required>
									<option value="">&nbsp;</option>
								</select>
								<label for="model_type" class="placeholder">모델 타입</label>
							</div>
						</div>
						<div>
							<div class="form-group form-floating-label">
								<textarea class="form-control input-border-bottom bg-dark-fill-5 item-field" id="model_desc" rows="5" ></textarea>
								<label for="model_desc" class="placeholder">비고</label>
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

<script type="text/javascript" src="/resources/js/main/system/modal/model.js"></script>
