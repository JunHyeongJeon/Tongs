<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name`,`data` FROM `hyper_list` WHERE `activate`=1 AND `id`={$_GET["id"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "id"=>$chk["id"], "name"=>$chk["name"], "data"=>json_decode(urldecode($chk["data"]))));
?>