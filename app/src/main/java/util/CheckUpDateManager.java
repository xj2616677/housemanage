package util;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.housemanage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.Contant;
import widget.LoadingLoadingDialog;
import widget.SaundProgressBar;

/**
 * Created by admin on 2016/5/23.
 */
public class CheckUpDateManager {


    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    // 下载apk失败
    private static final int DOWN_ERROR = 3;
    // 服务器超时
    private static final int GET_UNDATAINFO_ERROR = 4;

    private Handler handler;
    private final String packageName = "com.example.admin.housemanage";
    private Context mContext;
    private LoadingLoadingDialog dialog;
    /* 更新进度条 */
//    private ProgressBar mProgress;

    private SaundProgressBar saundProgressBar;

    private AlertDialog alertDialogProgressBar;
    //    private Dialog mDownloadDialog;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;

    private String appname;


    public CheckUpDateManager(Context context) {
        this.mContext = context;
//        Date date = new Date();
//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
//        String sdd = sf.format(date);
//        appname = Globals.appname + sdd + ".apk";
        appname = "housemanage.apk";
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    saundProgressBar.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case GET_UNDATAINFO_ERROR:
                    // 服务器超时
                    Toast.makeText(mContext, "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_ERROR:
                    // 下载apk失败
                    Toast.makeText(mContext, "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        };
    };




    /**
     * 检测软件更新
     *
     * 比较服务器端版本号和本地AndroidManifest.xml的version值做对比
     */
    public void checkUpdate() {
        new Async_checkupdate().execute((String) null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String result = (String) msg.obj;
                if(!"".equals(result)){
                    try {
                        JSONObject jsonObject = new JSONArray(result).getJSONObject(0);

                        int serviceCode = Integer.valueOf(jsonObject.getString("code"));
                        int versionCode = getVersionCode(mContext);

                        if (serviceCode == 0) {
                            Toast.makeText(mContext, "连接服务器超时，请检查网络", Toast.LENGTH_LONG)
                                    .show();
                        } else if (serviceCode > versionCode) {
                            // 显示提示对话框
                            showNoticeDialog(jsonObject.getString("describe"));
                        } else {
                            Toast.makeText(mContext, R.string.soft_update_no,
                                    Toast.LENGTH_LONG).show();
                            // Toast.makeText(mContext, serviceCode + "----" +
                            // versionCode, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    /**
     * 检测软件更新 不提示
     */
    public void checkUpdate2() {
        new Async_checkupdate2().execute((String) null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                if(!"".equals(result)){

                    try {
                        JSONObject jsonObject = new JSONArray(result).getJSONObject(0);

                        int serviceCode = Integer.valueOf(jsonObject.getString("code"));
                        int versionCode = getVersionCode(mContext);

                        if (serviceCode > versionCode) {
                            // 显示提示对话框
                            showNoticeDialog(jsonObject.getString("describe"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

            }
        };
        // 获取当前软件版本

    }


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 检查软件是否有更新版本
     *
     * @return
     */

    class Async_checkupdate extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            dialog = new LoadingLoadingDialog(mContext);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String  result = RequestUtil.checkUpdate();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Contant.FLAG = 0;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Message message = Message.obtain();
            message.obj = result;
            // super.onPostExecute(result);
            handler.sendMessage(message);

        }

    }


    class Async_checkupdate2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = RequestUtil.checkUpdate();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Contant.FLAG = 0;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Message message = Message.obtain();
            message.obj = result;
            // super.onPostExecute(result);
            handler.sendMessage(message);

        }

    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String describe) {

        StringBuilder strb = new StringBuilder();

        if("".equals(describe)){

            strb.append("检测到新版本，立即更新吗?\n");

        }else{
            strb.append("检测到新版本，立即更新吗?\n");
            String[] strs = describe.split(";");
            if(strs!=null&&strs.length!=0){
                for(int i=0;i< strs.length;i++){
                    strb.append((i+1)+"、"+strs[i]+"\n");
                }
            }
        }
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(strb.toString());
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }


    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
//        builder.setTitle("");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.video_progressbar, null);
//        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//        builder.setView(v);



//        alertDialogProgressBar.setContentView(R.layout.video_progressbar);
        saundProgressBar = (SaundProgressBar) v.findViewById(R.id.regularprogressbar);
        TextView text_title = (TextView) v.findViewById(R.id.text_videodialog_title);
        text_title.setText("正在更新...");
        builder.setView(v);

        saundProgressBar.setMax(100);

        Drawable indicator = mContext.getResources().getDrawable(
                R.drawable.progress_indicator);
        Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 5,
                indicator.getIntrinsicHeight());
        indicator.setBounds(bounds);

        saundProgressBar.setProgressIndicator(indicator);
        saundProgressBar.setProgress(0);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                    }
                });
        alertDialogProgressBar= builder.create();
        alertDialogProgressBar.show();
//        mDownloadDialog = builder.create();
//        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }


    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
//            try {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory()
                        + "/";
                mSavePath = sdpath + "download";
                File file = new File(mSavePath);
                if(!file.exists()){
                    file.mkdirs();
                }
//                    URL url = new URL(Globals.UPDATEURL);
//                    // 创建连接
//                    HttpURLConnection conn = (HttpURLConnection) url
//                            .openConnection();
//                    conn.connect();
//                    // 获取文件大小
//                    int length = conn.getContentLength();
//                    // 创建输入流
//                    InputStream is = conn.getInputStream();
//
//                    File file = new File(mSavePath);
//                    // 判断文件目录是否存在
//                    if (!file.exists()) {
//                        file.mkdir();
//                    }
//                    Log.i("TAG","length----"+length+"----");
//                    File apkFile = new File(mSavePath, appname);
//                    FileOutputStream fos = new FileOutputStream(apkFile);
//                    int count = 0;
//                    int len = 0;
//                    // 缓存
//                    byte buf[] = new byte[1024*1024];
//                    // 写入到文件中
//                    do {
////                    while((len = is.read(buf))!=-1 &&!cancelUpdate) {
//                        len = is.read(buf);
//                        Log.i("TAG","len----"+len+"----");
//                        count += len;
//                        // 计算进度条位置
//                        progress = (int) (((float) count / length) * 100);
//                        // 更新进度
//                        mHandler.sendEmptyMessage(DOWNLOAD);
//                        if (len <= 0) {
//                            // 下载完成
//                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//                            break;
//                        }
//                        // 写入文件
//                        fos.write(buf, 0, len);
//                    } while (!cancelUpdate);// 点击取消就停止下载.
//
////                    }
//                    fos.close();
//                    is.close();
//                }
//            } catch (Exception e) {
//                Message msg = Message.obtain();
//                msg.what = DOWN_ERROR;
//                mHandler.sendMessage(msg);
//
//                e.printStackTrace();
//            }
                URL url = null;
                URLConnection con = null;
                InputStream is = null;
                OutputStream os = null;
                try {
                    url = new URL(Globals.UPDATEURL);
                    // 打开连接
                    con = url.openConnection();
                    //设置请求超时为5s
                    con.setConnectTimeout(30 * 1000);
                    // 输入流
                    is = con.getInputStream();
                    int length = con.getContentLength();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    int count = 0;
                    // 输出的文件流

                    os = new FileOutputStream(mSavePath + File.separator + appname);
                    // 开始读取
                    while ((len = is.read(bs)) != -1&&!cancelUpdate) {
                        os.write(bs, 0, len);
                        count += len;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                    }
                    if(!cancelUpdate){
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                    }

                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = DOWN_ERROR;
                    mHandler.sendMessage(msg);
                    e.printStackTrace();

//                }
//                catch (FileNotFoundException e) {
////                e.printStackTrace();
//                } catch (IOException e) {
//                e.printStackTrace();
                } finally {
                    // 完毕，关闭所有链接
                    try {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 取消下载对话框显示
                alertDialogProgressBar.dismiss();
            }
        }
    }


    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, appname);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
