package com.wayos.firebase.pusher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.wayos.PathStorage;
import com.wayos.pusher.Pusher;

public class MobilePusher extends Pusher {
	
	private final String credentialPath = "/credentials/firebase.android";

	public MobilePusher(PathStorage storage) {
		
		super(storage);
						
		try {
			String credential = IOUtils.toString(getClass().getResourceAsStream(credentialPath), StandardCharsets.UTF_8.name());
			authen(new ByteArrayInputStream(credential.getBytes()));			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void authen(InputStream serviceAccount) throws IOException {
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
		        .build();
		
		FirebaseApp.initializeApp(options);
	}
	
	@Override
	public void push(String contextName, String sessionId, String message) {
		
	    String response = "";	    
	    try {
		    response = FirebaseMessaging.getInstance().send(Message.builder()
			        .putData("message", message)
			        .setToken(sessionId)
			        .build());
	    	
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage() + ", Firebase Response:" + response);
	    }
	    
	}

}
