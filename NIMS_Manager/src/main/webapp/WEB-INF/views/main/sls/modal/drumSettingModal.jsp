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
									<input type="text" class="form-control input-border-bottom item-field" id="posistep" disabled>
									<label for="posistep" class="disabled-placeholder">서가 번호</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="use" required="">
									<label for="use" class="placeholder">사용 여부</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="volume" required="">
									<label for="volume" class="placeholder">서가당 선반 수</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="grade" required="">
									<label for="grade" class="placeholder">선반 타입</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="use_smartlib" required="">
									<label for="use_smartlib" class="placeholder">스마트도서관 사용 여부</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="use_reserve" required="">
									<label for="use_reserve" class="placeholder">예약 사용 여부</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="use_other_loc_return" required="">
									<label for="use_other_loc_return" class="placeholder">자관 도서 반납 허용</label>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group form-floating-label">
									<input type="text" class="form-control input-border-bottom item-field" id="use_other_lib_return" required="">
									<label for="use_other_lib_return" class="placeholder">타관 도서 반납 허용</label>
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
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/sls/modal/drumSettingModal.js"></script>
