package barkur.findout.android.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import barkur.findout.android.BaseActivity;
import barkur.findout.android.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends BaseActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	private static String TAG = "MapActivity";
	private static String mapUrl = "file:///android_asset/map.html";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private WebView mWebView;
	private IMap webInterface;

	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) this, requestCode);
			dialog.show();
			return;
		}
		
		findViews();
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		// Set the interval ceiling to one minute
		mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		
		mLocationClient = new LocationClient(this, this, this);
		
		createWebInterface();
		initWebView();
        mWebView.loadUrl("about:blank");
	}
	
	private void createWebInterface() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("latitude") && bundle.containsKey("longitude")) {
				LatLng mLatLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
				webInterface = new IMap(mLatLng, new IMap.getLocationCallback() {
					
					@Override
					public void onLocationGet(LatLng location) {
						getAddress(location);
					}
				});
				
				return;
			}
		}
		
		webInterface = new IMap(mLocationClient, new IMap.getLocationCallback() {
			
			@Override
			public void onLocationGet(LatLng location) {
				getAddress(location);
			}
		});
	}

	private void findViews() {
		mWebView = (WebView) findViewById(R.id.webview_map);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.addJavascriptInterface(webInterface, "AndroidFunction");
        
        mWebView.setWebViewClient(new WebViewClient() {
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
        		startLoading();
        		super.onPageStarted(view, url, favicon);
        	}
        	
        	@Override
        	public void onPageFinished(WebView view, String url) {
        		stopLoading();
        		super.onPageFinished(view, url);
        	}
        });
    }
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
    	Log.e(TAG, "onConnectionFailed");
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            showErrorDialog(connectionResult.getErrorCode());
        }
	}

	@Override
	public void onConnected(Bundle arg0) {
		mWebView.loadUrl(mapUrl);
	}

	@Override
	public void onDisconnected() {
    	Log.e(TAG, "onDisconnected");
	}
	
	@Override
	public void onStop() {
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
		super.onStop();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (mLocationClient != null) {
			mLocationClient.connect();
		}
	}
	
	@SuppressLint("NewApi")
	public void getAddress(LatLng location) {

		// In Gingerbread and later, use Geocoder.isPresent() to see if a
		// geocoder is available.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			Toast.makeText(this, "Geocoder Unavailable", Toast.LENGTH_LONG).show();
			return;
		}

		Intent intent = new Intent();
		intent.putExtra("latitude", location.latitude);
		intent.putExtra("longitude", location.longitude);
		setResult(RESULT_OK, intent);
		finish();
	}
}
