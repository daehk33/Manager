<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Schedule Modal -->
<div class="modal fade" id="scheduleModal" role="modal" target="schedule" tabindex="-1"
	aria-labelledby="scheduleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="scheduleModalLabel">스케줄 추가</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="library_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="libray_key" class="placeholder">도서관명</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="equip_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="equip_key" class="placeholder">장비 아이디</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="date" required="">
									<option value="">&nbsp;</option>
									<option value="ALL">매일</option>
									<option value="0">일</option>
									<option value="1">월</option>
									<option value="2">화</option>
									<option value="3">수</option>
									<option value="4">목</option>
									<option value="5">금</option>
									<option value="6">토</option>
								</select>
								<label for="date" class="placeholder">요일</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<input type="text" class="form-control input-border-bottom item-field" id="time" required="">
								<label for="time" class="placeholder">시간</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="operation_type" required="">
									<option value="" selected>&nbsp;</option>
								</select>
								<label for="operation_type" class="placeholder">제어 설정</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="use_yn" required="">
										<option value="Y">사용</option>
										<option value="N">미사용</option>
								</select>
								<label for="use_yn" class="placeholder">사용 여부</label>
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
				<button class="btn btn-round btn-danger mr-2" role="action" action="delete">
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

<script type="text/javascript" src="/resources/js/main/gate/modal/scheduleModal.js"></script>
