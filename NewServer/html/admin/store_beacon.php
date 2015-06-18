<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	include("mariadb.php");

	$sql = "SELECT * FROM `store_beacon` WHERE `owner`={$_SESSION["user"]["id"]}";
	$beacon = mysqli_fetch_array($mysql->query($sql));

	$major = intval($_POST["major"]);
	$minor = intval($_POST["minor"]);

	if(empty($beacon)){
		$sql = "INSERT INTO `store_beacon` (`owner`,`major`,`minor`) VALUES ({$_SESSION["user"]["id"]},{$major},{$minor})";
		$mysql->query($sql);
	}
	else{
		$sql = "UPDATE `store_beacon` SET `major`={$major}, `minor`={$minor} WHERE `owner`={$_SESSION["user"]["id"]}";
		$mysql->query($sql);
	}

	header("Location: http://tongs.kr/admin/store_option.php")
?>