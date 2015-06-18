<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	include("mariadb.php");
	include("redis.php");

	$sql = "SELECT * FROM `store_list` WHERE `id`={$_SESSION["user"]["id"]}";
	$_SESSION["user"] = mysqli_fetch_array($mysql->query($sql));
	
	$detail = json_decode(urldecode($_SESSION["user"]["data"]));

	$sql = "SELECT * FROM `user_list` WHERE `mdn`='{$_POST["mdn"]}'";
	$user = mysqli_fetch_array($mysql->query($sql));

	if(empty($user)){
		header("Location: http://tongs.kr/admin/coupon_print.php?ret=2");
		exit;
	}

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$_POST["id"]} AND `owner`={$_SESSION["user"]["id"]}";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	if(!empty($coupon)){
		$sql = "INSERT INTO `user_coupon` (`owner`,`coupon`,`start`,`end`) VALUES ({$user["id"]},{$coupon["id"]},".time().",{$coupon["end"]})";
		$mysql->query($sql);

		$redis->publish("coupon",json_encode(array("owner"=>$user["id"])));
	}

	header("Location: http://tongs.kr/admin/coupon_print.php?ret=1");
?>