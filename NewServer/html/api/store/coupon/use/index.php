<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`, `coupon`,`start`,`end` FROM `user_coupon` WHERE `id`={$_GET["id"]} AND `used`=0 AND `end`>".time();
	$coupon = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$coupon["coupon"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if($chk["target"]==$user["id"]){
		$sql = "UPDATE `user_coupon` SET `used`=1 WHERE `id`={$coupon["id"]}";
		$mysql->query($sql);
		echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
	}
	else
		echo json_encode(array("result_code"=>0, "result_msg"=>"fail"));
?>