package barkur.findout.android.utility;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtility {
	private static String TAG = "LocationUtility";
	
	public static boolean isGpsEnabled(Context mContext) {
	    LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	    if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	    	Log.i(TAG, "locating disabled");
	    	return false;
    	}
	    
	    Log.i(TAG, "locating enabled");
		return true;
	}
}
