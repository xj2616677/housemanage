package bean;
import java.util.ArrayList;

import util.Globals;

public class Group {
    private String id;
    private String title;
    private String url;
    private int layer;
    private ArrayList<Child> children;
    private boolean isChecked;
    String mapLayar3= Globals.fxserviceURL2;
    String mapLayar4= Globals.fxserviceURL8;
    String mapLayar5= Globals.fxserviceURL3;

    public Group(String id, String title) {
        this.id=id;
        this.title = title;
        children = new ArrayList<Child>();
        this.isChecked = true;
        setUrl();
    }

    private void setUrl() {
        switch (id){
            case "1":
                url=mapLayar3;
                layer=5;
                break;
            case "2":
                url=mapLayar4;
                layer=4;
                break;
            case "3":
                url=mapLayar5;
                layer=6;
                break;
        }
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getChecked() {
        return this.isChecked;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex(){return layer;};

    public void addChildrenItem(Child child) {
        children.add(child);
    }

    public int getChildrenCount() {
        return children.size();
    }

    public Child getChildItem(int index) {
        return children.get(index);
    }

    public String getUrl(){
        return url;
    }
}

