<?php
	include("../../mariadb.php");
	include("../../token.php");
	header("Access-Control-Allow-Origin: *");

	$sql = "SELECT * FROM `user_ticket` WHERE `store`={$user["id"]} AND `id`={$_GET["id"]}";
	$chk = mysqli_fetch_assoc($mysql->query($sql));

	if(empty($chk)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}
	else{
		$sql = "UPDATE `user_ticket` SET `mdn`='{$_GET["mdn"]}' WHERE `store`={$user["id"]} AND `id`={$_GET["id"]}";
		$mysql->query($sql);
		echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
	}
?>