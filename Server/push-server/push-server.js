var mysql = require("mysql");
var redis = require("redis");
var gcm = require("node-gcm");

var rDb = redis.createClient();
var sDb = redis.createClient();
var mDb = mysql.createConnection({
	"host": "localhost",
	"user": "root",
	"password": "soma2015",
	"database": "somabell"
});

var sender = new gcm.Sender("AIzaSyBCA5cUWdFpxI08sfPDRruY6dtsV_3WGqE");

sDb.on("message",function(channel,message){
	if(channel=="calc"){
		var store = JSON.parse(message);
		store.json = JSON.parse(store.json);

		rDb.lrange("list_"+store.id,0,-1,function(err,value){
			store["current_num"] = JSON.parse(value[0]).ticket-1;

			for(var i=0;i<value.length;i++){
				value[i] = JSON.parse(value[i]);
				var gcmId = value[i].gcm;
				value[i].gcm = "";

				var msg = new gcm.Message({
					"collapseKey": "calc",
					"delayWhileIdle": true,
					"timeToLive": 10,
					"data": {"result_code": 0, "result_msg": "git", "ticket": value[i], "store": store.json}
				});

				sender.send(msg,gcmId,10,function(err,result){
					console.log("user");
					console.log(err);
					console.log(result);
				});
			}

			mDb.query("SELECT * FROM `somabell_store_email` WHERE `id`="+store.owner,function(err,rows,field){
				if(err){
					res.send({"result_code": -99, "result_msg": "Database Error"});
					return;
				}

				var msg = new gcm.Message({
					"collapseKey": "list",
					"delayWhileIdle": true,
					"timeToLive": 10,
					"data": {
						"data": value
					}
				});

				sender.send(msg,rows[0].gcm,1,function(err,result){
					console.log("store");
					console.log(err);
					console.log(result);
				});
			});
		});
	}
});

sDb.subscribe("calc");