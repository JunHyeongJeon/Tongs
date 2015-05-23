<?php
	include("../../mariadb.php");

	$sql = "SELECT `activate`,`token` FROM `store_list` WHERE `email`='{$_GET["email"]}' AND `password`='".sha1($_GET["password"])."'";
	$chk = mysqli_fetch_array($mysql->query($sql));
	
	if(empty($chk))
		echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
	else{
		if($chk["activate"]==1)
			echo json_encode(array("result_code"=>0 , "result_msg"=>"success", "token"=>$chk["token"]));
		else
			echo json_encode(array("result_code"=>-2 , "result_msg"=>"not activated", "token"=>$chk["token"]));
	}
		
?>