<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name` FROM `hyper_list` WHERE `activate`=1";
	$res = $mysql->query($sql);

	$output = array();

	$output[0] = array("id"=>0, "name"=>"기타매장");

	while($chk=mysqli_fetch_assoc($res)){
		$output[count($output)] = $chk;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>