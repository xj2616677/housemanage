package bean;

import util.Globals;

public class Child {
    private String userid;
    private String fullname;
    private String username;
    private String userurl;
    private int j;
    String mapLayar1= Globals.serviceURL0;
    String mapLayar2= Globals.serviceURL1;
//    String mapLayar6= Globals.fxserviceURL0;
//    String mapLayar7= Globals.fxserviceURL1;
    String mapLayar8= Globals.fxserviceURL7;
    private boolean isChecked;

    public Child(String userid, String fullname, String username) {
        this.userid = userid;
        this.fullname = fullname;
        this.username = username;
        this.isChecked = true;
        setUrl();
    }

    private void setUrl() {
        switch (userid){
            case "0":
                userurl=mapLayar1;
                j=8;
                break;
            case "1":
                userurl=mapLayar2;
                j=9;
                break;
//            case "2":
//                userurl=mapLayar6;
//                j=4;
//                break;
//            case "3":
//                userurl=mapLayar7;
//                j=5;
//                break;
            case "4":
                userurl=mapLayar8;
                j=7;
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

    public String getUserid() {
        return userid;
    }

    public String getFullname() {
        return fullname;
    }
    public String getUrl() {
        return userurl;
    }
    public int getlayer(){
        return j;
    }
    public String getUsername() {
        return username;
    }
}
