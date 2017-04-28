package util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import shareutil.Bimp;

/**
 * Created by admin on 2016/3/10.
 */
public class Util {

    private static PackageManager packageManager;


    public static boolean compare_date(String date1,String date2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
             if(d1.getTime()<=d2.getTime()){
                return true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static long getDaynum(String date1,String date2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long day = -1;
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            day = (d2.getTime()-d1.getTime())/1000/60/60/24;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return day;
    }




    public static boolean isRestart(String date1,String date2){
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            long diff = d2.getTime()-d1.getTime();
            long day=diff/(24*60*60*1000);
            long hour=(diff/(60*60*1000)-day*24);
            long min=((diff/(60*1000))-day*24*60-hour*60);
            long s=(diff/1000-day*24*60*60-hour*60*60-min*60);

            if(day>=12){
                return true;
            }else if(day==11&&hour>=50){
                return true;
            }else{
                return false;
            }

        } catch (ParseException e) {
            return false;
        }
    }



    public static boolean launchApp(Context mcontext,String filePath) {
        packageManager = mcontext.getPackageManager();
        List<PackageInfo> packages = getAllApps();
        PackageInfo pa = null;
        for (int i = 0; i < packages.size(); i++) {
            pa = packages.get(i);
            // 获得应用名
            // String appLabel =
            // packageManager.getApplicationLabel(pa.applicationInfo).toString();
            // 获得包名
            String appPackage = pa.packageName;
            // Log.d(""+i, appLabel+"  "+appPackage);
            if ("com.dynamixsoftware.printershare.amazon".equals(appPackage)) {
                File file = new File(filePath);
//                Intent intent = packageManager
//                        .getLaunchIntentForPackage(appPackage);
                Intent intent = new Intent();
                ComponentName comp = new ComponentName(appPackage,"com.dynamixsoftware.printershare.ActivityPrintDocuments");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                intent.putExtra("scaleFitToPage", true );
                mcontext.startActivity(intent);

                return true;
            }
        }
        return false;
    }

    public static List<PackageInfo> getAllApps() {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        List<PackageInfo> paklist = packageManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            // 判断是否为非系统预装的应用 (大于0为系统预装应用，小于等于0为非系统应用)
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(pak);
            }
        }
        return apps;
    }

    public static String fileToBase64(String path){
        File file = new File(path);;
        FileInputStream inputFile = null;
        byte[] buffer = null;
        try {
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(buffer);
    }

    public static void  base64ToFile(String base64,String filePath){
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            FileOutputStream write = null;
            write = new FileOutputStream(new File(filePath));
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String picPathToBase64(String filePath){

//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Bitmap bitmap = Bimp.getSelfImage(filePath,1572864);
        String str = bitmapToBase64(bitmap);
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
        }
        return str;
    }


    public static String encodeBase64File(String path){
        File  file = new File(path);
        FileInputStream inputFile = null;
        String a = "";
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            a = Base64.encodeToString(buffer,Base64.DEFAULT);
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return a;
    }


    public static String bitmapToBase64(Bitmap bitmap){
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void saveStrToFile(String str){
        File txt=new File(Environment.getExternalStorageDirectory()+"/test.txt");
        try {
            FileWriter fileWriter = new FileWriter(txt);
            fileWriter.write(str);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
