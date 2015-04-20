<?php
	$mysql = mysqli_connect("localhost","root","soma2015","somabell");
	$mysql->query("SET NAMES utf8");

	$sql = "SELECT * FROM `somabell_store_store` WHERE `id`=".intval($_GET["sid"]);
	$chk = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `somabell_store_email` WHERE `id`={$chk["owner"]}";
	$owner_chk = mysqli_fetch_array($mysql->query($sql));

	if($owner_chk["token"]!=$_GET["token"])
		exit;

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
			<table class="table">
<?php
	$sql = "SELECT * FROM `somabell_store_photo` WHERE `owner`=".intval($_GET["sid"]);
	$res = $mysql->query($sql);
	while($chk = mysqli_fetch_array($res)){
?>
			<tr>
				<td style="width: 80%;">
					<img src="<?=$chk["photo"]?>" alt="store_image" class="img-responsive" style="margin: 0 auto;">
				</td>
				<td style="width: 20%;">
					<button class="btn btn-danger">삭제</button>
				</td>
			</tr>
<?php
	}
?>
			</table>
		</div>
	</body>
</html>
