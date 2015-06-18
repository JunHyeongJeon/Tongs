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

	$name = str_replace("'","\\'",$_POST["name"]);
	$location = str_replace("'","\\'",$_POST["location"]);
	$description = str_replace("'","\\'",$_POST["description"]);

	$detail->menu = str_replace("'","\\'",$_POST["detail_menu"]);
	$detail->info = json_decode($_POST["detail_info"]);

	$detail = urlencode(json_encode($detail));

	$sql = "UPDATE `store_list` SET `name`='{$name}', `location`='{$location}', `description`='{$description}', `data`='{$detail}' WHERE `id`={$_SESSION["user"]["id"]}";
	$mysql->query($sql);

	header("Location: http://tongs.kr/admin/store_view.php")
?>