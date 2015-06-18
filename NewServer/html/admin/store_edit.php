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
	
	ob_start();
?>
<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">매장정보 수정</h4>
	</div>
	<div class="panel-body">
		<form action="store_submit.php" method="POST" class="form-horizontal" onsubmit="return store_submit();">
			<div class="form-group">
				<label class="col-md-3 control-label">
					매장 이름
				</label>
				<div class="col-md-9">
					<input type="text" name="name" class="form-control" placeholder="<?=$_SESSION["user"]["name"]?>" value="<?=$_SESSION["user"]["name"]?>">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					매장 위치
				</label>
				<div class="col-md-9">
					<input type="text" name="location" class="form-control" placeholder="<?=$_SESSION["user"]["location"]?>" value="<?=$_SESSION["user"]["location"]?>">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					매장 소개
				</label>
				<div class="col-md-9">
					<textarea name="description" class="form-control" rows="5"><?=$_SESSION["user"]["description"]?></textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					주 메뉴
				</label>
				<div class="col-md-9">
					<textarea name="detail_menu" class="form-control" rows="5"><?=$detail->menu?></textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">
					매장 정보
				</label>
				<div class="col-md-9">
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
				for($i=0;$i<count($detail->info);$i++){
	?>
								<tr>
									<td><a href="#" class="storeInfo" data-type="text" data-title="항목을 입력하세요" data-emptytext="항목을 입력하세요"><?=$detail->info[$i]->key?></a></td>
									<td><a href="#" class="storeInfo" data-type="text" data-title="내용을 입력하세요" data-emptytext="내용을 입력하세요"><?=$detail->info[$i]->value?></a></td>
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
				<label class="col-md-3 control-label">
					저장
				</label>
				<div class="col-md-9">
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

<div class="panel panel-inverse">
	<div class="panel-heading">
		<div class="panel-heading-btn">
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
			<a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
		</div>
		<h4 class="panel-title">매장 이미지</h4>
	</div>
	<div class="panel-body">
		<form id="fileupload" action="/admin/upload.php" method="POST" enctype="multipart/form-data">
			<div class="row fileupload-buttonbar">
				<div class="col-md-7">
					<span class="btn btn-success fileinput-button">
						<i class="fa fa-plus"></i>
						<span>Add files...</span>
						<input type="file" name="files[]" multiple>
					</span>
					<button type="submit" class="btn btn-primary start">
						<i class="fa fa-upload"></i>
						<span>Start upload</span>
					</button>
					<button type="reset" class="btn btn-warning cancel">
						<i class="fa fa-ban"></i>
						<span>Cancel upload</span>
					</button>
					<button type="button" class="btn btn-danger delete">
						<i class="glyphicon glyphicon-trash"></i>
						<span>Delete</span>
					</button>
					<span class="fileupload-process"></span>
				</div>
				<div class="col-md-5 fileupload-progress fade">
					<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
						<div class="progress-bar progress-bar-success" style="width:0%;"></div>
					</div>
					<div class="progress-extended">&nbsp;</div>
				</div>
			</div>
			<table role="presentation" class="table table-striped">
				<tbody class="files">
<?php
			function human_filesize($bytes, $decimals = 2) {
			    $size = array('B','kB','MB','GB','TB','PB','EB','ZB','YB');
			    $factor = floor((strlen($bytes) - 1) / 3);
			    return sprintf("%.{$decimals}f", $bytes / pow(1024, $factor)) . " " .  @$size[$factor];
			}

			for($i=0;$i<count($detail->img);$i++){
?>
					<tr class="template-download fade in">
						<td>
							<span class="preview">
								<a data-gallery="" download="<?=$detail->img[$i]?>.png" title="<?=$detail->img[$i]?>.png" href="http://tongs.kr/img/<?=$detail->img[$i]?>.png">
									<img src="http://tongs.kr/img/<?=$detail->img[$i]?>.preview.png">
								</a>
							</span>
						</td>
						<td>
							<span class="name">
								<a data-gallery="" download="<?=$detail->img[$i]?>.png" title="<?=$detail->img[$i]?>.png" href="http://tongs.kr/img/<?=$detail->img[$i]?>.png">
									<?=$detail->img[$i]?>.png
								</a>
							</span>
						</td>
						<td>
							<span class="size">
								<?=human_filesize(filesize("../img/{$detail->img[$i]}.png"))?>
							</span>
						</td>
						<td>
							<button class="btn btn-danger delete" data-url="http://tongs.kr/admin/delete.php?name=<?=$detail->img[$i]?>" data-type="DELETE">
								<i class="glyphicon glyphicon-trash"></i>
								<span>
									Delete
								</span>
							</button>
							<input type="checkbox" value="1" name="delete" class="toggle">
						</td>
					</tr>
<?php
			}
?>
				</tbody>
			</table>
		</form>
	</div>
</div>
<?php
	$content = ob_get_contents();
	ob_end_clean();

	ob_start();
?>
<script id="template-upload" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
        <tr class="template-upload fade">
            <td class="col-md-1">
                <span class="preview"></span>
            </td>
            <td>
                <p class="name">{%=file.name%}</p>
                <strong class="error text-danger"></strong>
            </td>
            <td>
                <p class="size">Processing...</p>
                <div class="progress progress-striped active"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
            </td>
            <td>
                {% if (!i && !o.options.autoUpload) { %}
                    <button class="btn btn-primary btn-sm start" disabled>
                        <i class="fa fa-upload"></i>
                        <span>Start</span>
                    </button>
                {% } %}
                {% if (!i) { %}
                    <button class="btn btn-white btn-sm cancel">
                        <i class="fa fa-ban"></i>
                        <span>Cancel</span>
                    </button>
                {% } %}
            </td>
        </tr>
    {% } %}
