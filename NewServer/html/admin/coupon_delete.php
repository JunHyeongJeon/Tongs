<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	include("mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `id`={$_SESSION["user"]["id"]}";
	$_SESSION["user"] = mysqli_fetch_array($mysql->query($sql));
	
	$detail = json_decode(urldecode($_SESSION["user"]["data"]));

	$sql = "SELECT * FROM `store_coupon` WHERE `owner`={$_SESSION["user"]["id"]} AND `disabled`=0 AND `end`>".time()." ORDER BY id DESC LIMIT 0,1";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	ob_start();

	if(empty($coupon)){
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">쿠폰정보 조회</h4>
	</div>
	<div class="panel-body">
		<div class="text-center">
			<h1>등록된 쿠폰이 없어요!</h1>
		</div>
	</div>
</div>
<?php
	}
	else{
		$sql = "SELECT * FROM `store_list` WHERE `id`={$coupon["target"]}";
		$target = mysqli_fetch_array($mysql->query($sql));
	
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">쿠폰정보 삭제</h4>
	</div>
	<div class="panel-body">
		<div class="row">
			<label class="col-md-3 control-label">
				쿠폰 이름
			</label>
			<div class="col-md-9">
				<?=$coupon["title"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				쿠폰 내용
			</label>
			<div class="col-md-9">
				<?=$coupon["content"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				사용 매장
			</label>
			<div class="col-md-9">
				<?=$target["name"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				쿠폰 기간
			</label>
			<div class="col-md-9">
				~ <?=date("Y-m-d H:i:s",$coupon["end"])?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				쿠폰 이미지
			</label>
			<div class="col-md-9">
				<img alt="" src="<?=$coupon["img"]?>" class="img-responsive"><br>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				삭제
			</label>
			<div class="col-md-9">
				<button class="btn btn-danger" type="button" onclick="location.href='/admin/coupon_del.php';">
					<i class="glyphicon glyphicon-trash"></i>
					<span>
						Delete
					</span>
				</button>
			</div>
		</div>
	</div>
</div>
<?php
	}

	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>

<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>