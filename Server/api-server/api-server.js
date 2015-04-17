var express = require("express");
var redis = require("redis");
var https = require("https");
var uniqid = require("uniqid");
var querystring = require("querystring");
var crypto = require("crypto");
var formData = require("form-data");
var mysql = require("mysql");
var nodemailer = require("nodemailer");

var app = express();
var rDb = redis.createClient();
var mDb = mysql.createConnection({
	"host": "localhost",
	"user": "root",
	"password": "soma2015",
	"database": "somabell"
});

var mail =  nodemailer.createTransport("SMTP",{
	"service": "Gmail",
	"auth": {
		"user": "somabell15@gmail.com",
		"pass": "soma2015"
	}
});

function sendSMSAuth(mdn){
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
	form.append("text","인증번호: "+code);

	var option = {
		"host": "api.coolsms.co.kr",
		"port": 443,
		"path": "/sms/1.1/send",
		"method": "POST",
		"headers": form.getHeaders()
	};

	var sms_req = https.request(option,function(res){});
	
	form.pipe(sms_req);

	return code;
}

function sendEmailAuth(email){
	var code="";
	for(var i=0;i<6;i++){
		code+=Math.floor(Math.random()*10);
	}

	mail.sendMail({
		"from": "somabell15@gmail.com",
		"to": email,
		"subject": "가입 인증번호 안내메일입니다",
		"text": "귀하의 인증번호는 "+code+" 입니다."
	});

	return code;
}

app.get("/user/auth/sms_request",function(req,res){
	code = sendSMSAuth(req.query.mdn);

	rDb.set("user_auth_"+req.query.mdn,code);
	rDb.expire("user_auth_"+req.query.mdn,180);

	res.send({"result_code": 0, "result_msg": "success"});
});

app.get("/user/auth/sms_check",function(req,res){
	rDb.get("user_auth_"+req.query.mdn,function(err,value){
		if(value==req.query.code){
			mDb.query("SELECT count(*) as cnt FROM `somabell_user` WHERE `mdn`='"+req.query.mdn+"'",function(err,rows,field){
				if(err){
					res.send({"result_code": -99, "result_msg": "Database Error"});
					return;
				}

				var date = Math.floor(Date.now()/1000);
				var token = crypto.createHash("sha1").update(req.query.mdn+rows[0].cnt+req.query.code+date).digest("hex");
				var sql = "UPDATE `somabell_user` SET `mdn`='"+req.query.mdn+"', `token`='"+token+"', `date`="+date+" WHERE `mdn`='"+req.query.mdn+"'";

				if(rows[0].cnt==0)
					sql = "INSERT INTO `somabell_user` (`mdn`,`token`,`date`) VALUES ('"+req.query.mdn+"','"+token+"',"+date+")";

				mDb.query(sql,function(err,rows,field){
					if(err){
						res.send({"result_code": -98, "result_msg": "Database Error"});
						return;
					}
					
					mDb.query("SELECT * FROM `somabell_user` WHERE `mdn`='"+req.query.mdn+"'",function(err,rows,field){
						if(err){
							res.send({"result_code": -97, "result_msg": "Database Error"});
							return;
						}

						res.send({"result_code": 0, "result_msg": "success", "token": rows[0].token, "uid": rows[0].id});
					});
				});
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "invaild code"});
	});
});

app.get("/user/auth/token_vaild",function(req,res){
	mDb.query("SELECT * FROM `somabell_user` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0])
			res.send({"result_code": 0, "result_msg": "success", "token_type": 1,"uid": rows[0].id});
		else{
			mDb.query("SELECT * FROM `somabell_user_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}

				if(rows[0])
					res.send({"result_code": 0, "result_msg": "success", "token_type": 2,"uid": rows[0].id});
				else
					res.send({"result_code": -1, "result_msg": "fail"});
			});
		}
	});
});

app.get("/user/join",function(req,res){
	mDb.query("SELECT * FROM `somabell_user` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("UPDATE `somabell_user` SET `data`='"+req.query.data+"' WHERE `token`='"+req.query.token+"'",function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}

				res.send({"result_code": 0, "result_msg": "success"});
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/user/waiting/get",function(req,res){
	mDb.query("SELECT * FROM `somabell_user` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			rDb.get("user_"+rows[0].id,function(err,value){
				res.send({"result_code": 0, "result_msg": "git", "ticket": JSON.parse(value)});
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/user/auth/email_request",function(req,res){
	mDb.query("SELECT * FROM `somabell_user_email` WHERE `email`='"+req.query.email+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0])
			res.send({"result_code": -1, "result_msg": "fail"});
		else{
			code = sendEmailAuth(req.query.email);

			rDb.set("user_auth_"+req.query.email,code);
			rDb.expire("user_auth_"+req.query.email,180);

			res.send({"result_code": 0, "result_msg": "success"});
		}
	});
});

