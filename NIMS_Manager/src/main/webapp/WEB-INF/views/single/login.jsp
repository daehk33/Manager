<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="auth-wrapper">
	<div class="auth-box row">
		<div class="col-lg-7 col-md-5 modal-bg-img">
			<div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
				<div class="carousel-inner">
					<div class="carousel-item active">
						<img class="d-block" src="/resources/img/login/banner1.png">
					</div>
					<div class="carousel-item">
						<img class="d-block" src="/resources/img/login/banner2.png">
					</div>
				</div>
			</div>
			<ol class="carousel-indicators">
				<li data-target="#carouselExampleControls" data-slide-to="0"></li>
				<li data-target="#carouselExampleControls" data-slide-to="1"></li>
			</ol>
			<button class="carousel-control-prev" type="button" data-target="#carouselExampleControls" data-slide="prev">
				<span class="carousel-control-prev-icon" aria-hidden="true"></span>
				<span class="sr-only">Previous</span>
			</button>
			<button class="carousel-control-next" type="button" data-target="#carouselExampleControls" data-slide="next">
				<span class="carousel-control-next-icon" aria-hidden="true"></span>
				<span class="sr-only">Next</span>
			</button>
		</div>
		<img src="/resources/img/common/company.png" class="company-logo" onclick="window.open('http://www.enicom.co.kr/', '_blank')" height="25px">
		<div class="col-lg-5 col-md-7 bg-white">
			<div class="p-3">
				<div class="mt-4 text-center">
					<img src="/resources/img/login/login_logo.png" alt="wrapkit" height="120px">
				</div>
				<p class="text-center mt-4">관리자페이지에 접속하시려면<br> 먼저 로그인해주시기 바랍니다.</p>
				<form class="mt-4" id="loginForm">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group bmd-form-group">
								<label class="bmd-label-floating" for="managerID">아이디</label>
								<input type="text" class="form-control param" id="managerID" name="managerID" autocomplete="username" autofocus="autofocus">
								<div role="msg" target="managerID"></div>
							</div>
						</div>
						<div class="col-lg-12">
							<div class="form-group bmd-form-group">
								<label class="bmd-label-floating" for="managerPWD">비밀번호</label>
								<input type="password" class="form-control param" id="managerPWD" name="managerPWD" autocomplete="current-password">
								<div role="msg" target="managerPWD"></div>
							</div>
						</div>
						<div class="col-lg-12 text-center">
							<button class="btn btn-primary" role="action" data-action="login">로그인</button>
						</div>
						<div class="col-lg-12 text-center mt-4"></div>
						
						<input type="hidden" name="publicKeyModulus" value="${publicKeyModulus}" />
						<input type="hidden" name="publicKeyExponent" value="${publicKeyExponent}" />
					</div>
				</form>
				
				<div role="loading" class="display-flex justify-content-center flex-direction-column mt-4 mb-5 hide">
					<div class="mt-5 mb-4"></div>
					<div class="dot-spin dot-spin-carrot" style="align-self: center;"></div>
					<p class="text-center mt-3 py-3">로그인 중입니다.<br>잠시만 기다려주세요.</p>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" href="/resources/css/single/login.css">

<!-- RSA 암호화 -->
<script type="text/javascript" src="/resources/js/lib/rsa/jsbn.js"></script>
<script type="text/javascript" src="/resources/js/lib/rsa/rsa.js"></script>
<script type="text/javascript" src="/resources/js/lib/rsa/prng4.js"></script>
<script type="text/javascript" src="/resources/js/lib/rsa/rng.js"></script>

<script type="text/javascript" src="/resources/js/single/login.js"></script>

<script type="text/javascript">
if(${not empty sessionScope.MANAGER}){
	location.href = "/main";
}
</script>
