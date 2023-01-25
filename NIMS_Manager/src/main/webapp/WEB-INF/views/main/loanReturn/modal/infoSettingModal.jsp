<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="infoModal" role="modal" tabindex="-1"
	aria-labelledby="useInfoModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="infoModalLabel">이용안내 설정</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="infoForm">
					<div class="form-group bmd-form-group">
						<label for="time" class="bmd-label-floating">이용 시간</label>
						<input type="text" class="form-control param" id="time" name="time">
					</div>
					<div class="form-group bmd-form-group">
						<label for="loan_cnt" class="bmd-label-floating">대출가능 권수</label>
						<input type="text" class="form-control param" id="loancnt" name="loan_cnt">
					</div>
					<div class="form-group bmd-form-group">
						<label for="loan_date" class="bmd-label-floating">대출기간</label>
						<input type="text" class="form-control param" id="loandate" name="loan_date">
					</div>
					<div class="form-group bmd-form-group">
						<label for="type" class="bmd-label">공휴일 포함여부 (대출기간)</label>
						<select class="form-control param" id="type" name="type">
							<option value="0">주말, 공휴일 제외</option>
							<option value="1">주말, 공휴일 포함</option>
							<option value="2">공휴일 제외</option>
							<option value="3">직접 설정</option>
						</select>
					</div>
					<div class="form-group bmd-form-group hide">
						<label for="returnplandate" class="bmd-label">반납 예정일</label>
						<input type="date" class="form-control param" id="returnplandate" name="return_plan_date">
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round btn-update" role="action" data-action="update">
					<div class="pen-container">
						<span class="pen"><span></span></span>
						<i></i>
					</div>
					<span class="icon-title">설정</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/loanReturn/modal/infoSettingModal.js"></script>