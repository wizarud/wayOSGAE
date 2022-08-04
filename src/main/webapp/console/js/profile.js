function getProfile() {
	
	overlayPopup("loader");
	
	let url = "/console/account/" + accountId;
	
	let xhr = new XMLHttpRequest();
	xhr.open("GET", url, true);
	xhr.onload = function() {
		
		if (xhr.status === 200) {
			
			let props = JSON.parse(xhr.responseText);
			
			$("#name").val(props.Name);
			$("#email").val(props.Email);
				
		}
		
		overlayPopup("loader");
	}
	
	xhr.send();	
}
