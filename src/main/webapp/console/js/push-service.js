function sendFiles(files) {
	if (files && files.length > 0) {
		for (var i in files) {
			if (files[i].name) {
				sendFile(files[i]);				
			}
		}
	}	
}

function sendFile(file) {
	
    const textarea = $('#chat_widget_input'); //get the value from the text input
	
	formData = new FormData();
	formData.append('content[]', file, file.name);
	
	textarea.val("(^o^)ๆ Uploading " + file.name + "...");

	$.ajax({
		url: "/console/storage/public/" + accountId + "/" + file.name,
		type: "POST",
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false
	}).done(function(data) {

		textarea.val('https://' + window.location.hostname + '/public/' + accountId + '/' + encodeURI(file.name));
		
		$('#chat_widget_button').trigger("click");
		
	}).fail(function(jqXHR, textStatus) {

		textarea.val("Upload " + file.name + " " + textStatus);

	});
	
}

function textAreaDropHandler(ev) {
	
	  ev.stopPropagation();
	  ev.preventDefault();
	  
	  if (!ev.dataTransfer) return;
	  
	  var files = [];

	  if (ev.dataTransfer.items) {
	    // Use DataTransferItemList interface to access the file(s)
	    for (var i = 0; i < ev.dataTransfer.items.length; i++) {
	      // If dropped items aren't files, reject them
	      if (ev.dataTransfer.items[i].kind === 'file') {
	    	  
	        files.push(ev.dataTransfer.items[i].getAsFile());
	        
	      } else {
	    	  
	    	  ev.dataTransfer.items[i].getAsString(function (s) {
	    		  console.log("(-.-)ๆ " + ev.dataTransfer.items[i].type + ":" +s);
	    	  });    		    	  
	      }
	    }
	  } else {
	    // Use DataTransfer interface to access the file(s)
	    for (var i = 0; i < ev.dataTransfer.files.length; i++) {
	    	
	        files.push(ev.dataTransfer.files[i]);

	    }
	  }
	  
	  //Upload only last file
	  if (files.length > 0) {
		  for (var i in files) {
			  sendFile(files[i]);
		  }
	  }
}

function textAreaDragOverHandler(ev) {
	  // Prevent default behavior (Prevent file from being opened)
	  ev.stopPropagation();
	  ev.preventDefault();    		  
}

function loadKeywords() {
	
    $.get("/console/keywords/" + contextName(botId), function(array, status) {
    	
    	const keyword = $('#keyword'); 
    	
    	keyword.empty();
    	keyword.append($('<option value="">-- Choose --</option>')); 
		
    	array.forEach(function(value) {
    		keyword.append($('<option value="' + value + '">' + value + '</option>'));
    	});
    	
    }, "json");

}

//submit the message to /chat
$('#chat_widget_button').click(function () {
			
	$("#chat_widget_button").attr("disabled", true);
	$("#chat_widget_button").val(sending_text);
	    
    var message = $('#chat_widget_input').val(); //get the value from the text input
	var keyword = $("#keyword").val();
	
    const data = {message, keyword};
    
    console.log("(^o^)ๆ Boardcast to " + contextName(botId));
    
    $.post('/console/push/' + contextName(botId), data,
        function (msg) {
    	
    	alert("Reach: " + msg + " targets!");
    	
        	$("#chat_widget_button").attr("disabled", false);
        	$("#chat_widget_button").val(notification_text);
        	$('#chat_widget_input').val("");
        	
        	location.reload();
        	
        }, "text");

});