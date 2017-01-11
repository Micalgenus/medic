<?php
// Value Init
$url = "http://m.kmle.co.kr/search.php";
$option->EbookTerminology = 'YES';
$option->DictAll = 'YES';
$option->DictAbbreviationAll = 'YES';
$option->DictDefAll = 'YES';
$option->DictNownuri = 'YES';
$option->DictWordNet = 'YES';
$option->Druginfo = 'YES';
$option->HTMLWebHtdig = 'YES';
$option->Search = iconv("UTF-8", "EUC-KR", $_POST['query']); 

$cURL = curl_init();
curl_setopt($cURL, CURLOPT_URL, $url);
curl_setopt($cURL, CURLOPT_POST, 1);
curl_setopt($cURL, CURLOPT_POSTFIELDS, $option);
curl_setopt($cURL, CURLOPT_POSTFIELDSIZE, 0);
curl_setopt($cURL, CURLOPT_RETURNTRANSFER, 1);

$res = curl_exec($cURL);
print_r( $res ); 
?>