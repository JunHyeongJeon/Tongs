var app = require("express")();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var gcm = require("node-gcm");

app.get("/",function(req,res){
	res.sendFile(__dirname + '/index.html');
});

var sender = new gcm.Sender("AIzaSyBCA5cUWdFpxI08sfPDRruY6dtsV_3WGqE");

io.on("connection", function(socket){
  socket.on("gcm",function(data){
  	var msg = new gcm.Message({
		"collapseKey": "calc",
		"delayWhileIdle": true,
		"timeToLive": 10,
		"data": JSON.parse(data.json)
	});

	sender.send(msg,data.token,10,function(err,result){
		console.log(err);
		console.log(result);
	});
  });
});

http.listen(8888,function(){
	console.log("listening on *:8888");
});