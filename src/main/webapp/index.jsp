<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<%@ include file="i18n.jspf"%>
<html class="no-js" lang="">
<!--<![endif]-->
<head>
<title>WAYOS</title>
<%@ include file="css.jspf" %>
<style>
.eossTextArea {
	width: 100%;
	border: 1px solid #DDDDDD;
	-webkit-box-sizing: border-box; /* Safari/Chrome, other WebKit */
	-moz-box-sizing: border-box; /* Firefox, other Gecko */
	box-sizing: border-box;  
	font-size: 16px;
}
.eossFileButton {
    width: 8%;
	border: 1px solid white;
   	background: #3498db;
    color: white;
   	line-height: 40px;
    margin-bottom: 10px;
}
.eossButton {
    width: 45%;
	border: 1px solid white;
   	background: #3498db;
    color: white;
   	line-height: 40px;
    margin-bottom: 10px;
}
.context {
  margin: 0;
  padding: 10px 0;
  width: 100%;
}
.footer {
   position: fixed;
   left: 0;
   bottom: 0;
   width: 100%;
   text-align: center;
}
</style>
</head>
<body>
<div id="fb-root"></div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/th_TH/sdk.js#xfbml=1&version=v3.2&appId=477788152679063&autoLogAppEvents=1"></script>
<!--[if lt IE 8]>
	<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->
	<div class='preloader'>
		<div class='loaded'>&nbsp;</div>
	</div>
	<%@ include file="nav-bar.jspf"%>
	
	<% 
	String accountId = (String) request.getAttribute("accountId");
	String botId = (String) request.getAttribute("botId");	
	/**
	* Default contextName
	*/
	if (accountId == null || botId == null) {
		accountId = System.getenv("showcaseAccountId");
		botId = System.getenv("showcaseBotId");
	}	
	%>
	
	<section class="context">
		<div class="col-md-12" style="text-align: center; padding: 0;">
			<iframe id="wayos-frame" frameborder="0" scrolling="no" allowfullscreen></iframe>
				<p class="footer">
					<textarea id="inputTextArea" class="eossTextArea" rows="3" cols="55" placeholder="<fmt:message key="textarea.typehere" />"></textarea><br>
					<input type="file" id="fileDialog" style="display: none">
					<button id="fileButton" class="eossFileButton">🖼</button><button id="ringButton" class="eossButton" >🔔</button><button id="sendButton" class="eossButton">SEND</button>	
				</p>
		</div>
	</section>
		
	<%@ include file="overlay.jspf" %>
	<%@ include file="javascript.jspf" %>	
	<%@ include file="fbSDK.jspf" %>

	<%--@ include file="firebase.jspf"--%>
	<script>
	const FRAME_HEIGHT = "59vh";
	var inputTextArea = document.getElementById("inputTextArea");
	var fileButton = document.getElementById("fileButton");
	var ringButton = document.getElementById("ringButton");
	var sendButton = document.getElementById("sendButton");
	
	inputTextArea.disabled = true;
	fileButton.disabled = true;
	ringButton.disabled = true;
	sendButton.disabled = true;
	
	function showLoading(display) {
		
		let element = document.getElementById("loader");
		if (display) {
			element.style.display = "block";
		} else {
			element.style.display = "none";
		}
	}
	
	function localSessionId() {
		
		var sessionId = localStorage.getItem("sessionId");
		if (sessionId==null) {
			sessionId = Date.now().toString();
			localStorage.setItem("sessionId", sessionId);
		}
		
		return sessionId;
	}
	
	var eossWayoBot = {
		domain: "<%= domain %>",
		sessionId: localSessionId(),
		accountId: "<%= accountId %>",
		botId: "<%= botId %>"
	};
	
	eossWayoBot.adjustFrameHeight = function(width, height) {
		
		let frame = document.getElementById("wayos-frame");
		if (frame) {
			frame.style.height = (1140 * (height / width)) + 'px';
		}
	}
	
	eossWayoBot.onRing = function(accountId, botId, success) {
		
		let frame = document.getElementById("wayos-frame");
		if (frame) {
			const src = frame.src;
			frame.onload = function() {
				if (success) {
					success();
				}
				this.onload = null;
			}
			frame.onerror = function () {
				this.src = src;
			}
			frame.src = "https://" + window.location.hostname + "/canvas.jsp?img=https://" + window.location.hostname + "/media/" + accountId + "/" + botId;
		}
	}
	
	eossWayoBot.onDisplayImage = function(imageURL) {

		let frame = document.getElementById("wayos-frame");	
		if (frame) {
			const src = frame.src;
			frame.onerror = function () {
				this.src = src;
			}
			
			frame.src = "https://" + window.location.hostname + "/canvas.jsp?img=" + imageURL;	    
		}
	}

	eossWayoBot.onDisplayYoutube = function(youtubeId) {
		
		let frame = document.getElementById("wayos-frame");	
		if (frame) {
			const src = frame.src;
			frame.onerror = function () {
				this.src = src;
			}
			frame.src = "https://www.youtube.com/embed/" + youtubeId;
		}	
	}

	eossWayoBot.onDisplayWeb = function(url) {
		
		let frame = document.getElementById("wayos-frame");
		if (frame /*&& (url.startsWith("https://wayobot.com") || url.startsWith("https://www.wayobot.com"))*/) {
			const src = frame.src;
			frame.onload = function () {
				this.style.height = this.contentDocument.body.offsetHeight + 'px';			
				this.onload = null;
			}
			frame.onerror = function () {
				this.src = src;
			}
			frame.src = url;
		}
	}

	eossWayoBot.onDisplayCatalog = function(innerHTML) {
		
		let frame = document.getElementById("wayos-frame");
		if (frame) {
			const src = frame.src;
			frame.onload = function() {
				this.contentDocument.body.innerHTML = "<div align=\"center\" class=\"vertical-center\"><p>" + innerHTML.replace(/width: 80/g, "width: 50") + "</p></div>";
				this.style.width = "100%";
				this.style.height = FRAME_HEIGHT;
				this.onload = null;
			}
			frame.onerror = function () {
				this.src = src;
			}
			frame.src = "/panel.jsp";
		}
	}
	
	eossWayoBot.onDisplayText = function(text) {
		
		let innerHTML = "<div align=\"center\" class=\"vertical-center\"><h1>" + text + "</h1></div>";
		let frame = document.getElementById("wayos-frame");
		if (frame) {
			const src = frame.src;
			frame.onload = function() {
				this.contentDocument.body.innerHTML = innerHTML;
				this.style.width = "100%";
				this.style.height = FRAME_HEIGHT;
				this.onload = null;
			}
			frame.onerror = function () {
				this.src = src;
			}
			frame.src = "/panel.jsp";
		}
		
	}
	
	eossWayoBot.animateResponseText = function(message) {
		
		if (message === "") return;
		
		let texts = message.split('\n\n\n');
	
		/**
		* Display Simple Animation on wayos-frame
		*/
		setTimeout(function() {
				
			let text = texts.shift();
			
			console.log("Show: " + text);
		        
		    let urls = text.match(/(http(s?):)([/|.|\w|\s|-])*(\?.*)?/g);
		        
		    //if (text.indexOf('<div style="overflow: auto; white-space: nowrap;">')!==-1) {
			 if (text.indexOf('<div')!==-1) {
		        	
		    	this.onDisplayCatalog(text);
		    	
		    } else if (urls!=null && urls.length > 0) {
		    	
 		        //let imageURLS = text.match(/(http(s?):)([/|.|\w|\s|-])*\.(?:jpg|jpeg|JPG|JPEG|gif|GIF|png|PNG)/g);
 		        let url;
 		        
 		        for (let i in urls) {
 		        	
 		        	url = urls[i];
 		        	
 		        	if (url.startsWith("https://www.youtube.com/watch?v=")) {
 		        		
 		        		let x = url.replace("https://www.youtube.com/watch?v=", "");
 		        		
 		        		if (x.indexOf(" target")>0) {
 		        			
 		        			this.onDisplayYoutube(x.substr(0, x.indexOf(" target") - 1));
 		        			
 		        		} else {
 		        			
 		        			this.onDisplayYoutube(x);
 		        			
 		        		}
 		        		
 		        	} else if (url.startsWith("https://i3.ytimg.com/vi/")) {
 		        		
		        		this.onDisplayYoutube(url.replace("https://i3.ytimg.com/vi/", "").replace("/maxresdefault.jpg", ""));
 		        		
 		        	} else if (url.indexOf(".jpg")!=-1 || url.indexOf(".jpeg")!=-1 || url.indexOf(".gif")!=-1 || url.indexOf(".png")!=-1) {

	 		        	this.onDisplayImage(url);
	 		        	
 		        	} /*else if (url.indexOf(".mp3")==-1 && url.indexOf(".m4a")==-1 && url.indexOf(".mp4")==-1) {
 		        		
	 		        	this.eossWayoBot.onDisplayWeb(url);
	 		        	
 		        	} */
 		        }
 		        
		    } else {
		    	
		      this.onDisplayText(text);
		    	
		    }

		    if (texts.length > 0) {
		    	
		    	setTimeout(arguments.callee.bind(this), 500);
		    	
		    } 
		    
		    inputTextArea.focus();
		
		}.bind(this), 0);
	}
		
	eossWayoBot.sendFilesToEossBot = function() {
		
		const input = document.getElementById("fileDialog");
		
		const files = Array.from(input.files);
		
		showLoading(true);
		
        let formData = new FormData();
        
        let fileNames = "";
        let totalSize = 0;
        
        for (let i in files) {
    		formData.append('file', files[i]);
    		totalSize += files[i].size;
    		fileNames += files[i].name + " ";
        }
        
        console.log("Sending File.." + fileNames);
        console.log("Total Files Size: " + totalSize);

 		let xhr = new XMLHttpRequest();
 		let url = this.domain + "/webhooks/" + this.accountId + "/" + this.botId + "/" + this.sessionId;
 		
 		xhr.open("POST", url); 
 		
 		xhr.onload = function() {
 			
 		    if (xhr.status === 200) {
 		    	
 		    	showLoading(false);
 				
 		    	this.animateResponseText(xhr.responseText);
 		    	
 		    }
 			
 		}.bind(this);
 		
 		xhr.send(formData);	
 	}
	
	eossWayoBot.sendMessageToEossBot = function(message, success) {
		
		showLoading(true);
		
		let xhr = new XMLHttpRequest();
 		let url = this.domain + "/webhooks/" + this.accountId + "/" + this.botId;
 		let params = "message=" + message + "&sessionId=" + this.sessionId;
 		
 		xhr.open("POST", url, true);
 		xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		xhr.onload = function() {
 			
 		    if(xhr.status === 200) {
 		    	
 		    	showLoading(false);
 				
 		    	console.log(xhr.responseText);
 		    	
 		    	this.animateResponseText(xhr.responseText);
 		    	
 		    	if (success) {
 		    		success();
 		    	}
 		    }
 		    
 		}.bind(this);
 		
 		xhr.send(params);
	}
	
	eossWayoBot.createRichMenuItem = function(key, value) {
		
		let li = document.createElement("li");
		li.className = "login";
		
		let a = document.createElement("a");
		a.innerHTML = key;
		a.href = "javascript:eossWayoBot.sendMessageToEossBot('" + value + "')";
		a.style.width = "150px";
		
		li.appendChild(a);
		
		return li;
	}
	
	eossWayoBot.configBorders = function(config) {
		
   		let titleHeader = document.getElementById("title");
   		titleHeader.innerHTML = config.title;
   		
	  	let socialSection = document.getElementById("social");
 	  	socialSection.style.background = config.borderColor;
   		
	   	let richMenus = document.getElementById("richMenus");
	   	
		//richMenus.appendChild(this.createRichMenuItem("🔔", "greeting"));
		
   		if (config.richMenus) {
   			
   			let items = config.richMenus.split(',');
   			
   			for (let i in items) {
   				
   				let item = items[i].trim();
   				
   				richMenus.appendChild(this.createRichMenuItem(item, item + "!"));
   			}
   		
   		}
 	  	 	  		
 	  	let liElements = document.querySelectorAll("li.login");
 	  	let buttonElement;
 	  	for (let i in liElements) {
 	  			
 	  		try {
 	  			
 	  			buttonElement = liElements[i].children[0];
	 	 				 	  					 	  				
	  			if (buttonElement.innerHTML === "Home") {
	 	  				
	   				buttonElement.style.color = "WHITE";
	   				buttonElement.style.backgroundColor = config.borderColor;
		 	  			
	   			} else {
	 	  				
	   				buttonElement.style.color = config.borderColor;
	   				buttonElement.style.borderColor = config.borderColor;
	 	  				
	   			}
	 	  			
 			} catch (e) {
 	  				
   			}
   		}
 	  	
 		inputTextArea.disabled = false;
 		inputTextArea.autofocus = true;
 		
 		fileButton.disabled = false;
 		fileButton.style.color = "WHITE";
 		fileButton.style.backgroundColor = config.borderColor;
 	  	
 		ringButton.disabled = false;
 	  	ringButton.style.color = "WHITE";
 	  	ringButton.style.backgroundColor = config.borderColor;
 	  	
 		sendButton.disabled = false;
 	  	sendButton.style.color = "WHITE";
 	  	sendButton.style.backgroundColor = config.borderColor;
 	  	
 	  	fileButton.addEventListener('click', function() {
 	  		let fileDialog = document.getElementById("fileDialog");
 	  		fileDialog.onchange = function () {
 	 	  		this.sendFilesToEossBot();
 	  		}.bind(this);
 	  		fileDialog.click();
 	  	}.bind(this));
 	  	
 		ringButton.addEventListener('click', function(event) {
            var message = "greeting";
			this.sendMessageToEossBot(message);
	    }.bind(this));
 		
 		sendButton.addEventListener('click', function(event) {
            var message = inputTextArea.value.trim();
			if (message != '') {
				this.sendMessageToEossBot(message, function() {
					inputTextArea.value = "";
				});
			}
	    }.bind(this));
 		
 		this.config = config;
	}

	eossWayoBot.load = function() {

		showLoading(true);
		
		var url = this.domain + '/props/' + this.accountId + '/' + this.botId;
    	
	 	var xhr = new XMLHttpRequest();
	 	xhr.open("GET", url, true);
	 	
	 	xhr.onload = function() {
	 		
	 	  	if (xhr.status === 200) {
	 	  		
	 			showLoading(false);
	 			
	 	  		console.log(xhr.responseText);
	 	  		
	 	  		this.configBorders(JSON.parse(xhr.responseText));
	 	  		
	 	  		this.sendMessageToEossBot("greeting");	 	  		
	 		}
	 	  	 			
	 	}.bind(this);
	 	
	 	xhr.send();
    }
    
	eossWayoBot.load();
		
	</script>
	
</html>