package com.miglab.miyo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import com.miglab.miyo.net.ImagesDownloadTask;
import org.apache.http.util.EncodingUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

	private final static String TAG = "ImageUtil";
	public final static String SD_ROOT_PATH = "/miyo";
	public final static String ICON_PATH = "/icon/";

	private final static int MB = 1024 * 1024;
	private final static int CACHE_SIZE = 8 * MB;
	public final static int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private final static long OVER_TIME = 36000;

	private static HashMap<String, Bitmap> emojiBitmapMap = new HashMap<String, Bitmap>();
	private static Pattern pattern = null;
	private static int width;
	private static boolean flag = false;
	private static String regex;

	private static ImageUtil instance;

	public static ImageUtil getInstance() {
		if (instance == null) {
			instance = new ImageUtil();
		}
		return instance;
	}

	/**
	 * 初始化图片缓存的磁盘路径(文件夹路径)
	 * 
	 * @param SD_Path
	 *            SD卡的路径
	 * @param Rom_Path
	 *            非SD卡路径
	 * @return
	 *
	 */
	public static String initImagePath(String SD_Path, String Rom_Path) {
		// MyLog.i(TAG, "缓存图片的保存路径:" + SD_Path + "; Rom_Path: " + Rom_Path);
		String return_path;
		if (TextUtils.isEmpty(SD_Path) || TextUtils.isEmpty(Rom_Path)) {
			return null;
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// MyLog.i(TAG, "有sd卡");
			File f = Environment.getExternalStorageDirectory();

			makeDirOnSD(f.getPath(), SD_Path.substring(1, SD_Path.length() - 1));
			StringBuffer path = new StringBuffer();
			// path.append(f.getPath()).append("/vgirl/");
			path.append(f.getPath()).append(SD_Path);
			File dir = new File(path.toString());
			if (!dir.exists()) {
				dir.mkdir();
			}
			return_path = path.toString();
			final String tPath = return_path;
			ToolUtil.executeInSingleThread(new Runnable() {
				@Override
				public void run() {
					getInstance().removeCache(tPath);
				}
			});
		} else {
			File dir = new File(Rom_Path);
			if (!dir.exists()) {
				dir.mkdir();
			}
			// return_path = "/sdcard/.vgirl/";
			return_path = Rom_Path;
		}
		return return_path;
	}

	/**
	 * 初始化图片缓存的磁盘路径(文件夹路径)
	 *
	 * @param con
	 * @param path
	 *            文件名（例：ImageUtil.CASH_PATH）
	 * @return
	 */
	public static String initImagePath(Context con, String path) {
		return initImagePath(SD_ROOT_PATH + path, con.getFilesDir()
				.getAbsolutePath() + path);
	}

	/**
	 * 创建绝对路径(包含多级)
	 *
	 * @param header
	 *            绝对路径的前半部分(已存在)
	 * @param tail
	 *            绝对路径的后半部分(第一个和最后一个字符不能是/，格式：123/258/456)
	 */
	public static void makeDirOnSD(String header, String tail) {
		// System.out.println("header::"+header+"====tail::"+tail);
		String[] sub = tail.split("/");
		File dir = new File(header);
		for (int i = 0; i < sub.length; i++) {
			if (!dir.exists()) {
				dir.mkdir();
			}
			File dir2 = new File(dir + File.separator + sub[i]);
			if (!dir2.exists()) {
				dir2.mkdir();
			}
			dir = dir2;
		}
	}

	/**
	 * 设置图片裁剪的一些参数
	 *
	 * @param intent
	 * @return
	 */
	public static Intent setCropExtra(Intent intent) {
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("return-data", false);

		return intent;
	}

	public static Intent setCropCarma(Intent intent, Uri imageUri) {
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		return intent;
	}

	/**
	 * 裁剪图片 尺寸不一样
	 *
	 * @param intent
	 * @return
	 */
	public static Intent setCropExtraImg(Intent intent) {
		// 设置裁剪
		intent.putExtra("crop", "true");
		// 宽高比例
		intent.putExtra("aspectX", 151);
		intent.putExtra("aspectY", 114);
		// 裁剪图片宽高
		intent.putExtra("outputX", 453);
		intent.putExtra("outputY", 342);
		intent.putExtra("return-data", false);

		return intent;
	}

	/**
	 * 保存图片，以100%的质量保存
	 *
	 * @param bitmap
	 *            图片资源
	 * @param picPath
	 *            保存路径
	 */
	public static void saveBitmap(Bitmap bitmap, String picPath) {
		// System.out.println("++"+(bitmap==null));
		if (bitmap == null || picPath == null) {
			return;
		}
		// System.out.println("====saveBitmap----"+picPath);
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 清理目录
				ImageUtil.getInstance().removeCache(picPath);
				// 判断SDCARD上的空间
				// System.out.println("space==="+freeSpaceOnSD());
				if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSD()) {
					// System.out.println("空间小于10");
					return;
				}
			}
			File file = new File(picPath);
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				if (bitmap.compress(CompressFormat.JPEG, 100, out)) {
					out.flush();
					out.close();
				}
				// if ((bitmap != null) && (!bitmap.isRecycled()))
				// bitmap.recycle();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("保存图片出错");
		}

	}

	/**
	 * 保存图片,以70%的质量保存
	 *
	 * @param bitmap
	 * @param picPath
	 */
	public static void saveBitmapOther(Bitmap bitmap, String picPath) {
		// System.out.println("++"+(bitmap==null));
		if (bitmap == null || picPath == null) {
			return;
		}
		// System.out.println("====saveBitmap----"+picPath);
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 清理目录
				ImageUtil.getInstance().removeCache(picPath);
				// 判断SDCARD上的空间
				// System.out.println("space==="+freeSpaceOnSD());
				if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSD()) {
					// System.out.println("空间小于10");
					return;
				}
			}
			File file = new File(picPath);
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);

				if (bitmap.compress(CompressFormat.JPEG, 70, out)) {
					out.flush();
					out.close();
				}
				// if ((bitmap != null) && (!bitmap.isRecycled()))
				// bitmap.recycle();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("保存图片出错");
		}

	}

	/**
	 * 保存二维码图片到本地,以及带了透明度的图片
	 *
	 * @param bitmap
	 * @param picPath
	 */
	public static void saveQRCode(Bitmap bitmap, String picPath) {
		if (bitmap == null || picPath == null) {
			return;
		}
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 清理目录
				ImageUtil.getInstance().removeCache(picPath);
				// 判断SDCARD上的空间
				// System.out.println("space==="+freeSpaceOnSD());
				if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSD()) {
					// System.out.println("空间小于10");
					return;
				}
			}

			File file = new File(picPath);

			FileOutputStream out;
			try {
				// if (!file.exists()){
				// file.createNewFile();
				// }

				out = new FileOutputStream(file);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				if (bitmap.compress(CompressFormat.PNG, 100,
						outputStream)) {
					out.write(outputStream.toByteArray());
					out.close();
				}
				// if ((bitmap != null) && (!bitmap.isRecycled()))
				// bitmap.recycle();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("保存图片出错");
		}

	}

	/**
	 * 获取图片
	 *
	 * @param path
	 *            图片的保存路径
	 * @return
	 */
	public static Bitmap getBitmap(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		Bitmap b = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, opts);
			opts.inSampleSize = ImageUtil
					.computeSampleSize(opts, -1, 320 * 320);
			System.out.println("图片取样值：" + opts.inSampleSize);
			opts.inJustDecodeBounds = false;
			b = BitmapFactory.decodeStream(new FileInputStream(path), null,
					opts);
			//
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (b != null) {
				b.recycle();
			}
			error.printStackTrace();

		}
		return b;
	}

	/**
	 * 获取图片 以ALPHA8的方式 by xpp
	 *
	 * @param path
	 *            图片的保存路径
	 * @return
	 */
	public static Bitmap getBitmapALPHA8(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		Bitmap b = null;
		try {

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Config.ALPHA_8;
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, opts);
			opts.inSampleSize = ImageUtil
					.computeSampleSize(opts, -1, 100 * 100);
			System.out.println("图片取样值：" + opts.inSampleSize);
			opts.inJustDecodeBounds = false;
			b = BitmapFactory.decodeStream(new FileInputStream(path), null,
					opts);
			//
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (b != null) {
				b.recycle();
			}
			error.printStackTrace();

		}
		return b;
	}

	/**
	 * 根据图片资源获取bitmap
	 *
	 * @param context
	 * @param resId
	 * @param isHigh
	 *            是否需要原图的像素
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId, boolean isHigh) {
		Bitmap b = null;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			if (isHigh) {
				// opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
				opt.inJustDecodeBounds = false;
				opt.inSampleSize = 1;
			} else {
				opt.inPreferredConfig = Config.ALPHA_8;
				opt.inJustDecodeBounds = false;
				opt.inSampleSize = 2;
			}

			// 获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			b = BitmapFactory.decodeStream(is, null, opt);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (b != null) {
				b.recycle();
			}
			error.printStackTrace();

		}
		return b;
	}

	/**
	 * 从本地获取图片
	 *
	 * @param path
	 *            路径
	 * @param maxWidth
	 *            取样最大宽度(单位px)
	 * @param maxHeight
	 *            取样最大高度(单位px)
	 * @return
	 */
	public static Bitmap getBitmap(String path, int maxWidth, int maxHeight) {
		Bitmap b = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, opts);
			opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, maxWidth
					* maxHeight);
			// System.out.println("图片取样值："+opts.inSampleSize);
			opts.inJustDecodeBounds = false;
			b = BitmapFactory.decodeStream(new FileInputStream(path), null,
					opts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (b != null) {
				b.recycle();
			}
			error.printStackTrace();
		}
		return b;
	}

	/**
	 * 将File转成byte[]
	 *
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将图片IO流数据转成byte[] 随后可以调用Bitmap b =
	 * BitmapFactory.decodeByteArray(byte,0,byte.length)获得Bitmap
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromIO(InputStream is) throws IOException {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 用数据装
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		// 关闭流一定要记得。
		return outstream.toByteArray();
	}

	/**
	 * 计算sdcard上的剩余空间
	 *
	 * @return
	 */
	public static int freeSpaceOnSD() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 删除过期文件
	 *
	 * @param dirPath
	 * @param filename
	 */
	public static void removeExpiredCache(String dirPath, String filename) {
		if (null == dirPath || null == filename) {
			return;
		}
		File file = new File(dirPath, filename);
		if (System.currentTimeMillis() - file.lastModified() > OVER_TIME) {
			file.delete();
		}
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的cache_size或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 *
	 * @param dirPath
	 */
	public void removeCache(String dirPath) {
		// System.out.println("removeCache");

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (null == files) {
			return;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {// 未判断多级目录缓存文件
			dirSize += files[i].length();
		}
		if (dirSize > CACHE_SIZE
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSD()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifySort());

			// clear some file
			if (removeFactor <= files.length) {
				for (int i = 0; i < removeFactor; i++) {
					files[i].delete();
					// System.out.println("removeCache delete file " +
					// files[i].getName());
				}
			}

		}
	}

	class FileLastModifySort implements Comparator<File> {

		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 截取图片，去除图片左右的暗色 暂时只处理左右两边
	 *
	 * @param srcImage
	 * @return
	 */
	public static Bitmap cutRealImage(Bitmap srcImage) {

		// System.out.println("begin time: " + System.currentTimeMillis());

		if (null == srcImage) {
			return null;
		}

		int srcWidth = srcImage.getWidth();
		int srcHeight = srcImage.getHeight();
		int middleIndexWidth = srcWidth / 2;
		int middleIndexHeight = srcHeight / 2;

		int widthLeft = 1;
		int widthRight = srcWidth - 1;
		int heightTop = 1;
		int heightBottom = srcHeight - 1;
		int indexColor = 0;

		// left,从中间往左计算
		int beginIndexWidth = 0;
		int endIndexWidth = middleIndexWidth;
		while (endIndexWidth > beginIndexWidth) {

			indexColor = srcImage.getPixel(endIndexWidth, middleIndexHeight);
			if (checkWithRemoveColor(indexColor)) {
				widthLeft = endIndexWidth + 10;
				break;
			}
			endIndexWidth = endIndexWidth - 5;
		}

		// right,从中间往右计算
		beginIndexWidth = middleIndexWidth;
		endIndexWidth = srcWidth;
		while (endIndexWidth > beginIndexWidth) {

			indexColor = srcImage.getPixel(beginIndexWidth, middleIndexHeight);
			if (checkWithRemoveColor(indexColor)) {
				widthRight = beginIndexWidth - 10;
				break;
			}
			beginIndexWidth = beginIndexWidth + 5;

		}

		// top
		int beginIndexHeight = 0;
		int endIndexHeight = middleIndexHeight;
		while (endIndexHeight > beginIndexHeight) {

			indexColor = srcImage.getPixel(middleIndexWidth, endIndexHeight);
			if (checkWithRemoveColor(indexColor)) {
				heightTop = endIndexHeight + 10;
				break;
			}
			endIndexHeight = endIndexHeight - 5;

		}

		// bottom
		beginIndexHeight = middleIndexHeight;
		endIndexHeight = srcHeight;
		while (endIndexHeight > beginIndexHeight) {

			indexColor = srcImage.getPixel(middleIndexWidth, beginIndexHeight);
			if (checkWithRemoveColor(indexColor)) {
				heightBottom = beginIndexHeight - 10;
				break;
			}
			beginIndexHeight = beginIndexHeight + 5;

		}

		// 截取中间的图片
		int dstWidth = widthRight - widthLeft;
		int dstHeight = heightBottom - heightTop;
		Bitmap dstImage = Bitmap.createBitmap(srcImage, widthLeft, heightTop,
				dstWidth, dstHeight);

		// System.out.println("end time: " + System.currentTimeMillis());

		return dstImage;
	}

	private static boolean checkWithRemoveColor(int indexColor) {

		final int removeRed = 75;
		final int removeGreen = 75;
		final int removeBlue = 75;

		int red = Color.red(indexColor);
		int green = Color.green(indexColor);
		int blue = Color.blue(indexColor);

		/*
		 * System.out.println("red: " + red); System.out.println("green: " +
		 * green); System.out.println("blue: " + blue);
		 */

		if (red == removeRed && green == removeGreen && blue == removeBlue) {
			return true;
		}

		return false;
	}

	public static int sampleSize(int width, int target) {
		int result = 1;
		for (int i = 0; i < 10; i++) {
			if (width < target * 2) {
				break;
			}
			width = width / 2;
			result = result * 2;
		}
		return result;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 截取图片的中间部分
	 *
	 * @param con
	 *            环境变量
	 * @param photo
	 *            要截取的图
	 * @param referenceResource
	 *            参照图的资源id
	 * @return 截完后的图
	 */
	public static Bitmap cutTheMiddleOfBitmap(Context con, Bitmap photo,
			int referenceResource) {
		Bitmap cutBitmap = null;
		try {
			Bitmap referenceBitmap = BitmapFactory.decodeResource(
					con.getResources(), referenceResource);
			int width = referenceBitmap.getWidth(); // 参照图宽度
			int height = referenceBitmap.getHeight(); // 参照图高度

			// 截图
			cutBitmap = cutTheMiddleOfBitmap(con, photo, width, height);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (cutBitmap != null) {
				cutBitmap.recycle();
			}
			error.printStackTrace();
		}

		return cutBitmap;
	}

	/**
	 * 截取图片的中间部分
	 *
	 * @param con
	 *            环境变量
	 * @param photo
	 *            要截取的图
	 * @param width
	 *            截完后的宽
	 * @param height
	 *            截完后的高
	 * @return 截完后的图
	 */
	public static Bitmap cutTheMiddleOfBitmap(Context con, Bitmap photo,
			int width, int height) {
		Bitmap cutBitmap = null;
		try {
			int photoWidth = photo.getWidth(); // 要截的图原宽度（目标图）
			int photoHeight = photo.getHeight(); // 要截的图原高度
			float scaleWidth = ((float) width) / photoWidth; // 目标图与参照图宽度的比例

			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			// MyLog.v(TAG, "scaleWidth,photoWidth,width---" + scaleWidth + ","
			// + photoWidth + "," + width);
			BitmapFactory.Options opts = new BitmapFactory.Options();

			// 将目标图按比例缩放原图直至等于某边参照图的宽高显示。
			Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0, photoWidth,
					photoHeight, matrix, true);

			// 取图形左上角坐标
			int retX = width > height ? 0 : (height - width) / 2;
			int retY = width > height ? (width - height) / 2 : 0;
			// MyLog.v(TAG, "retX,retY,width,height---" + retX + "," + retY +
			// ","
			// + width + "," + height);

			// 截图
			cutBitmap = Bitmap.createBitmap(resizedBitmap, retX, retY, width,
					height, null, false);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			if (cutBitmap != null) {
				cutBitmap.recycle();
			}
			error.printStackTrace();
		}

		return cutBitmap;
	}

	/** 显示头像 */
	public static void showIconImage(Context con, Handler uiHandler,
			List<String> images, int msgWhat, boolean needSleep) {
		showImage(con, uiHandler, images, msgWhat, needSleep,
				ImageUtil.ICON_PATH);
	}

	/** 显示图片 */
	public static void showImage(Context con, Handler uiHandler,
			List<String> images, int msgWhat, boolean needSleep, String pathtype) {
		showImage(con, uiHandler, images, msgWhat, needSleep, pathtype, 3);
	}

	/** 显示图片 */
	public static void showImage(Context con, Handler uiHandler,
			List<String> images, int msgWhat, boolean needSleep,
			String pathtype, int every) {
		if (images.size() <= 0)
			return;

		try {
			List<String> list = new ArrayList<String>();
			list.addAll(images);

			String path = ImageUtil.initImagePath(ImageUtil.SD_ROOT_PATH
					+ pathtype, con.getFilesDir().getAbsolutePath() + pathtype);

			ImagesDownloadTask task = new ImagesDownloadTask(uiHandler, list,
					path, msgWhat, needSleep, every);
			task.execute();

		} catch (Exception e) {
			// MyLog.e(TAG, "showImage 更新图片出错.");
			e.printStackTrace();
		}
	}

	/**
	 * 截屏
	 *
	 * @param v
	 *            视图
	 * @param filePath
	 *            保存路径
	 */
	public static String getScreenHot(View v, String filePath) {
		try {
			Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap);
			v.draw(canvas);
			try {
				FileOutputStream fos = new FileOutputStream(filePath);
				bitmap.compress(CompressFormat.PNG, 100, fos);
				return filePath;
			} catch (FileNotFoundException e) {
				e.printStackTrace();

			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// MyLog.i("截屏", "内存不足！");
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 10, baos);
		byte[] result = baos.toByteArray();
		try {
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Bitmap bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(b, 0, b.length, opts);

			opts.inSampleSize = ImageUtil
					.computeSampleSize(opts, -1, 100 * 100);
			opts.inJustDecodeBounds = false;

			return BitmapFactory.decodeByteArray(b, 0, b.length, opts);
			// return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 获得圆角图片的方法
	 *
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Drawable 转 bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
									: Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	public static boolean setIcon(ImageView iv, String url, Context con) {
		Bitmap bitmap = null;
		String path = ImageUtil.initImagePath(con, ICON_PATH);
		String imageName = ToolUtil.md5(url);
		return setIcon(iv, imageName, path, bitmap);
	}

	public static boolean setIcon(ImageView iv, String imageName, String path,
			Bitmap bitmap) {
		if (imageName != null && imageName.length() > 0
				&& new File(path + imageName).exists()) {
			try {
				bitmap = getBitmap(path + imageName);
				if (bitmap != null) {
					iv.setImageBitmap(bitmap);
					return true;
				}
			} catch (OutOfMemoryError e) {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
		return false;
	}
	
	public static boolean setActiveIcon(ImageView iv, String imageName, String path,
			Bitmap bitmap, int bitmapWidth, int bitmapHeight) {
		if (imageName != null && imageName.length() > 0
				&& new File(path + imageName).exists()) {
			try {
				bitmap = getBitmap(path + imageName);
				if (bitmap != null) {
					bitmap = Bitmap.createScaledBitmap(bitmap, 
							bitmapWidth, bitmapHeight, true);
					iv.setImageBitmap(bitmap);
					return true;
				}
			} catch (OutOfMemoryError e) {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
		return false;
	}
}
