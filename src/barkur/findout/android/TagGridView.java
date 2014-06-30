package barkur.findout.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import barkur.findout.android.model.ModelTag;
import barkur.findout.android.utility.ImageUtility;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TagGridView extends GridView {
	private Context mContext;
	private ArrayList<ModelTag> tags;
	
	public TagGridView(Context context, ArrayList<ModelTag> _tags) {
		super(context);
		mContext = context;
		tags = _tags;
		init();
	}

	private void init() {
		setNumColumns(3);
		setVerticalSpacing(5);
		setHorizontalSpacing(5);
		setAdapter(new TagAdapter());
	}
	
	class TagAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		@Override
		public int getCount() {
			return tags.size();
		}

		@Override
		public ModelTag getItem(int position) {
			return tags.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View itemView, ViewGroup arg2) {
			ViewHolder holder;
			if (itemView == null) {
				holder = new ViewHolder();
				mInflater = LayoutInflater.from(mContext);
				itemView = mInflater.inflate(R.layout.view_grid_tag, null);
				holder.image = (ImageView) itemView.findViewById(R.id.image);
				holder.text = (TextView) itemView.findViewById(R.id.text);
				
				itemView.setTag(holder);
			} else {
				holder = (ViewHolder) itemView.getTag();
			}
			
			ImageLoader.getInstance().displayImage(tags.get(position).icon, holder.image, ImageUtility.getNormalImageOptions(), new ImageUtility.AnimateFirstDisplayListener());
			holder.text.setText(tags.get(position).title);
			
			return itemView;
		}
		
		class ViewHolder {
			public ImageView image;
			public TextView text;
		}
	}
}
