<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/wayos.css" />
<style type="text/css">
.eoss_image_head {
    width: 90%;
    height: 70vh;
	margin: 0px auto;
    border-radius: 5px;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    background-size: auto;
    background-position: center; 
    background-repeat: no-repeat;
    background-size: cover;
}
.vertical-center {
  margin: 0;
  position: absolute;
  width: 100%;
  top: 50%;
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}
</style>
<script>

const eossWayoBot = parent.eossWayoBot;
const style = document.createElement('style');
style.textContent = ".eoss_menu_item { cursor: pointer; width: 80%; margin: 5px; padding: 5px 10px; border-radius: 5px; -moz-border-radius: 5px; -webkit-border-radius: 5px; background: #FFFFFF; text-align: center; color: " + eossWayoBot.config.borderColor + "; border: 1px solid " + eossWayoBot.config.borderColor + " }";

document.head.appendChild(style);

</script>
</head>
<body style="margin: 0px !important;">
<div id="content" class="vertical-center">
</div>
</body>
</html>