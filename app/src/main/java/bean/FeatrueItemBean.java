package bean;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Feature;

/**
 * Created by Administrator on 2016/5/22.
 */
public class FeatrueItemBean {
    private String ORGNAME;
    private Geometry geometry;
    private Feature feature;

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public String getORGNAME() {
        return ORGNAME;
    }

    public void setORGNAME(String ORGNAME) {
        this.ORGNAME = ORGNAME;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
