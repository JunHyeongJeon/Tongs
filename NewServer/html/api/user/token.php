<?php
	$sql = "SELECT * FROM `user_list` WHERE `token`='{$_GET["token"]}'";
	$user = mysqli_fetch_array($mysql->query($sql));

	if(empty($user)){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"token error"));
		exit;
	}
?>