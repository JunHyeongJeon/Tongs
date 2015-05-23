<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "UPDATE `user_list` SET `data`='{$_GET["data"]}' WHERE `id`={$user["id"]}";
	$mysql->query($sql);

	echo json_encode(array("result_code"=> 0, "result_msg"=>"success"));
?>