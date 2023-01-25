<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- CodeGroup Modal -->
<div class="modal fade" id="settingModal" role="modal" target="moduleSetting" tabindex="-1"
	aria-labelledby="settingModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="settingModalLabel">moduleSetting Modal</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="row" role="common">
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
								<select class="form-control input-border-bottom item-field" id="device_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="device_key" class="placeholder">장비명</label>
							</div>
						</div>
					</div>
					<div class="row" role="module">
						<div class="col-lg-6">
							<div class="form-group form-floating-label" style="display: flex">
								<input type="text" class="form-control input-border-bottom item-field" id="module_id" required="">
								<label for="module_id" class="placeholder">모듈 아이디</label>
								<div role="msg" target="duplicated"></div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="direction" required="">
									<option value="">&nbsp;</option>
									<option value="0">중앙</option>
									<option value="1">좌측</option>
									<option value="2">우측</option>
								</select>
								<label for="direction" class="placeholder">모듈 위치</label>
							</div>
						</div>
					</div>
					<div class="row" role="cabinet">
						<div class="col-lg-4">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="cabinet_no" required="">
								<label for="cabinet_no" class="placeholder">캐비닛 시작 번호</label>
							</div>
						</div>
						<div class="col-lg-4">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="column_cnt" required="">
								<label for="column_cnt" class="placeholder">행</label>
							</div>
						</div>
						<div class="col-lg-4">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="row_cnt" required="">
								<label for="row_cnt" class="placeholder">열</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="add">
					<i class="fas fa-plus"></i><span class="icon-title">등록</span>
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

<script type="text/javascript" src="/resources/js/main/resvLoan/modal/moduleSettingModal.js"></script>
