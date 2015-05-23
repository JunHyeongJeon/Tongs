<?php
	include("../../mariadb.php");
	include("../../token.php");

	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$type = intval($_GET["type"]);

	$sql = "SELECT * FROM `user_ticket` WHERE `pivot`='{$pivot}' AND `status`={$type} AND `store`={$user["id"]}";
	$res = $mysql->query($sql);

	$output = array();

	while($chk=mysqli_fetch_assoc($res)){
		$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$user["id"]} AND `pivot`='{$pivot}' AND `id`<={$chk["id"]}";
		$num = mysqli_fetch_array($mysql->query($sql));

		$chk["number"] = $num["cnt"];

		$output[count($output)] = $chk;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>