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

	$detail->wait = intval($_POST["wait"]);
	$detail->time = intval($_POST["time"]);

	$detail = urlencode(json_encode($detail));

	$sql = "UPDATE `store_list` SET `data`='{$detail}' WHERE `id`={$_SESSION["user"]["id"]}";
	$mysql->query($sql);

	header("Location: http://tongs.kr/admin/store_option.php")
?>