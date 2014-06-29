package barkur.indout.android;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import barkur.indout.android.api.API;
import barkur.indout.android.model.ModelCategory;
import barkur.indout.android.model.ModelTag;

public class MainActivity extends FragmentActivity {
	private static String TAG = "MainActivity";
	private Context mContext;
	private ActionBar mActionBar;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private ArrayList<ModelCategory> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mViewPager = new ViewPager(mContext);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		getIntentData();
		
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//	    actionBar.setDisplayShowTitleEnabled(false);
//	    actionBar.setDisplayShowHomeEnabled(false);

	    mTabsAdapter = new TabsAdapter(this, mViewPager);
	    
	    ImageView imageView = new ImageView(mContext);
	    imageView.setImageResource(R.drawable.ic_launcher);
	    for (ModelCategory model_category : categories) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", model_category);
			mTabsAdapter.addTab(mActionBar.newTab().setCustomView(imageView).setText(model_category.title), GridFragment.class, bundle);
		}
		
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_B"), BFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_A"), GridFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_B"), BFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_A"), GridFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_B"), BFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_A"), GridFragment.class, null);
	    mTabsAdapter.addTab(mActionBar.newTab().setText("Fragment_B"), BFragment.class, null);
		
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

	@SuppressWarnings("unchecked")
	private void getIntentData() {
		categories = (ArrayList<ModelCategory>) getIntent().getSerializableExtra("category");
	}
}
