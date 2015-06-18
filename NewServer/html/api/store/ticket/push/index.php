<?php
	include("../../redis.php");
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `owner`={$_GET["user"]} AND `status`=0";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(!empty($chk)){
		echo json_encode(array("result_code"=>-2, "result_msg"=>"fail"));
		exit;
	}

	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$time = time();

	$sql = "INSERT INTO `user_ticket` (`owner`,`store`,`people`,`pivot`,`status`,`time`) VALUES ({$_GET["user"]},{$user["id"]},{$_GET["people"]},'{$pivot}',0,{$time})";
	$mysql->query($sql);

	$sql = "SELECT * FROM `store_coupon` WHERE `owner`={$user["id"]} AND `disabled`=0 AND `end`>".time()." ORDER BY id DESC LIMIT 0,1";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	if(!empty($coupon)){
		$sql = "INSERT INTO `user_coupon` (`owner`,`coupon`,`start`,`end`) VALUES ({$_GET["user"]},{$coupon["id"]},".time().",{$coupon["end"]})";
		$mysql->query($sql);
	}

	echo json_encode(array("result_code"=>0, "result_msg"=> "success"));
	
	$redis->publish("change",json_encode(array("pivot"=>$pivot,"store"=>$user["id"])));
?>