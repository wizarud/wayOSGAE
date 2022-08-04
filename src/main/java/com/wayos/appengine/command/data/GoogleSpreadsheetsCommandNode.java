package com.wayos.appengine.command.data;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.wayos.Context;
import com.wayos.PathStorage;
import com.wayos.MessageObject;
import com.wayos.Session;
import com.wayos.command.CommandNode;
import com.wayos.Hook.Match;
import com.wayos.context.MemoryContext;
import com.wayos.drawer.Canvas2D;
import com.wayos.drawer.Drawer;
import com.wayos.drawer.basic.DataTableDrawer;
import com.wayos.util.Application;

@SuppressWarnings("serial")
public class GoogleSpreadsheetsCommandNode extends CommandNode {

	public GoogleSpreadsheetsCommandNode(Session session, String[] hooks, Match match) {
		super(session, hooks, match);
	}

	@Override
	public String execute(MessageObject messageObject) {
		
    	String url = messageObject.toString();
    	
		try {
			
    		String sheetId = url.replaceFirst("https://docs.google.com/spreadsheets/d/", "");
    		if (sheetId.contains("/")) {
    			sheetId = sheetId.substring(0, sheetId.indexOf("/"));
    		}
    		
    		String range = "A1:H200";
    		
	    	AppIdentityCredential credential = new AppIdentityCredential(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY));
	    	
	        Sheets service = new Sheets.Builder(new UrlFetchTransport(), new JacksonFactory(), credential).build();
	        
	        ValueRange valueRange = service.spreadsheets().values()
	                .get(sheetId, range)
	                .execute();
	       	        
        	Drawer drawer = new DataTableDrawer(new DataTableDrawer.GoogleSheetsAdapter(valueRange.getValues()));
        					        	
        	String title = session.context().prop("title");
        	
			Context newContext = new MemoryContext(session.context().name());
			
			Canvas2D canvas2D = new Canvas2D(newContext, title, 100, true);
			
			drawer.draw(canvas2D);
			
    		PathStorage storage = Application.instance().get(PathStorage.class);
    		
			storage.write(new ByteArrayInputStream(newContext.toJSONString().getBytes("UTF-8")), session.context().name() /*+ ".context"*/);
			
			session.context().load();
			
			return super.successMsg();
        	
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}    	
		
	}

}
