package util;

import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/4/25.
 */
public class RequestUtil {


    private static final String NAMESPACE = "http://tempuri.org/";
//    public static final String url = "http://123.56.210.130/PropertyPadCenter/Services/PropertyPadService.asmx";
    public static final String url = "http://123.56.210.253/PropertyPadCenter/Services/PropertyPadService.asmx";//正式服务器
    public static String Login = "LoginView";
    public static String Checktask = "Checktask";//检查任务
    public static String GetJJJG = "GetJJJG";//经纪机构
    public static String GetWY = "GetWY";//物业
    public static String GetDXS = "GetDXS";//地下室
    public static String GetFWList = "GetFWList";//房屋

    public static String GetBT = "GetBT";
    public static String GetJCTJCX  = "GetJCTJCX";
    public static String GetJCX = "GetJCX";
    public static String GetMBLB = "GetMBLB";


    public static String GetJJJGDetail = "GetJJJGDetail";
    public static String GetProWYDetail = "NewGetProWYDetail";
    public static String GetBasementDetail = "GetBasementDetail";
    public static String GetHouseDetail = "GetHouseDetail";
    public static String GetProDXSFWHeadInfo = "GetProDXSFWHeadInfo";//物业管理、普通地下室、房屋的详细信息

    public static String SaveNewTempleteHeadAndItem = "SaveNewTempleteHeadAndItem";
    public static String GetCheckManList = "GetCheckManList";//检查人员列表

    public static String GetInspectHistoryByObjName = "GetInspectHistoryByObjName";//检查对象获取记录

    public static String GetInspectHistoryByInspector = "GetInspectHistoryByInspector";
    public static String GetInspectHistoryByLoginName = "NewGetInspectHistoryByLoginName";//当前用户的检查记录

    public static String SaveNewCheckTable = "NewSaveNewCheckTable";//上传检查单
    public static String CheckTableToWord = "CheckTableToWord";//获取检查单的word文档
    public static String MarkBasicInfo = "MarkBasicInfo";//更改基本信息
    public static String UpdateInspectHistoryState = "UpdateInspectHistoryState";//更改检查单的状态
    public static String UpdateInspectHistroyInfo = "UpdateInspectHistroyInfo";//更改检查单的信息

    public  static String GetInspectHistory = "GetInspectHistoryNew";//历史追踪
    public  static String checkUpdate = "checkUpdate";//检查更新
    public  static String NewcheckUpdate = "NewcheckUpdate";//检查更新

    public static String GetStreetList = "GetStreetList";//获取街道列表
    public static String GetInspectStatcicData = "GetInspectStatcicData";
    public static String GetInspectStatcicDataByBranch = "NewGetInspectStatcicDataByBranch";
    public static String GetInspectStatcicDataByType = "NewGetInspectStatcicDataByType";
    public static String GetInspectStatcicDataByJJJGHead = "NewGetInspectStatcicDataByJJJGHead";

    public static String GetJJJGDetailByName = "GetJJJGDetailByName";
    public static String GetBasementDetailByName = "GetBasementDetailByName";
    public static String GetProWYDetailByName = "GetProWYDetailByName";
    public static String GetHouseDetailByName = "GetHouseDetailByName";


    public static String GetJJJGListByStreetID = "GetJJJGListByStreetID";//通过街道id来获取对象

    public static String GetWYListByStreetID = "GetWYListByStreetID";//通过街道id来获取物业项目对象

    public static String GetDXSListByStreetIDAndProName = "GetDXSListByStreetIDAndProName";//按项目名称获取普通地下室列表

    public static String GetBuildingListByStreetIDAndProName = "GetBuildingListByStreetIDAndProName";//按项目名称获取楼幢列表

    public static String UpdateInspectHistoryNoticed = "UpdateInspectHistoryNoticed";//点击下发表单改变告知状态

    public static String GetOtherBranchList = "GetOtherBranchList";//获取合作部门列表
    public static String UploadInspectFiles = "UploadInspectFiles";//上传附件
    public static String GetInspectAttachFiles = "GetInspectAttachFiles";//附件列表
    public static String NewGetInspectAttachFiles = "NewGetInspectAttachFiles";//附件列表
    public static String GetAttachFileData = "GetAttachFileData";//附件详情


    public static String GetInspectDetailByGuid = "NewGetInspectDetailByGuid";//通过guid请求检查单

    public static String GetInspectDetailByGuidCity = "GetInspectDetailByGuidCity";//请求市系统检查单详情

