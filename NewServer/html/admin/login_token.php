<?php
	include("mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `token`='{$_GET["token"]}'";
	$user = mysqli_fetch_array($mysql->query($sql));

	if(empty($user)){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	if($user["activate"]==0){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	session_start();
	$_SESSION["user"] = $user;

	if(empty($_GET["next"]))
		$_GET["next"] = urlencode("http://tongs.kr/admin/");

	header("Location: ".$_GET["next"]);
?>