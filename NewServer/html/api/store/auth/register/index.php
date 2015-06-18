<?php
	include("../../mariadb.php");

	$sql = "SELECT `token` FROM `store_list` WHERE `email`='{$_REQUEST["email"]}'";
	$chk = mysqli_fetch_array($mysql->query($sql));

	if(empty($chk)){
		$password = sha1($_REQUEST["password"]);
		$token = sha1(sha1(uniqid()).sha1(time()));
		$name = str_replace("'","\\'",$_REQUEST["name"]);
		$location = str_replace("'","\\'",$_REQUEST["location"]);
		$description = str_replace("'","\\'",$_REQUEST["description"]);

		$sql = "INSERT INTO `store_list` (`email`,`password`,`token`,`name`,`location`,`description`) VALUES ('{$_REQUEST["email"]}','{$password}','{$token}','{$name}','{$location}','{$description}')";
		$mysql->query($sql);

		$id = mysqli_insert_id($mysql);

		$sql = "SELECT `token`,`password` FROM `store_list` WHERE `id`={$id}";
		$chk = mysqli_fetch_array($mysql->query($sql));

		require("PHPMailerAutoload.php");

		$mail = new PHPMailer();
		
		$mail->isSMTP();
		$mail->Host = "smtp.gmail.com";
		//$mail->SMTPDebug = 1;
		$mail->SMTPAuth = true;
		$mail->Username = "somabell15";
		$mail->Password = "soma2015";
		$mail->SMTPSecure = "tls";
		$mail->Port = 587;
		
		$mail->setFrom("somabell15@gmail.com","TEAM SomaBell");
		$mail->addReplyTo("somabell15@gmail.com","TEAM SomaBell");
		$mail->addAddress($_REQUEST["email"],"Azu");

		$mail->Subject = "'고대기' 인증 메일입니다";
		$mail->Body = "http://tongs.kr/api/store/auth/activate/?token={$chk["token"]}&key=".sha1($chk["token"]."abc!@#".$chk["password"]);
		
		$mail->Send();

		if($_REQUEST["tool"]==1)
			header("Location: http://tongs.kr/admin/login.php");
		else
			echo json_encode(array("result_code"=>0, "result_msg"=>"success", "token"=>$chk["token"]));
	}
	else{
		if($_REQUEST["tool"]==1)
			header("Location: http://tongs.kr/admin/register.php");
		else
			echo json_encode(array("result_code"=>-1, "result_msg"=>"fail"));
	}