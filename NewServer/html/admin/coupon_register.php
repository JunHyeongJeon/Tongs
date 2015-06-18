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
		<form action="coupon_submit.php" method="POST" class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label class="col-md-3 control-label">
					쿠폰 이름
				</label>
				<div class="col-md-9">
					<input type="text" name="title" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					쿠폰 내용
				</label>
				<div class="col-md-9">
					<input type="text" name="content" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					사용 매장
				</label>
				<div class="col-md-9">
					<select class="form-control" name="target">
<?php
					$sql = "SELECT * FROM `store_list` WHERE `hyper`={$_SESSION["user"]["hyper"]}";
					$res = $mysql->query($sql);
					while($chk=mysqli_fetch_array($res)){
?>
						<option value="<?=$chk["id"]?>"><?=$chk["name"]?></option>
<?php
					}
?>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					쿠폰 기간
				</label>
				<div class="col-md-9">
					<input type="text" class="form-control" id="datepicker-autoclose" placeholder="날짜 선택" name="end">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					쿠폰 이미지
				</label>
				<div class="col-md-9">
					<input type="file" name="img">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					옵션
				</label>
				<div class="col-md-9">
					<label>
						<input type="checkbox" value="1" name="center">
						대표쿠폰으로 설정
					</label>
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
							Save
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