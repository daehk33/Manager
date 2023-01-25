<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Nicom Integrated Management System</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no"/>
	<link rel="icon" href="/resources/img/favicon/favicon.png" type="image/x-icon"/>
	
	<link rel="stylesheet" href="/resources/fonts/notokr/notokr.css">
	<link rel="stylesheet" href="/resources/css/lib/bootstrap/4.6.0/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="/resources/css/common/error.css" />
</head>
<body>
	<div class="content">
		<div class="meta">
			<div class="meta-title hidden">출입통제 관리자 홈페이지</div>
			<div class="meta-logo" role="library">
				<img src="/resources/img/common/404.png">
			</div>
			<h1>Page not found</h1>
			<div>
				<p>해당 페이지가 없거나, 권한이 없는 페이지 입니다.</p>
			</div>
			<button class="btn btn-danger" role="home">메인 페이지 바로가기</button>
		</div>
	</div>
	
	<script type="text/javascript">
		const button = document.querySelector('[role="home"]')
		button.addEventListener('click', function(){
			location.href = '/main';
		});
	</script>
</body>
</html>