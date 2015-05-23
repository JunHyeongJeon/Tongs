<?php
	include("../../mariadb.php");
	include("../../token.php");

	$sql = "SELECT `id`,`name` FROM `hyper_list` WHERE `activate`=1";
	$res = $mysql->query($sql);

	$output = array();

	while($chk=mysqli_fetch_assoc($res)){
		$output[count($output)] = $chk;
	}

	echo json_encode(array("result_code"=>0, "result_msg"=>"success", "list"=>$output));
?>