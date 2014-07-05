package barkur.findout.android.map;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

public class IMap {
	private static String TAG = "IMap";
	private LocationClient mLocationClient;
	private LatLng mLatLng;
	
	public interface getLocationCallback {
		public void onLocationGet(LatLng location);
	}
	
	private getLocationCallback onLocationCallback;
	
	public IMap(LocationClient client, getLocationCallback callback) {
		mLocationClient = client;
		onLocationCallback = callback;
	}
	
	public IMap(LatLng latlng, getLocationCallback callback) {
		mLatLng = latlng;
		onLocationCallback = callback;
	}
	
	@JavascriptInterface
	public void log(String str) {
		Log.i(TAG, str);
	}
	
	@JavascriptInterface
	public String getMyLocation() {
		JSONObject me = new JSONObject();
		if (mLatLng != null) {
			try {
	    		me.put("latitude", String.valueOf(mLatLng.latitude));
	    		me.put("longitude", String.valueOf(mLatLng.longitude));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			if (mLocationClient.isConnected()) {
				try {
					Location currentLocation = mLocationClient.getLastLocation();
					Log.i(TAG, "currentLocation is null: " + (currentLocation == null));
					if (currentLocation == null) {
						// 101 shopping mall
						me.put("latitude", "25.033190");
			    		me.put("longitude", "121.563573");
					} else {
			    		me.put("latitude", String.valueOf(currentLocation.getLatitude()));
			    		me.put("longitude", String.valueOf(currentLocation.getLongitude()));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				try {
		    		me.put("latitude", "25.033190");
		    		me.put("longitude", "121.563573");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
    	Log.i(TAG, "getMyLocation->Lat: " + me.optString("latitude") + ", Lon: " + me.optString("longitude"));
    	return me.toString();
	}
	
	@JavascriptInterface
	public void getAddress(String str) {
		Log.i(TAG, "getAddress: " + str);
		try {
			JSONObject obj = new JSONObject(str);
			LatLng location = new LatLng(obj.optDouble("latitude"), obj.optDouble("longitude"));
			onLocationCallback.onLocationGet(location);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
