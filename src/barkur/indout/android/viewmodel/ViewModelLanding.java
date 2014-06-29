package barkur.indout.android.viewmodel;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import barkur.indout.android.BFragment;
import barkur.indout.android.GridFragment;
import barkur.indout.android.api.API;
import barkur.indout.android.model.ModelCategory;
import barkur.indout.android.model.ModelDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class ViewModelLanding extends ViewModelBase {
	public ArrayList<ModelCategory> categories;
	
	public ViewModelLanding(Context context) {
		super(context);
	}
	
	public void login(String device_id, final API.onAPIResult callback) {
		API.getInstance().login(device_id, new Callback<ModelDevice>() {
			
			@Override
			public void success(ModelDevice model, Response response) {
				Log.i(TAG, "device_id:" + model.device_id);
				String session_key_and_value = null;
				String csrftoken_key_and_value = null;
                
				for (Header header : response.getHeaders()) {
					Log.w(getClass().getSimpleName(), header.getName() + ":" + header.getValue());
                    if (header.getName() == null) {
                    	continue;
                    }
                    
                    if (header.getName().equals(API.RESPONSE_COOKIE_KEY)) {
                    	if (header.getValue().startsWith(API.SESSION_KEY)) {
                    		session_key_and_value = header.getValue().split("; ")[0];
                    	} else if (header.getValue().startsWith(API.CSRF_KEY)) {
                    		csrftoken_key_and_value = header.getValue().split("; ")[0];
                    	}
        			}
                }
				
				API.setCsrfToken(csrftoken_key_and_value.split("=")[1]);
				API.setCookie(session_key_and_value + "; " + csrftoken_key_and_value);
				Log.i(TAG, "Cookie:" + API.getCookie());
				Log.i(TAG, "CsrfToken:" + API.getCsrfToken());
				callback.onComplete();
			}
			
			@Override
			public void failure(RetrofitError error) {
				if (error.isNetworkError()) {
					if (error.getCause() instanceof SocketTimeoutException) {
						callback.onTimeout();
					} else {
						callback.onNoNetwork();
					}
				} else {
					callback.onFail();
				}
			}
		});
	}
	
	public void getCategories(final API.onAPIResult callback) {
		API.getInstance().getCategory(API.getCookie(), API.getCsrfToken(), new Callback<ArrayList<ModelCategory>>() {

			@Override
			public void failure(RetrofitError error) {
				Log.e(getClass().getSimpleName(), "getCategory error: " + error.getMessage());
				if (error.isNetworkError()) {
					if (error.getCause() instanceof SocketTimeoutException) {
						callback.onTimeout();
					} else {
						callback.onNoNetwork();
					}
				} else {
					callback.onFail();
				}
			}

			@Override
			public void success(ArrayList<ModelCategory> list, Response response) {
				Log.i(TAG, "id:" + list.get(0).id);
				Log.i(TAG, "title:" + list.get(0).title);
				Log.i(TAG, "icon:" + list.get(0).icon);
				Log.i(TAG, "tag id:" + list.get(0).tags.get(0));
				categories = list;
				callback.onComplete();
			}
		});
	}
}
