package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import adapter.MainGridAdapter;
import constants.Contant;
import shareutil.Bimp;
import util.ActivityManage;
import util.CheckUpDateManager;
import util.RequestUtil;

public class MainActivity extends BaseActivity {

//    private ImageView imageView;
    private GridView gridView;
    private TextView text_person;

    private ProgressDialog progressDialog;
    private CheckUpDateManager upDateManager;



    @Override
    protected void initView() {
        hideTitleView();
        setContentLayout(R.layout.main_layout);
        ActivityManage.getInstance().addActivity(this);
        View contentView = getContentView();
//        imageView = (ImageView) contentView.findViewById(R.id.main_image);
        text_person = (TextView) findViewById(R.id.text_main_person);
        gridView = (GridView)contentView.findViewById(R.id.main_grid);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManage.getInstance().clearOtherActivity(MainActivity.this);
        Bimp.selectBitmaps.clear();
        Bimp.tempSelectBitmap0.clear();
        Contant.isAddObject = false;
        Contant.personBitmapList.clear();
        Contant.objectBitmapList.clear();
        Contant.isSign = false;
        System.gc();
    }

    @Override
    protected void initData() {
        text_person.setText(Contant.USERBEAN.getBranchName()+"---"+Contant.USERBEAN.getUserName());

        int[] imageId = new int[]{R.mipmap.spotcheck,R.mipmap.checklist,R.mipmap.map_logo,R.mipmap.history,R.mipmap.analy,R.mipmap.update};
        MainGridAdapter mainGridAdapter = new MainGridAdapter(this,getResources().getStringArray(R.array.grid_name),imageId);
        gridView.setAdapter(mainGridAdapter);

        upDateManager= new CheckUpDateManager(this);
        upDateManager.checkUpdate2();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, SpotCheckActivity.class);
                        intent.setAction("main");
                        MainActivity.this.startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, CheckRecordActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, MapActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, HistoryActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, StatisticAnalysisActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 5:
                        CheckUpDateManager upDateManager = new CheckUpDateManager(MainActivity.this);
                        upDateManager.checkUpdate();

//                        requestVideo("b.mp4");
//                        Intent intent11 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        intent11.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        intent11.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
//                        startActivityForResult(intent11,1);



                        break;
                }
            }
        });
    }


    private void requestVideo(final String fileName){

        String filePath = Environment.getExternalStorageDirectory()+File.separator+fileName;
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final byte[] fileString = buffer;
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String,Object> params = new HashMap<String, Object>();
                params.put("loginName","shh");
                params.put("inspectGuid","fd854310034c4ac28c94dcfaadb02457");
                params.put("filestring",fileString);
                params.put("filename",fileName);

                String result = RequestUtil.postob(RequestUtil.UploadVideoFiles,params);

            }
        }).start();




    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
//                this.finish();


                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(upDateManager!=null){
            upDateManager = null;
        }
        System.gc();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("用户退出");
            builder.setMessage("确定要退出应用吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityManage.getInstance().exit();
                    android.os.Process.killProcess(android.os.Process.myPid());
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

        return super.onKeyDown(keyCode, event);
    }
}
