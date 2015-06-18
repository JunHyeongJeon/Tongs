<?php
	include("mariadb.php");

	$password = sha1($_POST["password"]);
	
	$sql = "SELECT * FROM `store_list` WHERE `email`='{$_POST["email"]}' AND `password`='{$password}' AND `activate`=1";
	$user = mysqli_fetch_array($mysql->query($sql));

	if(empty($user)){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	session_start();
	$_SESSION["user"] = $user;

	if(empty($_GET["next"]))
		$_GET["next"] = urlencode("http://tongs.kr/admin/");

	header("Location: ".urldecode($_GET["next"]));
?>