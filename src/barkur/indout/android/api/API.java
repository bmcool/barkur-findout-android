package barkur.indout.android.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import barkur.indout.android.model.ModelCategory;
import barkur.indout.android.model.ModelDevice;
import barkur.indout.android.model.ModelTag;

public class API {
	private static RestApi _restApi;
	public static String RESPONSE_COOKIE_KEY = "Set-Cookie";
	public static String SESSION_KEY = "sessionid";
	public static String CSRF_KEY = "csrftoken";
	private static String Cookie;
	private static String X_CSRFToken;
	
	public interface onAPIResult {
		public void onComplete();
		public void onTimeout();
		public void onFail();
		public void onNoNetwork();
	}
	
	public interface RestApi {
		@FormUrlEncoded
		@POST("/account/connect/")
		void login(@Field("device_id") String unique_id, Callback<ModelDevice> callback);
		
		@GET("/landmarks/category/")
		void getCategory(@Header("Cookie") String cookie, @Header("X-CSRFToken") String csrf_toekn, Callback<ArrayList<ModelCategory>> callback);
		
		@GET("/landmarks/tag/")
		void getTag(@Header("Cookie") String cookie, @Header("X-CSRFToken") String csrf_toekn, Callback<ArrayList<ModelTag>> callback);
	}
	
	public static void setCookie(String value) {
		Cookie = value;
	}
	
	public static void setCsrfToken(String value) {
		X_CSRFToken = value;
	}
	
	public static String getCookie() {
		return Cookie;
	}
	
	public static String getCsrfToken() {
		return X_CSRFToken;
	}
	
	public static RestApi getInstance() {
		if (_restApi == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
		    .setEndpoint(ApiURL.HOST)
		    .setClient(new MyUrlClient())
		    .build();

			_restApi = restAdapter.create(RestApi.class);
		}
		
		return _restApi;
	}
	
	static class MyUrlClient extends UrlConnectionClient {
		@Override protected HttpURLConnection openConnection(Request request) throws IOException {
		    HttpURLConnection connection = super.openConnection(request);
		    if (request.getUrl().startsWith(ApiURL.HOST)) {
		    	connection.setConnectTimeout(60 * 1000); // 5 seconds.
		    	connection.setReadTimeout(60 * 1000);
		    }
		    return connection;
		}
	}
}
