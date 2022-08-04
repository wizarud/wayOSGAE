package com.wayos.appengine.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.google.api.gax.paging.Page;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.wayos.PathStorage;

public class GcsStorage implements PathStorage {

	private final String projectId;
	private final String bucket;

	/**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 5 * 1024 * 1024;

	/**
	 * This is where backoff parameters are configured. Here it is aggressively retrying with
	 * backoff, up to 10 times but taking no more that 15 seconds total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			.initialRetryDelayMillis(10)
			.retryMaxAttempts(10)
			.totalRetryPeriodMillis(15000)
			.build());
	
	private final GcsFileOptions instance = GcsFileOptions.getDefaultInstance();

	public GcsStorage(String projectId, String bucket) {
		this.projectId = projectId;
		this.bucket = bucket;
	}

	private InputStream getInputStream(String path) {
		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(new GcsFilename(bucket, path), 0, BUFFER_SIZE);
		return Channels.newInputStream(readChannel);		
	}

	private OutputStream getOutputStream(String path) throws Exception {
		GcsOutputChannel outputChannel = gcsService.createOrReplace(new GcsFilename(bucket, path), GcsFileOptions.getDefaultInstance());
		return Channels.newOutputStream(outputChannel);		
	}
	
	@Override
	public List<String> listObjectsWithPrefix(String directoryPrefix) {
		
		// The ID of your GCP project
		// String projectId = "your-project-id";

		// The ID of your GCS bucket
		// String bucketName = "your-unique-bucket-name";

		// The directory prefix to search for
		// String directoryPrefix = "myDirectory/"

		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		/**
		 * Using the Storage.BlobListOption.currentDirectory() option here causes the results to display
		 * in a "directory-like" mode, showing what objects are in the directory you've specified, as
		 * well as what other directories exist in that directory. For example, given these blobs:
		 *
		 * <p>a/1.txt a/b/2.txt a/b/3.txt
		 *
		 * <p>If you specify prefix = "a/" and don't use Storage.BlobListOption.currentDirectory(),
		 * you'll get back:
		 *
		 * <p>a/1.txt a/b/2.txt a/b/3.txt
		 *
		 * <p>However, if you specify prefix = "a/" and do use
		 * Storage.BlobListOption.currentDirectory(), you'll get back:
		 *
		 * <p>a/1.txt a/b/
		 *
		 * <p>Because a/1.txt is the only file in the a/ directory and a/b/ is a directory inside the
		 * /a/ directory.
		 */
		Page<Blob> blobs =
				storage.list(
						bucket,
						Storage.BlobListOption.prefix(directoryPrefix),
						Storage.BlobListOption.currentDirectory());

		List<String> nameList = new ArrayList<>();
		for (Blob blob : blobs.iterateAll()) {
			nameList.add(blob.getName());
		}
		
