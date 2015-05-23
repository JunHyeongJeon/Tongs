<?php
	include("../../redis.php");

	function sendCode($number){
		$salt = uniqid();
		$time = time();

		$charlist = "0123456789";
		$rand = "";

		for($i=0;$i<6;$i++){
			$rand.=$charlist[rand(0,strlen($charlist)-1)];
		}

		$data = array("api_key"=>"NCS550532CE1B91F","timestamp"=>$time,"salt"=>$salt,"signature"=>hash_hmac("md5",$time.$salt,"0A8DBF127E3AF5A1D83F7ADC1755099D"),"to"=>$number,"from"=>"821075346331","text"=>"[tongs.kr] 인증번호 ".$rand);

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

		return $rand;
	}

	$redis->set("sms_".$_GET["mdn"],sendCode($_GET["mdn"]));
	$redis->expire("sms_".$_GET["mdn"],"180");

	echo json_encode(array("result_code"=>0, "result_msg"=>"success"));
?>