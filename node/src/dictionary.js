var request = require('request');
var Iconv = require('iconv').Iconv;
var iconv = new Iconv('EUC-KR', 'UTF-8//TRANSLIT//IGNORE');
var iconv2 = new Iconv('UTF-8', 'EUC-KR');
var iconvl = require('iconv-lite');
var urlencode = require('urlencode');
var jsdom = require("jsdom");

exports.searchDic = function(req, res, next) {

  var htmlBody = req.htmlBody;
  var count = 0;
  var check = false;
  var result = new Array();
  var keyword = req.keyword;

  return jsdom.env(htmlBody, [
    'http://code.jquery.com/jquery-1.5.min.js'
  ], function(err, window) {
    var $ = window.$;


    ccount = 0;
    $('.container').each(function() {
      ccount++;
      if (ccount == 5) {
        count = 0;
        $('td').each(function() {
          count++;
          if (count == 3) {
            var text = $(this).text().trim();
            if (result.indexOf(text) == -1) {
              result.push(text);
            }
          }
        });
      } else if ((ccount >= 6 && ccount <= 17) || (ccount >= 20 && ccount <= 26) || ccount == 31 || ccount == 32) {
        $(this).find('.row').each(function() {
          var eng = "";
          var kor = "";

          count = 0;
          $(this).find('.col-xs-6').each(function() {
            count++;
            if (count == 1) { // eng
              eng = $(this).text().trim();
            } else { // kor
              kor = $(this).text().trim();
            }
          });

          console.log(eng, kor);
          var re1 = new RegExp("^" + keyword + ";", "g");
          var re2 = new RegExp(";" + keyword + ";", "g");
          var re3 = new RegExp(";" + keyword + "$", "g");
          var re4 = new RegExp("^" + keyword + "$", "g");

          if (eng == keyword) {
            if (result.indexOf(kor) == -1) {
              result.push(kor);
            }
          } else if (kor.match(re1) || kor.match(re2) || kor.match(re3) || kor.match(re4)) {

            if (result.indexOf(eng) == -1) {
              result.push(eng);
            }

            if (result.indexOf(kor) == -1 && kor != keyword) {
              result.push(kor);
            }
          }
        });
      }
    });
/*
    $('.col-xs-6').each(function(){
      count++;

      var text = $(this).text();
      text = text.trim();

      //console.log("org:" + text);
      if (text == "영문") {
        count = 1;
      } else if (text == "한글") {
        count = 2;
      } else if (text == "설명") {

      } else if (count % 2 == 1) { // english
        if (text == keyword) {
          //console.log(text);
          check = true;
        }
      } else { // korean
        if (check == true) {
          //console.log(text);
          if (result.indexOf(text) == -1) {
            result.push(text);
          }

          check = false;
        }
      }
    });
*/
    return res.status(200).json(result);
  });
}
exports.translateKeywordEncode = function(req, res, next) {
  var keyword = req.params.keyword;
  req.keyword = keyword;

  // Set the headers
  var headers = {
    'User-Agent':       'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36',
    'Content-Type':     'application/x-www-form-urlencoded;'
  }

  // Configure the request
  var options = {
    url: 'https://php.vrmedic.ml/utf-to-euckr.php',
    method: 'POST',
    encoding: null,
    headers: headers,
    form: {query:""}
  }

  options.form.query = keyword;

  //console.log(options);

  // Start the request
  return request(options, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var htmlBody = iconv.convert(body).toString();
      //console.log(htmlBody);
      req.htmlBody = htmlBody;
      return next();
    }
    req.keyword = keyword;
    return next();
  })
}
