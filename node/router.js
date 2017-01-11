/**
 * Express Router
 */
'use strict';

const multer = require('multer');

const Speech = require('./src/speech');
const Trend = require('./src/trend');
const Audio = require('./src/audio');
const Dictionary = require('./src/dictionary');

const Test = require('./src/test');

const AudioMulter = multer({ dest: __dirname + "/resource/upload"});

module.exports = function(app) {
  app.get('/test', function(req, res) {
    res.status(200).send(
      '<form action="/speech" method="post" enctype="multipart/form-data">' +
      ' <input type="file" name="audio" />' +
      ' <input type="submit" /> ' +
      '</form>'
    );
  });

  app.get('/j', Test.test, Speech.jsonToResult);

  app.get('/k', Test.test2, Speech.runSpeech, Speech.jsonToResult);

  app.get('/l', Test.test3); 

  app.post('/', AudioMulter.single('audio'), Audio.createAudio, function(req, res) {
    res.status(200).send(req.oldSrc);
  });

  app.get('/trend', Trend.runTrend);

  app.post('/speech', AudioMulter.single('audio'), Audio.createAudio, Speech.runSpeech, Speech.jsonToResult);
  
  app.get('/search/:keyword', Dictionary.translateKeywordEncode, Dictionary.searchDic);
}