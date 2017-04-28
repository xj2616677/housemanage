package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.CheckBean;
import bean.CheckHeadBean;
import bean.CheckLocationBean;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.InfoAttributeBean;
import bean.InspectHistoryBean;
import bean.TableHeadBean;
import constants.Contant;
import tiandituMap.TianDiTuTiledMapServiceLayer;
import tiandituMap.TianDiTuTiledMapServiceType;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MaptrackActivity extends BaseActivity implements View.OnClickListener {
    private MapView mapView_history;
    private Button button_callout;
    private String jsonStr,inspectGuid,inspectNo;
    private TextView nametext_callout,datetext_callout;
    private GraphicsLayer gLayerPos;
    private Point wgspoint;
    private List<CheckLocationBean> checkLoctionBeanList;
    private Callout callout;
    private Point calloutPoint;

    private TianDiTuTiledMapServiceLayer mapTiledLayer;
    private TianDiTuTiledMapServiceLayer  mapTextLayer;
    private ProgressDialog progressDialog;
    private Handler mHandler;
    private InspectHistoryBean inspectHistoryBean;
    private String bussinessType;
    private String brokerId;
    private String DXSList;
    private String FWList;
    private String xqDate;
    private Gson gson;
    private String inspectTime;
    private String AddUser = "";

    private List<TableHeadBean> headList;
    private List<InfoAttributeBean> infoList;
    private List<CheckTermBean> illegalTerm;

    private String inspector = "",startDate = "",endDate = "";

    private String branchs = "";

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.jcyxx));
        setRightText("列表");
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentLayout(R.layout.historytrack_activity);

        ActivityManage.getInstance().addActivity(this);
        mapView_history= (MapView) findViewById(R.id.mapview_history);
        mapTiledLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
        mapTextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
        mapView_history.addLayer(mapTiledLayer);
        mapView_history.addLayer(mapTextLayer);