		return nameList;
	}
	
	@Override
	public void serve(String filePath, HttpServletResponse resp) throws IOException {
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	    BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/" + bucket + "/" + filePath);
	    
	    String suffix = filePath;
	    
    	if (suffix.endsWith(".png")) {
    		
    		resp.setHeader("Content-Type", "image/png");
    		
    	} else if (suffix.endsWith("jpg")) {
    		
    		resp.setHeader("Content-Type", "image/jpeg");
    		
    	} else if (suffix.endsWith("gif")) {
    		
    		resp.setHeader("Content-Type", "image/gif");
    		
    	} else if (suffix.endsWith(".mp3") || suffix.endsWith(".m4a")) {
    		
    		resp.setHeader("Content-Type", "audio/mpeg");
    		
    	} else if (suffix.endsWith(".mp4")) {
    		
    		resp.setHeader("Content-Type", "video/mp4");
    		
    	} else if (suffix.endsWith(".mpeg")) {
    		
    		resp.setHeader("Content-Type", "video/mpeg");
    		
    	} else if (suffix.endsWith(".pdf")) {
    		
    		resp.setHeader("Content-Type", "application/pdf");
    		
    	} else if (suffix.endsWith(".ppt")) {
    		
        	resp.setHeader("Content-Type", "application/vnd.ms-powerpoint");
        	
    	} else if (suffix.endsWith(".pptx")) {
    		
        	resp.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        	
    	} else if (suffix.endsWith(".doc")) {
    		
        	resp.setHeader("Content-Type", "application/msword");
        	
    	} else if (suffix.endsWith(".docx")) {
    		
        	resp.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        	
    	} else if (suffix.endsWith(".mid") || suffix.endsWith(".midi")) {
    		
        	resp.setHeader("Content-Type", "audio/midi");
        	
    	} else if (suffix.endsWith(".zip")) {
    		
        	resp.setHeader("Content-Type", "application/zip");
        	
    	} else if (suffix.endsWith(".htm") || suffix.endsWith(".html")) {
    		
    		resp.setCharacterEncoding("UTF-8");
        	resp.setHeader("Content-Type", "text/html");
        	
    	} else if (suffix.endsWith(".txt") || suffix.endsWith(".context")) {
    		
    		resp.setCharacterEncoding("UTF-8");
    		resp.setHeader("Content-Type", "text/plain");
    		
    	}	    
	    		
	    blobstoreService.serve(blobKey, resp);
		
	}
	
	/**
	 * Return null if not found
	 * @param path
	 * @return
	 */
	@Override
	public JSONObject readAsJSONObject(String path) {
		
    	InputStream inputStream = getInputStream(path);
    	
        try {
        	
	        return new JSONObject(IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()));
	        					
		} catch (Exception e) {
						
		} finally {
			
			try { inputStream.close(); } catch (IOException e) { }
			
		}
		
        return null;
	}
	
	@Override
	public void write(String content, String toPath) {
		
		try {
			
			write(IOUtils.toInputStream(content, StandardCharsets.UTF_8.name()), toPath);
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void write(InputStream input, String toPath) throws IOException {
		
    	GcsFilename gcsFilename = new GcsFilename(bucket, toPath);
		
		GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, instance);
		
		copy(input, Channels.newOutputStream(outputChannel));				        	
		
	}
	
	@Override
	public void write(String fromPath, OutputStream outputStream) throws IOException {
		
    	GcsFilename gcsFilename = new GcsFilename(bucket, fromPath);
		
		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, BUFFER_SIZE);
		
		copy(Channels.newInputStream(readChannel), outputStream);		
		
	}
	
	/**
	 * Transfer the data from the inputStream to the outputStream. Then close both streams.
	 */
	private void copy(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			/**
			 * Error from GAE Concurrent Lock!
			 * com.google.apphosting.runtime.HardDeadlineExceededError: This request (0000017e9eec16e4) started at 2022/01/28 11:22:59.556 GMT+07:00 and was still executing at 2022/01/28 11:24:01.154 GMT+07:00.
	at sun.misc.Unsafe.park(Native Method)
	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)
	at com.google.common.util.concurrent.OverflowAvoidingLockSupport.parkNanos(OverflowAvoidingLockSupport.java:37)
	at com.google.common.util.concurrent.AbstractFuture.get(AbstractFuture.java:456)
	at com.google.common.util.concurrent.AbstractFuture$TrustedFuture.get(AbstractFuture.java:122)
	at com.google.appengine.tools.development.TimedFuture.get(TimedFuture.java:70)
	at com.google.common.util.concurrent.ForwardingFuture.get(ForwardingFuture.java:74)
	at com.google.apphosting.runtime.ApiProxyImpl.doSyncCall(ApiProxyImpl.java:304)
	at com.google.apphosting.runtime.ApiProxyImpl.lambda$makeSyncCall$0(ApiProxyImpl.java:283)
	at com.google.apphosting.runtime.ApiProxyImpl$$Lambda$109/324215000.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at com.google.apphosting.runtime.ApiProxyImpl.makeSyncCall(ApiProxyImpl.java:282)
	at com.google.apphosting.runtime.ApiProxyImpl.makeSyncCall(ApiProxyImpl.java:80)
	at com.google.apphosting.api.ApiProxy.makeSyncCall(ApiProxy.java:124)
	at com.google.appengine.api.urlfetch.URLFetchServiceImpl.fetch(URLFetchServiceImpl.java:40)
	at com.google.appengine.tools.cloudstorage.oauth.AbstractOAuthURLFetchService.fetch(AbstractOAuthURLFetchService.java:73)
	at com.google.appengine.tools.cloudstorage.oauth.OauthRawGcsService.put(OauthRawGcsService.java:412)
	at com.google.appengine.tools.cloudstorage.oauth.OauthRawGcsService.finishObjectCreation(OauthRawGcsService.java:324)
	at com.google.appengine.tools.cloudstorage.GcsOutputChannelImpl$1.call(GcsOutputChannelImpl.java:201)
	at com.google.appengine.tools.cloudstorage.GcsOutputChannelImpl$1.call(GcsOutputChannelImpl.java:198)
	at com.google.appengine.tools.cloudstorage.RetryHelper.doRetry(RetryHelper.java:108)
	at com.google.appengine.tools.cloudstorage.RetryHelper.runWithRetries(RetryHelper.java:166)
	at com.google.appengine.tools.cloudstorage.RetryHelper.runWithRetries(RetryHelper.java:156)
	at com.google.appengine.tools.cloudstorage.GcsOutputChannelImpl.close(GcsOutputChannelImpl.java:198)
	at java.nio.channels.Channels$1.close(Channels.java:178)
	at com.eoss.appengine.helper.Storage.copy(Storage.java:69)

			 */
			output.close();
		}
	}
		
}
