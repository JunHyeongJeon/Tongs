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

	$sql = "SELECT * FROM `store_coupon` WHERE `owner`={$_SESSION["user"]["id"]} AND `id`={$_GET["id"]} AND `end`>".time()." ORDER BY id DESC LIMIT 0,1";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	if(!empty($coupon)){
		$sql = "UPDATE `store_coupon` SET `disabled`={$_GET["code"]} WHERE `id`={$coupon["id"]}";
		$mysql->query($sql);
	}

	if(empty($_GET["next"]))
		header("Location: /admin/coupon_view.php");
	else
		header("Location: ".urldecode($_GET["next"]));
?>