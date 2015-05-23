<?php
	function active($pos,$type){
		if(strpos($_SERVER["PHP_SELF"],$pos)!==FALSE){
			if($type)
				return " class=\"active\"";
			else
				return " active";
		}
	}
?>
<!doctype HTML>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>tongs.kr</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
		<link href="//fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">
		<link href="/assets/plugins/jquery-ui/themes/base/minified/jquery-ui.min.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
		<link href="/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="/assets/css/animate.min.css" rel="stylesheet">
		<link href="/assets/css/style.min.css" rel="stylesheet">
		<link href="/assets/css/style-responsive.min.css" rel="stylesheet">
		<link href="/assets/css/theme/orange.css" rel="stylesheet" id="theme">
		<link href="/assets/plugins/jquery-jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap-datepicker/css/datepicker.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">
		<link href="/assets/plugins/gritter/css/jquery.gritter.css" rel="stylesheet">
		<script src="/assets/plugins/pace/pace.min.js"></script>
	</head>
	<body>
		<div id="page-loader" class="fade in"><span class="spinner"></span></div>
		<div id="page-container" class="fade page-header-fixed">
			<div id="header" class="header navbar navbar-default navbar-fixed-top">
				<div class="container-fluid">
					<div class="navbar-header">
						<a href="javascript:;" onclick="location.href='/admin';" class="navbar-brand">tongs.kr</a>
						<button type="button" class="navbar-toggle" data-click="sidebar-toggled">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
					</div>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown navbar-user">
							<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
								<span class="hidden-xs"><?=$_SESSION["user"]["email"]?></span> <b class="caret"></b>
							</a>
							<ul class="dropdown-menu animated fadeInLeft">
								<li class="arrow"></li>
								<li><a href="javascript:;" onclick="location.href='/admin/logout.php';">로그아웃</a></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<div id="sidebar" class="sidebar">
				<div data-scrollbar="ture" data-height="100%">
					<ul class="nav">
						<li class="nav-profile">
							<div class="info">
								<?=$_SESSION["user"]["name"]?>
								<small><?=$_SESSION["user"]["location"]?></small>
							</div>
						</li>
					</ul>
					<ul class="nav">
						<li class="nav-header">메뉴</li>
						<li class="has-sub<?=active("store_",false)?>">
							<a href="javascript:;">
								<i class="fa fa-shopping-cart"></i>
								<b class="caret pull-right"></b>
								<span>매장관리</span>
							</a>
							<ul class="sub-menu">
								<li<?=active("store_view",true)?>>
									<a href="/admin/store_view.php">매장정보 조회</a>
								</li>
								<li<?=active("store_edit",true)?>>
									<a href="/admin/store_edit.php">매장정보 수정</a>
								</li>
							</ul>
						</li>
						<li class="has-sub<?=active("coupon_",false)?>">
							<a href="javascript:;">
								<i class="fa fa-ticket"></i>
								<b class="caret pull-right"></b>
								<span>쿠폰관리</span>
							</a>
							<ul class="sub-menu">
								<li<?=active("coupon_view",true)?>>
									<a href="/admin/coupon_view.php">쿠폰정보 조회</a>
								</li>
								<li<?=active("coupon_register",true)?>>
									<a href="/admin/coupon_register.php">쿠폰정보 등록</a>
								</li>
								<li<?=active("coupon_delete",true)?>>
									<a href="/admin/coupon_delete.php">쿠폰정보 삭제</a>
								</li>
								<li<?=active("coupon_list",true)?>>
									<a href="/admin/coupon_list.php">사용내역 조회</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<div class="sidebar-bg"></div>
			<div id="content" class="content">
				<?=$content?>
			</div>
		</div>
		
		<script src="/assets/plugins/jquery/jquery-1.9.1.min.js"></script>
		<script src="/assets/plugins/jquery/jquery-migrate-1.1.0.min.js"></script>
		<script src="/assets/plugins/jquery-ui/ui/minified/jquery-ui.min.js"></script>
		<script src="/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
		<script src="/assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
		<script src="/assets/plugins/jquery-cookie/jquery.cookie.js"></script>
		
		<!-- <script src="/assets/plugins/gritter/js/jquery.gritter.js"></script> -->
		<!-- <script src="/assets/plugins/flot/jquery.flot.min.js"></script>
		<script src="/assets/plugins/flot/jquery.flot.time.min.js"></script>
		<script src="/assets/plugins/flot/jquery.flot.resize.min.js"></script>
		<script src="/assets/plugins/flot/jquery.flot.pie.min.js"></script> -->
		<!-- <script src="/assets/plugins/sparkline/jquery.sparkline.js"></script>
		<script src="/assets/plugins/jquery-jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
		<script src="/assets/plugins/jquery-jvectormap/jquery-jvectormap-world-mill-en.js"></script>
		<script src="/assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
		<script src="/assets/js/dashboard.min.js"></script> -->
		<script src="/assets/js/apps.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				App.init();
			});
		</script>
		<?=$script?>
	</body>
</html>