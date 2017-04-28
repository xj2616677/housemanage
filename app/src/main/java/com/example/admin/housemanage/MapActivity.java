package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Child;
import adapter.EListAdapter;
import adapter.FeatureItemAdapter;
import bean.Group;
import bean.FeatrueItemBean;
import constants.Contant;
import datepicker.cons.DPMode;
import datepicker.views.DatePicker;
import tiandituMap.TianDiTuTiledMapServiceLayer;
import tiandituMap.TianDiTuTiledMapServiceType;
import util.ActivityManage;
import util.Globals;
import util.NetUtil;
import util.RequestUtil;
import util.Util;

/**
 * Created by admin on 2016/3/31.
 */
public class MapActivity extends BaseActivity implements View.OnClickListener{
    private MapView mapView;
    private ImageButton basemapButton, layersButton, trackButton, aroundButton, ivsearch;
    private Button button1_content, button2_content, button1_around, button2_around, button1_base, button2_base, button3_base;
    private int select;
    private TextView text1_track, text2_track, text_content, text1_base, text2_base, dingwei_around, tvsearch;
    private EditText editText1, editText1_content, editText2_content, editText3_content, editText_around, editText2_around, etsearch;
    private Dialog dialog, aroundDialog, contentDialog, baseDialog, dateDialog, nameDialog, mapDialog;
    private ListView namelistView;
    private Spinner spinner_around;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String string, strsearch;
    private String btype = null;
    private String inspectType = null;
    private String objName = null;
    private String inspector = null;
    private String startDate = null;
    private String endDate = null;
    private Point point, p;
    private double radius;
    private int[] graphics, graphics2, graphics3, graphics6, graphics7, graphics8, graphics9;
    private Callout callout;
    private DatePicker datePicker;
    private static GraphicsLayer drawCircleLayer = null;
    private GraphicsLayer mGraphicsLayer;
    private FeatureResult featureResult;
    private Handler mHandler;
    private FeatrueItemBean featureItem;
    private ProgressDialog progressDialog = null;
    private String fgsid;
    private String mapUrl = Globals.map;
    private String mapLayar1 = Globals.serviceURL0;
    private String mapLayar2 = Globals.serviceURL1;
    private  String mapLayar3 = Globals.fxserviceURL2;
    private String mapLayar4 = Globals.fxserviceURL8;
    private String mapLayar5 = Globals.fxserviceURL3;
    private String mapLayar8 = Globals.fxserviceURL7;
    private String mapLayar9 = Globals.fxserviceURL9;
    private ArrayList<Group> groups;
    private  ExpandableListView listView;
    private ArcGISFeatureLayer.Options options;
    private EListAdapter adapter;
    //    private ArcGISTiledMapServiceLayer layer;
    private TianDiTuTiledMapServiceLayer mapTiledLayer;
    private TianDiTuTiledMapServiceLayer mapTextLayer;
    private TianDiTuTiledMapServiceLayer mapRSServiceLayer;
    private TianDiTuTiledMapServiceLayer mapRStextLayer;
    private ArcGISFeatureLayer nulllayer ;
    private ArcGISFeatureLayer layer1 ;
    private ArcGISFeatureLayer layer2 ;
    private ArcGISFeatureLayer layer3 ;
    private ArcGISFeatureLayer layer4 ;
    private ArcGISFeatureLayer layer5 ;
    private ArcGISFeatureLayer layer8 ;
    private ArcGISFeatureLayer layer9 ;

    private Feature feature;
    private List<FeatrueItemBean> featureList = new ArrayList<FeatrueItemBean>();
    private Map<String, String> params;
    private boolean isVisible;
    private FeatureItemAdapter featureAdapter;
    private ListView searchlistview;
    private String type = "";
    private String clickName = "";
    private String objectId;
    private List<Map<String, Object>> mapList;

    private LocationDisplayManager ldm;
    private Point locationPoint;
    private boolean isRS = false;

    private ImageButton img_location;
    private String searchText = "";
    private String editText = "";

    private List<Map<String, Object>> dataList;
    @Override
    protected void initView() {
        hideTitleView();
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentLayout(R.layout.maplayout);

        ActivityManage.getInstance().addActivity(this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setMapBackground(0xffffff, 0xffffff, 0, 0);


        basemapButton=(ImageButton)findViewById(R.id.basemap_mapview);
        layersButton = (ImageButton) findViewById(R.id.layers_mapview);
        trackButton = (ImageButton) findViewById(R.id.track_mapview);
        img_location = (ImageButton) findViewById(R.id.img_map_location);
        basemapButton.setOnClickListener(this);
        layersButton.setOnClickListener(this);
        trackButton.setOnClickListener(this);
        img_location.setOnClickListener(this);
//        aroundButton.setOnClickListener(this);

        tvsearch = (TextView) findViewById(R.id.tvsearch_mapview);
        etsearch = (EditText) findViewById(R.id.etsearch_mapview);
        searchlistview = (ListView) findViewById(R.id.searchlistview_mapview);
        ivsearch = (ImageButton) findViewById(R.id.ivsearch_mapview);

//        layer = new ArcGISTiledMapServiceLayer(mapUrl);
//    final ArcGISFeatureLayer nulllayer = new ArcGISFeatureLayer(null, options);

//    ArcGISFeatureLayer layer6 = new ArcGISFeatureLayer(mapLayar6, options);
//    ArcGISFeatureLayer layer7 = new ArcGISFeatureLayer(mapLayar7, options);

        mapTiledLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
        mapTextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
        mapView.addLayer(mapTiledLayer,0);
        mapView.addLayer(mapTextLayer,1);
        mapRSServiceLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.IMG_C);
        mapView.addLayer(mapRSServiceLayer,2);
        mapRStextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CIA_C);
        mapView.addLayer(mapRStextLayer,3);
        mapRSServiceLayer.setVisible(false);
        mapRStextLayer.setVisible(false);
        mapView.setMinScale(3062276.0678362367);
        mapView.setMaxScale(3193.372537059311);