//        mapView_history.addLayer(new ArcGISTiledMapServiceLayer(mapurl));
        mapView_history.setExtent(new Envelope(116.18315783037804,40.13212747148824, 116.37120941913443,39.835709062148766));
        callout = mapView_history.getCallout();
        callout.setStyle(R.xml.calloutstyle);

        mapView_history.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == mapView_history && status == STATUS.INITIALIZED) {
                    mapView_history.setOnSingleTapListener(new OnSingleTapListener() {
                        @Override
                        public void onSingleTap(float v, float v1) {
                            SelectOneGraphic(v, v1);


                        }
                    });
                }
            }
        });


        mapView_history.setOnZoomListener(new OnZoomListener() {

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
                if (mapTextLayer.isVisible()) {
                    mapTextLayer.refresh();
                }
            }
        });
    }

    private void SelectOneGraphic(float v, float v1) {
        if (callout.isShowing()){
            callout.hide();
        }
        // 获得图层
        GraphicsLayer layer = GetGraphicLayer();
        if (layer != null && layer.isInitialized() && layer.isVisible()) {
            Graphic result = null;
            // 检索当前 光标点（手指按压位置）的附近的 graphic对象
            result = GetGraphicsFromLayer(v, v1, layer);
            if (result != null) {
                // 获得附加特别的属性
                String name = (String) result.getAttributeValue("Name");
                String Inspector2 = (String) result.getAttributeValue("Inspector");
                inspectGuid= (String) result.getAttributeValue("InspectGuid");
                AddUser= ((String) result.getAttributeValue("User")).toLowerCase();
                // 显示提示
                View callout_view = LayoutInflater.from(MaptrackActivity.this).inflate(R.layout.callout_maptrack, null);
                nametext_callout = (TextView) callout_view.findViewById(R.id.nametext_callout);
                datetext_callout = (TextView) callout_view.findViewById(R.id.datetext_callout);
                button_callout = (Button) callout_view.findViewById(R.id.button_callout);
                nametext_callout.setText(name);
                datetext_callout.setText(Inspector2);
                button_callout.setOnClickListener(this);
                callout.setContent(callout_view);
                calloutPoint.setX((Double) result.getAttributeValue("Lon"));
                calloutPoint.setY((Double)result.getAttributeValue("Lat"));
                callout.setCoordinates(calloutPoint);
                callout.show();
            }// ednd if
        }// end if
    }

    private Graphic GetGraphicsFromLayer(float v, float v1, GraphicsLayer layer) {
        Graphic result = null;
        try {
            int[] idsArr = layer.getGraphicIDs();
            double x = v;
            double y = v1;
            for (int i = 0; i < idsArr.length; i++) {
                Graphic gpVar = layer.getGraphic(idsArr[i]);
                if (gpVar != null) {
                    Point pointVar = (Point) gpVar.getGeometry();
                    pointVar = mapView_history.toScreenPoint(pointVar);
                    double x1 = pointVar.getX();
                    double y1 = pointVar.getY();
                    if (Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < 50) {
                        result = gpVar;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    @Override
    protected void initData() {

        gson= new Gson();
        checkLoctionBeanList =new ArrayList<>();
        calloutPoint=new Point();
        Intent intent = getIntent();
        jsonStr=intent.getStringExtra("result");
        inspector = intent.getStringExtra("inspector");
        startDate =intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");

        checkLoctionBeanList.addAll((List<CheckLocationBean>) (gson.fromJson(jsonStr, new TypeToken<List<CheckLocationBean>>() {
        }.getType())));
        if (checkLoctionBeanList.size()==0){
        }
        else {
//            Log.d("TAG", String.valueOf(checkLoctionBeanList.size()));
//            ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < checkLoctionBeanList.size(); i++) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("user",checkLoctionBeanList.get(i).getUser());
//                map.put("Lon", checkLoctionBeanList.get(i).getLon());
//                map.put("Lat", checkLoctionBeanList.get(i).getLat());
//                map.put("ObjName", checkLoctionBeanList.get(i).getObjName());
//                map.put("Inspector", checkLoctionBeanList.get(i).getInspector());
//                map.put("InspectGuid", checkLoctionBeanList.get(i).getInspectGuid());
//                Log.d("TAG", "(" + checkLoctionBeanList.get(i).getLon() + "," + checkLoctionBeanList.get(i).getLat() + ")");
//                dataList.add(map);
//                map = null;
//            }
            AddNewGraphic(checkLoctionBeanList);
        }

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String result = (String) msg.obj;

                switch (msg.what){
                    case 1:
                        String[] arrays = result.split("\\^_\\^");
                        String jsonResult = "";
                        if(arrays!=null&&arrays.length==2){
                            jsonResult = arrays[0];
                                    String branchJson = arrays[1];
                                    JSONArray jsonArray = null;
                                    try {
                                        jsonArray = new JSONArray(branchJson);
                                        branchs = ((JSONObject)jsonArray.get(0)).getString("branchname");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                        }



                        List<CheckBean> checkBeans = gson.fromJson(jsonResult,new TypeToken<List<CheckBean>>(){}.getType());
                        if(checkBeans!=null&&checkBeans.size()!=0) {
                            CheckBean checkBean = checkBeans.get(0);
                            inspectHistoryBean = getData(checkBean);
                            inspectHistoryBean.setBranchs(branchs);
                            bussinessType = inspectHistoryBean.getBussinesstype();
                            headList = getTableHead(inspectHistoryBean.getInspectItemsResultBean().getHead());
                            brokerId = inspectHistoryBean.getId();
                            DXSList = inspectHistoryBean.getDXSList();
                            FWList = inspectHistoryBean.getFWList();
                            xqDate = inspectHistoryBean.getRectifDeadline();
                            inspectGuid = inspectHistoryBean.getInspectGuid();
                            inspectTime = inspectHistoryBean.getCheckBaseInfoBean().getInspectTime();
//                            if (inspectHistoryBean.getBussinesstype().contains("其他")||(Contant.userid.equals(AddUser)&&"0".equals(inspectHistoryBean.getNoticeState())&&Integer.parseInt(inspectHistoryBean.getState())< 3)) {

                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Intent intent = new Intent(MaptrackActivity.this, CommitActivity.class);
                                intent.setAction("checkRecordNO");
                                String history = gson.toJson(inspectHistoryBean);
                                intent.putExtra("history", history);
                                List<CheckResultBean> results = inspectHistoryBean.getInspectItemsResultBean().getResult();
                                List<CheckHeadBean> heads = inspectHistoryBean.getInspectItemsResultBean().getHead();
                                String resultstr = gson.toJson(results);
                                String headstr = gson.toJson(heads);
                                intent.putExtra("inspectGuid", inspectHistoryBean.getInspectGuid());
                                intent.putExtra("checklist", resultstr);
                                if ("1".equals(inspectHistoryBean.getNoticeState())||!Contant.userid.equals(AddUser)) {
                                    intent.putExtra("isEnd", "true");
                                } else {
                                    intent.putExtra("isEnd", "false");
                                }
                                intent.putExtra("headlist", headstr);
                                MaptrackActivity.this.startActivity(intent);
//                            } else {
//                                if (NetUtil.isNetworkAvailable(MaptrackActivity.this)) {
//                                    if (progressDialog == null) {
//                                        progressDialog = showProgressDialog();
//                                    } else {
//                                        if (!progressDialog.isShowing()) {
//                                            progressDialog.show();
//                                        }
//                                    }
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            String result = "";
//                                            Message msg = Message.obtain();
//                                            if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
//                                                Map<String, String> params = new HashMap<>();
//                                                params.put("idlist", brokerId);
//                                                result = RequestUtil.post(RequestUtil.GetJJJGDetail, params);
//                                                msg.what = 2;
//                                                msg.arg2 = 3;
//                                                //经纪机构
//                                            } else {
//                                                Map<String, String> params = new HashMap<>();
//                                                params.put("proID", brokerId);
//                                                params.put("DXSIDList", DXSList);
//                                                params.put("FWList", FWList);
//                                                result = RequestUtil.post(RequestUtil.GetProDXSFWHeadInfo, params);
//                                                msg.what = 2;
//                                                msg.arg2 = 4;
//                                                //物业管理
//                                            }
//                                            msg.obj = result;
//                                            mHandler.sendMessage(msg);
//                                        }
//                                    }).start();
//                                } else {
//                                    if (progressDialog != null) {
//                                        progressDialog.dismiss();
//                                    }
//                                    ShowToast(noNetText);
//                                }
//                            }
                        }
                        break;
                    case 2:

                        int arg2 = msg.arg2;
                        String[] keys = null;
                        String[] names = null;
                        if (arg2 == 3) {
                            keys = getResources().getStringArray(R.array.primeinfokey);
                            names = getResources().getStringArray(R.array.primeinfoname);
                        } else if (arg2 == 4) {
                            keys = getResources().getStringArray(R.array.propertyinfokey);
                            names = getResources().getStringArray(R.array.propertyinfoname);
                        }
                        List<InfoAttributeBean> attributeBeans = getInfoList(result, keys, names);
                        infoList = getHeadData(attributeBeans, headList);
                        String info = gson.toJson(infoList);
                        List<CheckResultBean> resultBeans = inspectHistoryBean.getInspectItemsResultBean().getResult();
                        illegalTerm = getIllegalTerm(getTermGroups(resultBeans));
                        String illegaltermstr = gson.toJson(illegalTerm);
                        ArrayList<String> bussinessList = new ArrayList<>();
                        String[] busstr = bussinessType.split(",");
                        if (busstr != null && busstr.length != 0) {
                            for (String str : busstr) {
                                bussinessList.add(str);
                            }
                        } else {
                            bussinessList.add(bussinessType);
                        }

                        progressDialog.dismiss();
                        Intent wordIntent = new Intent(MaptrackActivity.this, PreviewActivity.class);
                        wordIntent.setAction("checkRecord");
                        wordIntent.putExtra("info", info);
                        wordIntent.putExtra("illegalTerm", illegaltermstr);
                        wordIntent.putExtra("termCount", getTermSize(getTermGroups(resultBeans)));
                        wordIntent.putExtra("xqDate", xqDate);
                        wordIntent.putExtra("inspectTime", inspectTime);
                        wordIntent.putExtra("inspectGuid", inspectGuid);
                        wordIntent.putExtra("inspectNo", inspectNo);
                        wordIntent.putExtra("DXSList", DXSList);
                        wordIntent.putExtra("FWList", FWList);
                        wordIntent.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
                        wordIntent.putStringArrayListExtra("bussinessType", bussinessList);
                        MaptrackActivity.this.startActivity(wordIntent);
                        break;
                }

            }
        };
    }




    private void AddNewGraphic(ArrayList<Map<String, Object>> dataList) {
        GraphicsLayer layer = GetGraphicLayer();
        Map<String, Object> map = new HashMap<String, Object>();
        if (layer != null && layer.isInitialized() && layer.isVisible()) {
            for (int i1=0;i1<dataList.size();i1++){

                String locx = dataList.get(i1).get("Lon").toString();
                String locy = dataList.get(i1).get("Lat").toString();
                Double a= Double.valueOf(locx);
                Double b= Double.valueOf(locy);
                wgspoint = new Point(a, b);
                map.put("Name", dataList.get(i1).get("ObjName").toString());
                map.put("Inspector",dataList.get(i1).get("Inspector").toString());
                map.put("InspectGuid",dataList.get(i1).get("InspectGuid").toString());
                map.put("Lon",a);
                map.put("Lat",b);
                // 创建 graphic对象
                Graphic gp = CreateGraphic(wgspoint, map);
                // 添加 Graphics 到图层
                layer.addGraphic(gp);
            }
            mapView_history.addLayer(layer);
        }
    }


    private void AddNewGraphic(List<CheckLocationBean> checkLocationBeans) {
        GraphicsLayer layer = GetGraphicLayer();
        Map<String, Object> map = new HashMap<String, Object>();
        if (layer != null && layer.isInitialized() && layer.isVisible()) {
            for (int i=0;i<checkLocationBeans.size();i++){

                String locx = checkLocationBeans.get(i).getLon();
                String locy = checkLocationBeans.get(i).getLat();
                Double a= Double.valueOf(locx);
                Double b= Double.valueOf(locy);
                wgspoint = new Point(a, b);

                map.put("User", checkLocationBeans.get(i).getUser());
                map.put("Name", checkLocationBeans.get(i).getObjName());
                map.put("Inspector",checkLocationBeans.get(i).getInspector());
                map.put("InspectGuid",checkLocationBeans.get(i).getInspectGuid());
                map.put("Lon",a);
                map.put("Lat",b);
                // 创建 graphic对象
                Graphic gp = CreateGraphic(wgspoint, map);
                // 添加 Graphics 到图层
                layer.addGraphic(gp);
            }
            mapView_history.addLayer(layer);
        }
    }

    private Graphic CreateGraphic(Point geometry, Map<String, Object> map) {
        GraphicsLayer layer = GetGraphicLayer();// 获得图层
        Drawable image = MaptrackActivity.this.getBaseContext()
                .getResources().getDrawable(R.mipmap.local);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(image);

        // 构建graphic
        // Graphic g = new Graphic(geometry, symbol);
        Graphic g = new Graphic(geometry, symbol, map);
        return g;
    }

    private GraphicsLayer GetGraphicLayer() {
        if (gLayerPos == null) {
            gLayerPos = new GraphicsLayer();
//            mapView_history.addLayer(gLayerPos);
        }
        return gLayerPos;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_righttext:
                Intent intent=new Intent(MaptrackActivity.this,HistoryInfoActivity.class);
                intent.setAction("mapTrack");
                intent.putExtra("inspector",inspector);
                intent.putExtra("startDate",startDate);
                intent.putExtra("endDate", endDate);
                startActivity(intent);
                break;
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.button_callout:
                callout.hide();

                if(NetUtil.isNetworkAvailable(MaptrackActivity.this)) {
                    if (progressDialog == null) {
                        progressDialog = showProgressDialog();
                    } else {
                        if(!progressDialog.isShowing()){
                            progressDialog.show();
                        }
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("inspectGuid",inspectGuid);
                            String result = RequestUtil.post(RequestUtil.GetInspectDetailByGuid,params);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    }).start();

                }else{
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    ShowToast(noNetText);
                }


                break;

        }
    }
}
