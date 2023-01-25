<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>이용 달력</h3>
						<div class="card-title-action">
							<button class="btn btn-round btn-primary mr-2" role="action" data-action="useSetting">
								<i class="fas fa-cog"></i><span class="icon-title">휴관일 사용 설정</span>
							</button>
							<button class="btn btn-round btn-warning mr-2" role="action" data-action="saveSetting">
								<i class="fas fa-save"></i><span class="icon-title">휴관일 저장</span>
							</button>
						</div>
					</div>
				</div>
				<div class="card-header search-header">
					<select id="filter-field" class="form-control"></select>
				</div>
				<div class="card-body">
					<div class="calendar"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" href="/resources/css/lib/fullcalendar/5.1/main.min.css">

<script src="/resources/js/lib/fullcalendar/5.1/main.min.js"></script>
<script src="/resources/js/lib/fullcalendar/5.1/locales-all.min.js"></script>
<script src="/resources/js/main/loanReturn/useCalendar.js"></script>