        nulllayer = new ArcGISFeatureLayer(null, options);
        layer1 = new ArcGISFeatureLayer(mapLayar1, options);
        layer2 = new ArcGISFeatureLayer(mapLayar2, options);
        layer3 = new ArcGISFeatureLayer(mapLayar3, options);
        layer4 = new ArcGISFeatureLayer(mapLayar4, options);
        layer5 = new ArcGISFeatureLayer(mapLayar5, options);
        layer8 = new ArcGISFeatureLayer(mapLayar8, options);
        layer9 = new ArcGISFeatureLayer(mapLayar9, options);
//


//        ArcGISDynamicMapServiceLayer JJJGDynLayer = new ArcGISDynamicMapServiceLayer(Globals.JJJGserviceURL);
//        mapView.addLayer(JJJGDynLayer);
//        ArcGISDynamicMapServiceLayer otherDynLayer = new ArcGISDynamicMapServiceLayer(Globals.fxserviceURL);
//        mapView.addLayer(otherDynLayer);
        mapView.addLayer(layer4, 4);
        mapView.addLayer(layer3, 5);
        mapView.addLayer(layer5, 6);
        mapView.addLayer(layer8, 7);
        mapView.addLayer(layer1, 8);
        mapView.addLayer(layer2, 9);
        mapView.addLayer(layer9, 10);


        mapView.setOnZoomListener(new OnZoomListener() {

            @Override
            public void preAction(float paramFloat1, float paramFloat2,
                                  double paramDouble) {
                // TODO Auto-generated method stub
                //缩放后
            }

            @Override
            public void postAction(float paramFloat1, float paramFloat2,
                                   double paramDouble) {
                // TODO Auto-generated method stub
                //缩放前 防止标注重叠
                //map_tidiOld2.clearTiles();
                if (mapTextLayer.isVisible()) {
                    mapTextLayer.refresh();
                }
                if (mapRStextLayer.isVisible()) {
                    mapRStextLayer.refresh();
                }
            }
        });

