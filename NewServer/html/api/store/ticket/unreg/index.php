<?php
	include("../../redis.php");
	include("../../mariadb.php");
	include("../../token.php");
	header("Access-Control-Allow-Origin: *");

	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$time = time();

	$sql = "INSERT INTO `user_ticket` (`owner`,`store`,`people`,`pivot`,`status`,`time`,`mdn`) VALUES (-1,{$user["id"]},{$_GET["people"]},'{$pivot}',0,{$time},'{$_GET["mdn"]}')";
	$mysql->query($sql);

	echo json_encode(array("result_code"=>0, "result_msg"=> "success"));
	
	$redis->publish("change",json_encode(array("pivot"=>$pivot,"store"=>$user["id"])));
?>