<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	if(${not empty sessionScope.MANAGER}){
		//사용자 명 표시 추가 필요
	}
	else {
		Alert.danger({text: '로그인이 필요한 서비스입니다.'}, () => {
			location.href = '/login';
		})
	}
	
	sessionStorage.setItem('library_key', '${sessionScope.MANAGER.library_key}');
	sessionStorage.setItem('library_nm', '${sessionScope.MANAGER.library_nm}');
	
	const sessionInvalidateTime = '${sessionScope.TIME}' == '' ? '' : '${sessionScope.TIME}';
</script>

<button class="btn btn-secondary btn-round btn-icon btns btn-back" title="메인 메뉴로 이동" data-show="false">
	<i class="fas fa-arrow-left"></i>
</button>

<%@include file="/WEB-INF/views/main/main.jsp" %>

<link rel="stylesheet" href="/resources/css/single/dashboard.css">

<script src="/resources/js/single/dashboard.js"></script>