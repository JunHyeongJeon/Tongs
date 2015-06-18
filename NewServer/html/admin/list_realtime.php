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
	
	$pivot = empty($_GET["pivot"])?date("Ymd"):$_GET["pivot"];
	$type = intval($_GET["type"]);

	$sql = "SELECT * FROM `user_ticket` WHERE `pivot`='{$pivot}' AND `status`={$type} AND `store`={$_SESSION["user"]["id"]}";
	$res = $mysql->query($sql);

	$chk=mysqli_fetch_assoc($res);

	$sql = "SELECT count(*) as `cnt` FROM `user_ticket` WHERE `store`={$_SESSION["user"]["id"]} AND `pivot`='{$pivot}' AND `id`<={$chk["id"]}";
	$num = mysqli_fetch_array($mysql->query($sql));
	$chk["number"] = $num["cnt"];

	ob_start();
?>
<div id="azuKawaii"></div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>
<script type="text/javascript">
	function divRefresh(){
		$.ajax("http://tongs.kr/admin/list_realtime_ajax.php",{
			success: function(data){
				azuKawaii.innerHTML = data;
				setTimeout(divRefresh,1000);
			}
		});
	}

	divRefresh();
</script>
<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>