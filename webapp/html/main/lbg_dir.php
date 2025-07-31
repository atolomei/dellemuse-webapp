<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=3">
<title>Generate player playlist from folder</title>
<style>
.error {
	width:180px;
	padding:10px;
	border:2px solid #F00;
	color:#000000;
	font-weight:bold;
	}
</style>
<script>
function select_textarea_content() {
    document.getElementById("myTextarea").select();
	document.getElementById('copy_message').innerText="CTRL+C to copy, CTRL+V to paste it in your file";
}
</script>
</head>

<body bgcolor="#999999">
<?php


function read_mp3_meta($mp3) {
	$getID3 = new getID3;
	$fileinformation = $getID3->analyze($mp3);
	/*if (isset($fileinformation['id3v1'])) {
		echo 'id3v1:';
		print_r ($fileinformation['id3v1']['comments']);
	}
	if (isset($fileinformation['id3v2'])) {
		echo 'id3v2:';
		print_r ($fileinformation['id3v2']['comments']);
	}*/
	//echo "-----------------------------------------------------";
	//print_r ($fileinformation);
	$data["song"]='';
  $data["artist"]='';

	if (isset($fileinformation['id3v2'])) {
			if (array_key_exists("title",$fileinformation['id3v2']['comments'])) {
					$data["song"] = trim($fileinformation['id3v2']['comments']['title'][0]);
			}
			if (array_key_exists("artist",$fileinformation['id3v2']['comments'])) {
					$data["artist"]=trim($fileinformation['id3v2']['comments']['artist'][0]);
			}
			if ($data["artist"]=='' && array_key_exists("band",$fileinformation['id3v2']['comments'])) {
					$data["artist"]=trim($fileinformation['id3v2']['comments']['band'][0]);
			}
	}
	if ($data["song"]==='') {
		if (isset($fileinformation['id3v1'])) {
				if (array_key_exists("title",$fileinformation['id3v1']['comments'])) {
						$data["song"] = trim($fileinformation['id3v1']['comments']['title'][0]);
				}
				if (array_key_exists("artist",$fileinformation['id3v1']['comments'])) {
						$data["artist"]=trim($fileinformation['id3v1']['comments']['artist'][0]);
				}

				if ($data["artist"]=='' && array_key_exists("band",$fileinformation['id3v1']['comments'])) {
						$data["artist"]=trim($fileinformation['id3v1']['comments']['band'][0]);
				}
		}

	}


	 return $data;
}

function generate_playlist($dir_path,$categ) {

require_once('getid3/getid3.php');


$the_playlist_code='';

//$sPath = 'audio/*.mp3';
$sPath = $dir_path.'/*.mp3';
foreach (glob($sPath) AS $mp3)
{
	//$tag = id3_get_tag( $mp3 );
//print_r($tag);
	//$oReader = new ID3TagsReader();
	//print $mp3 . PHP_EOL;
	$meta_arr=read_mp3_meta($mp3);
	$the_playlist_code.='
	<ul>
		<li class="xtitle">'.$meta_arr["song"].'</li>
		<li class="xauthor">'.$meta_arr["artist"].'</li>
		<li class="xcategory">'.$categ.'</li>
		<li class="xsources_mp3">'.$mp3.'</li>
		<li class="xsources_ogg">'.$mp3.'</li>
	</ul>';
}
if ($the_playlist_code!='') {
	$the_playlist_code='<div class="xaudioplaylist">'.$the_playlist_code.'
</div>';
}

		return $the_playlist_code;
} ?>

<p>The path to the folder which contains the .MP3 files:</p>

<form id="form1" name="form1" method="post" action="">
    <p><input name="folder_path" type="text" size="100" /></p>
    <p>Default Category:</p>
    <p><input name="default_category" type="text" size="100" value="ALL CATEGORIES" /></p>
    <p><input type="submit" name="submit" id="submit" value="Generate Playlist" /></p>
</form>
</p>
<?php $the_playlist_code='';
if (isset($_POST['folder_path']) && $_POST['folder_path']!='' && isset($_POST['default_category']) && $_POST['default_category']!='') {
	//echo $_POST['folder_path'];
?>

<p>&nbsp;</p>
<p><textarea name="myTextarea" cols="100" rows="20" id="myTextarea"><?php echo generate_playlist($_POST['folder_path'],$_POST['default_category']);?></textarea></p>
<p><button type="button" onclick="select_textarea_content()">Select all</button></p>
<div id="copy_message">&nbsp;</div>
<?php
} else {
	if (isset($_POST['submit'])) {
		if (!isset($_POST['folder_path']) || $_POST['folder_path']=='') {
			echo '<div class="error">Please add the folder path!</div>';
		}
		if (!isset($_POST['default_category']) ||  $_POST['default_category']=='') {
			echo '<div class="error">Please add a category!</div>';
		}
	}
}
?>


</body>
</html>
