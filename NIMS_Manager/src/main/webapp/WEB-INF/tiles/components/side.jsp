<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Sidebar -->
<c:set var="theme" value="${sessionScope.THEME}"></c:set>
<c:set var="manager" value="${sessionScope.MANAGER}"></c:set>
<script type="text/javascript">
	const grade = '${manager.manager_grade}';
	const grade_nm = '${manager.manager_grade_nm}';
	const model_auth = '${manager.model_auth}';
	const manager_key = '${manager.rec_key}';
	const manager_id = '${manager.manager_id}';
	const manager_nm = '${manager.manager_nm}';
	const manager_library_key = '${manager.library_key}';
	let theme = '${theme}';
</script>

<div class="sidebar sidebar-style-2">
	<div class="sidebar-wrapper scrollbar scrollbar-inner">
		<div class="sidebar-content" role="sidebar">
			<div class="user">
				<div class="avatar-sm float-left mr-2">
					<img alt="" src="/resources/img/profile.png" class="rounded-circle avatar-img">
				</div>
				<div class="info">
					<a>
						<span role="profile">
							<span role="manager" type="name"></span>
							<span class="user-level" role="manager" type="grade"></span>
						</span>
					</a>
				</div>
				<div id="theme">
					<div class="mt-3"></div>
					<div class="selectgroup selectgroup-secondary selectgroup-pills">
						<span class="info-title"><i class="fas fa-palette"></i></span>
						<label class="selectgroup-item">
							<input type="radio" name="theme" value="white" class="selectgroup-input">
							<span class="selectgroup-button selectgroup-button-icon">white</span>
						</label>
						<label class="selectgroup-item">
							<input type="radio" name="theme" value="dark" class="selectgroup-input">
							<span class="selectgroup-button selectgroup-button-icon">dark</span>
						</label>
					</div>
				</div>
			</div>

			<!-- 메뉴바 -->
			<ul class="nav nav-primary" role="nav">
				<li class="nav-item active mb-4" role="menu" type="main">
					<a href="/main" role="link" data-category="main">
						<i class="fas fa-home"></i>
						<p>대시보드</p>
					</a>
				</li>
			</ul>
		</div>
	</div>
</div>
<!-- End Sidebar -->

<script src="/resources/js/common/side.js"></script>