<?php
	$width = intval($_GET["width"]);
	$resolution = floatval($_GET["resolution"]);

	$mysql = mysqli_connect("localhost","somabell","A4JQame6nBSuZBbG","somabell");
	$mysql->query("SET NAMES utf8");

	$sql = "SELECT * FROM `store_list` WHERE `id`=".intval($_GET["sid"]);
	$chk = mysqli_fetch_array($mysql->query($sql));

	$detail = json_decode(urldecode($chk["data"]));
?>
<!doctype HTML>
<html>
	<head>
		<title>Info</title>
		<meta charset="UTF-8">
	</head>
	<body ondragstart="return false;" onselectstart="return false;" onmousedown="return false;">
		<div style="position: fixed; top: 0px; left: 0px; width: 100%; height: <?=30*$resolution?>px; background-color: rgba(0,0,0,.7); line-height: <?=30*$resolution?>px; z-index: 1;">
			<div style="margin-left: <?=16*$resolution?>px;">
				<b style="font-size: 13pt; color: #FFFFFF;"><?=$chk["name"]?></b>
				<span style="font-size: 11pt; color: #FFFFFF;">
					&nbsp;|&nbsp;<?=$chk["location"]?>
				</span>
			</div>
		</div>
		<div style="position: absolute; top: 0px; left: 0px; width: 100%; height: <?=30*$resolution?>px; background-color: #000000; line-height: <?=30*$resolution?>px;"></div>
		<!--<img src="./spot.png" style="position: absolute; top: <?=7.5*$resolution?>px; right: <?=16*$resolution?>px; width: <?=11.5*$resolution?>px; height: <?=15*$resolution?>px; z-index: 2;">-->
		<div style="position: absolute; top: <?=30*$resolution?>px; left: 0px; width: 100%;">
			<div style="position: relative;">
				<img id="storeImg" style="position: absolute; top: 0px; left: 0px; width: 100%;">
				<img id="leftArrow" src="btn_goback_xhdpi.png" style="position: absolute; left: 0px;" onclick="swipeleft();">
				<img id="rightArrow" src="btn_goback_xhdpi.png" style="position: absolute; right: 0px; transform: scaleX(-1);" onclick="swiperight();">
				<div id="textbox" style="position: absolute; left: <?=16*$resolution?>px; right: <?=16*$resolution?>px;">
					<b style="font-size: 13pt; color: #23272E;">매장 소개</b>
					<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
					<div style="margin-top: <?=10*$resolution?>px; margin-bottom: <?=20*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
						<?=$chk["description"]?>
					</div>
					<b style="font-size: 13pt; color: #23272E; margin-top: <?=20*$resolution?>px;">주 메뉴</b>
					<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
					<div style="margin-top: <?=10*$resolution?>px; margin-bottom: <?=20*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
						<?=$detail->menu?>
					</div>
					<b style="font-size: 13pt; color: #23272E;">매장 정보</b>
					<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
					<div style="width: 100%; margin-top: <?=10*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
		<?php
					for($i=0;$i<count($detail->info);$i++){
		?>
						<div style="margin-bottom: <?=5.8*$resolution?>px;"><?=$detail->info[$i]->key?> : <?=$detail->info[$i]->value?></div><br>
		<?php
					}

					for($i=0;$i<count($detail->img);$i++){
						$detail->img[$i] = "http://tongs.kr/img/".$detail->img[$i].".png";
					}
		?>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		</script>
		<script src="/assets/plugins/jquery/jquery-1.9.1.min.js"></script>
		<script src="jquery.mobile.custom.min.js"></script>
		<script>
			var img_list = <?=json_encode($detail->img)?>;
			
			storeImg.onload = function(){
				textbox.style.top=(<?=24*$resolution?>+(window.innerWidth/storeImg.clientWidth*storeImg.clientHeight))+'px';
				leftArrow.style.top = rightArrow.style.top = (parseInt(storeImg.clientHeight/2)-18)+'px';
			};
			
			storeImg.src = img_list[0];
			
			var img_index = 0;

			function swipeleft(){
				img_index--;

				if(img_index==-1)
					img_index = img_list.length-1;

				storeImg.src = img_list[img_index];
			}

			function swiperight(){
				img_index++;

				if(img_index==img_list.length)
					img_index = 0;

				storeImg.src = img_list[img_index];
			}
			
			$(storeImg).on("swipeleft",swipeleft);

			$(storeImg).on("swiperight",swiperight);
		</script>
	</body>
</html>