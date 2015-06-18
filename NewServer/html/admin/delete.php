<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	include("mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `id`={$_SESSION["user"]["id"]}";
	$_SESSION["user"] = mysqli_fetch_array($mysql->query($sql));
	
	$detail = json_decode(urldecode($_SESSION["user"]["data"]));
	$new_img = array();
	for($i=0;$i<count($detail->img);$i++){
		if($detail->img[$i]==$_GET["name"])
			continue;
		$n = count($new_img);
		$new_img[$n] = $detail->img[$i];
	}
	$detail->img = $new_img;
	$json = urlencode(json_encode($detail));

	$sql = "UPDATE `store_list` SET `data`='{$json}' WHERE `id`={$_SESSION["user"]["id"]}";
	$mysql->query($sql);
?>
{"success": true}