app.get("/user/auth/email_check",function(req,res){
	rDb.get("user_auth_"+req.query.email,function(err,value){
		if(value==req.query.code){
			mDb.query("SELECT count(*) as cnt FROM `somabell_user_email` WHERE `email`='"+req.query.email+"'",function(err,rows,field){
				if(err){
					res.send({"result_code": -99, "result_msg": "Database Error"});
					return;
				}

				if(rows[0].cnt==0){
					var date = Math.floor(Date.now()/1000);
					var token = crypto.createHash("sha1").update(req.query.email+req.query.code+date).digest("hex");
					var pass = crypto.createHash("sha1").update(req.query.password).digest("hex");
					var sql = "INSERT INTO `somabell_user_email` (`token`,`email`,`password`,`date`) VALUES ('"+token+"','"+req.query.email+"','"+pass+"',"+date+")";

					mDb.query(sql,function(err,rows,field){
						if(err){
							res.send({"result_code": -99, "result_msg": "Database Error"});
							return;
						}
						
						mDb.query("SELECT * FROM `somabell_user_email` WHERE `email`='"+req.query.email+"'",function(err,rows,field){
							if(err){
								res.send({"result_code": -98, "result_msg": "Database Error"});
								return;
							}

							res.send({"result_code": 0, "result_msg": "success", "token": rows[0].token, "uid": rows[0].id});
						});
					});
				}
				else
					res.send({"result_code": -2, "result_msg": "already registered"});
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "invaild code"});
	});
});

app.get("/store/auth/email_request",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `email`='"+req.query.email+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0])
			res.send({"result_code": -1, "result_msg": "fail"});
		else{
			code = sendEmailAuth(req.query.email);

			rDb.set("store_auth_"+req.query.email,code);

			var date = Math.floor(Date.now()/1000);
			var token = crypto.createHash("sha1").update(req.query.email+date).digest("hex");
			var sql = "INSERT INTO `somabell_store_email` (`token`,`email`,`date`) VALUES ('"+token+"','"+req.query.email+"',"+date+")";
			
			mDb.query(sql,function(err,rows,field){
				if(err){
					res.send({"result_code": -99, "result_msg": "Database Error"});
					return;
				}
				
				mDb.query("SELECT * FROM `somabell_store_email` WHERE `email`='"+req.query.email+"'",function(err,rows,field){
					if(err){
						res.send({"result_code": -98, "result_msg": "Database Error"});
						return;
					}

					res.send({"result_code": 0, "result_msg": "success", "token": rows[0].token});
				});
			});
		}
	});
});

app.get("/store/auth/sms_request",function(req,res){
	code = sendSMSAuth(req.query.mdn);

	rDb.set("store_auth_"+req.query.mdn,code);
	rDb.expire("store_auth_"+req.query.mdn,180);

	res.send({"result_code": 0, "result_msg": "success"});
});

app.get("/store/auth/sms_check",function(req,res){
	rDb.get("store_auth_"+req.query.mdn,function(err,value){
		if(value==req.query.code){
			mDb.query("SELECT count(*) as cnt FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
				if(err){
					res.send({"result_code": -99, "result_msg": "Database Error"});
					return;
				}

				if(rows[0].cnt==0){
					res.send({"result_code": -1, "result_msg": "Not registered"});
				}
				else{
					var pass = crypto.createHash("sha1").update(req.query.password).digest("hex");

					mDb.query("UPDATE `somabell_store_email` SET `password`='"+pass+"', `mdn`='"+req.query.mdn+"' WHERE `token`='"+req.query.token+"'",function(err,rows,field){
						if(err){
							res.send({"result_code": -98, "result_msg": "Database Error"});
							return;
						}
						else{
							mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
								if(err){
									res.send({"result_code": -97, "result_msg": "Databagse Error"});
									return;
								}
								
								res.send({"result_code": 0, "result_msg": "success", "token": rows[0].token, "uid": rows[0].id});
							});
						}
					});
				}
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "invaild code"});
	});
});

