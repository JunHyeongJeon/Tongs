<?php
	session_start();

	if(empty($_SESSION["user"])){
		header("Location: http://tongs.kr/admin/login.php");
		exit;
	}

	ob_start();
?>
p
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>

<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>