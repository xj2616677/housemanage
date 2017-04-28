package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.housemanage.R;

import constants.Contant;


public class InstallPrint {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	//下载apk失败
	private static final int DOWN_ERROR = 3;
	//服务器超时
	private static final int GET_UNDATAINFO_ERROR = 4;

	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private ProgressDialog progressDialog;
	//服务器版本号
	private int serviceCode;
	private Handler handler;
	private String appname;
	private File apkfile ;



	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件

					installApk();
//				openApk();
					break;
				case GET_UNDATAINFO_ERROR:
					//服务器超时
					Toast.makeText(mContext, "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
					break;
				case DOWN_ERROR:
					//下载apk失败
					Toast.makeText(mContext, "下载新版本失败", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		};
	};

	public InstallPrint(Context context)
	{
		this.mContext = context;
		progressDialog = new ProgressDialog(context);

		appname = Globals.print+".apk";
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{

		showNoticeDialog();

	}

	/**
	 * 检查软件是否有更新版本
	 *
	 * @return
	 */








	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.print_download_title);
		builder.setMessage(R.string.print_download);
		// 更新
		builder.setPositiveButton(R.string.print_downloadbtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.print_laterbtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle(R.string.print_downloading);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}


	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Contant.filepath;
					mSavePath = sdpath + "update";
					URL url = new URL(Globals.PRINTURL);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}

					File apkFile = new File(mSavePath, appname );
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what = DOWN_ERROR;
				mHandler.sendMessage(msg);

				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};




	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		apkfile = new File(mSavePath,appname);
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);


	}



	public  void openApk(Context context) {
		PackageManager manager = context.getPackageManager();
		// 这里的是你下载好的文件路径
		PackageInfo info = manager.getPackageArchiveInfo(apkfile.toString(), PackageManager.GET_ACTIVITIES);
		if (info != null) {
			Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
			context.startActivity(intent);
		}
	}

}