app.get("/store/auth/token_vaild",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0])
			res.send({"result_code": 0, "result_msg": "success", "uid": rows[0].id});
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/store/list/get",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("SELECT * FROM `somabell_store_store` WHERE `owner`="+rows[0].id,function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}
				res.send({"result_code": 0, "result_msg": "success", "store": rows});
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/store/list/put",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("INSERT INTO `somabell_store_store` SET ?",{"owner": rows[0].id, "json": JSON.stringify({"brand": req.query.brand,"group": req.query.group,"office_call": req.query.office_call,"office_address": req.query.office_address,"store_image": req.query.store_image,"store_info": req.query.store_info,"office_kind": req.query.office_kind})},function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}
				else{
					mDb.query("SELECT LAST_INSERT_ID() as id",function(err,rows,field){
						if(err){
							res.send({"result_code": -97, "result_msg": "Database Error"});
							return;
						}
						else{
							res.send({"result_code": 0, "result_msg": "success", "sid": rows[0].id});
						}
					});
				}
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/store/list/put",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,field){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("INSERT INTO `somabell_store_store` SET ?",{"owner": rows[0].id, "json": JSON.stringify({"brand": req.query.brand,"group": req.query.group,"office_call": req.query.office_call,"office_address": req.query.office_address,"store_image": req.query.store_image,"store_info": req.query.store_info,"office_kind": req.query.office_kind})},function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}
				else{
					mDb.query("SELECT LAST_INSERT_ID() as id",function(err,rows,field){
						if(err){
							res.send({"result_code": -97, "result_msg": "Database Error"});
							return;
						}
						else{
							res.send({"result_code": 0, "result_msg": "success", "sid": rows[0].id});
						}
					});
				}
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/store/store/get",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,filed){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("SELECT * FROM `somabell_store_store` WHERE `id`="+req.query.sid+" AND `owner`="+rows[0].id,function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}
				else{
					rDb.llen("list_"+req.query.sid,function(err,extraPeople){
						if(extraPeople==0){
							res.send({"result_code": 0, "result_msg": "success", "sid": req.query.sid, "extraPeople": extraPeople});
							return;
						}

						rDb.lrange("list_"+req.query.sid,0,-1,function(err,value){
							for(var i=0;i<value.length;i++){
								value[i] = JSON.parse(value[i]);
							}

							res.send({"result_code": 0, "result_msg": "success", "sid": req.query.sid, "extraPeople": extraPeople, "tickets": value});
						});
					});
				}
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

app.get("/store/store/put",function(req,res){
	mDb.query("SELECT * FROM `somabell_store_email` WHERE `token`='"+req.query.token+"'",function(err,rows,filed){
		if(err){
			res.send({"result_code": -99, "result_msg": "Database Error"});
			return;
		}

		if(rows[0]){
			mDb.query("SELECT * FROM `somabell_store_store` WHERE `id`="+req.query.sid+" AND `owner`="+rows[0].id,function(err,rows,field){
				if(err){
					res.send({"result_code": -98, "result_msg": "Database Error"});
					return;
				}
				else{
					mDb.query("SELECT * FROM `somabell_user` WHERE id="+req.query.uid,function(err,rows,field){
						if(err){
							res.send({"result_code": -97,"result_msg": "Database Error"});
							return;
						}

						var today = new Date();
						var dd = today.getDate();
						var mm = today.getMonth()+1;
						var yyyy = today.getFullYear()+1;

						if(dd<10)
							dd="0"+dd;
						if(mm<10)
							mm="0"+mm;

						rDb.rpush("list_"+yyyy+mm+dd+"_"+req.query.sid,JSON.stringify({"uid": req.query.uid,"num": req.query.num, "mdn": rows[0].mdn, "createTime": Math.floor(Date.now()/1000)}));
						rDb.llen("list_"+yyyy+mm+dd+"_"+req.query.sid,function(err,value){
							rDb.set("user_"+req.query.uid,JSON.stringify({"sid": req.query.sid,"ticket": value,"uid": req.query.uid,"num": req.query.num, "mdn": rows[0].mdn, "createTime": Math.floor(Date.now()/1000)}));
							rDb.rpush("list_"+req.query.sid,JSON.stringify({"ticket": value,"uid": req.query.uid,"num": req.query.num, "mdn": rows[0].mdn, "createTime": Math.floor(Date.now()/1000)}));
							res.send({"result_code": 0, "result_msg": "success"});
						});
					});

				}
			});
		}
		else
			res.send({"result_code": -1, "result_msg": "fail"});
	});
});

mDb.connect();
app.listen(8080);