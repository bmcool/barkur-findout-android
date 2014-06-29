package barkur.indout.android;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class TabsAdapter extends FragmentStatePagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	private static String TAG = "TabListener";
	private Context mContext;
	private ActionBar mActionBar;
	private ViewPager mViewPager;
	private ArrayList<TabInfo> mTabs = new ArrayList<TabsAdapter.TabInfo>();
	
	static final class TabInfo {
		private final int _position;
		private final Class<?> _class;
		private final Bundle _args;
		
		public TabInfo(int position, Class<?> cls, Bundle args) {
			_class = cls;
			_args = args;
			_position = position;
		}
	}
	
    public TabsAdapter(FragmentActivity activity, ViewPager pager) {
    	super(activity.getSupportFragmentManager());
    	mContext = activity;
    	mActionBar = activity.getActionBar();
    	mViewPager = pager;
    	mViewPager.setAdapter(this);
    	mViewPager.setOnPageChangeListener(this);
    }
    
    public void addTab(Tab tab, Class<?> cls, Bundle args) {
    	TabInfo tabInfo = new TabInfo(mTabs.size(), cls, args);
    	tab.setTag(tabInfo);
    	tab.setTabListener(this);
    	mTabs.add(tabInfo);
    	mActionBar.addTab(tab);
    	notifyDataSetChanged();
    }
    
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Log.w(TAG, "onTabReselected");
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.w(TAG, "onTabSelected");
		mViewPager.setCurrentItem(tab.getPosition());
		TabInfo tabInfo = (TabInfo) tab.getTag();
		mViewPager.setCurrentItem(tabInfo._position);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.w(TAG, "onTabUnselected");
//		if (mFragment != null) {
//            // Detach the fragment, because another one is being attached
//            ft.detach(mFragment);
//        }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info._class.getName(), info._args);
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

}
