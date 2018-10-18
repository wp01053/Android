<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>NaverLoginSDK Test with BootStrap</title>
<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
	integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
	crossorigin="anonymous">

<style type="text/css">
.header, body {
	padding-bottom: 20px
}

.header, .jumbotron {
	border-bottom: 1px solid #e5e5e5
}

body {
	padding-top: 20px
}

.footer, .header, .marketing {
	padding-right: 15px;
	padding-left: 15px
}

.header h3 {
	margin-top: 0;
	margin-bottom: 0;
	line-height: 40px
}

.footer {
	padding-top: 19px;
	color: #777;
	border-top: 1px solid #e5e5e5
}

@media ( min-width :768px) {
	.container {
		max-width: 730px
	}
}

.container-narrow>hr {
	margin: 30px 0
}

.jumbotron {
	text-align: center
}

.jumbotron .btn {
	padding: 14px 24px;
	font-size: 21px
}

.marketing {
	margin: 40px 0
}

.marketing p+h4 {
	margin-top: 28px
}

@media screen and (min-width:768px) {
	.footer, .header, .marketing {
		padding-right: 0;
		padding-left: 0
	}
	.header {
		margin-bottom: 30px
	}
	.jumbotron {
		border-bottom: 0
	}
}
</style>

</head>
<body>

	<div class="container">
		<div class="header clearfix">
			<nav>
				<ul class="nav nav-pills pull-right">
					<li role="presentation" class="active"><a href="#">Home</a></li>
					<li role="presentation"><a id="gnbLogin" href="#">Login</a></li>
				</ul>
			</nav>
			<h3 class="text-muted">Login With NaverID Javascript SDK</h3>
		</div>

		<div id="naverIdLogin">
			<a id="naverIdLogin_loginButton" href="#" role="button"><img
				src="https://static.nid.naver.com/oauth/big_g.PNG" width=320></a>
		</div>

	</div>


	<!-- /container -->
	<script src="https://code.jquery.com/jquery-1.12.1.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<!-- (2) LoginWithNaverId Javscript SDK -->
	<script src="naveridlogin_js_sdk_2.0.0.js"></script>

	<!-- (3) LoginWithNaverId Javscript 설정 정보 및 초기화 -->
	<script>
		var naverLogin = new naver.LoginWithNaverId({
			clientId : "SlBYxQBBquA7PzjnZ4fg",
			callbackUrl : "http://localhost:8081/NaverLogin/login.jsp",
			isPopup : false,
			loginButton : {
				color : "green",
				type : 3,
				height : 60
			}
		});
		/* (4) 네아로 로그인 정보를 초기화하기 위하여 init을 호출 */
		naverLogin.init();

		/* (4-1) 임의의 링크를 설정해줄 필요가 있는 경우 */
		$("#gnbLogin").attr("href", naverLogin.generateAuthorizeUrl());

		/* (5) 현재 로그인 상태를 확인 */
		window.addEventListener('load', function() {
			naverLogin.getLoginStatus(function(status) {
				if (status) {
					/* (6) 로그인 상태가 "true" 인 경우 로그인 버튼을 없애고
					   사용자 정보를 출력합니다. */
					setLoginStatus();
				}
			});
		});

		/* (6) 로그인 상태가 "true" 인 경우 로그인 버튼을 없애고
		   사용자 정보를 출력합니다. */
		function setLoginStatus() {
			console.log(naverLogin.user);
			var uid = naverLogin.user.getId();
			var profileImage = naverLogin.user.getProfileImage();
			var uName = naverLogin.user.getName();
			var nickName = naverLogin.user.getNickName();
			var eMail = naverLogin.user.getEmail();
			$("#naverIdLogin_loginButton").html(
					'<br><br><img src="' + profileImage + 
					'" height=50 /> <p>'
							+ uid + "-" + uName + '님 반갑습니다.</p>');
			$("#gnbLogin").html("Logout");
			$("#gnbLogin").attr("href", "#");
			/* (7) 로그아웃 버튼을 설정하고 동작을 정의합니다. */
			$("#gnbLogin").click(function() {
				naverLogin.logout();
				location.reload();
			});
		}
	</script>


</body>
</html>

