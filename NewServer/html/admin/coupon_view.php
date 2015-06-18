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
	$root_coupon = $coupon = mysqli_fetch_array($mysql->query($sql));

	$null_coupon = true;

	ob_start();

	if(!empty($coupon)){
		$null_coupon = false;

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
		<h4 class="panel-title">대표쿠폰</h4>
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
				동작
			</label>
			<div class="col-md-9">
				<button class="btn btn-warning" type="button" onclick="location.href='/admin/coupon_del.php?id=<?=$coupon["id"]?>&amp;code=2';">
					<i class="glyphicon glyphicon-remove"></i>
					<span>
						매장 대표쿠폰에서 해제
					</span>
				</button>
				<button class="btn btn-danger" type="button" onclick="location.href='/admin/coupon_del.php?id=<?=$coupon["id"]?>&amp;code=1';">
					<i class="glyphicon glyphicon-trash"></i>
					<span>
						삭제
					</span>
				</button>
			</div>
		</div>
	</div>
</div>
<?php
	}

	$sql = "SELECT * FROM `store_coupon` WHERE `owner`={$_SESSION["user"]["id"]} AND `disabled`=2 AND `end`>".time();
	$res = $mysql->query($sql);

	while($coupon=mysqli_fetch_array($res)){
		$null_coupon = false;
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">쿠폰</h4>
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
				동작
			</label>
			<div class="col-md-9">
				<button class="btn btn-info" type="button" onclick="location.href='/admin/coupon_del.php?id=<?=$root_coupon["id"]?>&amp;code=2&amp;next=%2Fadmin%2Fcoupon_del.php%3Fid%3D<?=$coupon["id"]?>%26code%3D0';">
					<i class="glyphicon glyphicon-ok"></i>
					<span>
						매장 대표쿠폰으로 등록
					</span>
				</button>
				<button class="btn btn-danger" type="button" onclick="location.href='/admin/coupon_del.php?id=<?=$coupon["id"]?>&amp;code=1';">
					<i class="glyphicon glyphicon-trash"></i>
					<span>
						삭제
					</span>
				</button>
			</div>
		</div>
	</div>
</div>
<?php
	}

	if($null_coupon){
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">쿠폰</h4>
	</div>
	<div class="panel-body">
		<h1>등록 된 쿠폰이 없어요!</h1>
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