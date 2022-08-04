<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	String imgPath = request.getParameter("img");
%>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body, html {
  height: 100%;
  margin: 0;
}

.bg {
  /* The image used */
  background-image: url("<%= imgPath %>");

  /* Full height */
  height: 100%; 

  /* Center and scale the image nicely */
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
}
</style>
</head>
<body>
<div class="bg"></div>
<script>
	window.onload = function (e) {
		if (parent && parent.eossWayoBot) {
			
			const image = new Image();
			image.onload = function () {
				//parent.eossWayoBot.adjustFrameHeight(this.width, this.height);
			}
			image.src = "<%= imgPath %>";
			
		}
	}
</script>
</body>
</html>

