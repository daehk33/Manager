<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="slide-container" role="antiLost">
	<p class="slide-title">
		분실방지기
	</p>
	<div class="row">
		<div class="col-xl-5">
			<div class="row row-card-no-pd book-status-container">
				<div class="col-xl-6 col-sm-6">
					<div class="card card-stats card-round" data-card="antiLost-total-count">
						<div class="card-body">
							<div class="row align-items-center justify-content-center">
								<div class="col-icon">
									<div class="icon-big text-center icon-secondary bubble-shadow-small">
										<i class="fas fa-door-open"></i>
									</div>
								</div>
								<div class="col col-stats ml-3 ml-sm-0">
									<div class="numbers">
										<p class="card-category">카운트 횟수</p>
										<h4 class="card-title" role="count">0</h4> 회
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xl-6 col-sm-6">
					<div class="card card-stats card-round" data-card="antiLost-total-book">
						<div class="card-body">
							<div class="row align-items-center justify-content-center">
								<div class="col-icon">
									<div class="icon-big text-center icon-success bubble-shadow-small">
										<i class="fas fa-book-open"></i>
									</div>
								</div>
								<div class="col col-stats ml-3 ml-sm-0">
									<div class="numbers">
										<p class="card-category">도서 권수</p>
										<h4 class="card-title" role="count">0</h4> 권
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xl-12">
					<div class="card" role="card" data-card="danger-feed">
						<div class="card-header">
							<div class="card-title">
								<span class="feed-box-title">
									<i class="fas fa-bell" role="danger-feed-icon"></i>
								</span>
								<h4>장비 이벤트 알림</h4>
							</div>
							<span role="msg" target="danger-feed">0</span>
							<div class="card-title-action" style="float: right; margin-top: 5px;">
								<span role="show"><i class="fas fa-plus"></i></span>
							</div>
						</div>
						<div class="card-body card-body-danger-feed danger-feed">
							<div class="feed-box-content">
								<div class="no-content">
									<i class="fas fa-exclamation-circle"></i><br />
									<span>최근 장비 이벤트 알림이 없습니다.</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>장비 목록</h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="deviceList"><i class="fas fa-sync-alt"></i></span>
							</div>
							<ul class="status-legend">
								<li><span class="status-success"></span>정상</li>
								<li><span class="status-warning"></span>연결안됨</li>
								<li><span class="status-danger"></span>오류</li>
							</ul>
						</div>
						<div class="card-body card-body-device">
							<div class="row" id="deviceStatus" role="list"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>장비 이벤트 현황<span class="event-no">(총 <span role="msg" target="device-event-count"></span>건)</span></h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="eventStatus"><i class="fas fa-sync-alt"></i></span>
								
								<ul class="nav nav-pills nav-secondary title-pills" id="pills-tab-type" role="event" style="float: right">
									<li class="nav-item submenu">
										<a class="nav-link" role="type" type="today">금일</a>
									</li>
									<li class="nav-item submenu">
										<a class="nav-link" role="type" type="week">주간</a>
									</li>
									<li class="nav-item submenu">
										<a class="nav-link active" role="type" type="month">월간</a>
									</li>
								</ul>
							</div>
						</div>
						<div class="card-body card-body-event-status">
							<div class="row" id="chartEventStatus" role="list">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xl-7">
			<div class="row">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>금일 현황</h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="todayStatistics"><i class="fas fa-sync-alt"></i></span>
							</div>
							<ul class="status-legend">
								<li><span class="status-book"></span>카운트 횟수</li>
								<li><span class="status-user"></span>부정도서 권수</li>
							</ul>
						</div>
						<div class="card-body card-body-statistic" target="today">
							<div class="chart-container"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>주간 현황</h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="weekStatistics"><i class="fas fa-sync-alt"></i></span>
								<div role="week" id="chartLegend"></div>
							</div>
						</div>
						<div class="card-body card-body-statistic" target="week"></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>시간별 현황</h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="timeStatistics"><i class="fas fa-sync-alt"></i></span>
								<div role="time" id="chartLegend"></div>
							</div>
						</div>
						<div class="card-body card-body-statistic" target="time"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/slide/antiLost.js"></script>