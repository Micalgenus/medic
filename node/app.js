'use strict';

var express = require('express');
var router  = require('./router');
var bodyParser = require('body-parser');

var cluster = require('cluster');
var http = require('http');
var numCPUs = require('os').cpus().length;

if (cluster.isMaster) {
  // Fork workers.
  for (var i = 0; i < numCPUs; i++) {
    cluster.fork();
  }

  cluster.on('death', function(worker) {
    console.log('worker ' + worker.pid + ' died');
  });
} else {
  console.log("worker: %s", process.env.NODE_WORKER_ID);

  var app = express();

  app.use(bodyParser.json());

  router(app);

  app.listen(14420, function () {
    console.log('Node listening on port 14420!');
  });
}
