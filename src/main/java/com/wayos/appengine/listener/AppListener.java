package com.wayos.appengine.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.json.JSONObject;

import com.wayos.Configuration;
import com.wayos.PathStorage;
import com.wayos.appengine.storage.GcsStorage;
import com.wayos.connector.SessionPool;
import com.wayos.connector.BLESessionPoolFactory;
import com.wayos.firebase.pusher.MobilePusher;
import com.wayos.firebase.pusher.WebPusher;
import com.wayos.pusher.FacebookPusher;
import com.wayos.pusher.LinePusher;
import com.wayos.pusher.PusherUtil;
import com.wayos.util.Application;
import com.wayos.util.ConsoleUtil;

@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		
		//TODO: createStorage(storageImplName);
		PathStorage storage = new GcsStorage(Configuration.projectId, Configuration.storageBucket);
		
		ConsoleUtil consoleUtil = new ConsoleUtil(storage);
		
		PusherUtil pusherUtil = new PusherUtil();
		
		BLESessionPoolFactory sessionPoolFactory = new BLESessionPoolFactory(storage, consoleUtil, pusherUtil);
		
		SessionPool sessionPool = sessionPoolFactory.create();

		/**
		 * Register Single Instance of Utilities class
		 */		
		Application.instance().register(SessionPool.class.getName(), sessionPool);
		Application.instance().register(PathStorage.class.getName(), storage);
		Application.instance().register(ConsoleUtil.class.getName(), consoleUtil);
		
		/**
		 * Register pusher to channel
		 */
		Application.instance().register(PusherUtil.class.getName(), pusherUtil);
		Application.instance().register("line", new LinePusher(storage));
		Application.instance().register("facebook.page", new FacebookPusher(storage));
		Application.instance().register("mobile", new MobilePusher(storage));
		Application.instance().register("web", new WebPusher(storage));

		JSONObject firebaseConfig = new JSONObject();
		firebaseConfig.put("apiKey", System.getenv("apiKey"));
		firebaseConfig.put("authDomain", System.getenv("authDomain"));
		firebaseConfig.put("databaseURL", System.getenv("databaseURL"));
		firebaseConfig.put("projectId", System.getenv("projectId"));
		firebaseConfig.put("storageBucket", System.getenv("storageBucket"));
		firebaseConfig.put("messagingSenderId", System.getenv("messagingSenderId"));
		firebaseConfig.put("appId", System.getenv("appId"));
		
		sce.getServletContext().setAttribute("firebaseConfig", firebaseConfig);
		
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
