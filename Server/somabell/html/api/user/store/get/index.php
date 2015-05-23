<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name`,`location`,`description`,`data` FROM `store_list` WHERE `activate`=1 AND `hyper`={$_GET["hyper"]} AND `id`={$_GET["id"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "id"=>$chk["id"], "name"=>$chk["name"], "location"=>$chk["location"], "description"=> $chk["description"], "data"=>json_decode(urldecode($chk["data"]))));
?>