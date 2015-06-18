<?php
	$width = intval($_GET["width"]);
	$resolution = floatval($_GET["resolution"]);

	$mysql = mysqli_connect("localhost","somabell","A4JQame6nBSuZBbG","somabell");
	$mysql->query("SET NAMES utf8");

	$sql = "SELECT * FROM `store_list` WHERE `token`=".$_GET["token"];
	$chk = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `user_coupon` WHERE `id`={$_GET["id"]}";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$coupon["coupon"]}";
	$data = mysqli_fetch_array($mysql->query($sql));
?>
<!doctype HTML>
<html>
	<head>
		<title>Info</title>
		<meta charset="UTF-8">
	</head>
	<body ondragstart="return false;" onselectstart="return false;" onmousedown="return false;">
		<div style="display: absolute; bottom: <?=24*$resolution?>px;">
			<div style="display: absolute; bottom: <?=9*$resolution?>px;">
				<img src="<?=$data["img"]?>">
			</div>
		</div>
	</body>
</html>