<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed"
						data-target="#searchCollapse" aria-controls="searchCollapse">
						<h3>캐비닛 설정</h3>
						<span class="caret"></span>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary" role="type">
							<i class="fas fa-child"></i>
							<span class="icon-title">캐비닛 타입 설정 (일반 / 어린이용)</span>
						</button>
					</div>
				</div>
				
				<div class="collapse show" id="searchCollapse">
					<div class="card-header search-header">
						<select id="filter-field" class="form-control"></select>
					</div>
				</div>
				
				<div class="card-body">
					<div class="legendContainer">
						<ul class="html-legend">
							<li>
								<span aria-type="nobook"></span> 사용 (도서 없음)
							</li>
							<li>
								<span aria-type="bookIn"></span> 사용 (도서 있음)
							</li>
							<li>
								<span aria-type="kids"></span> 어린이용
							</li>
							<li>
								<span aria-type="disabled"></span> 사용 안함
							</li>
							<li>
								<span aria-type="error"></span> 오류
							</li>
						</ul>
					</div>
					<div class="cabinet-container overflow-x-scroll">
						<div class="direction-container" role="1"></div>
						<div class="direction-container" role="0"></div>
						<div class="direction-container" role="2"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/resvLoan/cabinetSetting.js"></script>