        fgsid = Contant.USERBEAN.getBranchID();
        if (fgsid==null){
            mapView.setExtent(new Envelope(116.1288661234941, 40.206246305886964, 116.37821056517957, 39.813733190922285));
        }else {
            switch (fgsid) {
                case "11010801":
                    mapView.setExtent(new Envelope(116.30175636756874, 39.92939850778408, 116.3218589786505, 39.896563712465316));
                    break;
                case "11010802":
                    mapView.setExtent(new Envelope(116.20324121669302, 40.01404737715362, 116.27705720009098, 39.896841541452545));
                    break;
                case "11010803":
                    mapView.setExtent(new Envelope(116.31307835381497, 39.9997506819434, 116.36214543620322, 39.92118367898071));
                    break;
                case "11010804":
                    mapView.setExtent(new Envelope(116.17669988453979, 40.09081167560488, 116.27800950829774, 39.92663182580552));
                    break;
                case "11010805":
                    mapView.setExtent(new Envelope(116.32135123758023, 40.02944741716699, 116.36390534630476, 39.961325719878396));
                    break;
                case "11010806":
                    mapView.setExtent(new Envelope(116.28624838495497, 40.09423548356619, 116.35660341296689, 39.98111931141457));
                    break;
                case "11010807":
                    mapView.setExtent(new Envelope(116.14143272592902, 40.162626259714344, 116.24139889954787, 40.00135371834479));
                    break;
                case "110108":
                    mapView.setExtent(new Envelope(116.1288661234941, 40.206246305886964, 116.37821056517957, 39.813733190922285));
                    break;
                case "0":
                    mapView.setExtent(new Envelope(116.1288661234941, 40.206246305886964, 116.37821056517957, 39.813733190922285));
                    break;

            }
        }
        callout = mapView.getCallout();
        callout.setStyle(R.xml.calloutstyle);
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status==STATUS.LAYER_LOADED){
                    mapView.setOnSingleTapListener(new OnSingleTapListener() {
                        @Override
                        public void onSingleTap(float v, float v1) {
                            if(mapView.isLoaded()){
                                jianpandelete();
                                Point b = mapView.toMapPoint(v, v1);
                                searchlistview.setVisibility(View.GONE);
                                if (callout.isShowing()) {
                                    callout.hide();
                                } else {
                                    point = mapView.toMapPoint(v, v1);
                                    if (mapView.getLayer(4) == nulllayer) {
                                        graphics = new int[0];
                                    }else{
                                        if(layer4.isInitialized()){
                                            graphics = layer4.getGraphicIDs(v, v1, 20);
                                        }
                                    }
                                    if (mapView.getLayer(6) == nulllayer) {
                                        graphics2 = new int[0];
                                    }else {
                                        if(layer3.isInitialized()){
                                            graphics2 = layer3.getGraphicIDs(v, v1, 20);

                                        }
                                    }
                                    if (mapView.getLayer(6) == nulllayer) {
                                        graphics3 = new int[0];
                                    }else {
                                        if(layer5.isInitialized()){
                                            graphics3 = layer5.getGraphicIDs(v, v1, 20);

                                        }
                                    }
                                    if (mapView.getLayer(7) == nulllayer) {
                                        graphics6 = new int[0];
                                    }else {
                                        if(layer8.isInitialized()){
                                            graphics6 = layer8.getGraphicIDs(v, v1, 20);
                                        }
                                    }
                                    if (mapView.getLayer(8) == nulllayer) {
                                        graphics7 = new int[0];
                                    }else {
                                        if(layer1.isInitialized()){
                                            graphics7 = layer1.getGraphicIDs(v, v1, 20);

                                        }
                                    }
                                    if (mapView.getLayer(9) == nulllayer) {
                                        graphics8 = new int[0];
                                    }else {
                                        if(layer2.isInitialized()){
                                            graphics8 = layer2.getGraphicIDs(v, v1, 20);
                                        }
                                    }
                                    if (mapView.getLayer(10) == nulllayer) {
                                        graphics8 = new int[0];
                                    }else {
                                        if(layer9.isInitialized()){
                                            graphics9 = layer9.getGraphicIDs(v, v1, 20);
                                        }
                                    }

                                    if(graphics!=null&&graphics2!=null&&graphics3!=null&&graphics6!=null&&graphics7!=null&&graphics8!=null){
                                        int a = graphics.length + graphics2.length + graphics3.length + graphics6.length + graphics7.length + graphics8.length;
                                        if (a == 0) {
                                            return;
                                        } else {
                                            setCalloutData();
                                        }
                                    }

                                }
                            }
                        }
                    });
                }
            }
        });