</script>
<script id="template-download" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
        <tr class="template-download fade">
            <td>
                <span class="preview">
                    {% if (file.thumbnailUrl) { %}
                        <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                    {% } %}
                </span>
            </td>
            <td>
                <p class="name">
                    {% if (file.url) { %}
                        <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                    {% } else { %}
                        <span>{%=file.name%}</span>
                    {% } %}
                </p>
                {% if (file.error) { %}
                    <div><span class="label label-danger">Error</span> {%=file.error%}</div>
                {% } %}
            </td>
            <td>
                <span class="size">{%=o.formatFileSize(file.size)%}</span>
            </td>
            <td>
                {% if (file.deleteUrl) { %}
                    <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                        <i class="glyphicon glyphicon-trash"></i>
                        <span>Delete</span>
                    </button>
                    <input type="checkbox" name="delete" value="1" class="toggle">
                {% } else { %}
                    <button class="btn btn-warning cancel">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                {% } %}
            </td>
        </tr>
    {% } %}
</script>
<script type="text/javascript">
	var handleJqueryFileUpload = function() {
	    $("#fileupload").fileupload({
	        autoUpload: false,
	        disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),
	        maxFileSize: 5e6,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
	    });
	    $("#fileupload").fileupload("option", "redirect", window.location.href.replace(/\/[^\/]*$/, "/cors/result.html?%s"));
	    if ($.support.cors) {
	        $.ajax({
	            type: "HEAD"
	        }).fail(function() {
	            $('<div class="alert alert-danger"/>').text("Upload server currently unavailable - " + new Date).appendTo("#fileupload")
	        })
	    }
	    $("#fileupload").addClass("fileupload-processing");
	    $.ajax({
	        url: $("#fileupload").fileupload("option", "url"),
	        dataType: "json",
	        context: $("#fileupload")[0]
	    }).always(function() {
	        $(this).removeClass("fileupload-processing")
	    }).done(function(e) {
	        $(this).fileupload("option", "done").call(this, $.Event("done"), {
	            result: e
	        })
	    })
	};
	var FormMultipleUpload = function() {
	    "use strict";
	    return {
	        init: function() {
	            handleJqueryFileUpload()
	        }
	    }
	}();

	FormMultipleUpload.init();

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
<?php
	$script = ob_get_contents();
	ob_end_clean();

	include("layout.php");
?>