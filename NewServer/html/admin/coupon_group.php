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
	
	ob_start();
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">쿠폰정보 등록</h4>
	</div>
	<div class="panel-body">
<?php
	if($_GET["ret"]==1){
?>
		<div class="alert alert-success fade in m-b-15">
			쿠폰이 정상적으로 발급되었습니다.
		</div>
<?php
	}
	else if($_GET["ret"]==2){
?>
		<div class="alert alert-danger fade in m-b-15">
			등록 되지 않은 고객입니다.
		</div>
<?php
	}
?>
		<form action="coupon_grouping.php" method="POST" class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label class="col-md-3 control-label">
					쿠폰
				</label>
				<div class="col-md-9">
					<select class="form-control" name="id">
<?php
						$sql = "SELECT * FROM `store_coupon` WHERE `owner`={$_SESSION["user"]["id"]} AND (`disabled`=0 OR `disabled`=2) AND `end`>".time()." ORDER BY id DESC";
						$res = $mysql->query($sql);

						while($chk = mysqli_fetch_array($res)){
?>
						<option value="<?=$chk["id"]?>"><?=$chk["title"]?></option>
<?php
						}
?>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					그룹
				</label>
				<div class="col-md-9">
					<select class="form-control" name="target">
						<option value="1">일반고객 (1회 이상 방문)</option>
						<option value="3">VIP고객 (3회 이상 방문)</option>
						<option value="5">VVIP고객 (5회 이상 방문)</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					저장
				</label>
				<div class="col-md-9">
					<button class="btn btn-primary" type="submit">
						<i class="glyphicon glyphicon-floppy-disk"></i>
						<span>
							발급
						</span>
					</button>
				</div>
			</div>
		</form>
	</div>
</div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>
<script type="text/javascript">
	$("#datepicker-autoclose").datepicker({todayHighlight:!0,autoclose:!0,format:"yyyy-mm-dd"});
</script>
<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>