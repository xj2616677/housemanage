package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bean.UserBean;
import constants.Contant;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/7/28.
 */
public class NewLoginActivity extends  BaseActivity {

    private EditText edit_user;
    private EditText edit_password;
    private Button bt_login;
    private CheckBox cb_memberPW ;

    private String userid;
    private String password;

    private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
    /** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
    private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";
    /** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
    private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";
    /** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
    private boolean isNetError;
    /** 登录loading提示框 */
    private ProgressDialog proDialog;

    private  Handler loginHandler;
    private boolean isChecked;
    private TextView text_user;

    @Override
    protected void initView() {

        hideTitleView();
        setContentLayout(R.layout.newlogin_activity);
        ActivityManage.getInstance().addActivity(this);
        edit_user = (EditText) findViewById(R.id.edit_login_user);
        edit_password = (EditText) findViewById(R.id.edit_login_password);
        bt_login = (Button) findViewById(R.id.bt_login_login);
        cb_memberPW = (CheckBox) findViewById(R.id.cb_login_memberpassword);
        text_user = (TextView) findViewById(R.id.text_login_user);

        bt_login.setOnClickListener(this);

        cb_memberPW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NewLoginActivity.this.isChecked = isChecked;
            }
        });
    }

    @Override
    protected void initData() {
        text_user.setFocusable(true);
        text_user.setFocusableInTouchMode(true);
        text_user.requestFocus();


        SharedPreferences sharePre = getSharedPreferences("dialog",0);
        if(sharePre.contains("isShowDialog")){

        }else{
            SharedPreferences.Editor editor = sharePre.edit();
            editor.putBoolean("isShowDialog", false);
            editor.commit();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("公告");
            builder.setMessage(getResources().getString(R.string.dialogmessage));
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog .show();

        }
        setView(false);


        loginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                isNetError = msg.getData().getBoolean("isNetError");
                if (proDialog != null) {
                    proDialog.dismiss();
                }
                if (isNetError) {
                    Toast.makeText(NewLoginActivity.this, "登陆失败:\n请检查您的网络连接",
                            Toast.LENGTH_SHORT).show();
                }
                // 用户名和密码错误
                else {
                    Toast.makeText(NewLoginActivity.this, "登陆失败:\n请输入正确的用户名和密码",
                            Toast.LENGTH_SHORT).show();
                    // 清除以前的SharePreferences密码
                    clearSharePassword();

                }
            }
        };

    }


    /**
     * 初始化界面
     *
     * @param isRememberMe
     *            如果当时点击了RememberMe,并且登陆成功过一次,则saveSharePreferences(true,ture)后,则直接进入
     * */
    private void setView(boolean isRememberMe) {
        SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
        String userName = share.getString(SHARE_LOGIN_USERNAME, "");
        String password = share.getString(SHARE_LOGIN_PASSWORD, "");
        if (!"".equals(userName)) {
            edit_user.setText(userName);
        }
        if (!"".equals(password)) {
            edit_password.setText(password);
            isChecked = true;
            cb_memberPW.setChecked(true);
        }
        // 如果密码也保存了,则直接让登陆按钮获取焦点
        if (edit_password.getText().toString().length() > 0) {
            bt_login.requestFocus();
            // view_password.requestFocus();
        }
        share = null;
    }

    class LoginFailureHandler implements Runnable {
        @Override
        public void run() {
            userid = edit_user.getText().toString().trim().toLowerCase();
            password = edit_password.getText().toString();
            boolean loginState = validateLocalLogin(userid, password,
                    RequestUtil.url);


            // 登陆成功
            if (loginState) {
                // 需要传输数据到登陆后的界面,
                Contant.userid = userid;

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                String nowTime = year + "-" + month + "-" + day+" "+hour+":"+minute+":"+second;
                Contant.loginTime = nowTime;

                Intent intent = new Intent(NewLoginActivity.this,MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("userid", userid);
                intent.putExtras(bundle);
                startActivity(intent);
                proDialog.dismiss();
                finish();

            } else {
                // 通过调用handler来通知UI主线程更新UI,
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNetError", isNetError);
                message.setData(bundle);
                loginHandler.sendMessage(message);
            }
        }

    }


    private boolean validateLocalLogin(String userName, String password,String validateUrl){
        boolean loginState = false;
        UserBean userBean = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(validateUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();

            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){

                isNetError = true;
                return false;

            }

            Map<String,Object> map = new HashMap();
            map.put("name",userName);
            map.put("password", password);


            String isloginString = RequestUtil.postob(RequestUtil.Login, map);
            userBean = jsonJX(isloginString);
            loginState = userBean.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            isNetError = true;
            if(isRememberMe()){
                saveSharePreferences(true, true);
            }
            // TODO: handle exception
        }finally{
            if (conn != null) {
                conn.disconnect();
            }
        }

        if (loginState) {
            Contant.USERBEAN = userBean;
            if (isRememberMe()) {
                saveSharePreferences(true, true);
            } else {
                saveSharePreferences(true, false);
            }

        } else {
            if (!isNetError) {
                clearSharePassword();
            }
        }
        if (!isChecked) {
            clearSharePassword();
        }

        return loginState;

    }

    /** 记住我的选项是否勾选 */
    private boolean isRememberMe() {
        if (cb_memberPW.isChecked()) {
            return true;
        }
        return false;
    }

    private UserBean jsonJX(String resultStr){
        UserBean userBean = new UserBean();
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            String result = jsonObject.getString("result");
            if(result!=null){
                if(result.equals("1")){
                    userBean.setIsSuccess(true);
                    JSONObject object = jsonObject.getJSONArray("users").getJSONObject(0);
                    userBean.setName(object.optString("LoginName"));
                    userBean.setBranchID(object.optString("BranchID"));
                    userBean.setRoleName(object.optString("RoleName"));
                    userBean.setRoleID(object.optString("RoleID"));
                    userBean.setBranchName(object.optString("BranchName"));
                    userBean.setPassword(object.optString("Password"));
                    userBean.setRegionList(object.optString("RegionList"));
                    userBean.setUserName(object.optString("UserName"));
                }else{
                    userBean.setIsSuccess(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userBean;
    }

    private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
        SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
        if (saveUserName) {
            share.edit().putString(SHARE_LOGIN_USERNAME,
                    edit_user.getText().toString()).commit();
        }
        if (savePassword) {
            share.edit().putString(SHARE_LOGIN_PASSWORD,
                    edit_password.getText().toString()).commit();
        }
        share = null;
    }

    /** 清除密码*/
    private void clearSharePassword() {
        SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
        share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
        share = null;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_login_login:
                if(NetUtil.isNetworkAvailable(NewLoginActivity.this)){
                    userid = edit_user.getText().toString().trim().toLowerCase();
                    password = edit_password.getText().toString().trim();

                    proDialog = ProgressDialog.show(NewLoginActivity.this, "连接中..",
                            "连接中..请稍后....", true, true);
                    // 开一个线程进行登录验证,主要是用于失败,成功可以直接通过startAcitivity(Intent)转向

                    Thread loginThread = new Thread(new LoginFailureHandler());
                    loginThread.start();
                }else{
                    ShowToast(noNetText);
                }
                break;
        }
    }
}
