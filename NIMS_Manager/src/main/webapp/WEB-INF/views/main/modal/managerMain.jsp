<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- manager Modal -->
<div class="modal fade" id="managerMainModal" role="modal" target="managerInfo" tabindex="-1"
	aria-labelledby="managerModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-medium modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="managerModalLabel">내 정보 관리</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        	<span aria-hidden="true">&times;</span>
		        </button>
			</div>
			<div class="modal-body">
				<div class="row ta-l">
					<div class="col-lg-12">
						<form>
						<div class="form-group">
							<label for="managerID">사용자 ID</label>
							<input type="text" class="form-control" id="managerID" disabled />
						</div>
						<div class="form-group">
							<label for="managerName">사용자 이름</label>
							<input type="text" class="form-control" id="managerName" autocomplete="username" disabled />
						</div>
						<div class="form-group">
							<label for="oldPWD">기존 비밀번호</label>
							<input type="password" class="form-control" id="oldPWD" autocomplete="current-password"/>
							<small id="invalidMsg" class="form-text text-muted"></small>
						</div>
						<div class="form-group">
							<label for="newPWD">신규 비밀번호</label>
							<input type="password" class="form-control" id="newPWD" autocomplete="new-password"/>
							<small id="invalidMsg" class="form-text text-muted"></small>
						</div>
						<div class="form-group">
							<label for="checkPWD">비밀번호 확인</label>
							<input type="password" class="form-control" id="checkPWD" autocomplete="new-password" />
							<small id="invalidMsg" class="form-text text-muted"></small>
						</div>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary btn-round mr-2" role="action" action="update">
					<div class="pen-container">
						<span class="pen">
							<span></span>
						</span>
						<i></i>
					</div>
					<span class="icon-title">변경</span>
				</button>
				<button class="btn btn-danger btn-round" role="action" action="cancel">
					<i class="fas fa-times"></i><span class="icon-title">취소</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/modal/managerMain.js"></script>

