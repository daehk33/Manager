<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>선반 설정</h3>
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
								<span></span> 빈 선반
							</li>
							<li>
								<span aria-type="use"></span> 사용중 선반
							</li>
							<li>
								<span aria-type="disabled"></span> 비활성화 선반
							</li>
						</ul>
					</div>
					<div class="slot-container"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/sls/slotSetting.js"></script>
