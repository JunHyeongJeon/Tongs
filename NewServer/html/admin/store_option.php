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
	$high = json_decode(urldecode($_SESSION["user"]["high"]));

	if(empty($detail->time))
		$detail->time=0;
	if(empty($detail->wait))
		$detail->wait=1;
	else
		$detail->wait = intval($detail->wait);
	
	ob_start();
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">옵션 설정</h4>
	</div>
	<div class="panel-body">
		<form action="store_save.php" method="POST" class="form-horizontal" onsubmit="return submitSave();">
			<div class="form-group">
				<label class="col-md-3 control-label">
					대기열 추가 허용 방법
				</label>
				<div class="col-md-9">
					<label class="checkbox-inline">
						<input type="checkbox" id="wait1"<?=($detail->wait&1)==1?" checked":""?>>
						매장 직접 추가
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" id="wait2"<?=($detail->wait&2)==2?" checked":""?>>
						비콘 추가 허용
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" id="wait3"<?=($detail->wait&4)==4?" checked":""?>>
						원격 추가 허용
					</label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					평균 대기 시간
				</label>
				<div class="col-md-9">
					<div class="input-group">
						<input type="text" name="time" class="form-control" placeholder="<?=$detail->time?>" value="<?=$detail->time?>">
						<span class="input-group-addon">
							분
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					저장
				</label>
				<div class="col-md-9">
					<input type="hidden" name="wait" id="wait4">
					<button class="btn btn-primary" type="submit">
						<i class="glyphicon glyphicon-floppy-disk"></i>
						<span>
							Save
						</span>
					</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">비컨 설정</h4>
	</div>
	<div class="panel-body">
		<form action="store_beacon.php" method="POST" class="form-horizontal">
<?php
	$sql = "SELECT * FROM `store_beacon` WHERE `owner`={$_SESSION["user"]["id"]}";
	$beacon = mysqli_fetch_array($mysql->query($sql));
?>
			<div class="form-group">
				<label class="col-md-3 control-label">
					Beacon Major
				</label>
				<div class="col-md-9">
					<input type="text" name="major" class="form-control" placeholder="<?=$beacon["major"]?>" value="<?=$beacon["major"]?>">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					Beacon Minor
				</label>
				<div class="col-md-9">
					<input type="text" name="minor" class="form-control" placeholder="<?=$beacon["minor"]?>" value="<?=$beacon["minor"]?>">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					저장
				</label>
				<div class="col-md-9">
					<button class="btn btn-primary" type="submit">
						<i class="glyphicon glyphicon-floppy-disk"></i>
						<span>
							Save
						</span>
					</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">고급 설정</h4>
	</div>
	<div class="panel-body">
		<form action="store_high.php" method="POST" class="form-horizontal" onsubmit="return store_submit();">
			<div class="form-group">
				<div class="container-fluid">
					<div class="table-responsive">
						<table id="user" class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>항목</th>
									<th>내용</th>
									<th>동작</th>
								</tr>
							</thead>
							<tbody id="storeInfoRoot">
	<?php
				for($i=0;$i<count($high);$i++){
	?>
								<tr>
									<td><a href="#" class="storeInfo" data-type="text" data-title="항목을 입력하세요" data-emptytext="항목을 입력하세요"><?=$high[$i]->key?></a></td>
									<td><a href="#" class="storeInfo" data-type="text" data-title="내용을 입력하세요" data-emptytext="내용을 입력하세요"><?=$high[$i]->value?></a></td>
									<td>
										<button class="btn btn-danger" type="button" onclick="this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);">
											<i class="glyphicon glyphicon-trash"></i>
											<span>
												Delete
											</span>
										</button>
									</td>
								</tr>
	<?php
				}
	?>
								<tr>
									<td><a href="#" id="infoHeader" class="storeInfo" data-type="text" data-title="항목을 입력하세요" data-emptytext="항목을 입력하세요"></a></td>
									<td><a href="#" id="infoContent" class="storeInfo" data-type="text" data-title="내용을 입력하세요" data-emptytext="내용을 입력하세요"></a></td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="container">
					<input type="hidden" name="detail_info" id="detail_info">
					<button class="btn btn-primary" type="submit">
						<i class="glyphicon glyphicon-floppy-disk"></i>
						<span>
							Save
						</span>
					</button>
				</div>
			</div>
		</form>
	</div>
