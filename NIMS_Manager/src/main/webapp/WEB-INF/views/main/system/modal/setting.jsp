<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Setting Modal -->
<div class="modal fade" id="settingModal" role="modal" target="system" tabindex="-1"
	aria-labelledby="systemModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="settingModalLabel">시스템 정책</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<input type="hidden" class="item-field" id="rec_key">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label" style="display: flex">
								<input type="text" class="form-control input-border-bottom item-field" id="rule_id" required="">
								<label for="rule_id" class="placeholder">정책 번호</label>
								<div role="msg" target="duplicated"></div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="rule_nm" required="">
								<label for="rule_nm" class="placeholder">정책 내용</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<textarea class="form-control input-border-bottom bg-dark-fill-5 item-field" id="rule_desc"></textarea>
								<label for="rule_desc" class="placeholder">정책 상세 정보</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="option_1" required="">
								<label for="option_1" class="placeholder">정책 값 1</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="option_2" required="">
								<label for="option_2" class="placeholder">정책 값 2</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="option_3" required="">
								<label for="option_3" class="placeholder">정책 값 3</label>
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

<script type="text/javascript" src="/resources/js/main/system/modal/setting.js"></script>
