<?php
	include("../../mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `token`='{$_GET["token"]}'";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if($_GET["key"]==sha1($chk["token"]."abc!@#".$chk["password"])){
		$sql = "UPDATE `store_list` SET `activate`=1 WHERE `id`={$chk["id"]}";
		$mysql->query($sql);
	}

	header("Location: http://tongs.kr/admin");
?>