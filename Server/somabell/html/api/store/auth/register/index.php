<?php
	include("../../mariadb.php");

	$sql = "SELECT `token` FROM `store_list` WHERE `email`='{$_GET["email"]}'";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		$password = sha1($_GET["password"]);
		$token = sha1(sha1(uniqid()).sha1(time()));
		$name = str_replace("'","\\'",$_GET["name"]);
		$location = str_replace("'","\\'",$_GET["location"]);
		$description = str_replace("'","\\'",$_GET["description"]);

		$sql = "INSERT INTO `store_list` (`email`,`password`,`token`,`name`,`location`,`description`) VALUES ('{$_GET["email"]}','{$password}','{$token}','{$name}','{$location}','{$description}')";
		$mysql->query($sql);

		$id = mysqli_insert_id($mysql);

		$sql = "SELECT `token` FROM `store_list` WHERE `id`={$id}";
		$chk = mysqli_fetch_array($mysql->query($sql));

		echo json_encode(array("result_code"=>0, "result_msg"=>"success", "token"=>$chk["token"]));
	}
	else
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));