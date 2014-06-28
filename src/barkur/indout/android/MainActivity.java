package barkur.indout.android;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.util.Log;
import barkur.indout.android.api.API;
import barkur.indout.android.model.ModelCategory;
import barkur.indout.android.model.ModelTag;

public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		API.getInstance().getCategory(API.getCookie(), API.getCsrfToken(), new Callback<ArrayList<ModelCategory>>() {

			@Override
			public void failure(RetrofitError error) {
				Log.e(getClass().getSimpleName(), "getCategory error: " + error.getMessage());
			}

			@Override
			public void success(ArrayList<ModelCategory> list, Response response) {
				Log.i(TAG, "id:" + list.get(0).id);
				Log.i(TAG, "title:" + list.get(0).title);
				Log.i(TAG, "icon:" + list.get(0).icon);
				Log.i(TAG, "tag id:" + list.get(0).tags.get(0));
			}
		});
		
		API.getInstance().getTag(API.getCookie(), API.getCsrfToken(), new Callback<ArrayList<ModelTag>>() {

			@Override
			public void failure(RetrofitError error) {
				Log.e(getClass().getSimpleName(), "getTag error: " + error.getMessage());
			}

			@Override
			public void success(ArrayList<ModelTag> list, Response response) {
				Log.i(TAG, "title:" + list.get(0).title);
			}
		});
	}
}