//        aroundButton= (ImageButton) findViewById(R.id.around_mapview);


        tvsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvsearch.setVisibility(View.GONE);
                etsearch.setVisibility(View.VISIBLE);
                etsearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//2.调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
                imm.showSoftInput(etsearch, InputMethodManager.SHOW_FORCED);
            }
        });
        ivsearch.setOnClickListener(new ivsearchOnClickListener());
    }

    @Override
    protected void initData() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        progressDialog.dismiss();
                        String result = (String) msg.obj;
                        if (result.equals("")) {
                            ShowToast("服务器连接异常");
                        } else if (result.equals("[]")) {
                            ShowToast("该对象没有检查历史");
                        } else {
                            Intent intent = new Intent(MapActivity.this, HistoryInfoActivity.class);
                            intent.setAction("map");
                            intent.putExtra("result", result);
                            MapActivity.this.startActivity(intent);
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        progressDialog.dismiss();
                        String result2 = (String) msg.obj;
                        if (result2.equals("")) {
                            ShowToast("服务器连接失败");
                        } else if (result2.equals("[]")) {
                            ShowToast("你搜索的对象不存在");
                        } else {
                            Intent intent = new Intent(MapActivity.this, MaptrackActivity.class);
                            intent.putExtra("inspector",inspector);
                            intent.putExtra("startDate",startDate);
                            intent.putExtra("endDate",endDate);
                            intent.putExtra("result", result2);
                            startActivity(intent);
                        }

                        break;
                }
            }
        };
    }

    private Point getLocationGPS() {
        Point point = null;
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locMan.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc==null){
                Toast.makeText(MapActivity.this, "定位失败,请移动后重试", Toast.LENGTH_SHORT).show();
            }else{
                double latitude =  loc.getLatitude();
                double longitude =loc.getLongitude();
                point = new Point(latitude,longitude);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
            builder.setTitle("温馨提示");
            builder.setMessage("GPS未打开,请打开GPS");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MapActivity.this.startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return point;
    }


    private void locationGPS(){


        ldm = mapView.getLocationDisplayManager();
        ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        ldm.start();
        ldm.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                locationPoint = new Point(location.getLongitude(),location.getLatitude());
                ldm.pause();
                mapView.zoomToScale(locationPoint, 3193.372537059311);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                ldm.pause();
                LocationManager locationManager = (LocationManager) MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
                boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetWork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!isGps){
                    ShowToast("GPS没有打开，请打开Gps");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
//                    builder.setTitle("GPS提示").setMessage("是否打开GPS");
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            Intent intent = new Intent(
//                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivityForResult(intent, 0);
//
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
                }else if(!isNetWork){
                    ShowToast("定位失败，请移动到空旷位置重试!");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basemap_mapview:
                if (isRS) {
                    mapRSServiceLayer.setVisible(false);
                    mapRStextLayer.setVisible(false);
                    mapTiledLayer.setVisible(true);
                    mapTextLayer.setVisible(true);
                    isRS = false;
                } else {
                    mapTiledLayer.setVisible(false);
                    mapTextLayer.setVisible(false);
                    mapRSServiceLayer.setVisible(true);
                    mapRStextLayer.setVisible(true);
                    isRS = true;
                }
                break;
            case R.id.layers_mapview:
                setSelect(1);
                break;
            case R.id.track_mapview:
                setSelect(2);
                break;
            case R.id.img_map_location:
                if(ldm==null){
                    locationGPS();
                }else{
                    ldm.resume();
                }
                break;
            case R.id.text1_track:
                setSelect(4);
                break;
            case R.id.text2_track:
                setSelect(5);
                break;
            case R.id.button1_content:
                setSelect(6);
                break;
            case R.id.button2_content:
                setSelect(7);
                break;
//            case R.id.button1_around:
//                setSelect(8);
//                break;
//            case R.id.button2_around:
//                setSelect(9);
//                break;
//            case R.id.dingwei_around:
//                setSelect(10);
//                break;
//            case R.id.editText3_content:
//                setSelect(11);
//                break;
//            case R.id.checkbox3_layers:
//                setSelect(12);
//                break;
//            case R.id.checkbox4_layers:
//                setSelect(13);
//                break;
//            case R.id.checkbox5_layers:
//                setSelect(14);
//                break;
            case R.id.button1_basedialog:
                setSelect(15);
                break;
            case R.id.button2_basedialog:
                setSelect(16);
                break;
            case R.id.button3_basedialog:
                setSelect(17);
                break;
            default:
                break;
        }
    }


    public void setSelect(int select) {
        switch (select) {
            case 0:
                break;

            case 1:
                showlayers();
                break;
            case 2:
                string = "检查人：";
                showcontent();
                break;
            case 3:
                showaround();
                break;
            case 6:
                contentDialog.dismiss();
                break;
            case 7:
                inspector = editText1_content.getText().toString();
                startDate = editText2_content.getText().toString();
                endDate = editText3_content.getText().toString();

                if("".equals(inspector)){
                    ShowToast("请输入检查人姓名或编号");
                }else if(!"".equals(startDate)&&!"".equals(endDate)&&!Util.compare_date(startDate,endDate)){
                    ShowToast("您选择的开始时间在结束时间之后");
                }else {

                    if (NetUtil.isNetworkAvailable(MapActivity.this)) {
                        if (progressDialog == null) {
                            progressDialog = showProgressDialog();
                        } else {
                            progressDialog.show();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params2 = new HashMap<String, String>();
                                params2.put("param", inspector);
                                params2.put("startTime", startDate);
                                params2.put("endTime", endDate);
                                String result = RequestUtil.post(RequestUtil.GetInspectHistoryByInspector, params2);
                                Message msg = Message.obtain();
                                msg.what = 3;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
                break;
            case 8:
                aroundDialog.dismiss();
                mapView.setOnSingleTapListener(new OnSingleTapListener() {
                    @Override
                    public void onSingleTap(float v, float v1) {
                        point = mapView.toMapPoint(v, v1);
                    }
                });
                break;
            case 9:
                aroundDialog.dismiss();
                mapView.setOnSingleTapListener(new OnSingleTapListener() {
                    @Override
                    public void onSingleTap(float v, float v1) {
                        point = mapView.toMapPoint(v, v1);
                    }
                });
                getfeature();
                mapView.addLayer(drawCircleLayer);
                break;
            case 10:
                aroundDialog.dismiss();

                mapView.setOnSingleTapListener(new OnSingleTapListener() {
                    @Override
                    public void onSingleTap(float v, float v1) {
                        point = mapView.toMapPoint(v, v1);
                        editText2_around.setText("(" + point.getX() + "," + point.getY() + ")");
                        aroundDialog.show();

                    }
                });
                break;
//            case 11:
//                showdate(editText3_content);
//                break;
            case 15:
                //看详情
//                if (objectId==null){
//                    ShowToast("当前对象ID为Null");
//                }else {
                Intent intent1 = new Intent(MapActivity.this, BrokerInfoActivity.class);
                intent1.setAction("map");
                if("物业项目".equals(type)){
                    intent1.putExtra("type", "物业管理");
                }else if(type.contains("经纪机构")){
                    intent1.putExtra("type", "经纪机构");
                }else{
                    intent1.putExtra("type", type);
                }
                intent1.putExtra("name", clickName);
                startActivity(intent1);
//                }
                break;
            case 16:
                //查看历史


                if (NetUtil.isNetworkAvailable(MapActivity.this)) {
                    if (progressDialog == null) {
                        progressDialog = showProgressDialog();
                    } else {
                        progressDialog.show();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            String nowTime = year + "-" + month + "-" + day;
                            Map<String, String> params = new HashMap<String, String>();
                            if("物业项目".equals(type)){
                                params.put("btype", "物业管理");
                            }else if(type.contains("经纪机构")){
                                params.put("btype", "经纪机构");
                            }else{
                                params.put("btype", type);
                            }
                            params.put("inspectType", "");
                            params.put("objName", clickName);
                            params.put("inspector", "");
                            params.put("startDate", "");
                            params.put("endDate", "");
                            String result = RequestUtil.post(RequestUtil.GetInspectHistory, params);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }else{
                    ShowToast(noNetText);
                }
                break;
            case 17:
                //现在检查
                Intent intent3 = new Intent(MapActivity.this, SpotCheckActivity.class);
                intent3.putExtra("name", clickName);
                intent3.putExtra("type", type);
                intent3.setAction("map");
                startActivity(intent3);
                break;
            default:
                break;
        }
        this.select = select;
    }


    private void showdate(EditText editText) {
        editText1 = editText;
        dateDialog = new Dialog(this, R.style.DialogStyle);
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.show();
        dateDialog.setContentView(R.layout.date_layout);
        datePicker = (DatePicker) dateDialog.findViewById(R.id.datepicker);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        datePicker.setDate(year, month);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                dateDialog.dismiss();
                editText1.setText(date);
            }
        });
    }

    private void showaround() {
        aroundDialog = new Dialog(this, R.style.DialogStyle);
        aroundDialog.setCanceledOnTouchOutside(true);
        aroundDialog.show();
        aroundDialog.setContentView(R.layout.around_dialog);

        editText_around = (EditText) aroundDialog.findViewById(R.id.editText_around);
        editText2_around = (EditText) aroundDialog.findViewById(R.id.editText2_around);
        dingwei_around = (TextView) aroundDialog.findViewById(R.id.dingwei_around);
        spinner_around = (Spinner) aroundDialog.findViewById(R.id.spinner_around);
        button1_around = (Button) aroundDialog.findViewById(R.id.button1_around);
        button2_around = (Button) aroundDialog.findViewById(R.id.button2_around);

        button1_around.setOnClickListener(this);
        button2_around.setOnClickListener(this);
        dingwei_around.setOnClickListener(this);

        data_list = new ArrayList<>();
        data_list.add("经纪机构");
        data_list.add("物业企业");
        data_list.add("物业项目");
        data_list.add("普通地下室");
        data_list.add("房屋安全");
        //设置适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置下拉样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner_around.setAdapter(arr_adapter);

    }

    private void showcontent() {
        contentDialog = new Dialog(this, R.style.DialogStyle);
        contentDialog.setCanceledOnTouchOutside(true);
        contentDialog.show();
        contentDialog.setContentView(R.layout.content_dialog);
        text_content = (TextView) contentDialog.findViewById(R.id.text_content);

        button1_content = (Button) contentDialog.findViewById(R.id.button1_content);
        button2_content = (Button) contentDialog.findViewById(R.id.button2_content);

        editText1_content = (EditText) contentDialog.findViewById(R.id.editText1_content);
        editText2_content = (EditText) contentDialog.findViewById(R.id.editText2_content);
        editText3_content = (EditText) contentDialog.findViewById(R.id.editText3_content);
        text_content.setText(string);

        editText2_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showdate(editText2_content);
                }
                return false;
            }
        });
        editText3_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showdate(editText3_content);
                }
                return false;
            }
        });
        button1_content.setOnClickListener(this);
        button2_content.setOnClickListener(this);
    }

