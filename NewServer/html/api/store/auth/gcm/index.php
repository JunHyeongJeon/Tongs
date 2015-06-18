<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "UPDATE `store_list` SET `gcm`='{$_GET["gcm"]}' WHERE `id`={$user["id"]}";
	$mysql->query($sql);

	echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
?>