<?php
	include("../../redis.php");
	include("../../mariadb.php");
	include("../../token.php");
	header("Access-Control-Allow-Origin: *");

	function sendSMS($to,$from,$text){
		$salt = uniqid();
		$time = time();

		$data = array("api_key"=>"NCS550532CE1B91F","timestamp"=>$time,"salt"=>$salt,"signature"=>hash_hmac("md5",$time.$salt,"0A8DBF127E3AF5A1D83F7ADC1755099D"),"to"=>$to,"from"=>$from,"text"=>$text);

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
	}

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
		if($chk["owner"]==-1)
			sendSMS($chk["mdn"],"025321332","[다미의원] 차례가 되었습니다.\n데스크에 문의해주세요.");
		else
			$redis->publish("turn",json_encode(array("owner"=>$chk["owner"],"store"=>$user["id"],"hyper"=>$user["hyper"])));

		if($_GET["tool"]==1)
			header("Location: http://tongs.kr/admin/list_realtime.php");
		else
			echo json_encode(array("result_code"=>0, "result_msg"=> "success"));
	} 
?>