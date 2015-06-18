<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `store`={$_GET["id"]} ORDER BY `id` DESC LIMIT 0,1";
	$chk = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$_GET["id"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
	$num = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `status`=0 AND `store`={$_GET["id"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
	$cnt = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT `data` FROM `store_list` WHERE `id`={$_GET["id"]}";
	$store = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_beacon` WHERE `owner`={$_GET["id"]}";
	$beacon = mysqli_fetch_array($mysql->query($sql));

	$detail = json_decode(urldecode($store["data"]));
	
	if(empty($cnt["cnt"]))
		$cnt["cnt"] = 0;
	if(empty($detail->time))
		$detail->time = 0;

	if(empty($detail->wait))
		$detail->wait = 1;

	echo json_encode(array("result_code"=>0, "result_msg"=> "success", "extra"=>$cnt["cnt"], "time"=>$cnt["cnt"]*$detail->time, "wait"=>$detail->wait, "beacon"=>json_encode(array("major"=>$beacon["major"], "minor"=>$beacon["minor"]))));
?>