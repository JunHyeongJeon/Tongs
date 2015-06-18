<?php
	include("../../redis.php");
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `owner`={$user["id"]} AND `status`=0";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}
	else{
		$sql = "SELECT `gcm` FROM `store_list` WHERE `id`={$chk["store"]}";
		$store = mysqli_fetch_array($mysql->query($sql));

		echo json_encode(array("result_code"=>0, "result_msg"=> "success"));

		$redis->publish("accept",json_encode(array("gcm"=>$store["gcm"],"ticket"=>$chk["id"])));
	}
?>