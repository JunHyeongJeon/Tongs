<?php
	include("../../redis.php");

	$salt = uniqid();
	$time = time();

	$data = array("api_key"=>"NCS550532CE1B91F","timestamp"=>$time,"salt"=>$salt,"signature"=>hash_hmac("md5",$time.$salt,"0A8DBF127E3AF5A1D83F7ADC1755099D"),"to"=>$_GET["to"],"from"=>$_GET["from"],"text"=>$_GET["text"]);

	$ch = curl_init();
	curl_setopt($ch,CURLOPT_URL,"https://api.coolsms.co.kr/sms/1.1/send");
	curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
	curl_setopt($ch,CURLOPT_SSLVERSION,3);
	curl_setopt($ch,CURLOPT_POST,true);
	curl_setopt($ch,CURLOPT_HTTPHEADER,array("Content-Type:multipart/form-data"));
	curl_setopt($ch,CURLOPT_POSTFIELDS,$data);
	curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
	json_decode(curl_exec($ch));
	curl_close($ch);

	echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
?>