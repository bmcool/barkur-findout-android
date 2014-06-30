package barkur.findout.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

public class BaseActivity extends Activity {
	protected String TAG;
	protected Context mContext;
	protected ProgressDialog loadingDialog;
	protected ArrayList<Bitmap> _bitmaps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = getClass().getSimpleName();
		mContext = this;
		__initLoadingDialog();
	}
	
	private void __initLoadingDialog() {
		loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(getString(R.string.please_wait));
	}
	
	protected ProgressDialog getLoadingDialog() {
		if (loadingDialog == null) {
			__initLoadingDialog();
		}
		
		return loadingDialog;
	}

	public void startLoading() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						loadingDialog.show();
					}
				});
			}
		}).start();
	}
	
	public void stopLoading() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						loadingDialog.dismiss();
					}
				});
			}
		}).start();
	}
	
	protected void setLoadingText(String text) {
		loadingDialog.setMessage(text);
	}
	
	protected void addUsedBitmap(Bitmap bmp) {
		if (_bitmaps == null) {
			_bitmaps = new ArrayList<Bitmap>();
		}
		_bitmaps.add(bmp);
	}
	
	protected void recycleAllUsedBitmaps() {
		if (_bitmaps == null) {
			return;
		}
		
		for (Bitmap bmp : _bitmaps) {
			if (bmp != null && !bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
		}
	}
}
