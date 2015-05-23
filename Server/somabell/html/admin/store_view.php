<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	$detail = json_decode($_SESSION["data"]);

	ob_start();
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">매장정보 조회</h4>
	</div>
	<div class="panel-body">
		<div>
			<label class="col-md-3 control-label">
				매장 이름
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["name"]?>
			</div>
		</div>
		<div>
			<label class="col-md-3 control-label">
				매장 위치
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["location"]?>
			</div>
		</div>
		<div>
			<label class="col-md-3 control-label">
				매장 소개
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["description"]?>
			</div>
		</div>
		
		
	</div>
</div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>

<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>