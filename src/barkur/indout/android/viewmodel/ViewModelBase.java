package barkur.indout.android.viewmodel;

import android.content.Context;

public class ViewModelBase {
	protected String TAG;
	protected Context mContext;
	
	public ViewModelBase(Context context) {
		TAG = getClass().getSimpleName();
		mContext = context;
	}
}
