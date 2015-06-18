var mysql = require("mysql");
var redis = require("redis");
var gcm = require("node-gcm");
var express = require("express");
var uniqid = require("uniqid");
var crypto = require("crypto");
var formData = require("form-data");
var https = require("https");

var app = express();

var sDb = redis.createClient();
var mDb = mysql.createConnection({
	"host": "localhost",
	"user": "somabell",
	"password": "A4JQame6nBSuZBbG",
	"database": "somabell"
});

var sender = new gcm.Sender("AIzaSyBCA5cUWdFpxI08sfPDRruY6dtsV_3WGqE");

var buffer = [];

app.get("/:token",function(req,res){
	var token = req.params["token"];
	delete buffer[token];
	res.send({"result_code": 0, "result_msg": "success"});
});

app.listen(8080);

function sendSMS(mdn){
	var time = Math.floor(Date.now()/1000);
	var salt = uniqid();
	var sign = crypto.createHmac("md5","0A8DBF127E3AF5A1D83F7ADC1755099D").update(time+salt).digest("hex");

	var code="";
	for(var i=0;i<6;i++){
		code+=Math.floor(Math.random()*10);
	}

	var form = new formData();
	form.append("api_key","NCS550532CE1B91F");
	form.append("timestamp",time);
	form.append("salt",salt);
	form.append("signature",sign);
	form.append("to",mdn);
	form.append("from","821075346331");
	form.append("text","[tongs.kr] Your Turn. Soon."+code);

	var option = {
		"host": "api.coolsms.co.kr",
		"port": 443,
		"path": "/sms/1.1/send",
		"method": "POST",
		"headers": form.getHeaders()
	};

	var sms_req = https.request(option,function(res){});
	
	form.pipe(sms_req);
}

function sendMsg(token,gcm,data){
	if(buffer[token]){
		sendSMS(buffer[token]);
		delete buffer[token];

		var msg = new gcm.Message({
			"collapseKey": "sms",
			"delayWhileIdle": true,
			"timeToLive": 20,
			"data": data
		});

		sender.send(msg,gcm,20,function(err,result){
			if(err){
				throw err;
				return;
			}

			console.log(result);
		});
	}
}

sDb.on("message",function(channel,message){
	if(channel=="change"){
		var data = JSON.parse(message);

		mDb.query("SELECT * FROM `user_ticket` WHERE `pivot`='"+data["pivot"]+"' AND `store`="+data["store"]+" AND `status`=0",function(err,rows,field){
			if(err){
				throw err;
				return;
			}

			for(var i=0;i<rows.length;i++){
				if(rows[i].owner==-1)
					continue;

				mDb.query("SELECT * FROM `user_list` WHERE `id`="+rows[i].owner,function(err,rows,field){
					if(err){
						throw err;
						return;
					}

					var msg = new gcm.Message({
						"collapseKey": channel,
						"delayWhileIdle": true,
						"timeToLive": 20,
						"data": data
					});

					sender.send(msg,rows[0].gcm,20,function(err,result){
						if(err){
							throw err;
							return;
						}

						console.log(result);
					});
				});
			}
		});

		mDb.query("SELECT * FROM `store_list` WHERE `id`="+data["store"],function(err,rows,field){
			if(err){
				throw err;
				return;
			}

			var msg = new gcm.Message({
				"collapseKey": channel,
				"delayWhileIdle": true,
				"timeToLive": 20,
				"data": data
			});

			sender.send(msg,rows[0].gcm,20,function(err,result){
				if(err){
					throw err;
					return;
				}

				console.log(result);
			});
		});
	}
	else if(channel=="turn"||channel=="coupon"){
		var data = JSON.parse(message);

		console.log(data);

		mDb.query("SELECT * FROM `user_list` WHERE `id`="+data["owner"],function(err,rows,field){
			if(err){
				throw err;
				return;
			}

			console.log(rows);

			var msg = new gcm.Message({
				"collapseKey": channel,
				"delayWhileIdle": true,
				"timeToLive": 20,
				"data": data
			});

			sender.send(msg,rows[0].gcm,20,function(err,result){
				if(err){
					throw err;
					return;
				}

				console.log(result);

				if(channel=="turn"){
					mDb.query("SELECT * FROM `store_list` WHERE `id`="+data["store"],function(err,rows2,field){
						if(err){
							throw err;
							return;
						}

						buffer[rows[0].token] = rows[0].mdn;
						setTimeout(sendMsg,20000,rows[0].token,rows2[0].gcm,data);
					});
				}
			});
		});
	}
	else if(channel=="accept"){
		var data = JSON.parse(message);

		var msg = new gcm.Message({
			"collapseKey": channel,
			"delayWhileIdle": true,
			"timeToLive": 20,
			"data": {"ticket": data.ticket}
		});

		sender.send(msg,data.gcm,20,function(err,result){
			if(err){
				throw err;
				return;
			}

			console.log(result);
		});
	}
	else if(channel=="pop"){
		var data = JSON.parse(message);

		mDb.query("SELECT * FROM `user_list` WHERE `id`="+data["id"],function(err,rows,field){
			if(err){
				throw err;
				return;
			}

			var msg = new gcm.Message({
				"collapseKey": channel,
				"delayWhileIdle": true,
				"timeToLive": 20,
				"data": {}
			});

			sender.send(msg,rows[0].gcm,20,function(err,result){
				if(err){
					throw err;
					return;
				}

				console.log(result);
			});
		});
	}
});

sDb.subscribe("change");
sDb.subscribe("turn");
sDb.subscribe("coupon");
sDb.subscribe("accept");
sDb.subscribe("pop");