//    private void showtrack() {
//        dialog = new Dialog(this, R.style.DialogStyle);
//
//        dialog.setCanceledOnTouchOutside(true);
//        Window mWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = mWindow.getAttributes();
//        lp.x=230;
//        lp.y=-320;
//        dialog.show();
//        dialog.setContentView(R.layout.track_dialog);
//        text1_track=(TextView)dialog.findViewById(R.id.text1_track);
//        text2_track=(TextView)dialog.findViewById(R.id.text2_track);
//        text1_track.setOnClickListener(this);
//        text2_track.setOnClickListener(this);
//    }

    private void showlayers() {
        dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.x = 190;
        lp.y = -320;
        dialog.show();
        dialog.setContentView(R.layout.layers_diolog);
        groups = new ArrayList<Group>();
        getJSONObject();
        listView = (ExpandableListView) dialog.findViewById(R.id.exlistView_layers);
        adapter = new EListAdapter(this, groups, mapView,nulllayer);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

    }


    public void getJSONObject() {
        String jsonStr = "{'CommunityUsersResult':[{'CommunityUsersList':[{'fullname':'总支','userid':'0','username':'a1'}"
                + ",{'fullname':'分支','userid':'1','username':'b2'}],'id':'0','title':'经纪机构'},{'CommunityUsersList':[],'id':'1','title':'物业企业'},"
                + "{'CommunityUsersList':[],'id':'2','title':'物业项目'},{'CommunityUsersList':[],'id':'3','title':'普通地下室'}," +
                "{'CommunityUsersList':[" +
//                "{'fullname':'危房','userid':'2','username':'c1'},{'fullname':'积水点','userid':'3','username':'d1'}," +
                "{'fullname':'房屋','userid':'4','username':'e1'}],'id':'4','title':'房屋安全'}]}";
        try {
            JSONObject CommunityUsersResultObj = new JSONObject(jsonStr);
            JSONArray groupList = CommunityUsersResultObj.getJSONArray("CommunityUsersResult");

            for (int i = 0; i < groupList.length(); i++) {
                JSONObject groupObj = (JSONObject) groupList.get(i);
                Group group = new Group(groupObj.getString("id"), groupObj.getString("title"));
                JSONArray childrenList = groupObj.getJSONArray("CommunityUsersList");

                for (int j = 0; j < childrenList.length(); j++) {
                    JSONObject childObj = (JSONObject) childrenList.get(j);
                    Child child = new Child(childObj.getString("userid"), childObj.getString("fullname"),
                            childObj.getString("username"));
                    group.addChildrenItem(child);
                }

                groups.add(group);
            }
        } catch (JSONException e) {
        }
    }

    public void getfeature() {
        if (drawCircleLayer != null) {
            drawCircleLayer.removeAll();
        }
        drawCircleLayer = new GraphicsLayer();
        mapView.addLayer(drawCircleLayer);
        Polygon circle = new Polygon();
        getCircle(point, circle);
        int color = Color.parseColor("#FFEC8B");
        FillSymbol symbol = new SimpleFillSymbol(color);
        symbol.setAlpha(100);
        Graphic g = new Graphic(circle, symbol);
        drawCircleLayer.addGraphic(g);
    }

    private void getCircle(Point point, Polygon circle) {
        if (!circle.isEmpty()) {
            circle.setEmpty();
        }
        String r = editText_around.getText().toString();
        radius = Double.valueOf(r);
        Double scale = mapView.getScale();
        Point[] points = getPoints(point, radius / scale);
        circle.startPath(points[0]);
        for (int i = 1; i < points.length; i++)
            circle.lineTo(points[i]);
    }

    private Point[] getPoints(Point point, double radius) {

        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = point.getX() + radius * sin;
            y = point.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

    public void setCalloutData() {
        nameDialog = new Dialog(this, R.style.DialogStyle);
        nameDialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = nameDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = 400; // 宽度
        lp.height = 400; // 高度
        dialogWindow.setAttributes(lp);
        nameDialog.show();
        nameDialog.setContentView(R.layout.selectname);
        namelistView = (ListView) nameDialog.findViewById(R.id.list_selectname);
        mapList = getdata();
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(this, mapList, R.layout.item_selectname, new String[]{"name", "address"}, new int[]{R.id.text1_nameitem, R.id.text2_nameitem});
        namelistView.setAdapter(simpleAdapter2);
        namelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Point centerPoint = new Point();
                centerPoint.setXY((Double) mapList.get(position).get("x"), (Double) mapList.get(position).get("y"));
                clickName = (String) mapList.get(position).get("name");
                String address = (String) mapList.get(position).get("address");
                type = (String) mapList.get(position).get("type");
                nameDialog.dismiss();
                calloutshow(centerPoint, clickName, address);
            }
        });
    }

    private void calloutshow(Point centerPoint, String name, String type) {

        if (callout.isShowing()) {
            callout.hide();
        }
        mapView.zoomToScale(centerPoint, mapView.getScale());
        View callout_view = LayoutInflater.from(MapActivity.this).inflate(R.layout.base_dialog, null);
        LinearLayout lin_button = (LinearLayout) callout_view.findViewById(R.id.lin_callout_button);
        if(!"物业项目".equals(type)&&!type.contains("经纪机构")){
            lin_button.setVisibility(View.GONE);
        }
        text1_base = (TextView) callout_view.findViewById(R.id.text1_basedialog);
        text2_base = (TextView) callout_view.findViewById(R.id.text2_basedialog);
        text1_base.setText(name);
        text2_base.setText(type);

        button1_base = (Button) callout_view.findViewById(R.id.button1_basedialog);
        button2_base = (Button) callout_view.findViewById(R.id.button2_basedialog);
        button3_base = (Button) callout_view.findViewById(R.id.button3_basedialog);
        button1_base.setOnClickListener(this);
        button2_base.setOnClickListener(this);
        button3_base.setOnClickListener(this);
        callout.setContent(callout_view);
        callout.setCoordinates(centerPoint);
        callout.show();
    }

    public List<Map<String, Object>> getdata() {

        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if (graphics.length != 0) {
            for (int i = 0; i < graphics.length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature = layer4.getGraphic(graphics[i]);
                map.put("name", feature.getAttributeValue("XMMC"));
                map.put("id", feature.getAttributeValue("PROJECTID"));
                map.put("address", "物业项目");
                map.put("type", "物业管理");
                map.put("x", feature.getAttributeValue("x"));
                map.put("y", feature.getAttributeValue("y"));
                dataList.add(map);
            }
        }
        if (graphics2.length != 0) {
            for (int i2 = 0; i2 < graphics2.length; i2++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature2 = layer3.getGraphic(graphics2[i2]);
                map.put("name", feature2.getAttributeValue("QYMC"));
                map.put("id", graphics2[i2]);
                map.put("address", "物业企业");
                map.put("type", "物业企业");
                p = (Point) feature2.getGeometry();
                map.put("x", p.getX());
                map.put("y", p.getY());
                dataList.add(map);
            }
        }
        if (graphics3.length != 0) {
            for (int i3 = 0; i3 < graphics3.length; i3++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature3 = layer5.getGraphic(graphics3[i3]);
                map.put("name", feature3.getAttributeValue("DXSDZ"));
                map.put("id", graphics3[i3]);
                map.put("address", "普通地下室");
                map.put("type", "普通地下室");
                p = (Point) feature3.getGeometry();
                map.put("x", p.getX());
                map.put("y", p.getY());
                dataList.add(map);
            }
        }
//        if (graphics4.length != 0) {
//            for (int i4 = 0; i4 < graphics4.length; i4++) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                Feature feature4 = layer6.getGraphic(graphics4[i4]);
//                Log.d("TAG", feature4.getAttributes().toString());
//                map.put("name",feature4.getAttributeValue("BUILD_SITE"));
//                map.put("id", graphics4[i4]);
//                map.put("address", "房屋安全-危房");
//                map.put("type", "房屋安全");
//                p = (Point) feature4.getGeometry();
//                map.put("x", p.getX());
//                map.put("y", p.getY());
//                dataList.add(map);
//            }
//        }
//        if (graphics5.length != 0) {
//            for (int i5 = 0; i5 < graphics5.length; i5++) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                Feature feature5 = layer7.getGraphic(graphics5[i5]);
//                Log.d("TAG", feature5.getAttributes().toString());
//                map.put("name", feature5.getAttributeValue("BUILD_SITE"));
//                map.put("id", graphics5[i5]);
//                map.put("address", "房屋安全-积水点");
//                map.put("type", "房屋安全");
//                p = (Point) feature5.getGeometry();
//                map.put("x", p.getX());
//                map.put("y", p.getY());
//                dataList.add(map);
//            }
//        }
        if (graphics6.length != 0) {
            for (int i6 = 0; i6 < graphics6.length; i6++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature6 = layer8.getGraphic(graphics6[i6]);
                map.put("name", feature6.getAttributeValue("BUILD_SITE"));
                map.put("id", graphics6[i6]);
                map.put("address", "房屋安全-房屋");
                map.put("type", "房屋安全");
                map.put("x", feature6.getAttributeValue("CenterX"));
                map.put("y", feature6.getAttributeValue("CenterY"));
                dataList.add(map);
            }
        }
        if (graphics7.length != 0) {
            for (int i7 = 0; i7 < graphics7.length; i7++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature7 = layer1.getGraphic(graphics7[i7]);
                map.put("name", feature7.getAttributeValue("ORGNAME"));
                map.put("id", graphics7[i7]);
                map.put("address", "经纪机构-总支");
                map.put("type", "经纪机构");
                p = (Point) feature7.getGeometry();
                map.put("x", p.getX());
                map.put("y", p.getY());
                dataList.add(map);
            }
        }
        if (graphics8.length != 0) {
            for (int i8 = 0; i8 < graphics8.length; i8++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Feature feature8 = layer2.getGraphic(graphics8[i8]);
                map.put("name", feature8.getAttributeValue("ORGNAME"));
                map.put("id", graphics8[i8]);
                feature8.getAttributes();
                map.put("address", "经纪机构-分支");
                map.put("type", "经纪机构");
                p = (Point) feature8.getGeometry();
                map.put("x", p.getX());
                map.put("y", p.getY());
                dataList.add(map);

            }
        }
        return dataList;
    }


    public class ivsearchOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            editText= etsearch.getText().toString();
            if(editText.equals(searchText)){
                searchlistview.setVisibility(View.VISIBLE);
            }else{
                MyAsyncQuery myAsyncQuery = new MyAsyncQuery(editText);
                myAsyncQuery.execute();
            }
            if(searchlistview!=null){
                searchlistview.setFocusable(true);
                searchlistview.setFocusableInTouchMode(true);
                searchlistview.requestFocus();
                jianpandelete();
            }
        }
    }

    private Map<String,Object> getSelectMap(Map<java.lang.String, java.lang.Object> objectMap,int id,Geometry geometry){

        Map<java.lang.String, java.lang.Object> map = new HashMap<>();
        switch (id){
            case 2:
                if(objectMap.containsKey("QYMC")){
                    map.put("name", objectMap.get("QYMC"));
                }else{
                    map.put("name", "");
                }
                map.put("type","物业企业");
                break;
            case 3:
                if(objectMap.containsKey("DXSDZ")){
                    map.put("name", objectMap.get("DXSDZ"));
                }else{
                    map.put("name", "");
                }
                map.put("type","普通地下室");
                break;
            case 7:
                if(objectMap.containsKey("BUILD_SITE")){
                    map.put("name", objectMap.get("BUILD_SITE"));
                }else{
                    map.put("name", "");
                }
                map.put("type","房屋安全-房屋");
                break;
            case 8:
                if(objectMap.containsKey("XMMC")){
                    map.put("name", objectMap.get("XMMC"));
                }else{
                    map.put("name", "");
                }
                map.put("type","物业项目");
                break;
            case 11:
                if(objectMap.containsKey("ORGNAME")){
                    map.put("name", objectMap.get("ORGNAME"));
                }else{
                    map.put("name", "");
                }
                map.put("type","经纪机构-总支");
                break;
            case 12:
                if(objectMap.containsKey("ORGNAME")){
                    map.put("name", objectMap.get("ORGNAME"));
                }else{
                    map.put("name", "");
                }
                map.put("type","经纪机构-分支");
                break;
        }

        if(objectMap.containsKey("x")){
            map.put("x",objectMap.get("x")+"");
            map.put("y",objectMap.get("y")+"");
        }else if(objectMap.containsKey("CenterX")){
            map.put("x",objectMap.get("CenterX")+"");
            map.put("y",objectMap.get("CenterY")+"");
        }else{
            Point point = (Point) geometry;
            map.put("x",point.getX()+"");
            map.put("y",point.getY()+"");
        }
        return map;
    }


    class MyAsyncQuery extends AsyncTask<String,Void,List<FindResult>>{

        private String name;

        public MyAsyncQuery(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(NetUtil.isNetworkAvailable(MapActivity.this)){
                if(progressDialog ==null){
                    progressDialog = MapActivity.this.showProgressDialog();
                }else{
                    progressDialog.show();
                }
            }else{
                MapActivity.this.ShowToast(MapActivity.this.noNetText);
            }

        }

        @Override
        protected List<FindResult> doInBackground(String... params) {

            FindParameters findParameters = new FindParameters();
            findParameters.setLayerIds(new int[]{2, 3, 7, 8, 11, 12});
            findParameters.setOutputSpatialRef(SpatialReference.create(4326));
            findParameters.setReturnGeometry(true);
            findParameters.setSearchFields(new String[]{"XMMC", "QYMC", "DXSDZ", "BUILD_SITE", "ORGNAME"});
            findParameters.setSearchText(name);
            FindTask findTask = new FindTask(Globals.fxserviceURL);

            List<FindResult> findResults = null;

            try {
                findResults = findTask.execute(findParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return findResults;
        }

        @Override
        protected void onPostExecute(List<FindResult> objects) {
            super.onPostExecute(objects);
            progressDialog.dismiss();
            if(objects==null){
                MapActivity.this.ShowToast("未搜索到相关信息");
            }else{
                MapActivity.this.ShowToast("length"+objects.size());
                if(objects.size()!=0){
                     dataList= new ArrayList<>();
                    for(FindResult findResult:objects){
                        Map<String,Object> objectMap = findResult.getAttributes();
                        Map<String,Object> map = getSelectMap(objectMap,findResult.getLayerId(),findResult.getGeometry());
                        dataList.add(map);
                    }
                    if (dataList.size() == 0) {
                        if (callout.isShowing()) {
                            callout.hide();
                        }
                        searchlistview.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(MapActivity.this, "未搜索到相关信息", Toast.LENGTH_SHORT).show();
                    } else {
                        searchText = editText;
                        searchlistview.setVisibility(View.VISIBLE);
                        jianpandelete();
                        progressDialog.dismiss();
                        SimpleAdapter simpleAdapter = new SimpleAdapter(MapActivity.this, dataList, R.layout.item_selectname, new String[]{"name", "type"}, new int[]{R.id.text1_nameitem, R.id.text2_nameitem});
                        searchlistview.setAdapter(simpleAdapter);
                        searchlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Point point = new Point();
                                Map<String ,Object> map = dataList.get(position);
                                point.setXY(Double.parseDouble((String) map.get("x")), Double.parseDouble((String) map.get("y")));
                                clickName = map.get("name").toString();
                                type = map.get("type").toString();
                                calloutshow(point, clickName, type);
                                progressDialog.dismiss();
                                searchlistview.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        }
    }




    private List<Map<String, Object>> getPoint(ArcGISFeatureLayer featureLayer, String key, String text, String type, int a) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        int[] uids = featureLayer.getGraphicIDs();
        if (uids != null&&uids.length!=0) {
            for (int i = 0; i < uids.length; i++) {
                Feature layerFeature = featureLayer.getGraphic(uids[i]);
                if(layerFeature!=null){
                    String name = (String) layerFeature.getAttributeValue(key);
                    if (name.contains(text)) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", name);
                        map.put("address", type);
                        if (a == 0) {
                            p = (Point) layerFeature.getGeometry();
                            map.put("x", p.getX());
                            map.put("y", p.getY());
                        } else if(a==1) {
                            map.put("x", layerFeature.getAttributeValue("x"));
                            map.put("y", layerFeature.getAttributeValue("y"));
                        }else if(a==2) {
                            map.put("x", layerFeature.getAttributeValue("CenterX"));
                            map.put("y", layerFeature.getAttributeValue("CenterY"));
                        }
                        mapList.add(map);
                    }
                }
            }
        }
        return mapList;
    }

//    private void jianpandelete() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////获取状态信息
//        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (isOpen) {
//
////2.调用hideSoftInputFromWindow方法隐藏软键盘
//            imm.hideSoftInputFromWindow(etsearch.getWindowToken(), 0); //强制隐藏键盘
//        }
//
//    }
}

