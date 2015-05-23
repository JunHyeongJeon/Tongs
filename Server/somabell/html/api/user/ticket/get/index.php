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
		$sql = "SELECT `name` FROM `store_list` WHERE `id`={$chk["store"]}";
		$store = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `status`=0 AND `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<{$chk["id"]}";
		$cnt = mysqli_fetch_array($mysql->query($sql));

		echo json_encode(array("result_code"=>0, "result_msg"=> "success", "store"=>$store["name"], "number"=>$num["cnt"], "people"=>$cnt["cnt"]));
	}
?>