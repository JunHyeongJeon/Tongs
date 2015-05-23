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
		$sql = "UPDATE `user_ticket` SET `status`=3 WHERE `id`={$chk["id"]}";
		$mysql->query($sql);

		echo json_encode(array("result_code"=>0, "result_msg"=> "success"));

		$redis->publish("change",json_encode(array("pivot"=>$chk["pivot"],"store"=>$chk["store"])));
	}
?>