<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_coupon` WHERE `id`={$_GET["id"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$chk["coupon"]} AND `target`={$user["id"]}";
	$coupon = mysqli_fetch_array($mysql->query($sql));

	if(empty($coupon)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "target"=>$coupon["target"] ,"store"=>$user["name"], "location"=>$user["location"], "title"=> $coupon["title"], "content"=>$coupon["content"], "data"=>json_decode(urldecode($coupon["data"])), "img"=>$coupon["img"], "end"=>$chk["end"]));
?>