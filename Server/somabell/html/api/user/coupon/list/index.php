<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT * FROM `user_coupon` WHERE `used`=1 AND `owner`={$user["id"]} LIMIT 0,6";
	$res = $mysql->query($sql);

	$output = array();

	while($coupon=mysqli_fetch_assoc($res)){
		$sql = "SELECT * FROM `store_coupon` WHERE `id`={$coupon["coupon"]}";
		$chk = mysqli_fetch_array($mysql->query($sql));

		$sql = "SELECT * FROM `store_list` WHERE `id`={$chk["target"]}";
		$store = mysqli_fetch_array($mysql->query($sql));
		
		$index = count($output);

		$output[$index]["target"] = $store["id"];
		$output[$index]["title"] = $chk["title"];
		$output[$index]["content"] = $chk["content"];
		$output[$index]["location"] = $store["location"];
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>