<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `store`={$user["id"]} AND `id`={$_GET["id"]}";
	$chk = mysqli_fetch_assoc($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}
	else{
		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT `id` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `status`=1 ORDER BY `id` DESC LIMIT 0,1";
		$last = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$last["id"]}";
		$last = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT `id`,`mdn`,`data` FROM `user_list` WHERE `id`={$chk["owner"]}";
		$user = mysqli_fetch_assoc($mysql->query($sql));

		$user["data"] = json_decode($user["data"]);

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `owner`={$chk["owner"]} AND `status`=1";
		$revisit = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `owner`={$chk["owner"]} AND `status`=2";
		$storecancle = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `owner`={$chk["owner"]} AND `status`=3";
		$usercancle = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT `time` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `owner`={$chk["owner"]} AND `status`=1 ORDER BY id DESC LIMIT 0,1";
		$lastvisit = mysqli_fetch_array($mysql->query($sql));

		echo json_encode(array("result_code"=>0, "result_msg"=> "success", "ticket"=>$chk, "user"=>$user, "number"=>$num["cnt"], "last"=>$last["cnt"], "revisit"=> $revisit["cnt"], "storecancle"=> $storecancle["cnt"], "usercancle"=>$usercancle["cnt"],"lastvisit"=>$lastvisit["time"]));
	}
?>