<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name`,`location`,`description`,`data`,`high` FROM `store_list` WHERE `activate`=1 AND `hyper`={$_GET["hyper"]} AND `id`={$_GET["id"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	$sql = "SELECT * FROM `store_beacon` WHERE `owner`={$chk["id"]}";
	$beacon = mysqli_fetch_array($mysql->query($sql));

	$chk["img"] = "http://tongs.kr/img/".json_decode(urldecode($chk["data"]))->img[0].".png";
	$chk["beacon"] = json_encode(array("major"=>$beacon["major"], "minor"=>$beacon["minor"]));

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "id"=>$chk["id"], "name"=>$chk["name"], "location"=>$chk["location"], "description"=> $chk["description"], "data"=>json_decode(urldecode($chk["data"])), "img"=>$chk["img"]));
?>