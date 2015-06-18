<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `status`=0 AND `store`={$user["id"]} ORDER BY `id` ASC LIMIT 0,1";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}

	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$time = time();

	$sql = "SELECT * FROM `user_ticket` WHERE `id`>={$chk["id"]} AND `store`={$user["id"]}";
	$res = $mysql->query($sql);

	$output = array();

	while($chk=mysqli_fetch_assoc($res)){
		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$user["id"]} AND `pivot`='{$chk["pivot"]}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$chk["store"]} AND `owner`={$chk["owner"]} AND `status`=1";
		$revisit = mysqli_fetch_array($mysql->query($sql));

		$chk["number"] = $num["cnt"];
		$chk["revisit"] = $revisit["cnt"];

		$output[count($output)] = $chk;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>