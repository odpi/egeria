/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const express = require('express');
const app = express();

const PORT = 58080;

var options = {
  dotfiles: 'ignore',
  etag: false,
  extensions: ['htm', 'html', 'js'],
  index: false,
  maxAge: '1d',
  redirect: false,
  setHeaders: function (res, path, stat) {
    res.set('x-timestamp', Date.now())
  }
}

app.use(express.static('public',options));

var http  = require('http').Server(app);
var io    = require('socket.io')(http);
var kafka = require('kafka-node');

globalCohort = "undefined";
consumer     = null;
client       = null;
admin        = null;


// Allow the client to specify the cohort 
app.get('/cohort/:cohortName', function(req, res){
  console.log("handling get request with params "+req.params);
  globalCohort = req.params.cohortName;
  console.log('cohort is ' + req.params.cohortName);
  getKafkaConsumer(globalCohort);
  res.sendFile(__dirname + '/monitor.html');
});


getKafkaConsumer = function(cohortName) {
  console.log('Invoked getKC');
  // Close any previous Kafka resources
  if (consumer != null) consumer.close();
  if (client != null) client.close();
  // Get new Kafka resources
  kafka_endpoint = process.env.KAFKA_ENDPOINT;
  console.log('Will connect to kafka using '+kafka_endpoint);
  client = new kafka.KafkaClient({ kafkaHost : kafka_endpoint });
  console.log('Client connected to kafka using '+kafka_endpoint);
  admin = new kafka.Admin(client);
  console.log('Kafka Admin created - try to list topics...');
  admin.listTopics((err, res) => { console.log('topics', res); });
  console.log('Try to create Kafka Consumer...');
  consumer = new kafka.Consumer( client, [ { topic: 'open-metadata.repository-services.cohort.'+cohortName+'.OMRSTopic', partition: 0 } ], { autoCommit: true });
  consumer.on('error', function() { io.emit('cohort', 'does not exist - please try a different cohort name')});
  consumer.on('message', function(message) {
    try {
        var data = JSON.parse(message.value);
        io.emit('event', data); console.log(data);
    }
    catch (e) {
        if (e instanceof SyntaxError) {
            console.log('Could not make sense of JSON - skipping this event');
        } else {
            console.log('Encountered (non-syntax) error in JSON.parse - skipping this event');
        }
    }
    });

};

io.on('connection', function(socket){
  console.log('client connected');
  io.emit('cohort', globalCohort); 
  socket.on('disconnect', function(){
    console.log('client disconnected');
  });
});

http.listen(PORT, function(){
  console.log('listening on *:58080');
});


