<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name`,`location`,`description`,`data`,`high` FROM `store_list` WHERE `activate`=1 AND `hyper`={$_GET["hyper"]}";
	$res = $mysql->query($sql);

	$output = array();

	while($chk=mysqli_fetch_assoc($res)){
		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `status`=0 AND `store`={$chk["id"]}";
		$cnt = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT * FROM `store_beacon` WHERE `owner`={$chk["id"]}";
		$beacon = mysqli_fetch_array($mysql->query($sql));

		$chk["wait"] = $cnt["cnt"];
		$chk["img"] = "http://tongs.kr/img/".json_decode(urldecode($chk["data"]))->img[0].".preview.png";
		$chk["beacon"] = json_encode(array("major"=>$beacon["major"], "minor"=>$beacon["minor"]));

		$output[count($output)] = $chk;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>