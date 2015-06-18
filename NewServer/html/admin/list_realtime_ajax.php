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
	
	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$type = intval($_GET["type"]);

	$sql = "SELECT * FROM `user_ticket` WHERE `pivot`='{$pivot}' AND `status`={$type} AND `store`={$_SESSION["user"]["id"]}";
	$res = $mysql->query($sql);

	$chk=mysqli_fetch_assoc($res);

	$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$_SESSION["user"]["id"]} AND `pivot`='{$pivot}' AND `id`<={$chk["id"]}";
	$num = mysqli_fetch_array($mysql->query($sql));
	$chk["number"] = $num["cnt"];
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">현재 대기순번</h4>
	</div>
	<div class="panel-body">
		<div class="container-fluid text-center">
			<h4>대기번호</h4>
			<h1><?=$chk["number"]?></h1>
		</div>
		<div class="container-fluid text-center">
			<div class="row">
				<div class="col-md-6">
					<h4>
						인원수
					</h4>
					<h3>
						<i class="fa fa-group"></i>
						<?=$chk["people"]?>명
					</h3>
				</div>
				<div class="col-md-6">
					<h4>
						대기시간
					</h4>
					<h3>
						<i class="fa fa-clock-o"></i>
						<?=intval((time()-$chk["time"])/60)?>분
					</h3>
				</div>
			</div>
		</div>
		<div class="container-fluid text-center">
			<button class="btn btn-success" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/pop/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
				<i class="glyphicon glyphicon-ok"></i>
				<span>
					입장
				</span>
			</button>
			<button class="btn btn-primary" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/call/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
				<i class="glyphicon glyphicon-bell"></i>
				<span>
					호출
				</span>
			</button>
			<button class="btn btn-danger" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/remove/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
				<i class="glyphicon glyphicon-trash"></i>
				<span>
					취소
				</span>
			</button>
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
		<h4 class="panel-title">대기순번 리스트</h4>
	</div>
	<div class="panel-body">
<?php
	while($chk=mysqli_fetch_assoc($res)){
		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$_SESSION["user"]["id"]} AND `pivot`='{$pivot}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$chk["number"] = $num["cnt"];
?>
		<div class="row text-center">
			<div class="col-md-3 col-xs-3">
				<label>대기번호</label>
				<h4><?=$chk["number"]?></h4>
			</div>
			<div class="col-md-6 col-xs-6">
				<div class="row">
					<div class="col-md-6 col-xs-6">
						<label>
							인원수
						</label>
						<h4>
							<i class="fa fa-group"></i>
							<?=$chk["people"]?>명
						</h4>
					</div>
					<div class="col-md-6 col-xs-6">
						<label>
							대기시간
						</label>
						<h4>
							<i class="fa fa-clock-o"></i>
							<?=intval((time()-$chk["time"])/60)?>분
						</h4>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-xs-3">
				<div>
					<button class="btn btn-success btn-xs" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/pop/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
						<i class="glyphicon glyphicon-ok"></i>
						<span>
							입장
						</span>
					</button>
				</div>
				<div>
					<button class="btn btn-primary btn-xs" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/call/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
						<i class="glyphicon glyphicon-bell"></i>
						<span>
							호출
						</span>
					</button>
				</div>
				<div>
					<button class="btn btn-danger btn-xs" type="button" onclick="location.href='http://tongs.kr/api/store/ticket/remove/?token=<?=$_SESSION["user"]["token"]?>&amp;tool=1&amp;id=<?=$chk["id"]?>';">
						<i class="glyphicon glyphicon-trash"></i>
						<span>
							취소
						</span>
					</button>
				</div>
			</div>
		</div>
		<hr>
<?php
	}
?>
	</div>
</div>