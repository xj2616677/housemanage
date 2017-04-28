package com.example.admin.housemanage;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;

import java.util.List;

import adapter.EListAdapter;
import bean.Group;
import constants.Contant;
import tiandituMap.TianDiTuTiledMapServiceLayer;
import tiandituMap.TianDiTuTiledMapServiceType;
import util.ActivityManage;
import util.Globals;

/**
 * Created by admin on 2016/7/1.
 */
public class NewMapActivity extends  BaseActivity {

    private MapView mapView;
    private TianDiTuTiledMapServiceLayer mapTiledLayer;
    private TianDiTuTiledMapServiceLayer mapTextLayer;
    private TianDiTuTiledMapServiceLayer mapRSServiceLayer;
    private TianDiTuTiledMapServiceLayer mapRStextLayer;
    private ArcGISDynamicMapServiceLayer JJJGDynLayer;
    private ArcGISDynamicMapServiceLayer otherDynLayer;

    private ImageButton basemapButton;
    private ImageButton layersButton;
    private ImageButton trackButton;

    private Dialog dialog;
    private List<Group> groups;
    private ExpandableListView listView;
    private EListAdapter adapter;

    private boolean isRS = false;



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
        basemapButton.setOnClickListener(this);
        layersButton.setOnClickListener(this);
        trackButton.setOnClickListener(this);

        mapTiledLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
        mapView.addLayer(mapTiledLayer);
        mapTextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
        mapView.addLayer(mapTextLayer);

        mapRSServiceLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.IMG_C);
        mapView.addLayer(mapRSServiceLayer);

        mapRStextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CIA_C);
        mapView.addLayer(mapRStextLayer);
        mapRSServiceLayer.setVisible(false);
        mapRStextLayer.setVisible(false);
        mapView.setMinScale(3062276.0678362367);
        mapView.setMaxScale(3193.372537059311);


        JJJGDynLayer = new ArcGISDynamicMapServiceLayer(Globals.JJJGserviceURL);
        mapView.addLayer(JJJGDynLayer);
        otherDynLayer = new ArcGISDynamicMapServiceLayer(Globals.fxserviceURL);
        mapView.addLayer(otherDynLayer);

        JJJGDynLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status==STATUS.INITIALIZED){
                    JJJGDynLayer.getLayers()[1].setVisible(false);
                    JJJGDynLayer.refresh();
                }
            }
        });

//        ArcGISFeatureLayer layer1 = new ArcGISFeatureLayer(Globals.serviceURL0, new ArcGISFeatureLayer.Options());
//        mapView.addLayer(layer1);
        String fgsid = Contant.USERBEAN.getBranchID();
        Log.d("TAG", "fgsid" + fgsid);
        if (fgsid==null){
            mapView.setExtent(new Envelope(116.1288661234941, 40.206246305886964, 116.37821056517957, 39.813733190922285));
        }else {
            switch (fgsid) {
                case "11010801":
                    Log.i("TAG","11010801");
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


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
//                showlayers();
                break;
            case R.id.track_mapview:
//                setSelect(2);
                break;
//            case R.id.text1_track:
//                setSelect(4);
//                break;
//            case R.id.text2_track:
//                setSelect(5);
//                break;
//            case R.id.button1_content:
//                setSelect(6);
//                break;
//            case R.id.button2_content:
//                setSelect(7);
//                break;
//            case R.id.button1_basedialog:
//                setSelect(15);
//                break;
//            case R.id.button2_basedialog:
//                setSelect(16);
//                break;
//            case R.id.button3_basedialog:
//                setSelect(17);
//                break;
            default:
                break;
        }
    }

//    private void showlayers() {
//        dialog = new Dialog(this, R.style.DialogStyle);
//        dialog.setCanceledOnTouchOutside(true);
//        Window mWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = mWindow.getAttributes();
//        lp.x = 190;
//        lp.y = -320;
//        dialog.show();
//        dialog.setContentView(R.layout.layers_diolog);
//        groups = new ArrayList<Group>();
//        getJSONObject();
//        listView = (ExpandableListView) dialog.findViewById(R.id.exlistView_layers);
//        adapter = new EListAdapter(this, groups, mapView,nulllayer);
//        listView.setAdapter(adapter);
//        listView.setOnChildClickListener(adapter);
//
//    }



}
