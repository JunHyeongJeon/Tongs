<?php
	$mysql = mysqli_connect("localhost","root","soma2015","somabell");
	$mysql->query("SET NAMES utf8");

	$sql = "SELECT * FROM `somabell_store_store` WHERE `id`=".intval($_GET["sid"]);
	$chk = mysqli_fetch_array($mysql->query($sql));

	$data = json_decode($chk["json"]);
?>
<!doctype HTML>
<html>
	<head>
		<title><?=$data->brand?></title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link href="/tool/res/css/bootstrap.min.css" rel="stylesheet">
		<script src="/tool/res/js/jquery-1.11.2.min.js" type="text/javascript"></script>
		<script src="/tool/res/js/bootstrap.min.js" type="text/javascript"></script>
	</head>
	<body>
		<div class="container">
			<h1><?=$data->brand?></h1>
			<div class="row">
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
					<?=$data->store_intro?>
					<br><br>
					전화 : <a href="tel:<?=$data->office_call?>"><?=$data->office_call?></a><br>
					주소 : <?=$data->office_address?>
					<hr>
					<div class="text-center">
						평균 대기 시간 : <?=26?>분
						<button class="btn btn-primary">대기하기</button>
					</div>
				</div>
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
					<img src="<?=$data->store_image?>" alt="store_image" class="img-responsive">
				</div>
			</div>
			<hr>
			<div class="text-center">
<?php
	$sql = "SELECT * FROM `somabell_store_photo` WHERE `owner`=".intval($_GET["sid"]);
	$res = $mysql->query($sql);
	while($chk = mysqli_fetch_array($res)){
?>
				<img src="<?=$chk["photo"]?>" alt="store_image" class="img-responsive" style="margin: 0 auto;">
<?php
	}
?>
			</div>
		</div>
	</body>
</html>