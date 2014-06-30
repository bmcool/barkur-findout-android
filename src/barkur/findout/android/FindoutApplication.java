package barkur.findout.android;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class FindoutApplication extends Application {
	private static String TAG = "FindoutApplication";
	private static FindoutApplication mApplication;

	
	public static FindoutApplication getApplication() {
		return mApplication;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		initImageLoader(getApplicationContext());
	}
	
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.MAX_PRIORITY)
				.threadPoolSize(3)
				.denyCacheImageMultipleSizesInMemory()
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
//				.memoryCache(new FIFOLimitedMemoryCache(15 * 1024 * 1024))
				.memoryCacheSize(30 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
//				.discCache(new TotalSizeLimitedDiscCache(tDataPath, 50 * 1024 * 1024))
				
//				default
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.threadPoolSize(3)
//				.tasksProcessingOrder(QueueProcessingType.FIFO)
//				.imageDecoder(new CustomImageDecoder(false))
				
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
