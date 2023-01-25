<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>요일별 통계</h3>
					</div>
				</div>
				<div class="card-header search-header">
					<select id="filter-field" class="form-control"></select>
				</div>
				<div class="card-body">
					<div class="legendContainer" role="calendar">
						<ul class="html-legend">
							<li>
								<span aria-type="loan"></span> 대출
							</li>
						</ul>
					</div>
					<div class="calendar"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" href="/resources/css/lib/fullcalendar/5.1/main.min.css">

<script src="/resources/js/lib/fullcalendar/5.1/main.min.js"></script>
<script src="/resources/js/lib/fullcalendar/5.1/locales-all.min.js"></script>
<script src="/resources/js/main/resvLoan/useCalendar.js"></script>