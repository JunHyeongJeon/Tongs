<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `owner`={$user["id"]} AND `status`=0";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}
	else{
		$sql = "SELECT `name`,`data` FROM `store_list` WHERE `id`={$chk["store"]}";
		$store = mysqli_fetch_array($mysql->query($sql));

		$detail = json_decode(urldecode($store["data"]));

		if(empty($detail->time))
			$detail->time = 0;

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `status`=0 AND `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<{$chk["id"]}";
		$cnt = mysqli_fetch_array($mysql->query($sql));

		$ticket = true;

		$sql = "SELECT `coupon`,`start`,`end` FROM `user_coupon` WHERE `owner`={$user["id"]} AND `used`=0 AND `end`>".time();
		$coupon = mysqli_fetch_array($mysql->query($sql));

		if(empty($coupon))
			$ticket = false;

		echo json_encode(array("result_code"=>0, "result_msg"=> "success", "store"=>$store["name"], "number"=>$num["cnt"], "people"=>$cnt["cnt"], "time"=>($cnt["cnt"]+1)*$detail->time, "ticket"=>$ticket));
	}
?>