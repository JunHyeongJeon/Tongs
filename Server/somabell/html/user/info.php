<?php
	$width = intval($_GET["width"]);
	$resolution = floatval($_GET["resolution"]);

	$img_size = getimagesize("./sample.png");
?>
<!doctype HTML>
<html>
	<head>
		<title>Info</title>
		<meta charset="UTF-8">
	</head>
	<body>
		<div style="position: absolute; top: 0px; left: 0px; width: 100%; height: <?=30*$resolution?>px; background-color: rgba(0,0,0,.7); line-height: <?=30*$resolution?>px; z-index: 1;">
			<div style="margin-left: <?=16*$resolution?>px;">
				<b style="font-size: 13pt; color: #FFFFFF;">[강남점] delacourt</b>
				<span style="font-size: 11pt; color: #FFFFFF;">
					&nbsp;|&nbsp;서울시 강남구 논현동 123-4
				</span>
			</div>
		</div>
		<img src="./spot.png" style="position: absolute; top: <?=7.5*$resolution?>px; right: <?=16*$resolution?>px; width: <?=11.5*$resolution?>px; height: <?=15*$resolution?>px; z-index: 2;">
		<img src="./sample.png" style="position: absolute; top: 0px; left: 0px; width: 100%;">
		<div id="textbox" style="position: absolute; left: <?=16*$resolution?>px; right: <?=16*$resolution?>px;">
			<b style="font-size: 13pt; color: #23272E;">매장 소개</b>
			<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
			<div style="margin-top: <?=10*$resolution?>px; margin-bottom: <?=20*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
				디저트 카페 전문점
			</div>
			<b style="font-size: 13pt; color: #23272E; margin-top: <?=20*$resolution?>px;">주 메뉴</b>
			<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
			<div style="margin-top: <?=10*$resolution?>px; margin-bottom: <?=20*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
				프랑스 디저트 전문 (케이크/초콜렛/마카롱 등)
			</div>
			<b style="font-size: 13pt; color: #23272E;">매장 정보</b>
			<div style="width: 100%; height: <?=0.5*$resolution?>px; background-color: #CCCCCC; margin-top: <?=7*$resolution?>px;"></div>
			<div style="width: 100%; margin-top: <?=10*$resolution?>px; font-size: 11pt; color: #GGGGGG;">
				<div style="margin-bottom: <?=5.8*$resolution?>px;">운영시간 08:00 ~ 23:00</div>
				<div style="margin-bottom: <?=5.8*$resolution?>px;">휴무일 연중무휴</div>
				<div style="margin-bottom: <?=5.8*$resolution?>px;">전화번호 02-1234-1234</div>
			</div>
		</div>
		<script type="text/javascript">
			textbox.style.top=(<?=24*$resolution?>+(window.innerWidth/<?=$img_size[0]?>*<?=$img_size[1]?>))+'px';
		</script>
	</body>
</html>