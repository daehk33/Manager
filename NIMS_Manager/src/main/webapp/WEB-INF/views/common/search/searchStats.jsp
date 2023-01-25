<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<div class="selectgroup selectgroup-secondary selectgroup-pills">
			<label class="selectgroup-item">
				<input type="radio" name="type" value="day" class="selectgroup-input" checked>
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-star"></i><span class="icon-title">월별</span>
				</span>
			</label>
			<label class="selectgroup-item">
				<input type="radio" name="type" value="month" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-moon"></i><span class="icon-title">연별</span>
				</span>
			</label>
			<label class="selectgroup-item">
				<input type="radio" name="type" value="all" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-clock"></i><span class="icon-title">기간</span>
				</span>
			</label>
		</div>
		<span role="type">
		</span>
		<div class="form-group form-floating-label hide" id="filter-device">
			<select class="form-control input-border-bottom" id="select_device">
				<option value="">전체</option>
			</select>
			<label for="select_device" class="placeholder">장비명</label>
		</div>
		
		<button class="btn btn-round" role="search">
			<i class="fa fa-search"></i><span class="icon-title">검색</span>
		</button>
		
		<ul class="nav nav-pills nav-secondary title-pills" id="pills-tab-without-border" style="float: right; display: none">
			<li class="nav-item submenu">
				<a class="nav-link active show" id="stats-grid-tab" data-toggle="pill" href="#stats-grid"
					aria-controls="stats-user" aria-selected="true">
					<svg class="svg-icon" viewBox="0 0 12.7 12.7">
						<g transform="translate(-1.323 -1.354) scale(1.17647)">
							<path class="list-background" fill="#556080" fill-rule="evenodd" stroke="#556080" stroke-width="1.141" stroke-linecap="round" stroke-linejoin="round" d="M1.789 2.634h9.475v7.918H1.789z"/>
							<ellipse class="circle-1" cx="3.508" cy="4.577" rx=".509" ry=".528" fill="#fff" fill-rule="evenodd"/>
							<ellipse class="circle-2" cx="3.508" cy="6.688" rx=".509" ry=".528" fill="#fff" fill-rule="evenodd"/>
							<ellipse class="circle-3" cx="3.508" cy="8.799" rx=".509" ry=".528" fill="#fff" fill-rule="evenodd"/>
							<path class="bar-1"  d="M5.064 4.575h4.831" fill="none" stroke="#fff" stroke-width="1.057"/>
							<path class="bar-2" d="M5.068 6.689H9.9" fill="none" stroke="#fff" stroke-width="1.057"/>
							<path class="bar-3" d="M5.062 8.797h4.831" fill="none" stroke="#fff" stroke-width="1.057"/>
						</g>
					</svg>
				</a>
			</li>
			<li class="nav-item submenu">
				<a class="nav-link" id="stats-chart-tab" data-toggle="pill" href="#stats-chart"
					aria-controls="stats-chart" aria-selected="false">
					<svg class="svg-icon" viewBox="0 0 12.7 12.7">
						<g class="chart-group" fill="none" stroke="#556080">
							<path d="M1.058 3.44v8.202h11.377" stroke-width="1.5"/>
							<path class="bar-1" d="M3.175 10.054V8.731" stroke-width="1.2"/>
							<path class="bar-2" d="M5.292 10.054V4.498" stroke-width="1.2"/>
							<path class="bar-3" d="M7.408 10.054V6.085" stroke-width="1.2"/>
							<path class="bar-4" d="M9.525 10.054V3.575" stroke-width="1.2"/>
							<path class="bar-5" d="M11.642 10.054V7.408" stroke-width="1.2"/>
						</g>
					</svg>
				</a>
			</li>
		</ul>
	</div>
</div>
<script src="/resources/js/common/search/searchStats.js"></script>