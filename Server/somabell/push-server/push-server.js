var mysql = require("mysql");
var redis = require("redis");
var gcm = require("node-gcm");

var sDb = redis.createClient();
var mDb = mysql.createConnection({
	"host": "localhost",
	"user": "somabell",
	"password": "A4JQame6nBSuZBbG",
	"database": "somabell"
});

var sender = new gcm.Sender("AIzaSyBCA5cUWdFpxI08sfPDRruY6dtsV_3WGqE");

sDb.on("message",function(channel,message){
	if(channel=="change"){
		var data = JSON.parse(message);

		mDb.query("SELECT * FROM `user_ticket` WHERE `pivot`='"+data["pivot"]+"' AND `store`="+data["store"]+" AND `status`=0",function(err,rows,field){
			if(err){
				throw err;
				return;
			}

			for(var i=0;i<rows.length;i++){
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
	else if(channel=="turn"){
		var data = JSON.parse(message);

		mDb.query("SELECT * FROM `user_list` WHERE `id`="+data["owner"],function(err,rows,field){
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

sDb.subscribe("change");
sDb.subscribe("turn");