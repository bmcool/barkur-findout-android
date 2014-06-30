package barkur.findout.android.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import barkur.findout.android.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageUtility {
	private static String TAG = "ImageUtility";
	
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		
		return BitmapFactory.decodeStream(is,null,opt);
	}

	public static int getSampleFitView(int bmpWidth, int bmpHeight, int viewWidth, int viewHeight) {
		int sample = 1;
		if (bmpWidth > bmpHeight) {
			if (bmpWidth > viewWidth) {
				sample = bmpWidth / viewWidth;
			}
		} else {
			if (bmpHeight > viewHeight) {
				sample = bmpHeight / viewHeight;
			}
		}
		
//		Log.i("ImageUtility", "sample rate: " + sample);
		return sample;
	}
	
	public static void saveImageFileFromUrl(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
//	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//		
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				FadeInBitmapDisplayer.animate(imageView, 500);
//			}
//		}
//	}
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List displayedImages = Collections.synchronizedList(new LinkedList());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                            FadeInBitmapDisplayer.animate(imageView, 500);
                    } else {
                            imageView.setImageBitmap(loadedImage);
                    }
                    displayedImages.add(imageUri);
            }
        }
	}

	public static String getRealPathFromURI(Context context,Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) context).managedQuery( contentUri,
                        proj, // Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	
	public static Bitmap rotateBitmapFromFile(Context context, String fpath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inPreferredConfig = Bitmap.Config.ALPHA_8;
		option.inPurgeable = true;
		option.inInputShareable = true;
		option.inSampleSize = 1;
		Bitmap photo = BitmapFactory.decodeFile(fpath, option);
		
		option.inJustDecodeBounds = false;
		option.inSampleSize = getSampleFitView(option.outWidth, option.outHeight, Utility.dp2px(context, 100), Utility.dp2px(context, 100));
		
		photo = BitmapFactory.decodeFile(fpath, option);
		return rotateImage(fpath, photo);
	}

	private static Bitmap rotateImage(String filepath, Bitmap image) {
		ExifInterface ei;
		int orientation = 0;
		try {
			ei = new ExifInterface(filepath);
			orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch(orientation) {
		    case ExifInterface.ORIENTATION_ROTATE_90:
		    	image = rotate(image, 90);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_180:
		    	image = rotate(image, 180);
		        break;
		}
		
		return image;
	}
	
	private static Bitmap rotate(Bitmap image, int angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap photo = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
		image.recycle();
		
		return photo;
	}
	
	public static Bitmap decodeFile(String filepath) {
		int sample = 1;
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inPreferredConfig = Bitmap.Config.ALPHA_8;
		option.inPurgeable = true;
		option.inInputShareable = true;
		option.inSampleSize = 1;
		BitmapFactory.decodeFile(filepath, option);
		
		while (option.outWidth >= 1000 || option.outHeight >= 1000) {
			sample++;
			option.inJustDecodeBounds = true;
			option.inPreferredConfig = Bitmap.Config.ALPHA_8;
			option.inPurgeable = true;
			option.inInputShareable = true;
			option.inSampleSize = sample;
			BitmapFactory.decodeFile(filepath, option);
		}
		
		option.inJustDecodeBounds = false;
		option.inSampleSize = sample;
		Bitmap _bmp = BitmapFactory.decodeFile(filepath, option);
		return rotateImage(filepath, _bmp);
	}
	
	public static boolean compressImageFile(String soure_path, String target_path) {
		Bitmap bitmap = decodeFile(soure_path);
		
        File imageFile = new File(target_path);
		try {
			FileOutputStream out = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static DisplayImageOptions getNormalImageOptions() {
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageOnFail(null)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.NONE)
		.resetViewBeforeLoading(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	public static File saveBitmapToFile(Context mContext, Bitmap bmp, String filepath) {
        File imageFile = new File(filepath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(imageFile);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
	        out.flush();
	        out.close();
	        bmp.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return imageFile;
	}
	
	public static File rotateImageFileToPortrait(Context mContext, String fpath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inPreferredConfig = Bitmap.Config.ALPHA_8;
		option.inPurgeable = true;
		option.inInputShareable = true;
		option.inSampleSize = 1;
		Bitmap photo = BitmapFactory.decodeFile(fpath, option);
		Log.w(TAG, "bitmap ori width:" + option.outWidth);
		Log.w(TAG, "bitmap ori height:" + option.outHeight);
		option.inJustDecodeBounds = false;
		option.inSampleSize = getSampleFitView(option.outWidth, option.outHeight, 1080, 1920);
		
		photo = BitmapFactory.decodeFile(fpath, option);
		photo = rotateImage(fpath, photo);
		Log.w(TAG, "bitmap width:" + photo.getWidth());
		Log.w(TAG, "bitmap height:" + photo.getHeight());
		
		return saveBitmapToFile(mContext, photo, fpath);
	}
}
