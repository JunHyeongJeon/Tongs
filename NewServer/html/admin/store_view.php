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
		<h4 class="panel-title">매장정보 조회</h4>
	</div>
	<div class="panel-body">
		<div class="row">
			<label class="col-md-3 control-label">
				매장 이름
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["name"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				매장 위치
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["location"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				매장 소개
			</label>
			<div class="col-md-9">
				<?=$_SESSION["user"]["description"]?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				주 메뉴
			</label>
			<div class="col-md-9">
				<?=$detail->menu?>
			</div>
		</div>
		<div class="row">
			<label class="col-md-3 control-label">
				매장 정보
			</label>
			<div class="col-md-9">
<?php
			for($i=0;$i<count($detail->info);$i++){
?>
				<?=$detail->info[$i]->key?> : <?=$detail->info[$i]->value?><br>
<?php
			}
?>
			</div>
		</div>
	</div>
</div>

<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">매장 이미지</h4>
	</div>
	<div class="panel-body">
		<div class="superbox" data-offset="10">
<?php
			for($i=0;$i<count($detail->img);$i++){
?>
			<div class="superbox-list">
				<img class="superbox-img" alt="" data-img="http://tongs.kr/img/<?=$detail->img[$i]?>.png" src="http://tongs.kr/img/<?=$detail->img[$i]?>.png">
			</div>
<?php
			}
?>
		</div>
	</div>
</div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>
<script type="text/javascript">
	var handleSuperboxGallery=function(){
		"use strict";
		$(window).load(function(){
			$(".superbox").SuperBox()
		})
	};
	var Gallery=function(){
		"use strict";
		return {
			init: function(){
				handleSuperboxGallery()
			}
		}
	}();

	Gallery.init();
</script>
<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>