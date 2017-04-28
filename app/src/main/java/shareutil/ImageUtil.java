package shareutil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import bean.ImageItem;
import constants.Contant;

/**
 * Created by Administrator on 2015/11/13 0013.
 */
public class ImageUtil {

    public static Bitmap getBitmap(String imgBase64Str){
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(imgBase64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<ImageItem> getImageItems(String guid,String type){
        Log.i("TAG","guid"+guid);
        List<ImageItem> imageItems = new ArrayList<>();
        String path = Contant.filepath+"/image/"+guid+"/"+type+"/";
        File file= new File(path);
        if(file.exists()){
            File[] files = file.listFiles();
            for(File imageFile:files){
                ImageItem imageItem = new ImageItem();
                imageItem.setImagePath(imageFile.getPath());
                imageItems.add(imageItem);
            }
        }
        return imageItems;
    }

    public static String  downloadPic(String urlString, String fileName,String imageGuid,String CheckGuid,String type) {
        // 构造URL

        String name = imageGuid+"=guid="+fileName;
        String path = Contant.filepath+"/image/"+CheckGuid+"/"+type+"/"+name;
        String nativePath = Contant.filepath+"/image/"+CheckGuid+"/"+type+"/"+fileName;
        File sf=new File(path);
        File nativeFile = new File(nativePath);
        if(!sf.exists()) {
            if(!sf.getParentFile().exists()){
                sf.getParentFile().mkdirs();
            }else if(nativeFile.exists()){
                nativeFile.delete();
            }


            URL url = null;
            URLConnection con = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                url = new URL(urlString);
                // 打开连接
                con = url.openConnection();
                //设置请求超时为5s
                con.setConnectTimeout(30 * 1000);
                // 输入流
                is = con.getInputStream();

                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流

                os = new FileOutputStream(path);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (MalformedURLException e) {
//                e.printStackTrace();
                return "0";
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
                return "0";
            } catch (IOException e) {
//                e.printStackTrace();
                return "0";
            } finally {
                // 完毕，关闭所有链接
                try {
                    if(os!=null){
                        os.flush();
                        os.close();
                    }
                    if(is!=null){
                        is.close();
                    }
                    return "1";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "0";
                }
            }
        }else{
            if(nativeFile.exists()){
                nativeFile.delete();
            }
            return "1";
        }
    }




    public static void saveMyBitmap(String fileName,String imageGuid,Context context,Bitmap mBitmap,String CheckGuid,String type)  {
//        String path = context.getApplicationContext().getFilesDir().getAbsolutePath()+"/files/XML/image/"+taskId+"/"+equipid;
        String name = imageGuid+"=guid="+fileName;
        String path = Contant.filepath+"/image/"+CheckGuid+"/"+type+"/"+name;
        String nativePath = Contant.filepath+"/image/"+CheckGuid+"/"+type+"/"+fileName;
        File f = new File(path);
        File nativeFile = new File(nativePath);
        if(!f.exists()) {
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if(nativeFile.exists()){
                f.delete();
            }
            Log.i("tag", "pathFile:" + f.getParentFile());
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
                if (mBitmap != null) {
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                }
                fOut.flush();
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                context.getApplicationContext().sendBroadcast(mediaScanIntent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    fOut.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else{
            if(nativeFile.exists()){
                f.delete();
            }
        }
    }


    public static String saveMyBitmap(String fileName,Context context,Bitmap mBitmap,String dbid,String type)  {
//        String path = context.getApplicationContext().getFilesDir().getAbsolutePath()+"/files/XML/image/"+taskId+"/"+equipid;
        String name = type+fileName;
        String path = Contant.filepath+"/localimage/"+dbid+"/"+type+"/"+name;
        File f = new File(path);
        if(f.exists()) {
            f.delete();
        }

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        Log.i("tag", "pathFile:" + f.getParentFile());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            if (mBitmap != null) {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            }
            fOut.flush();
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.getApplicationContext().sendBroadcast(mediaScanIntent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } finally {
            try {
                fOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        }
        return path;
    }


    public static Bitmap imageToBitmap(String path){
        Bitmap bitmap = null;
        File file = new File(path);
        Log.i("TAG","filefilefile"+path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(byteArrayOutputStream);
            copy(fileInputStream,outputStream);
            outputStream.flush();
            byte[]  data = byteArrayOutputStream.toByteArray();
            Log.i("TAG","Data"+data.length);
            bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/DCIM/Camera/";




    public static String saveBitmap(Context context,Bitmap bm) {
        File f =null;
        try {

            Time t=new Time();
            t.setToNow();
            int year=t.year;
            int month=t.month;
            int day=t.monthDay;
            int hour=t.hour;
            int minute=t.minute;
            int second=t.second;
            String filename=""+year+month+day+hour+minute+second;

            File ff = new File(SDPATH);
            if(!(ff.exists())){
                ff.mkdirs();
            }

            f = new File(SDPATH, filename + ".jpg");
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


//            MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(), f.getAbsolutePath(), f.getName(), null);
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.getApplicationContext().sendBroadcast(mediaScanIntent);
//            f.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SDPATH+f.getName();
    }


    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[2*1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static  void sendBroadCaseRemountSDcard(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("TAG","已经进来");
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, null, null);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }
}
