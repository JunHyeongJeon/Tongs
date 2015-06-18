<?php
	include("../../redis.php");
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_ticket` WHERE `id`={$_GET["id"]} AND `store`={$user["id"]}";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		if($_GET["tool"]==1)
			header("Location: http://tongs.kr/admin/list_realtime.php");
		else
			echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
		exit;
	}
	else{
		$sql = "UPDATE `user_ticket` SET `status`=1 WHERE `id`={$_GET["id"]}";
		$mysql->query($sql);

		if($_GET["tool"]==1)
			header("Location: http://tongs.kr/admin/list_realtime.php");
		else
			echo json_encode(array("result_code"=>0, "result_msg"=> "success"));

		$redis->publish("change",json_encode(array("pivot"=>$chk["pivot"],"store"=>$user["id"])));

		if($chk["owner"]!=-1)
			$redis->publish("pop",json_encode(array("id"=>$chk["owner"])));
	}
?>