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

	$title = str_replace("'","\\'",$_POST["title"]);
	$content = str_replace("'","\\'",$_POST["content"]);
	$target = intval($_POST["target"]);
	$end = strtotime($_POST["end"]." 23:59:59");

	$name = $_FILES["img"]["name"];
	$tmp = $_FILES["img"]["tmp_name"];
	$name = uniqid().time();

	$size = getimagesize($tmp);
	$im = imagecreatefromstring(file_get_contents($tmp));
	imagepng($im,"../img/{$name}.png");
	$pre = imagecreatetruecolor(145,112);
	imagecopyresampled($pre,$im,0,0,0,0,145,112,$size[0],$size[1]);
	imagepng($pre,"../img/{$name}.preview.png");
	imagedestroy($im);
	imagedestroy($pre);
	unlink($tmp);

	$img = "http://tongs.kr/img/{$name}.preview.png";

	$disabled = 2;

	if($_POST["center"]==1){
		$sql = "SELECT * FROM `store_coupon` WHERE `disabled`=0 AND `owner`={$_SESSION["user"]["id"]}";
		$chk = mysqli_fetch_array($mysql->query($sql));

		if(!empty($chk)){
			$sql = "UPDATE `store_coupon` SET `disabled`=2 WHERE `owner`={$_SESSION["user"]["id"]} AND `id`={$chk["id"]}";
			$mysql->query($sql);
		}

		$disabled = 0;
	}

	$sql = "INSERT INTO `store_coupon` (`owner`,`target`,`title`,`content`,`end`,`img`,`disabled`) VALUES ({$_SESSION["user"]["id"]},{$target},'{$title}','{$content}',{$end},'{$img}',{$disabled})";
	$mysql->query($sql);

	header("Location: http://tongs.kr/admin/coupon_view.php")
?>