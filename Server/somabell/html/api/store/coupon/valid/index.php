<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `coupon`,`start`,`end` FROM `user_coupon` WHERE `owner`={$_GET["user"]} AND `used`=0 AND `end`>".time();
	$coupon = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_coupon` WHERE `id`={$coupon["coupon"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if($chk["target"]==$user["id"])
		echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
	else
		echo json_encode(array("result_code"=>0, "result_msg"=>"fail"));
?>