</div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>
<script type="text/javascript">
	$.fn.editable.defaults.mode = "inline";
	$.fn.editable.defaults.inputclass = "form-control input-sm";
	var sList = document.getElementsByClassName("storeInfo");
	for(var i=0;i<sList.length;i++){
		$(sList[i]).editable();
	}
</script>
<script type="text/javascript">
	var infoH = "";
	var infoC = "";

	function infoHeaderChange(e,params){
		infoH = params.newValue;
		infoChange(true);
	}
	function infoContentChange(e,params){
		infoC = params.newValue;
		infoChange(false);
	}
	function infoChange(header){
		if((!header&&$(storeInfoRoot.children[storeInfoRoot.children.length-1].children[0].children[0]).hasClass("editable-open")===false&&$(storeInfoRoot.children[storeInfoRoot.children.length-1].children[0].children[0]).hasClass("editable-empty")===false)||(header&&$(storeInfoRoot.children[storeInfoRoot.children.length-1].children[1].children[0]).hasClass("editable-open")===false&&$(storeInfoRoot.children[storeInfoRoot.children.length-1].children[1].children[0]).hasClass("editable-empty")===false)){
			var ctx = storeInfoRoot;
			var child = ctx.children;
			$(ctx).append("<tr><td><a href=\"#\" class=\"storeInfo\" data-type=\"text\" data-title=\"항목을 입력하세요\" data-emptytext=\"항목을 입력하세요\">"+infoH+"</a></td><td><a href=\"#\" class=\"storeInfo\" data-type=\"text\" data-title=\"내용을 입력하세요\" data-emptytext=\"내용을 입력하세요\">"+infoC+"</a></td><td><button class=\"btn btn-danger\" type=\"button\" onclick=\"this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);\"><i class=\"glyphicon glyphicon-trash\"></i><span>&nbsp;Delete</span></button></td></tr>").next();
			$(child[child.length-1].children[0].children[0]).editable();
			$(child[child.length-1].children[1].children[0]).editable();

			$(ctx).append("<tr><td><a href=\"#\" id=\"infoHeader\" class=\"storeInfo\" data-type=\"text\" data-title=\"항목을 입력하세요\" data-emptytext=\"항목을 입력하세요\"></a></td><td><a href=\"#\" id=\"infoContent\" class=\"storeInfo\" data-type=\"text\" data-title=\"내용을 입력하세요\" data-emptytext=\"내용을 입력하세요\"></a></td><td></td></tr>").next();

			$(child[child.length-1].children[0].children[0]).editable();
			$(child[child.length-1].children[1].children[0]).editable();

			$(child[child.length-1].children[0].children[0]).on("save",infoHeaderChange);
			$(child[child.length-1].children[1].children[0]).on("save",infoContentChange);

			$(child[child.length-3]).remove();
		}
	}

	$("#infoHeader").on("save",infoHeaderChange);
	$("#infoContent").on("save",infoContentChange);
</script>
<script type="text/javascript">
	function store_submit(){
		var ctx = storeInfoRoot.children;
		var json = [];

		for(var i=0;i<ctx.length-1;i++){
			var key = $(ctx[i].children[0].children[0]).text();
			var value = $(ctx[i].children[1].children[0]).text();

			json.push({"key": key, "value": value});
		}

		detail_info.value = JSON.stringify(json);

		return true;
	}
</script>
<script type="text/javascript">
		function submitSave(){
			var flag = 0;

			if(wait1.checked)
				flag = flag|1;
			if(wait2.checked)
				flag = flag|2;
			if(wait3.checked)
				flag = flag|4;

			wait4.value = flag;

			return true;
		}
	</script>
<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>