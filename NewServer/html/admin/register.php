<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title>tongs.kr | Register Page</title>
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
	<link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">
	<link href="/assets/plugins/jquery-ui/themes/base/minified/jquery-ui.min.css" rel="stylesheet" />
	<link href="/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link href="/assets/css/animate.min.css" rel="stylesheet" />
	<link href="/assets/css/style.min.css" rel="stylesheet" />
	<link href="/assets/css/style-responsive.min.css" rel="stylesheet" />
	<link href="/assets/css/theme/default.css" rel="stylesheet" id="theme" />
	<script src="/assets/plugins/pace/pace.min.js"></script>
</head>
<body class="pace-top bg-white">
	<div id="page-loader" class="fade in"><span class="spinner"></span></div>
	<div id="page-container" class="fade">
	    <div class="register register-with-news-feed">
            <div class="news-feed">
                <div class="news-image">
                    <img src="/assets/img/login-bg/bg-8.jpg" alt="" />
                </div>
                <div class="news-caption">
                    <h4 class="caption-title"><i class="fa fa-edit text-success"></i> Announcing the Color Admin app</h4>
                    <p>
                        As a Color Admin Apps administrator, you use the Color Admin console to manage your organization’s account, such as add new users, manage security settings, and turn on the services you want your team to access.
                    </p>
                </div>
            </div>
            <div class="right-content">
                <h1 class="register-header">
                    Sign Up
                    <small>Create your Tongs.kr Account. It’s free and always will be.</small>
                </h1>
                <div class="register-content">
                    <form action="http://tongs.kr/api/store/auth/register/" method="POST" class="margin-bottom-0">
                        <label class="control-label">Email</label>
                        <div class="row m-b-15">
                            <div class="col-md-12">
                                <input type="text" name="email" class="form-control" placeholder="Email address" />
                            </div>
                        </div>
                        <label class="control-label">Password</label>
                        <div class="row m-b-15">
                            <div class="col-md-12">
                                <input type="password" name="password" class="form-control" placeholder="Password" />
                            </div>
                        </div>
                        <label class="control-label">Shop Name</label>
                        <div class="row m-b-15">
                            <div class="col-md-12">
                                <input type="text" name="name" class="form-control" placeholder="Shop Name" />
                            </div>
                        </div>
                        <label class="control-label">Shop Location</label>
                        <div class="row m-b-15">
                            <div class="col-md-12">
                                <input type="text" name="location" class="form-control" placeholder="Shop Location" />
                            </div>
                        </div>
                        <label class="control-label">Shop Description</label>
                        <div class="row m-b-15">
                            <div class="col-md-12">
                                <input type="text" name="description" class="form-control" placeholder="Shop Description" />
                            </div>
                        </div>
                        
                        <input type="hidden" name="tool" value="1">
                        
                        <!--<div class="checkbox m-b-30">
                            <label>
                                <input type="checkbox" /> By clicking Sign Up, you agree to our <a href="#">Terms</a> and that you have read our <a href="#">Data Policy</a>, including our <a href="#">Cookie Use</a>.
                            </label>
                        </div>-->
                        <div class="register-buttons">
                            <button type="submit" class="btn btn-primary btn-block btn-lg">Sign Up</button>
                        </div>
                        <div class="m-t-20 m-b-40 p-b-40">
                            Already a member? Click <a href="login.php">here</a> to login.
                        </div>
                        <hr />
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
