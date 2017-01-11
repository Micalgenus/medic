var exec = require('child_process').exec;
var fs = require('fs');
var path = require('path');

exports.createFlac = function(req, res, next) {

  const resourceDir = __dirname + "/../resource";
  const tmpDir = resourceDir + "/upload";
  const audioDir = resourceDir + "/audio";

  var tmpFileName = tmpDir + '/' + req.file.filename;
  var flacFileName = audioDir + '/' + Date.now() + ".flac";

  // wav to flac
  var cmd = 'sox ' + tmpFileName + ' --channels=1 --bits=16 --rate=44100 --endian=little ' + flacFileName;

  return exec(cmd, function(error, stdout, stderr) {
    fs.unlinkSync(tmpFileName);
  }).on('exit', function() { 
    req.fileSrc = flacFileName;
    return next();
  });
}

exports.createAudio = function(req, res, next) {

  const resourceDir = __dirname + "/../resource";
  const tmpDir = resourceDir + "/upload";
  const audioDir = resourceDir + "/audio";

  var tmpFileName = tmpDir + '/' + req.file.filename;
  var newFileName = audioDir + '/' + Date.now() + path.extname(req.file.originalname);

  fs.rename(tmpFileName, newFileName);
  req.oldSrc = newFileName;

  console.log(req.oldSrc);

  return next();
}