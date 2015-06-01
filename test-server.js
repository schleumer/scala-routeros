/*
In the node.js intro tutorial (http://nodejs.org/), they show a basic tcp 
server, but for some reason omit a client connecting to it.  I added an 
example at the bottom.

Save the following server in example.js:
*/
 
var net = require('net');
 
var server = net.createServer(function(socket) {
  console.log("%s client connected", new Date());
  socket.on('data', function(data) {
    console.log(data.toString())
  });
  socket.write('Echo server\r\n');
});
 
server.listen(8729, '127.0.0.1');