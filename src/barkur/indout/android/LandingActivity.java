package barkur.indout.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import barkur.indout.android.api.API;
import barkur.indout.android.utility.LocationUtility;
import barkur.indout.android.utility.RequestCode;
import barkur.indout.android.utility.Utility;
import barkur.indout.android.viewmodel.ViewModelLanding;

public class LandingActivity extends BaseActivity {
	private ViewModelLanding viewModel;
	private AlertDialog gpsEnableDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setInternetMode();
		setContentView(R.layout.activity_landing);
		viewModel = new ViewModelLanding(mContext);
		
		findView();
		
		if (LocationUtility.isGpsEnabled(mContext)) {
			login();
		} else {
			gpsEnableDialog.show();
		}
	}

	private void findView() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
		.setTitle(getString(R.string.dialog_title_notify))
		.setMessage(getString(R.string.please_enable_gps))
		.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
		  		
		  		public void onClick(DialogInterface dialogInterface, int i) {
			    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    startActivityForResult(intent, RequestCode.GPS_SETTING);
		    }
	  	});
	  	gpsEnableDialog = builder.create();
	  	gpsEnableDialog.setCanceledOnTouchOutside(false);
	  	gpsEnableDialog.setCancelable(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RequestCode.GPS_SETTING) {
			if (LocationUtility.isGpsEnabled(mContext)) {
				login();
			} else {
				gpsEnableDialog.show();
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void login() {
		startLoading();
		viewModel.login(Utility.getDeviceId(mContext), new API.onAPIResult() {
			
			@Override
			public void onTimeout() {
				finishAPP(getString(R.string.connection_error), getString(R.string.connection_timeout_error));
			}
			
			@Override
			public void onNoNetwork() {
				finishAPP(getString(R.string.connection_error), getString(R.string.network_error));
			}
			
			@Override
			public void onFail() {
				finishAPP(getString(R.string.connection_error), getString(R.string.connection_server_error));
			}
			
			@Override
			public void onComplete() {
				stopLoading();
				startActivity(new Intent(mContext, MainActivity.class));
				finish();
			}
		});
	}

	private void setInternetMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
	}

	private void finishAPP(String title, final String msg) {
		stopLoading();
		Utility.showDialogOneButtonWithListener(mContext, title, msg, R.string.finish, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
	}
}
