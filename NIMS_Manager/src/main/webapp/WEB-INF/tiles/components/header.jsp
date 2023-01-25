<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="style" scope="session" value="${sessionScope.THEME == 'dark' ? 'data-background-color=\"dark\"' : ''}" />

<script type="text/javascript">
	if(${not empty sessionScope.MANAGER}){
		// 사용자 명 표시 추가 필요
		if('${sessionScope.MANAGER.manager_grade}' != '0'){
			sessionStorage.setItem('library_key', '${sessionScope.MANAGER.library_key}');
			sessionStorage.setItem('library_nm', '${sessionScope.MANAGER.library_nm}');
		}
		else {
			if(sessionStorage.getItem('library_key') === null)
				sessionStorage.setItem('library_key', '');
		}
		sessionStorage.setItem('slibrary_yn', '${sessionScope.MANAGER.slibrary_yn}');
		sessionStorage.setItem('loanReturn_yn', '${sessionScope.MANAGER.loanReturn_yn}');
		sessionStorage.setItem('return_yn', '${sessionScope.MANAGER.return_yn}');
		sessionStorage.setItem('resvLoan_yn', '${sessionScope.MANAGER.resvLoan_yn}');
		sessionStorage.setItem('gate_yn', '${sessionScope.MANAGER.gate_yn}');
	}
	else {
		Alert.danger({text: '로그인이 필요한 서비스입니다.'}, () => {
			SessionStorage.clear();
			location.href = '/login';
		});
	}
</script>
<body ${style}>
<div class="wrapper">
	<div class="main-header">
		<!-- Logo Header -->
		<div class="logo-header">
			
			<a href="/main" class="logo">
				<img src="/resources/img/favicon/logo.png" height="50px" alt="navbar brand" class="navbar-brand">
			</a>
			<button class="navbar-toggler sidenav-toggler ml-auto" type="button" data-toggle="collapse" data-target="collapse" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon">
					<i class="icon-menu"></i>
				</span>
			</button>
			<button class="topbar-toggler more"><i class="icon-options-vertical"></i></button>
			<div class="nav-toggle filter-invert">
				<button class="btn btn-toggle toggle-sidebar">
					<i class="icon-menu"></i>
				</button>
			</div>
		</div>
		<!-- End Logo Header -->
		
		<!-- Navbar Header -->
		<nav class="navbar navbar-header navbar-expand-lg">
		
			<div class="container-fluid" role="header" type="container">
				<div class="navbar-nav topbar-nav">
					<div class="page-title-container">
						<font class="page-title"></font>
					</div>
				</div>
				<ul class="navbar-nav topbar-nav ml-md-auto align-items-center">
					<div role="expired">
						<span class="header-timer mr-3" id="timer"></span>
						<button class="btn" role="extention">+ 연장</button>
					</div>
					<!-- Profile 영역 -->
					<li class="nav-item dropdown hidden-caret">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
							<span class="header-profile">
							</span>
						</a>
						<ul class="dropdown-menu dropdown-user animated fadeIn">
							<div class="dropdown-user-scroll scrollbar-outer">
								<li>
									<div class="user-box">
										<div class="avatar-lg"><img src="/resources/img/profile.png" alt="image profile" class="avatar-img rounded"></div>
										<div class="u-text">
											<h4 role="manager" type="name"></h4>
											<a role="manager" type="modal" class="btn btn-xs btn-secondary btn-sm" 
												style="padding: .3rem .5rem!important; margin-top: 4px;">정보 관리</a>
										</div>
									</div>
								</li>
								<li>
									<div class="dropdown-divider"></div>
									<a class="dropdown-item" role="logout">로그아웃</a>
								</li>
							</div>
						</ul>
					</li>
					<!-- Profile 버튼 영역 끝 -->
				</ul>
			</div>
		</nav>
		<!-- End Navbar -->
	</div>
	<div class="loader-container">
		<div class="loader loader-lg"></div>
	</div>
	
<script src="/resources/js/common/header.js"></script>

<%@include file="/WEB-INF/views/main/modal/managerMain.jsp"  %>