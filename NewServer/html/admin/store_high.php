<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	include("mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `id`={$_SESSION["user"]["id"]}";
	$_SESSION["user"] = mysqli_fetch_array($mysql->query($sql));
	
	$high = json_decode($_POST["detail_info"]);

	$high = urlencode(json_encode($high));

	$sql = "UPDATE `store_list` SET `high`='{$high}' WHERE `id`={$_SESSION["user"]["id"]}";
	$mysql->query($sql);

	header("Location: http://tongs.kr/admin/store_option.php")
?>