    public static String GetInspectHistoryByParamsInStatic= "GetInspectHistoryByParamsInStaticNew";//统计分析之后请求检查单

    public static String GetInspectHistoryByParamsInStaticJJJGHead= "NewGetInspectHistoryByParamsInStaticJJJGHeadNew";//通过guid请求检查单

    public static String DeletAttachFile= "DeletAttachFile";//请求删除附件

    public static String AddCommonBasement = "AddCommonBasement";//填加普通地下室
    public static String AddBuildingBlocks = "AddBuildingBlocks";//填加房屋

    public static String UploadVideoFiles  = "UploadVideoFiles";


    public static String GetVideo = "GetVideo";//上传视频的片段
    public static String UpdateVideoHT = "UpdateVideoHT";//整合视频片段为一个MP4文件

    public static String GetVideoList = "GetVideoList";
    public static String InspectFeekBack = "InspectFeekBack";

    public static String GetInspectHistoryObject = "NewGetInspectHistoryObjectNew";

    public  static  String UploadInspectLog = "UploadInspectLog";
    public  static  String DelBaseMentInfo = "DelBaseMentInfo";
    public  static  String Del_BuildingFromDPT = "Del_BuildingFromDPT";

    public  static  String NewAddNewObjectToTable = "NewAddNewObjectToTable";
    public  static  String GetJJJGHead = "GetJJJGHead";










    public static String post(String methodName,Map<String,String> param){

//      ExecutorService executorService =  Executors.newFixedThreadPool(4);
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });


        String object = "";

        // 指定WebService的命名空间和调用方法
        SoapObject soapObject = new SoapObject(NAMESPACE,methodName);
        //设置参数
        Set<String> keySet = param.keySet();
        for(String key:keySet){
            String value = param.get(key);
            soapObject.addProperty(key,value);
        }
        Log.i("TAG","------------"+methodName);
        // 生成调用WebService方法调用的soap信息，并且指定Soap版本(版本号与jar包一致)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        try {
            HttpTransportSE trans = new HttpTransportSE(url,100000);
            trans.call(NAMESPACE + methodName, envelope);
            Object result =  envelope.getResponse();
            Log.i("TAG","------------"+result.toString());
            object = result.toString();
            if("nouser".equals(object)){
                object = "";
            }
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            return object;
        }
    }

    public static String postob(String methodName,Map<String,Object> param){
        String object = "";
        Log.i("TAG","------------"+methodName);

        // 指定WebService的命名空间和调用方法
        SoapObject soapObject = new SoapObject(NAMESPACE,methodName);
        //设置参数
        Set<String> keySet = param.keySet();
        for(String key:keySet){
            Object value = param.get(key);
            soapObject.addProperty(key,value);
        }
        // 生成调用WebService方法调用的soap信息，并且指定Soap版本(版本号与jar包一致)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        try {
            HttpTransportSE trans = new HttpTransportSE(url,100000);
            trans.call(NAMESPACE + methodName, envelope);
            Object result =  envelope.getResponse();
            Log.i("TAG","------------"+result.toString());
            object = result.toString();
            if("nouser".equals(object)){
                object = "";
            }
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        finally {
            return object;
        }
    }

    public static String  checkUpdate() {
        SoapObject soapObject = new SoapObject(NAMESPACE,NewcheckUpdate);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);// 版本
        envelope.bodyOut = soapObject ;
        envelope.dotNet = true ;
        envelope.setOutputSoapObject(soapObject) ;
        HttpTransportSE trans = new HttpTransportSE(url,100000) ;
        trans.debug = true ;	// 使用调试功能

        try {
            trans.call(NAMESPACE + NewcheckUpdate, envelope) ;
            Object result = envelope.getResponse();
            Log.i("TAG","------------"+result.toString());
            String change = result.toString();
            return change;

        } catch (IOException r) {
            r.printStackTrace();
            return "";

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean downLoadFile(String newFilename,String urlstr){

        int index = urlstr.lastIndexOf("/");
        String newurl = "";
        try {
            newurl = urlstr.substring(0,index)+"/"+URLEncoder.encode(urlstr.substring(index+1,urlstr.length()),"UTF-8");
            Log.i("TAG","url"+newurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        boolean isSuccess = false;

        File dirs = new File(newFilename.substring(0,newFilename.lastIndexOf("/")));
        if(!dirs.exists()){
            dirs.mkdirs();
        }
        File file = new File(newFilename);
//如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if(file.exists())
        {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(newurl);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :"+contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(newFilename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }
        return isSuccess;
    }
}
