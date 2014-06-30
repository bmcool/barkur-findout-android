package barkur.findout.android;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import barkur.findout.android.model.ModelCategory;
import barkur.findout.android.utility.ImageUtility;

public class MainActivity extends BaseActivity {
	private static String TAG = "MainActivity";
	private Context mContext;
	private ImageView image_category;
	private TextView text_category;
	private ViewPager mViewPager;
	private ArrayList<ModelCategory> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		getIntentData();
		findView();
		setView();
	}

	private void findView() {
		image_category = (ImageView) findViewById(R.id.image_category);
		text_category = (TextView) findViewById(R.id.text_category);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void setView() {
		ImageLoader.getInstance().displayImage(categories.get(0).icon, image_category, ImageUtility.getNormalImageOptions(), new ImageUtility.AnimateFirstDisplayListener());
		text_category.setText(categories.get(0).title);
		mViewPager.setAdapter(new CategoryAdapter());
		mViewPager.setOnPageChangeListener(pager_change_listener);
	}

	@SuppressWarnings("unchecked")
	private void getIntentData() {
		categories = (ArrayList<ModelCategory>) getIntent().getSerializableExtra("category");
	}
	
	class CategoryAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ModelCategory model_category = categories.get(position);
			TagGridView grid = new TagGridView(mContext, model_category.tags);
			container.addView(grid, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			return grid;
		}
	}
	
	private ViewPager.OnPageChangeListener pager_change_listener = new ViewPager.OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			ImageLoader.getInstance().displayImage(categories.get(position).icon, image_category, ImageUtility.getNormalImageOptions(), new ImageUtility.AnimateFirstDisplayListener());
			text_category.setText(categories.get(position).title);
			Log.i(TAG, "title:" + categories.get(position).title);
			Log.i(TAG, "icon:" + categories.get(position).icon);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
