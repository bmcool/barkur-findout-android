package barkur.findout.android.utility;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import barkur.findout.android.R;

public class Utility {

    public static String getDeviceId(Context mContext) {
    	TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    	
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
     
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.w("GcmRegister", "UUID: " + uniqueId);
        
        return uniqueId;
	}
	
	public static void showDialogOneButton(Context context, String dialogTitle, String message, String buttonName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(dialogTitle)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(buttonName,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showConfirmDialog(Context context, String dialogTitle, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(dialogTitle)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(context.getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showDialogOneButtonWithListener(Context context, String dialogTitle, String message, int button_res, DialogInterface.OnClickListener button_listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(dialogTitle)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(button_res, button_listener);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void showDialogTwoButton(Context context, String dialogTitle, String message, int left_res, int right_res, DialogInterface.OnClickListener left_listener, DialogInterface.OnClickListener right_listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(dialogTitle)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(right_res, right_listener)
				.setNegativeButton(left_res, left_listener);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void showTimeoutDialog(Context mContext) {
		Utility.showConfirmDialog(mContext, mContext.getString(R.string.connection_error), mContext.getString(R.string.connection_timeout_error));
	}

	public static void showServerErrorDialog(Context mContext) {
		Utility.showConfirmDialog(mContext, mContext.getString(R.string.connection_error), mContext.getString(R.string.connection_server_error));
	}
	
	public static void showNetworkErrorNotification(Context mContext) {
		Utility.showConfirmDialog(mContext, mContext.getString(R.string.connection_error), mContext.getString(R.string.network_error));
	}
	
	public static int dp2px(Context context, float dpValue) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());
		return (int) px;
    }

	public static int getDeviceDpi(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i("Utility", "dpi: " + metrics.densityDpi);
		
		return metrics.densityDpi;
	}

	public static int getDeviceWidthInPX(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i("Utilities", "device width is " + metrics.widthPixels + " px");
		
		return metrics.widthPixels;
	}

	public static int getDeviceHeightInPX(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i("Utilities", "device width is " + metrics.heightPixels + " px");
		
		return metrics.heightPixels;
	}

	public static void save(Context context, String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = prefs.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public static String load(Context context, String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String result = prefs.getString(key, "");
		
		return result;
	}
}
