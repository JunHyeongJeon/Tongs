<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="utf-8">
        <title>tongs.kr | Login Page</title>
        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">
        <link href="/assets/plugins/jquery-ui/themes/base/minified/jquery-ui.min.css" rel="stylesheet">
        <link href="/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet">
        <link href="/assets/css/animate.min.css" rel="stylesheet">
        <link href="/assets/css/style.min.css" rel="stylesheet">
        <link href="/assets/css/style-responsive.min.css" rel="stylesheet">
        <link href="/assets/css/theme/default.css" rel="stylesheet" id="theme">
        <script src="assets/plugins/pace/pace.min.js"></script>
    </head>
<body class="pace-top bg-white">
    <div id="page-loader" class="fade in"><span class="spinner"></span></div>
    <div id="page-container" class="fade">
        <div class="login login-with-news-feed">
            <div class="news-feed">
                <div class="news-image">
                    <img src="/assets/img/login-bg/bg-7.jpg" data-id="login-cover-image" alt="">
                </div>
                <div class="news-caption">
                    <h4 class="caption-title"><i class="fa fa-diamond text-success"></i> Announcing the Color Admin app</h4>
                    <p>
                        Download the Color Admin app for iPhone®, iPad®, and Android™. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    </p>
                </div>
            </div>
            <div class="right-content">
                <div class="login-header">
                    <div class="brand">
                        Tongs.kr
                        <small>Tongs.kr Shop Admin Page</small>
                    </div>
                    <div class="icon">
                        <i class="fa fa-sign-in"></i>
                    </div>
                </div>
                <div class="login-content">
                    <form action="login_callback.php" method="POST" class="margin-bottom-0">
                        <div class="form-group m-b-15">
                            <input type="text" class="form-control input-lg" placeholder="Email Address" name="email">
                        </div>
                        <div class="form-group m-b-15">
                            <input type="password" class="form-control input-lg" placeholder="Password" name="password">
                        </div>
                        <div class="login-buttons">
                            <button type="submit" class="btn btn-success btn-block btn-lg">Sign me in</button>
                        </div>
                        <div class="m-t-20 m-b-40 p-b-40">
                            Not a member yet? Click <a href="register_v3.html" class="text-success">here</a> to register.
                        </div>
                        <hr>
                        <p class="text-center text-inverse">
                            &copy; Tongs.kr All Right Reserved 2015
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </div>
	
    <script src="/assets/plugins/jquery/jquery-1.9.1.min.js"></script>
    <script src="/assets/plugins/jquery/jquery-migrate-1.1.0.min.js"></script>
    <script src="/assets/plugins/jquery-ui/ui/minified/jquery-ui.min.js"></script>
    <script src="/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="/assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="/assets/plugins/jquery-cookie/jquery.cookie.js"></script>
    <script src="/assets/js/apps.min.js"></script>
	<script>
		$(document).ready(function() {
			App.init();
		});
	</script>
</body>
</html>
