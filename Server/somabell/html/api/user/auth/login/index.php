<?php
	include("../../redis.php");
	include("../../mariadb.php");

	$orig = $redis->get("sms_".$_GET["mdn"]);

	if($orig===FALSE){
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}

	if($orig==$_GET["code"]){
		$sql = "SELECT `id`,`token` FROM `user_list` WHERE `mdn`='{$_GET["mdn"]}'";
		$chk = mysqli_fetch_array($mysql->query($sql));

		if(empty($chk)){
			$token = sha1(sha1(uniqid()).sha1(time()));

			$sql = "INSERT INTO `user_list` (`mdn`,`token`) VALUES ('{$_GET["mdn"]}','{$token}')";
			$mysql->query($sql);

			$id = mysqli_insert_id($mysql);

			$sql = "SELECT `id`,`token` FROM `user_list` WHERE `id`={$id}";
			$chk = mysqli_fetch_array($mysql->query($sql));
		}

		echo json_encode(array("result_code"=>0, "result_msg"=>"success", "id"=>$chk["id"], "token"=>$chk["token"]));
	}
	else
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
?>