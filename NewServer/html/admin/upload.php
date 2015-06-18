<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	$name = $_FILES["files"]["name"][0];
	$tmp = $_FILES["files"]["tmp_name"][0];
	$name = uniqid().time();

	$size = getimagesize($tmp);
	$im = imagecreatefromstring(file_get_contents($tmp));
	imagepng($im,"../img/{$name}.png");
	$pre = imagecreatetruecolor(142,91);
	imagecopyresampled($pre,$im,0,0,0,0,142,91,$size[0],$size[1]);
	imagepng($pre,"../img/{$name}.preview.png");
	imagedestroy($im);
	imagedestroy($pre);
	unlink($tmp);

	include("mariadb.php");

	$sql = "SELECT * FROM `store_list` WHERE `id`={$_SESSION["user"]["id"]}";
	$_SESSION["user"] = mysqli_fetch_array($mysql->query($sql));
	
	$detail = json_decode(urldecode($_SESSION["user"]["data"]));
	$n = count($detail->img);
	$detail->img[$n] = $name;
	$json = urlencode(json_encode($detail));

	$sql = "UPDATE `store_list` SET `data`='{$json}' WHERE `id`={$_SESSION["user"]["id"]}";
	$mysql->query($sql);
?>
{
	"files":[
		{
			"thumbnailUrl": "http://tongs.kr/img/<?=$name?>.preview.png",
			"name": "<?=$name?>.png",
			"url": "http://tongs.kr/img/<?=$name?>.png",
			"deleteType": "DELETE",
			"type": "image/png",
			"deleteUrl": "http://tongs.kr/admin/delete.php?name=<?=$name?>",
			"size": <?=filesize("../img/{$name}.png")?>
		}
	]
}