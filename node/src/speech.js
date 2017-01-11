var speech = require('google-speech-api');

var opts = {
  //file: '/home/node/public_html/resource/audio/1480185165283.flac',
  key: 'AIzaSyCRrpslBC1RjNob0wPg7nOCT6rZgNiuPKg',
  lang: 'ko-KR'
};

exports.runSpeech = function(req, res, next) {
  opts['file'] = req.oldSrc;
  console.log(opts);

  speech(opts, function (err, results) {
    req.result = results;
    return next();
  });
}

exports.jsonToResult = function(req, res, next) {

  console.log(req.result);

  if (typeof req.result === 'object') {
    var json = req.result;
  } else {
    var json = JSON.parse(req.result);
  }

  var result = "";
  for (var i = 0; i < json.length; i++) {
    var idx = json[i].result_index;
    var r_array = json[i].result;
    result += " ";

    if (r_array.length == 0) {
      continue;
    } else {
      result += r_array[0].alternative[idx].transcript;
    }
  }

  res.status(200).send(result);
}