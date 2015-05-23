<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `coupon`,`start`,`end` FROM `user_coupon` WHERE `owner`={$user["id"]} AND `used`=0 AND `end`>".time();
	$coupon = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$coupon["coupon"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_list` WHERE `id`={$chk["target"]}";
	$store = mysqli_fetch_array($mysql->query($sql));

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "store"=>$store["name"], "location"=>$store["location"], "title"=> $chk["title"], "content"=>$chk["content"], "data"=>json_decode(urldecode($chk["data"])), "start"=>$coupon["start"], "end"=>$coupon["end"]));
?>