<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>비치 도서 목록</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-warning mr-2" role="action" data-action="pickingOut">
							<span class="btn-label svg-container">
							<svg class="svg-icon" viewBox="0 0 8.47 8.47">
								<path fill="#fff" d="M3.52 1.37c-.15 0-.28.14-.28.3v3.65c.09 0 .17.01.26.03.9-.27 1.68-.3 1.68.13s-1.55.43-1.55.43c-.13.1-.26.18-.39.24v1.36c0 .17.13.3.28.3H8c.15 0 .27-.13.27-.3V1.68c0-.17-.12-.3-.27-.3zm.89.68h2.71v.43H4.41Zm0 .86h2.71v.43H4.41Z" paint-order="stroke fill markers"/>
								<path fill="none" stroke="#fff" stroke-linecap="round" stroke-linejoin="round" stroke-width=".26" d="M4.1 2.38s.02-.35.2-.43h2.98" transform="matrix(1.46544 0 0 1.62184 -2.57 -2.24)"/>
								<path fill="none" stroke="#fff" stroke-linecap="round" stroke-width=".26" d="M7.27 1.95c-.08.1-.07.4.01.48" transform="matrix(1.46544 0 0 1.62184 -2.57 -2.24)"/>
								<g fill="none" stroke="#fff" transform="matrix(1.46544 0 0 1.62184 -1.41 -2.67)">
									<path stroke-linecap="round" stroke-linejoin="round" stroke-width=".15" d="M1.8 5.3c.5.27.98.2 1.6 0 0 0 1.05 0 1.05-.26s-.53-.25-1.15-.08c-.41-.07-.9-.01-.9-.01"/>
									<path stroke-linecap="round" stroke-linejoin="round" stroke-width=".15" d="m1.82 4.44 1.09-.47.3.15"/>
									<rect width=".55" height="1.09" x="1.17" y="4.35" stroke-linecap="square" stroke-width=".24" paint-order="stroke fill markers" rx="0" ry="0"/>
								</g>
							</svg>
							</span>도서 배출
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-action="syncInfo">
							<span class="btn-label">
								<svg class="svg-icon" viewBox="0 0 12.7 12.7">
									<path class="arrow" d="M6.35 8.202l2.91-2.381M6.35 8.202L3.44 5.821m2.91-4.498v6.614" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"></path>
									<path class="holder" d="M1.058 10.224v1.852h10.584v-1.852" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"></path>
								</svg>
							</span>도서 정보 동기화
						</button>
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 권</div>
				</div>
				<div class="card-header search-header">
					<select id="filter-field" class="form-control"></select>
				</div>
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/sls/slsBookList.js"></script>