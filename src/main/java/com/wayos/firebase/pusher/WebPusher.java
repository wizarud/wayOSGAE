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
import com.google.firebase.messaging.Notification;
import com.wayos.PathStorage;
import com.wayos.pusher.Pusher;

public class WebPusher extends Pusher {
	
	private final String credentialPath = "/credentials/firebase.admin";

	public WebPusher(PathStorage storage) {
		
		super(storage);
		
		if (FirebaseApp.getApps() == null || FirebaseApp.getApps().isEmpty()) {
			try {
				String credential = IOUtils.toString(getClass().getResourceAsStream(credentialPath), StandardCharsets.UTF_8.name());
				authen(new ByteArrayInputStream(credential.getBytes()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
	}
	
	private void authen(InputStream serviceAccount) throws IOException {
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
		        .build();
		
		FirebaseApp.initializeApp(options);
	}
	
	@Override
	public void push(String contextName, String targetId, String message) {
		
		if (targetId==null || !targetId.startsWith("fcm:")) throw new IllegalArgumentException("Cannot push to sessionId:" + targetId);
		
		String [] tokens = contextName.split("/");
		
		String accountId = tokens[0];
		String botId = tokens[1];
		
		String token = targetId.substring(4);
		
		//How to check that targetId if generate from FirebaseApp in js
	    String response = "";	    
	    try {
	    	
		    response = FirebaseMessaging.getInstance().send(
		    		Message.builder().setNotification(
		    				Notification.builder()
		    					.setTitle("You've Got Message!")
		    					.setBody(message)
		    					.build())
			        .setToken(token)
			        .putData("accountId", accountId)
			        .putData("botId", botId)
			        .build());
	    	
	    } catch (Exception e) {
	    	
	    	throw new RuntimeException(e.getMessage() + ", Firebase Response:" + response);
	    }
	    
	}

}
