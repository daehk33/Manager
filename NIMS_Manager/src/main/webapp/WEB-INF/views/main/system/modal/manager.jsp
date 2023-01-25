<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Manager Modal -->
<div class="modal fade" id="managerModal" role="modal" target="manager" tabindex="-1"
	aria-labelledby="managerModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="managerModalLabel">Manager Detail</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row ta-l">
					<div class="col-lg-12">
						<form>
						<div class="form-group form-floating-label" style="display: flex">
							<input type="text" class="form-control input-border-bottom item-field" id="manager_id" required>
							<label for="manager_id" class="placeholder">사용자 아이디</label>
							<div role="msg" target="duplicated"></div>
						</div>
						<div class="form-group form-floating-label">
							<input type="text" class="form-control input-border-bottom item-field" id="manager_nm" required>
							<label for="manager_nm" class="placeholder">사용자 이름</label>
						</div>
						<div class="form-group form-floating-label">
							<select class="form-control input-border-bottom item-field" id="manager_grade" required></select>
							<label for="manager_grade" class="placeholder">사용자 등급</label>
						</div>
						<div class="form-group form-floating-label" role="libShow">
							<select class="form-control input-border-bottom item-field" id="library_key" name="libarary_select" required></select>
							<label for="library_key" class="placeholder">소속</label>
						</div>
						<div class="form-group form-floating-label">
							<input type="password" class="form-control input-border-bottom item-field" id="password" autocomplete="new-password" required>
							<label for="password" class="placeholder">사용자 비밀번호</label>
						</div>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="add">
					<i class="fas fa-user-plus"></i><span class="icon-title">등록</span>
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

<script type="text/javascript" src="/resources/js/main/system/modal/manager.js"></script>
