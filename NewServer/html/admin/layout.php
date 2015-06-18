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
		<link href="/assets/plugins/jquery-file-upload/blueimp-gallery/blueimp-gallery.min.css" rel="stylesheet">
		<link href="/assets/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet">
		<link href="/assets/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap3-editable/inputs-ext/address/address.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap3-editable/inputs-ext/typeaheadjs/lib/typeahead.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap-datetimepicker/css/datetimepicker.css" rel="stylesheet">
		<link href="/assets/plugins/select2/select2.css" rel="stylesheet">
		<link href="/assets/plugins/bootstrap-wysihtml5/src/bootstrap-wysihtml5.css" rel="stylesheet">
		<script src="/assets/plugins/pace/pace.min.js"></script>
	</head>
	<body>
		<div id="page-loader" class="fade in"><span class="spinner"></span></div>
		<div id="page-container" class="fade page-sidebar-fixed page-header-fixed">
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
					<!-- <ul class="nav navbar-nav navbar-right">
						<li class="dropdown navbar-user">
							<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
								<span class="hidden-xs"><?=$_SESSION["user"]["email"]?></span> <b class="caret"></b>
							</a>
							<ul class="dropdown-menu animated fadeInLeft">
								<li class="arrow"></li>
								<li><a href="javascript:;" onclick="location.href='/admin/logout.php';">로그아웃</a></li>
							</ul>
						</li>
					</ul> -->
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
								<li<?=active("store_option",true)?>>
									<a href="/admin/store_option.php">매장옵션 설정</a>
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
								<li<?=active("coupon_print",true)?>>
									<a href="/admin/coupon_print.php">개인쿠폰 발급</a>
								</li>
								<li<?=active("coupon_group",true)?>>
									<a href="/admin/coupon_group.php">그룹쿠폰 발급</a>
								</li>
								<!--<li<?=active("coupon_list",true)?>>
									<a href="/admin/coupon_list.php">사용내역 조회</a>
								</li>-->
							</ul>
						</li>
						<li class="has-sub<?=active("list_",false)?>">
							<a href="javascript:;">
								<i class="fa fa-list-ol"></i>
								<b class="caret pull-right"></b>
								<span>대기열관리</span>
							</a>
							<ul class="sub-menu">
								<li<?=active("list_realtime",true)?>>
									<a href="/admin/list_realtime.php">실시간 대기열</a>
								</li>
							</ul>
						</li>
						<li><a href="/admin/logout.php"><i class="fa fa-sign-out"></i> <span>로그아웃</span></a></li>
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
		<script src="/assets/plugins/superbox/js/superbox.js"></script>
		<script src="/assets/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/vendor/tmpl.min.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/vendor/load-image.min.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/vendor/canvas-to-blob.min.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/blueimp-gallery/jquery.blueimp-gallery.min.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.iframe-transport.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-process.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-image.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-audio.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-video.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
	    <script src="/assets/plugins/jquery-file-upload/js/jquery.fileupload-ui.js"></script>
	    <script src="/assets/plugins/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
		<script src="/assets/plugins/bootstrap3-editable/inputs-ext/address/address.js"></script>
		<script src="/assets/plugins/bootstrap3-editable/inputs-ext/typeaheadjs/lib/typeahead.js"></script>
		<script src="/assets/plugins/bootstrap3-editable/inputs-ext/typeaheadjs/typeaheadjs.js"></script>
		<script src="/assets/plugins/bootstrap3-editable/inputs-ext/wysihtml5/wysihtml5.js"></script>
		<script src="/assets/plugins/bootstrap-wysihtml5/lib/js/wysihtml5-0.3.0.js"></script>
		<script src="/assets/plugins/bootstrap-wysihtml5/src/bootstrap-wysihtml5.js"></script>
		<script src="/assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
		<script src="/assets/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
		<script src="/assets/plugins/select2/select2.min.js"></script>
		<script src="/assets/plugins/mockjax/jquery.mockjax.js"></script>
		<script src="/assets/plugins/moment/moment.min.js"></script>
		
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