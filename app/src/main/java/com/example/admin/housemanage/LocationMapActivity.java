package com.example.admin.housemanage;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import constants.Contant;
import tiandituMap.TianDiTuTiledMapServiceLayer;
import tiandituMap.TianDiTuTiledMapServiceType;

/**
 * Created by admin on 2016/7/19.
 */
public class LocationMapActivity extends BaseActivity {

    private MapView mapView;
    private TianDiTuTiledMapServiceLayer mapTiledLayer;
    private TianDiTuTiledMapServiceLayer mapTextLayer;
    private GraphicsLayer graphicsLayer;

    @Override
    protected void initView() {
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        hideTitleView();
        setContentLayout(R.layout.locationmap_activity);
        mapView = (MapView) findViewById(R.id.mapview_location);

        mapTiledLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
        mapTextLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
        mapView.addLayer(mapTiledLayer);
        mapView.addLayer(mapTextLayer);

        graphicsLayer = new GraphicsLayer();
        mapView.addLayer(graphicsLayer);
        mapView.setExtent(new Envelope(116.18315783037804, 40.13212747148824, 116.37120941913443, 39.835709062148766));
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
                if (mapTextLayer.isVisible()) {
                    mapTextLayer.refresh();
                }
            }
        });

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(o==mapView&&status == STATUS.INITIALIZED){
                    mapView.setOnSingleTapListener(new OnSingleTapListener() {
                        @Override
                        public void onSingleTap(float v, float v1) {
                            graphicsLayer.removeAll();
                            Point point = mapView.toMapPoint(v, v1);
                            Contant.longitube = point.getX()+"";
                            Contant.latitube = point.getY()+"";
                            Graphic graphic = new Graphic(point,new SimpleMarkerSymbol(Color.RED,15, SimpleMarkerSymbol.STYLE.CIRCLE));
                            graphicsLayer.addGraphic(graphic);
                            Contant.isChooseMap = true;
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void initData() {

    }
}
