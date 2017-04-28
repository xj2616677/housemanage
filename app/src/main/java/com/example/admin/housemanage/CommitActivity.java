package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import adapter.CheckListExpandableAdapter;
import adapter.GeneralListAdapter;
import adapter.GridAdapter;
import adapter.HouseSafeListAdapter;
import adapter.IllegalAdapter;
import adapter.InfoListAdapter;
import adapter.PersonAdapter;
import adapter.PersonDialogAdapter;
import adapter.PersonSignAdapter;
import adapter.VideoListAdapter;
import bean.BranchBean;
import bean.CheckBaseInfoBean;
import bean.CheckDBBean;
import bean.CheckHeadBean;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import bean.GeneralBean;
import bean.HouseSafeBean;
import bean.ImageFileBean;
import bean.ImageItem;
import bean.InfoAttributeBean;
import bean.InspectBean;
import bean.InspectHistoryBean;
import bean.InspectItemsResultBean;
import bean.PersonBean;
import bean.PrimeBrokerBean;
import bean.PropertyBean;
import bean.StreetBean;
import bean.TableHeadBean;
import bean.VideoBean;
import constants.Contant;
import datepicker.cons.DPMode;
import datepicker.views.DatePicker;
import shareutil.Bimp;
import shareutil.ImageUtil;
import shareutil.PublicWay;
import shareutil.Res;
import util.ActivityManage;
import util.DensityUtil;
import util.FileAccessI;
import util.InstallPrint;
import util.NetUtil;
import util.RequestUtil;
import util.Util;
import widget.MyExpandableListView;
import widget.MyGridView;
import widget.MyListView;
import widget.SaundProgressBar;

/**
 * Created by admin on 2016/5/9.
 */
public class CommitActivity  extends  BaseActivity{

    private MyListView listInfo;

    private ProgressDialog progressDialog;

    private Handler mHandler;
    private List<TableHeadBean> headList;
    private String brokerId = "";
    private List<InfoAttributeBean> infoList;

    private InspectHistoryBean historyBean;

    private ImageButton imgbt_createword;
    private ImageButton imgbt_uploadtoweb;
    private ImageButton imgbt_endCheck;

    private Gson gson;
    private InspectBean inspectBean;
    private List<CheckTermGroupBean> checkTermGroupBeansPrime;
    private List<CheckTermGroupBean> checkTermGroupBeansPro;
    private List<CheckTermGroupBean> checkTermGroupBeansGen;
    private List<CheckTermGroupBean> checkTermGroupBeansHouse;
    private FinalDb finalDb;
    private String checkGuid = "";

    private CheckDBBean checkDBBean;

    private MyListView list_illegal;
    private List<CheckTermBean> illegalTermBeans;

    private String action = "";

    private CheckListExpandableAdapter checkListExpandableAdapterPrime;
    private CheckListExpandableAdapter checkListExpandableAdapterPro;
    private CheckListExpandableAdapter checkListExpandableAdapterGen;
    private CheckListExpandableAdapter checkListExpandableAdapterHouse;

    //    private CheckBox cb_XCZG;
//    private CheckBox cb_XQZG;
    private TextView text_insertperson;
    private ListView list_person;
    private TextView text_checkdate;
    private DatePicker datePicker;
    private AlertDialog alertDialog;
    private  AlertDialog personAlertDialog;
    private ExpandableListView listView_person;
    private TextView text_more;
    private List<String> treatmentList;
    private IllegalAdapter illegalAdapter;
    private  TextView text_person_sure;
    private  TextView text_person_cancel;
    private EditText edit_describe;

    private LinearLayout lin_DXSList;
    private LinearLayout lin_FWList;
    private MyListView list_DXS;
    private MyListView list_FW;

    private LinearLayout lin_topThree;
    private LinearLayout lin_option;
    private LinearLayout lin_spotphoto;
    private LinearLayout lin_rectifyphoto;
    private LinearLayout lin_creatword;

    private RelativeLayout rel_imgButton;

    private LinearLayout lin_location;
    private Button bt_mapChoose,bt_useCurLoc;



    private MyGridView grid_spotphoto;
    private MyGridView grid_rectifyphoto;

    private View view_takephoto;
    private Button button_crmera;
    private Button button_cancel;
    private Button button_photo;
    private static final int TAKE_PICTURE = 0;
    private static final int TAKE_RECTIFYPICTURE = 1;
    private static final int TAKE_FEEDPICTURE = 2;

    private static final int TAKE_VIDEO = 3;
    //    public static Bitmap bimap;
    private GridAdapter spotPhotoAdapter;
    private GridAdapter rectifyPhotoAdapter;
    private GridAdapter feedPhotoAdapter;

    private File parrentfile = null;
    private File recordVideoSaveFileDir = null;
    private boolean sdcardExists = false;

    private  ArrayList<ImageItem> spotPhotos;
    private  ArrayList<ImageItem> rectifyPhotos;

    private ArrayList<ImageItem> personSigns;
    private ArrayList<ImageItem> objectSigns;

    private String filePath = "";

    private List<BranchBean> personDialogBeans;
    private List<PersonBean> personBeans;

    private PersonDialogAdapter personDialogAdapter;
    private PersonAdapter personAdapter;

    private RadioGroup rg_checkterm;
    private RelativeLayout rel_checkterm;
    private List<View> expandLists;
    private List<CheckListExpandableAdapter> adapters;

    //    private LinearLayout lin_xqzgopinion;
//    private TextView text_xqzgdate;
    private LinearLayout lin_radioline;
    private List<TextView> radioLines;
    private List<RadioButton> radioButtons;
    private String deadline;//限期整改日期
    private String DXSList = "";
    private String FWList = "";
    private String previewDXSStr = "";
    private String previewFWStr = "";
    private String nowTime;
    private Intent intent;
    private ArrayList<String> bussinessType;
    private String treatmentRecord = "";

    private int dbId =  -1;

    private AlertDialog alertDialogNotify;
//    private TextView text_notifydate;

    private List<GeneralBean> generalBeans;
    private List<HouseSafeBean> houseBeans;

    private String code = "";
//    private String notifyDate = "";

    public static int selectItem = -1;
    private String isEnd = "false";

    private List<ImageItem> lastSpotPhotos;
    private List<ImageItem> lastRectifyPhotos;

    private List<ImageItem> failPhotos;

    private List<CheckHeadBean> headBeans;

    private InfoListAdapter infoListAdapter;

    private List<ImageFileBean> imageFileBeans;
    private List<ImageFileBean> failImageBeans;
    private LocationManager locationManager;


    private TextView text_loadinggps;
    private TextView text_choosedLoc;

    private Button bt_checkperson,bt_checkedobject;
//    private ImageView img_checkedobject;

    private MyListView list_video;
    private Button bt_video;
    private String videoPath = "";
    private List<VideoBean> videoBeans;
    private VideoListAdapter videoListAdapter;

    private LinearLayout lin_feedback,lin_feedphoto;
    private TextView text_feedback,text_feeddate;
    private EditText edit_feedback;
    private MyGridView grid_feedphoto;

    private  ArrayList<ImageItem> feedphotos;

    private LinearLayout lin_checkterm;

    private RelativeLayout rel_person,rel_checkdate;

    private LinearLayout lin_video;
    private LinearLayout lin_checkperson,lin_checkedobject,lin_describe;
    private TextView text_describeshow;
    private LinearLayout lin_feedupload;
    private ImageButton imgbt_feedupload;
    private EditText edit_feedman;

    private SaundProgressBar saundProgressBar;

    private AlertDialog alertDialogProgressBar;

    private TextView text_noIllegal,text_setninepic,text_setthreepic,text_setvideosize,text_feedsetnine;

    private TextView text_preview;

    private GridView grid_checkperson;
    private GridView grid_checkedObject;

    private RadioGroup rg_opinion;
    private Spinner sp_street;
    private String streetId,streetName = "";
    private LinearLayout lin_street;

    private  List<StreetBean> streetBeans;
    private String streetInfo;
    private ArrayAdapter<StreetBean> arrayAdapterStreet;

    private PersonSignAdapter personSignAdapter  ;

    private PersonSignAdapter objectSignAdapter;

    private TextView text_signperson,text_signobject;

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.checkrecord));
        setContentLayout(R.layout.commit_activity);
        setRightText("");
        setRightBackGround(R.mipmap.main_logo);
        ActivityManage.getInstance().addActivity(this);


        CountDownLatch countDownLatch = new CountDownLatch(5);


        gson = new Gson();
        intent = getIntent();
        action = intent.getAction();
        bussinessType = new ArrayList<>();
        if(action.equals("check")||action.equals("objectAddress")){
            bussinessType.clear();
            bussinessType.addAll(Contant.enforceList);
        }else{
            bussinessType.clear();
            String history = intent.getStringExtra("history");

            isEnd = intent.getStringExtra("isEnd");
            historyBean = gson.fromJson(history, new TypeToken<InspectHistoryBean>() {
            }.getType());
            String bussiness = historyBean.getBussinesstype();
            String[] buss = bussiness.split(",");
            if(buss!=null&&buss.length!=0){
                for(String str:buss){
                    bussinessType.add(str);
                }
            }else{
                bussinessType.add(bussiness);
            }
        }

//        text_noIllegal,text_setninepic,text_setthreepic,text_setvideosize

        text_noIllegal = (TextView) findViewById(R.id.text_commit_noillegal);
        text_setninepic = (TextView) findViewById(R.id.text_commit_setninepic);
        text_setthreepic = (TextView) findViewById(R.id.text_commit_setthreepic);
        text_setvideosize = (TextView) findViewById(R.id.text_commit_setvideosize);
        text_feedsetnine = (TextView) findViewById(R.id.text_commit_feedsetnine);
        text_preview = (TextView) findViewById(R.id.text_commit_preview);
        text_preview.setOnClickListener(this);

        lin_feedback = (LinearLayout) findViewById(R.id.lin_commit_feedback);
        lin_feedphoto = (LinearLayout) findViewById(R.id.lin_commit_feedphoto);
        text_feedback = (TextView) findViewById(R.id.text_commit_feedback);
        edit_feedback = (EditText) findViewById(R.id.edit_commit_feedback);
        text_feeddate = (TextView) findViewById(R.id.text_commit_feeddate);
        grid_feedphoto = (MyGridView) findViewById(R.id.grid_commit_feedphoto);

        grid_checkperson = (GridView) findViewById(R.id.grid_commit_checkPerson);
        grid_checkedObject = (GridView) findViewById(R.id.grid_commit_checkedobject);


        text_signobject = (TextView) findViewById(R.id.text_commit_signobject);
        text_signperson = (TextView) findViewById(R.id.text_commit_signperson);

        text_feeddate.setOnClickListener(this);

        lin_checkterm = (LinearLayout) findViewById(R.id.lin_commit_checkterm);
        rel_person = (RelativeLayout) findViewById(R.id.rel_commit_person);
        rel_checkdate = (RelativeLayout) findViewById(R.id.rel_commit_checkdate);

        lin_video = (LinearLayout) findViewById(R.id.lin_commit_video);
        lin_checkperson = (LinearLayout) findViewById(R.id.lin_commit_checkperson);
        lin_checkedobject = (LinearLayout) findViewById(R.id.lin_commit_checkedobject);
        lin_describe = (LinearLayout) findViewById(R.id.lin_commit_describe);
        edit_feedman = (EditText) findViewById(R.id.edit_commit_feedman);

        lin_feedupload = (LinearLayout) findViewById(R.id.lin_commit_feedupload);
        imgbt_feedupload = (ImageButton) findViewById(R.id.imgbt_commit_feedupload);
        imgbt_feedupload.setOnClickListener(this);

        text_describeshow = (TextView) findViewById(R.id.text_commit_describeshow);

        listInfo = (MyListView) findViewById(R.id.list_commit_info);
//        myExpandableListView = (MyExpandableListView) findViewById(R.id.expand_commit_term);
        rg_checkterm  = (RadioGroup) findViewById(R.id.rg_commit_checkterm);
        lin_radioline = (LinearLayout) findViewById(R.id.lin_commit_radioline);
        rel_checkterm = (RelativeLayout) findViewById(R.id.rel_commit_checkterm);
//        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_commit);
        text_loadinggps = (TextView) findViewById(R.id.text_loadinggps);
        text_choosedLoc = (TextView) findViewById(R.id.text_choosedloc);
//        RelativeLayout rel_allview = (RelativeLayout) findViewById(R.id.rel_allview);
//        rel_allview.setOnClickListener(this);
        list_illegal = (MyListView) findViewById(R.id.list_commit_illegal);
        lin_creatword = (LinearLayout) findViewById(R.id.lin_creatword);

        imgbt_createword = (ImageButton) findViewById(R.id.createword);
        imgbt_uploadtoweb = (ImageButton) findViewById(R.id.uploadtoweb);
        imgbt_endCheck = (ImageButton) findViewById(R.id.imgbt_commit_endcheck);

        edit_describe = (EditText) findViewById(R.id.edit_commit_describe);
        lin_DXSList = (LinearLayout) findViewById(R.id.lin_commit_dxslist);
        lin_FWList = (LinearLayout) findViewById(R.id.lin_commit_fwlist);
        lin_topThree = (LinearLayout) findViewById(R.id.lin_commit_topthree);
        list_DXS = (MyListView) findViewById(R.id.list_commit_dxs);
        list_FW = (MyListView) findViewById(R.id.list_commit_fw);
        lin_option = (LinearLayout) findViewById(R.id.lin_commit_opinion);
        rel_imgButton = (RelativeLayout) findViewById(R.id.rel_commit_imagebutton);

        lin_spotphoto = (LinearLayout) findViewById(R.id.lin_commit_spotphoto);
        lin_rectifyphoto = (LinearLayout) findViewById(R.id.lin_commit_rectifyphoto);
        grid_spotphoto = (MyGridView) findViewById(R.id.grid_commit_spotphoto);
        grid_rectifyphoto = (MyGridView) findViewById(R.id.grid_commit_rectifyphoto);
        view_takephoto = findViewById(R.id.include_commit_takephote);

        button_crmera = (Button) findViewById(R.id.input_popupwindows_camera);
        button_cancel = (Button) findViewById(R.id.input_popupwindows_cancel);
        button_photo = (Button) findViewById(R.id.input_popupwindows_Photo);

        button_crmera.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        button_photo.setOnClickListener(this);
        view_takephoto.setOnClickListener(this);

        lin_location = (LinearLayout) findViewById(R.id.lin_commit_location);
        bt_mapChoose = (Button) findViewById(R.id.bt_commit_mapchoose);
        bt_useCurLoc = (Button) findViewById(R.id.bt_commit_useCurLoc);
        bt_mapChoose.setOnClickListener(this);
        bt_useCurLoc.setOnClickListener(this);

//        cb_XCZG = (CheckBox) findViewById(R.id.cb_xczg);
//        cb_XQZG = (CheckBox) findViewById(R.id.cb_xqzg);
        text_insertperson = (TextView) findViewById(R.id.text_checkresult_insertperson);
        list_person = (ListView) findViewById(R.id.list_checkresult_person);
        text_checkdate = (TextView) findViewById(R.id.text_checkresult_date);

        rg_opinion = (RadioGroup) findViewById(R.id.rg_commit_opinion);

        sp_street = (Spinner) findViewById(R.id.sp_commit_street);
        lin_street = (LinearLayout) findViewById(R.id.lin_commit_street);


//        lin_xqzgopinion = (LinearLayout) findViewById(R.id.lin_commit_xqzgopinion);
//        text_xqzgdate = (TextView) findViewById(R.id.text_commit_xqzgdate);
//        text_xqzgdate.setOnClickListener(this);

        bt_checkperson = (Button) findViewById(R.id.bt_commit_checkperson);
        bt_checkedobject = (Button) findViewById(R.id.bt_commit_checkedobject);
//        img_checkperson = (ImageView) findViewById(R.id.img_commit_checkperson);
//        img_checkedobject = (ImageView) findViewById(R.id.img_commit_checkedobject);
//        img_checkperson.setOnClickListener(this);
//        img_checkedobject.setOnClickListener(this);
        bt_checkperson.setOnClickListener(this);
        bt_checkedobject.setOnClickListener(this);

        list_video = (MyListView) findViewById(R.id.list_commit_video);
        bt_video = (Button) findViewById(R.id.bt_commit_video);
        bt_video.setOnClickListener(this);


        if(bussinessType.size()==1){
            rg_checkterm.setVisibility(View.GONE);
        }else{
            rg_checkterm.setVisibility(View.VISIBLE);
        }
        expandLists = new ArrayList<>();
        radioButtons = new ArrayList<>();
        radioLines = new ArrayList<>();

        for(int i=0;i<bussinessType.size();i++){

            String enforce = bussinessType.get(i);
            //radioButton的动态添加
            RadioGroup.LayoutParams lineParams = new RadioGroup.LayoutParams(DensityUtil.dip2px(this, 1),RadioGroup.LayoutParams.MATCH_PARENT);
            TextView text_line = new TextView(this);
            text_line.setBackgroundColor(getResources().getColor(R.color.graymy));
            lineParams.setMargins(0, 5, 0, 5);
            RadioGroup.LayoutParams rb_layoutParams = new RadioGroup.LayoutParams(DensityUtil.getScreenWidth(CommitActivity.this)/bussinessType.size(), RadioGroup.LayoutParams.MATCH_PARENT);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(enforce);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setButtonDrawable(new ColorDrawable());
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setTextSize(20.0f);
            radioButton.setBackground(getResources().getDrawable(R.drawable.inputradiobutton_selector));
            rg_checkterm.addView(radioButton,rb_layoutParams);
            radioButtons.add(radioButton);
            if(i==0){
                rg_checkterm.check(radioButton.getId());
            }

            TextView textView = new TextView(this);
            if(i==0){
                textView.setBackgroundColor(getResources().getColor(R.color.titleback));
            }else{
                textView.setBackgroundColor(getResources().getColor(R.color.lightgray));
            }
            LinearLayout.LayoutParams radiolines = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(CommitActivity.this)/bussinessType.size(), DensityUtil.px2dip(this,2));
            radioLines.add(textView);
            lin_radioline.addView(textView,radiolines);


            MyExpandableListView myExpandableListView = new MyExpandableListView(this);
            rel_checkterm.addView(myExpandableListView);
            expandLists.add(myExpandableListView);
            if(i==0){
                myExpandableListView.setVisibility(View.VISIBLE);
            }else{
                myExpandableListView.setVisibility(View.GONE);
            }
        }

        text_checkdate.setOnClickListener(this);
        text_insertperson.setOnClickListener(this);
//
//        cb_XQZG.setOnCheckedChangeListener(this);
//        cb_XCZG.setOnCheckedChangeListener(this);



        imgbt_createword.setClickable(false);

        imgbt_createword.setOnClickListener(this);
        imgbt_uploadtoweb.setOnClickListener(this);
        imgbt_endCheck.setOnClickListener(this);

        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        nowTime = year + "-" + month + "-" + day;
        text_checkdate.setText(nowTime);
//        text_xqzgdate.setText(nowTime);
        text_feeddate.setText(nowTime);
        deadline = nowTime;

        rg_opinion.check(R.id.rb_commit_noproblem);
        treatmentList = new ArrayList<>();
        treatmentList.clear();
        treatmentList.add(CommitActivity.this.getResources().getString(R.string.noproblem));
        rg_opinion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_commit_noproblem:
                        treatmentList.clear();
                        treatmentList.add(CommitActivity.this.getResources().getString(R.string.noproblem));
                        break;
                    case R.id.rb_commit_zlgz:
                        treatmentList.clear();
                        treatmentList.add(CommitActivity.this.getResources().getString(R.string.zlgz));
                        break;
                    case R.id.rb_commit_jyxzcf:
                        treatmentList.clear();
                        treatmentList.add(CommitActivity.this.getResources().getString(R.string.jyxzcf));
                        break;
                    case R.id.rb_commit_ybxzcf:
                        treatmentList.clear();
                        treatmentList.add(CommitActivity.this.getResources().getString(R.string.ybxzcf));
                        break;
                }
            }
        });



        rg_checkterm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = ((RadioButton) rg_checkterm.findViewById(checkedId));
                int index = radioButtons.indexOf(radioButton);
                for (int i = 0; i < radioButtons.size(); i++) {
                    RadioButton button = radioButtons.get(i);
                    button.setTextColor(CommitActivity.this.getResources().getColor(R.color.black));
                    if (i == index) {
                        radioLines.get(i).setBackgroundColor(getResources().getColor(R.color.titleback));
                        expandLists.get(i).setVisibility(View.VISIBLE);
                    } else {
                        radioLines.get(i).setBackgroundColor(getResources().getColor(R.color.lightgray));
                        expandLists.get(i).setVisibility(View.GONE);
                    }
                }
                radioButton.setTextColor(CommitActivity.this.getResources().getColor(R.color.titleback));

            }
        });
    }

    @Override
    protected void initData() {
        streetBeans = new ArrayList<>();
        if("check".equals(action)){
            streetInfo = Contant.streetInfo;
        }else if("checkRecordLocal".equals(action)){
            streetInfo = historyBean.getStreetInfo();
            streetId = historyBean.getStreetId();
        }


        if(streetInfo!=null&&!"".equals(streetInfo)){
            List<StreetBean> streets = gson.fromJson(streetInfo, new TypeToken<List<StreetBean>>() {
            }.getType());
            if(streets!=null&&streets.size()>0){
                StreetBean streetBean = new StreetBean();
                streetBean.setSTREET_NAME("全部");
                streetBean.setSTREET_ID("");
                streetBeans.add(streetBean);
            }

            arrayAdapterStreet = new ArrayAdapter<StreetBean>(CommitActivity.this,android.R.layout.simple_spinner_item,streetBeans);
            arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_street.setAdapter(arrayAdapterStreet);
            streetBeans.addAll(streets);
            arrayAdapterStreet.notifyDataSetChanged();
        }else{
            initStreetData();
        }

        sp_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                streetId = streetBeans.get(position).getSTREET_ID();
                streetName = streetBeans.get(position).getSTREET_NAME();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        grid_checkperson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Contant.personBitmapList.size() > position) {
                    Intent imgIntent = new Intent(CommitActivity.this, SignShowActivity.class);
                    Contant.personBitmap = Contant.personBitmapList.get(position);
                    imgIntent.setAction("commit");
                    imgIntent.putExtra("index", 1);
                    startActivity(imgIntent);
                }
            }
        });

        grid_checkperson.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                if ("check".equals(action) || "checkRecordLocal".equals(action)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommitActivity.this);
                    builder.setTitle("删除提示");
                    builder.setMessage("确定要删除该签名吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Contant.personBitmapList.remove(position);
                            if (personSignAdapter == null) {
                                personSignAdapter = new PersonSignAdapter(CommitActivity.this, Contant.personBitmapList);
                                grid_checkperson.setAdapter(personSignAdapter);
                            } else {
                                personSignAdapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();
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
                return true;
            }
        });

        grid_checkedObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Contant.objectBitmapList.size() > position) {
                    Intent imgIntent = new Intent(CommitActivity.this, SignShowActivity.class);
                    Contant.objectBitmap = Contant.objectBitmapList.get(position);
                    imgIntent.setAction("commit");
                    imgIntent.putExtra("index", 2);
                    startActivity(imgIntent);
                }
            }
        });

        grid_checkedObject.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                if("check".equals(action)||"checkRecordLocal".equals(action)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommitActivity.this);
                    builder.setTitle("删除提示");
                    builder.setMessage("确定要删除该签名吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Contant.objectBitmapList.remove(position);
                            if (objectSignAdapter==null) {
                                objectSignAdapter = new PersonSignAdapter(CommitActivity.this,Contant.objectBitmapList);
                                grid_checkedObject.setAdapter(objectSignAdapter);
                            }else{
                                objectSignAdapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();
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
                return true;
            }
        });

        if(!"".equals(streetId)){
//            sp_street.setSelection(1,true);
            if(streetBeans!=null&&streetBeans.size()>0){
                for(int i=0;i<streetBeans.size();i++){
                    String id = streetBeans.get(i).getSTREET_ID();
                    if(id.equals(streetId)){
                        sp_street.setSelection(i,true);
                        break;
                    }
                }
            }
        }else{
            sp_street.setSelection(0,true);
        }





        if (finalDb == null) {
            finalDb = getFinalDb(this);
        }
        lastSpotPhotos = new ArrayList<>();
        lastRectifyPhotos = new ArrayList<>();
        imageFileBeans = new ArrayList<>();
        infoList = new ArrayList<>();
        videoBeans = new ArrayList<>();
        videoListAdapter = new VideoListAdapter(CommitActivity.this,videoBeans);
        list_video.setAdapter(videoListAdapter);

        list_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = null;
                if (videoBeans.get(position).isLocal()) {
                    uri = Uri.parse(videoBeans.get(position).getPath());
                } else {
                    uri = Uri.parse(videoBeans.get(position).getUrl());
                }
                intent.setDataAndType(uri, "video/mp4");
                startActivity(intent);

            }
        });

        list_video.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CommitActivity.this);
                builder.setTitle("删除确认");
                builder.setMessage("确定要删除该视频吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoBeans.remove(position);
                        videoListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

//        Bimp.isCarame = false;

        Res.init(this);

        filePath = Contant.filepath1+Contant.fileName+File.separator;

        if ((this.sdcardExists = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {
            parrentfile = new File(filePath);
            if (!this.parrentfile.exists()) {
                this.parrentfile.mkdirs(); // 创建文件夹
            }
            this.recordVideoSaveFileDir = new File(filePath); // 保存文件夹
            if (!this.recordVideoSaveFileDir.exists()) {
                this.recordVideoSaveFileDir.mkdirs(); // 创建文件夹
            }
        }
        spotPhotos = new ArrayList<>();
        rectifyPhotos = new ArrayList<>();
        feedphotos = new ArrayList<>();

        personSigns = new ArrayList<>();
        objectSigns = new ArrayList<>();




        grid_spotphoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                int count = spotPhotos.size();
                Bimp.tempSelectBitmap0.clear();
                Bimp.tempSelectBitmap0.addAll(spotPhotos);
                selectItem = 0;
                if (arg2 == count) {
                    show();
                } else {
                    GalleryActivity.imageItems = spotPhotos;
                    Bimp.isCarame = false;
                    Intent intent = new Intent(CommitActivity.this, GalleryActivity.class);
                    intent.putExtra("selectItem",selectItem);
                    intent.putExtra("action", action);
                    intent.putExtra("ID", arg2);
                    intent.putExtra("inspectGuid", checkGuid);
                    startActivity(intent);
                }
            }
        });


        grid_feedphoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                int count = feedphotos.size();
                Bimp.tempSelectBitmap0.clear();
                Bimp.tempSelectBitmap0.addAll(feedphotos);
                selectItem = 2;
                if (arg2 == count) {
                    show();
                } else {
                    GalleryActivity.imageItems = feedphotos;
                    Bimp.isCarame = false;
                    Intent intent = new Intent(CommitActivity.this, GalleryActivity.class);
                    intent.putExtra("selectItem",selectItem);
                    intent.putExtra("action",action);
                    intent.putExtra("ID", arg2);
                    intent.putExtra("inspectGuid",checkGuid);
                    startActivity(intent);
                }
            }
        });
        grid_rectifyphoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                int count = rectifyPhotos.size();
                Bimp.tempSelectBitmap0.clear();
                Bimp.tempSelectBitmap0.addAll(rectifyPhotos);
                selectItem = 1;
                if (arg2 == count) {
                    show();

                } else {
                    GalleryActivity.imageItems = rectifyPhotos;
                    Bimp.isCarame = false;
                    Intent intent = new Intent(CommitActivity.this, GalleryActivity.class);
                    intent.putExtra("selectItem",selectItem);
                    intent.putExtra("action",action);
                    intent.putExtra("ID", arg2);
                    intent.putExtra("inspectGuid",checkGuid);
                    startActivity(intent);
                }
            }
        });



        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Object result = msg.obj;
                if("".equals(result)){
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    ShowToast("连接服务器异常");
                }else if("[]".equals(result)){
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    if(msg.what==14){
                        lin_video.setVisibility(View.GONE);
                    }
                }else {
                    if (msg.what < 5) {
                        String[] keys = new String[]{};
                        String[] names = new String[]{};
                        switch (msg.what) {
                            case 1:
                                keys = getResources().getStringArray(R.array.primeinfokey);
                                names = getResources().getStringArray(R.array.primeinfoname);
                                break;
                            case 2:
                                keys = getResources().getStringArray(R.array.propertyinfokey);
                                names = getResources().getStringArray(R.array.propertyinfoname);
                                break;
                        }
                        List<InfoAttributeBean> attributeBeans = getInfoList((String) result, keys, names);
                        infoList.clear();
                        infoList.addAll(getHeadData(attributeBeans, headList));
                        if(infoListAdapter==null){
                            infoListAdapter = new InfoListAdapter(CommitActivity.this, infoList,bussinessType,historyBean.getName(),brokerId);
                            listInfo.setAdapter(infoListAdapter);
                        }else{
                            infoListAdapter.notifyDataSetChanged();
                        }

                        progressDialog.dismiss();
                    } else if (msg.what == 5) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String guid = "";

                        try {
                            jsonObject = new JSONObject((String) result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            guid = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ("1".equals(isSuccess)) {

                            if(msg.arg1!=3){
                                checkGuid = guid;
                            }
                            if(!action.equals("check")){
                                Contant.isChange = true;
                                if(finalDb==null){
                                    finalDb = getFinalDb(CommitActivity.this);
                                }
                                if(action.equals("checkRecordLocal")&&dbId!=-1){
                                    finalDb.deleteById(CheckDBBean.class,dbId);
                                }
                            }
                            if(msg.arg1==2){
//                                requestNotify(notifyDate);
                            }else{
                                ShowToast("上传成功");
                            }
                        } else  {
                            inspectBean = getData();
                            checkDBBean = getDBBean(inspectBean);
                            if (finalDb == null) {
                                finalDb = getFinalDb(CommitActivity.this);
                            }
                            finalDb.save(checkDBBean);
                            List<CheckDBBean> checkDBBeans = finalDb.findAll(CheckDBBean.class, "id DESC");
                            if(checkDBBeans!=null&&checkDBBeans.size()!=0){
                                dbId = checkDBBeans.get(0).getId();
                            }

                            saveSignDb();
                            if(finalDb==null){
                                finalDb = getFinalDb(CommitActivity.this);
                            }
                            finalDb.update(checkDBBean, "id = " + dbId);
                            ShowToast("上传服务器失败，已为您保存到本地，可在本地记录中查看");
                        }

                        List<ImageItem> loadSpot = new ArrayList<>();
                        List<ImageItem> loadRectify = new ArrayList<>();
                        if(lastSpotPhotos.size()!=0){
                            for(ImageItem imageItem:spotPhotos){
                                String path = imageItem.getImagePath();
                                boolean isHave = false;
                                for(ImageItem lastImage:lastSpotPhotos){
                                    if(path.equals(lastImage.getImagePath())){
                                        isHave = true;
                                        break;
                                    }
                                }
                                if(!isHave){
                                    loadSpot.add(imageItem);
                                }
                            }


                        }else{
                            loadSpot = spotPhotos;
                        }

                        if(loadSpot!=null&&loadSpot.size()!=0){
                            for(ImageItem imageItem:loadSpot){
                                imageItem.setType("IMG");
                            }
                        }

                        if(bussinessType.contains("房屋安全")){
                            if(lastRectifyPhotos.size()!=0){
                                for(ImageItem imageItem:rectifyPhotos){
                                    String path = imageItem.getImagePath();
                                    boolean isHave = false;
                                    for(ImageItem lastImage:lastRectifyPhotos){
                                        if(path.equals(lastImage.getImagePath())){
                                            isHave = true;
                                            break;
                                        }
                                    }
                                    if(!isHave){
                                        loadRectify.add(imageItem);
                                    }
                                }
                            }else{
                                loadRectify = rectifyPhotos;
                            }
                            if(loadRectify!=null&&loadRectify.size()!=0){
                                for(ImageItem imageItem:loadRectify){
                                    imageItem.setType("PIC");
                                }
                            }
                        }else{
                            loadRectify.clear();
                        }
//                        upLoadPic(isSuccess, loadSpot, loadRectify);
                        loadSpot.addAll(loadRectify);
                        uploadonePic(isSuccess, loadSpot);
                        if(alertDialogNotify!=null){
                            alertDialogNotify.dismiss();
                        }
                    } else if (msg.what == 6) {
                        try {
                            JSONObject jsonObject = new JSONObject((String) result);
                            String isSuccess = jsonObject.getString("result");
                            if (isSuccess.equals("0")) {
                                progressDialog.dismiss();
                                String failReason = jsonObject.getString("failReason");
                                ShowToast(failReason);
                            } else if (isSuccess.equals("1")) {
                                final String url = jsonObject.getString("succeedString");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String[] split = url.split("\\/");
                                        String filePath = Contant.filepathword + split[split.length - 1];

                                        boolean isLoad = RequestUtil.downLoadFile(filePath, url);
                                        Message msg = Message.obtain();
                                        msg.what = 7;
                                        msg.obj = isLoad;
                                        mHandler.sendMessage(msg);
                                    }
                                }).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (msg.what == 7) {
                        progressDialog.dismiss();
                        if ((boolean) result) {
                            ShowToast("word文件下载成功");
                            boolean isLaunch = Util.launchApp(CommitActivity.this,"");
                            if (!isLaunch) {
                                InstallPrint installPrint = new InstallPrint(CommitActivity.this);
                                installPrint.checkUpdate();
                            }
                        } else {
                            ShowToast("word下载失败，请检查网络连接");
                        }
                    } else if (msg.what == 8) {
                        progressDialog.dismiss();
                        if (personDialogBeans == null) {
                            personDialogBeans = new ArrayList<>();
                            personDialogBeans.addAll(getPersonBeans((String) result));
                            personDialogAdapter = new PersonDialogAdapter(CommitActivity.this, personDialogBeans,1);
                            listView_person.setAdapter(personDialogAdapter);
                        } else {
                            personDialogBeans.clear();
                            personDialogBeans.addAll(getPersonBeans((String) result));
                            personDialogAdapter.notifyDataSetChanged();
                        }
                    }else if(msg.what==9){


                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String succeedString = "";

                        try {
                            jsonObject = new JSONObject((String) result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            succeedString = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isSuccess.equals("1")) {
                            if(msg.arg2==1){
                                progressDialog.dismiss();
                                ShowToast("提交成功");
                                alertDialogNotify.dismiss();
                                if(dbId!=-1){
                                    finalDb.deleteById(CheckDBBean.class,dbId);
                                }
                                Intent intent = new Intent(CommitActivity.this,MainActivity.class);
                                CommitActivity.this.startActivity(intent);
                            }else{
                                if(msg.arg1==1){
                                    progressDialog.dismiss();
                                    ShowToast("图片上传成功");
                                    lastSpotPhotos.clear();
                                    lastSpotPhotos.addAll(spotPhotos);
                                    lastRectifyPhotos.clear();
                                    lastRectifyPhotos.addAll(rectifyPhotos);

                                    if(videoBeans!=null&&videoBeans.size()!=0){
                                        uploadVideo();
                                    }else{
                                        if(dbId!=-1){
                                            finalDb.deleteById(CheckDBBean.class,dbId);
                                        }

                                        Intent intent = new Intent(CommitActivity.this,MainActivity.class);
                                        CommitActivity.this.startActivity(intent);
                                        CommitActivity.this.finish();

                                    }
                                }
                            }
                        } else if (isSuccess.equals("0")) {
                            ShowToast(failReason);
                        }

                    }else if(msg.what==10){
                        String path = (String) msg.obj;
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(path);
                        if(selectItem==0){
                            spotPhotos.add(takePhoto);
                            spotPhotoAdapter.notifyDataSetChanged();
                        }else if(selectItem==1){
                            rectifyPhotos.add(takePhoto);
                            rectifyPhotoAdapter.notifyDataSetChanged();
                        }else if(selectItem==2){
                            feedphotos.add(takePhoto);
                            feedPhotoAdapter.notifyDataSetChanged();
                        }
                    }else if(msg.what==11){

                        String fileResult  = (String) msg.obj;
                        if(!"[]".equals(fileResult)){
                            imageFileBeans.addAll((List<ImageFileBean>)(gson.fromJson(fileResult,new TypeToken<List<ImageFileBean>>(){}.getType())));

                            if(imageFileBeans!=null&&imageFileBeans.size()!=0){
                                if (progressDialog == null) {
                                    progressDialog = showProgressDialog();
                                } else {
                                    if (!progressDialog.isShowing()) {
                                        progressDialog.show();
                                    }
                                }

                                for(ImageFileBean fileBean:imageFileBeans){
                                    fileBean.setIsFail(false);
                                }

                                for(int i=0;i<imageFileBeans.size();i=0){

                                    if("Video".equals(imageFileBeans.get(i).getType())){
                                        imageFileBeans.remove(i);
                                    }else{
                                        savePhoto(imageFileBeans.get(i));
                                        break;
                                    }
                                }

                            }
                        }
                    }else if(msg.what==12){
                        ImageFileBean imageFileBean = (ImageFileBean) msg.getData().getSerializable("imageFileBean");
//                        JSONObject jsonObject = null;
                        String isSuccess = (String) result;
//                        String failReason = "";
//                        String succeedString = "";

//                        try {
//                            jsonObject = new JSONObject((String) result);
//                            isSuccess = jsonObject.getString("result");
//                            failReason = jsonObject.getString("failReason");
//                            succeedString = jsonObject.getString("succeedString");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        if (isSuccess.equals("1")) {
//                            imageFileBeans.remove(imageFileBean);
//                            Bitmap bitmap = ImageUtil.getBitmap(succeedString);
//                            if(bitmap!=null) {
//                                ImageUtil.saveMyBitmap(imageFileBean.getName(),imageFileBean.getGuid(), CommitActivity.this, bitmap, checkGuid, imageFileBean.getType());
//                            }
                        } else if (isSuccess.equals("0")) {
                            if(failImageBeans==null){
                                failImageBeans = new ArrayList<>();
                            }
                            if(!imageFileBean.isFail()){
                                imageFileBean.setIsFail(true);
                                failImageBeans.add(imageFileBean);
                            }
                        }
                        imageFileBeans.remove(imageFileBean);
                        if(imageFileBeans.size()!=0){
                            loadpic();
                        }else{

                            getloadImage();
                        }

                    }else if(msg.what==13){
                        progressDialog.dismiss();
                        if("no".equals(result)){
                            ShowToast("视频上传失败");
                            alertDialogProgressBar.dismiss();
                        }else {
                            ShowToast("视频上传成功");
                            if(dbId!=-1){
                                finalDb.deleteById(CheckDBBean.class,dbId);
                            }
                            Intent intent = new Intent(CommitActivity.this,MainActivity.class);
                            CommitActivity.this.startActivity(intent);
                            CommitActivity.this.finish();
                        }
                    }else if(msg.what==14){
                        List<VideoBean> videos = gson.fromJson((String) result, new TypeToken<List<VideoBean>>() {
                        }.getType());

                        for(VideoBean videoBean:videos){
                            videoBean.setIsLocal(false);
                        }
                        videoBeans.addAll(videos);
                        list_video.setVisibility(View.VISIBLE);
                        videoListAdapter.notifyDataSetChanged();
                        if(videoBeans.size()==0){
                            lin_video.setVisibility(View.GONE);
                        }

                        progressDialog.dismiss();
                    }else if(msg.what==15){
                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String succeedString = "";

                        try {
                            jsonObject = new JSONObject((String) result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            succeedString = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        if("1".equals(isSuccess)){
                            ShowToast("上传成功");
                            if(feedphotos!=null&&feedphotos.size()!=0){
//                            upLoadFeedPic();
                                uploadonePic(isSuccess,feedphotos);
                            }
                        }else{
                            ShowToast(failReason);
                        }



                    }else if(msg.what==16){
                        int progress = Math.abs((Integer) msg.obj);
                        if(progress>100){
                            saundProgressBar.setProgress(100);
                            alertDialogProgressBar.dismiss();
                            ShowToast("视频上传成功");
                        }else{
                            saundProgressBar.setProgress(progress);
                        }
                    }else if(msg.what ==17){
                        String street = (String) msg.obj;
                        if(result!=null&&!result.equals("")){
                            if(streetBeans==null){
                                streetBeans = new ArrayList<>();
                            }else{
                                streetBeans.clear();
                            }
                            List<StreetBean> streets = gson.fromJson(street, new TypeToken<List<StreetBean>>() {
                            }.getType());

                            if(streets.size()>0){
                                StreetBean streetBean = new StreetBean();
                                streetBean.setSTREET_NAME("全部");
                                streetBean.setSTREET_ID("");
                                streetBeans.add(streetBean);
                            }
//                            if("房管局".equals(Contant.USERBEAN.getBranchName())){
//                                streets.addAll(getManageStreet("第一房管所"));
//                            }else {
//                                streets.addAll(getManageStreet(Contant.USERBEAN.getBranchName()));
//                            }
                            streetBeans.addAll(streets);
//
//                            isFirstStreet = true;
                            if(arrayAdapterStreet!=null){
                                arrayAdapterStreet.notifyDataSetChanged();
                            }else{
                                arrayAdapterStreet = new ArrayAdapter<StreetBean>(CommitActivity.this,android.R.layout.simple_spinner_item,streetBeans);
                                arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp_street.setAdapter(arrayAdapterStreet);
                            }
//                            Contant.streetBeans.clear();
//                            Contant.streetBeans.addAll(streets);
                            Contant.streetInfo = street;

                        }else{
                            ShowToast("服务器异常");

                        }
                        if(progressDialog!=null&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }
            }
        };


        personBeans = new ArrayList<>();
        if(isEnd.equals("true")){
            personAdapter = new PersonAdapter(this,personBeans,false);
        }else if(isEnd.equals("false")){
            personAdapter = new PersonAdapter(this,personBeans,true);
        }

        list_person.setAdapter(personAdapter);
        generalBeans = new ArrayList<>();
        houseBeans = new ArrayList<>();



        checkTermGroupBeansPrime = new ArrayList<>();
        checkTermGroupBeansPro = new ArrayList<>();
        checkTermGroupBeansGen = new ArrayList<>();
        checkTermGroupBeansHouse = new ArrayList<>();
        adapters = new ArrayList<>();
        illegalTermBeans = new ArrayList<>();
        illegalAdapter = new IllegalAdapter(this,illegalTermBeans,1);
        list_illegal.setAdapter(illegalAdapter);
//        flashIllegal(illegalTermBeans);
        if(historyBean==null){
            historyBean = new InspectHistoryBean();
        }
        if(action.equals("check")){
            spotPhotoAdapter = new GridAdapter(this, spotPhotos,9,true);
            grid_spotphoto.setAdapter(spotPhotoAdapter);
            rectifyPhotoAdapter = new GridAdapter(this, rectifyPhotos,3,true);
            grid_rectifyphoto.setAdapter(rectifyPhotoAdapter);
            feedPhotoAdapter = new GridAdapter(this,feedphotos,9,true);
            grid_feedphoto.setAdapter(feedPhotoAdapter);
//            String checkTerm = intent.getStringExtra("checkTerm");
//            Log.i("TAG","checkTerm"+checkTerm);
            list_video.setVisibility(View.GONE);
            setDXAANDFW();
            lin_location.setVisibility(View.VISIBLE);
            if(bussinessType.size()==1&&bussinessType.contains(getResources().getString(R.string.primebroker))){

                historyBean.setCheckBaseInfoBean(Contant.checkBaseInfoBean);
                historyBean.setName(Contant.primeObjectList.get(0).getName());
                historyBean.setAddress(Contant.primeObjectList.get(0).getAddress());
                historyBean.setId(Contant.primeObjectList.get(0).getPKID());
                previewDXSStr= "";
                previewFWStr= "";

            }else{

                illegalAdapter.notifyDataSetChanged();
                historyBean.setCheckBaseInfoBean(Contant.checkBaseInfoBean);
                historyBean.setName(Contant.propertyObjectList.get(0).getName());
                historyBean.setAddress(Contant.propertyObjectList.get(0).getAddress());
                historyBean.setId(Contant.propertyObjectList.get(0).getProId());

                PropertyBean propertyBean = Contant.propertyObjectList.get(0);
                if(propertyBean.getGeneralBeans()==null||propertyBean.getGeneralBeans().size()==0){
                    previewDXSStr = "该项目下的所有地下室";
                }else{
                    previewDXSStr = propertyBean.getGeneralBeans().size()+"处";
                    generalBeans = propertyBean.getGeneralBeans();
                }

                StringBuilder stringBuilder = new StringBuilder();
                if(propertyBean.getHouseBeans()!=null&&propertyBean.getHouseBeans().size()!=0){
                    houseBeans = propertyBean.getHouseBeans();
                    for(int i=0;i<propertyBean.getHouseBeans().size();i++){
                        HouseSafeBean houseBean = propertyBean.getHouseBeans().get(i);
                        if(i==0){
                            stringBuilder.append(houseBean.getBUILD_SITE());
                        }else{
                            stringBuilder.append("、"+houseBean.getBUILD_SITE());
                        }
                    }
                    previewFWStr = stringBuilder.toString();
                }else{
                    previewFWStr = "该项目下的所有楼栋";
                }
            }
            //初始化检查项
            for(int i=0;i<bussinessType.size();i++){
                String type = bussinessType.get(i);
                if(type.equals(getResources().getString(R.string.primebroker))&&Contant.checkedTerms.containsKey("prime")){
                    checkTermGroupBeansPrime.addAll(Contant.checkedTerms.get("prime"));
                    checkListExpandableAdapterPrime = new CheckListExpandableAdapter(checkTermGroupBeansPrime,this,1,illegalAdapter, illegalTermBeans);
                    adapters.add(checkListExpandableAdapterPrime);
                }else if(type.equals(getResources().getString(R.string.propertymanage))&&Contant.checkedTerms.containsKey("property")){
                    checkTermGroupBeansPro.addAll(Contant.checkedTerms.get("property"));
                    checkListExpandableAdapterPro = new CheckListExpandableAdapter(checkTermGroupBeansPro,this,1,illegalAdapter, illegalTermBeans);
                    adapters.add(checkListExpandableAdapterPro);
                }else if(type.equals(getResources().getString(R.string.generalbasement))&&Contant.checkedTerms.containsKey("general")){
                    checkTermGroupBeansGen.addAll(Contant.checkedTerms.get("general"));
                    checkListExpandableAdapterGen = new CheckListExpandableAdapter(checkTermGroupBeansGen,this,1,illegalAdapter, illegalTermBeans);
                    adapters.add(checkListExpandableAdapterGen);

                }else if(type.equals(getResources().getString(R.string.housesafeuse))&&Contant.checkedTerms.containsKey("houseSafe")){
                    checkTermGroupBeansHouse.addAll(Contant.checkedTerms.get("houseSafe"));
                    checkListExpandableAdapterHouse = new CheckListExpandableAdapter(checkTermGroupBeansHouse,this,1,illegalAdapter, illegalTermBeans);
                    adapters.add(checkListExpandableAdapterHouse);
                }
            }
            if(Bimp.selectBitmaps.containsKey("spotphoto")){
                spotPhotos.addAll(Bimp.selectBitmaps.get("spotphoto"));
                spotPhotoAdapter.notifyDataSetChanged();
            }
            if(Bimp.selectBitmaps.containsKey("rectifyphoto")){
                rectifyPhotos.addAll(Bimp.selectBitmaps.get("rectifyphoto"));
                rectifyPhotoAdapter.notifyDataSetChanged();
            }
            if(Bimp.selectBitmaps.containsKey("feedphoto")){
                feedphotos.addAll(Bimp.selectBitmaps.get("feedphoto"));
                feedPhotoAdapter.notifyDataSetChanged();
            }



        } else if(!action.equals("objectAddress")){

            if(action.equals("checkRecordLocal")) {
                lin_location.setVisibility(View.VISIBLE);
                dbId = historyBean.getDbID();
                spotPhotoAdapter = new GridAdapter(this, spotPhotos,9,true);
                rectifyPhotoAdapter = new GridAdapter(this, rectifyPhotos,3,true);
            }else{
                lin_location.setVisibility(View.GONE);
                spotPhotoAdapter = new GridAdapter(this, spotPhotos,9,false);
                rectifyPhotoAdapter = new GridAdapter(this, rectifyPhotos,3,false);
                if(!bussinessType.contains("其他")){
                    text_preview.setVisibility(View.VISIBLE);
                }else{
                    text_preview.setVisibility(View.GONE);
                }
            }

            grid_spotphoto.setAdapter(spotPhotoAdapter);
            grid_rectifyphoto.setAdapter(rectifyPhotoAdapter);
            if("feed".equals(action)){
                feedPhotoAdapter = new GridAdapter(this,feedphotos,9,true);
                grid_feedphoto.setAdapter(feedPhotoAdapter);
            }else{
                feedPhotoAdapter = new GridAdapter(this,feedphotos,9,false);
                grid_feedphoto.setAdapter(feedPhotoAdapter);
            }

            historyBean.getInspectItemsResultBean().getHead();
            Contant.isChange = false;
            String checklist = intent.getStringExtra("checklist");
            String headlist = intent.getStringExtra("headlist");
            checkGuid = intent.getStringExtra("inspectGuid");

            headBeans = new ArrayList<>();
            List<CheckHeadBean> checkHeadBeans = gson.fromJson(headlist, new TypeToken<List<CheckHeadBean>>() {}.getType());
            if(checkHeadBeans!=null){
                headBeans.addAll(checkHeadBeans);
            }
            headList = getTableHead(headBeans);

            for(int i=0;i<bussinessType.size();i++) {
                String type = bussinessType.get(i);
                if (type.equals(getResources().getString(R.string.primebroker))) {
                    checkTermGroupBeansPrime.addAll(getTermGroups(checklist,getResources().getString(R.string.primebroker)));
                    if(bussinessType.contains(getResources().getString(R.string.other))||action.equals("checkRecordLocal")){
                        checkListExpandableAdapterPrime = new CheckListExpandableAdapter(checkTermGroupBeansPrime, this, 1, illegalAdapter, illegalTermBeans);
                    }else {
                        checkListExpandableAdapterPrime = new CheckListExpandableAdapter(checkTermGroupBeansPrime, this, 0, illegalAdapter, illegalTermBeans);
                    }
                    adapters.add(checkListExpandableAdapterPrime);
                } else if (type.equals(getResources().getString(R.string.propertymanage))) {
                    checkTermGroupBeansPro.addAll(getTermGroups(checklist,getResources().getString(R.string.propertymanage)));
                    if(bussinessType.contains(getResources().getString(R.string.other))||action.equals("checkRecordLocal")){
                        checkListExpandableAdapterPro = new CheckListExpandableAdapter(checkTermGroupBeansPro, this, 1, illegalAdapter, illegalTermBeans);
                    }else {
                        checkListExpandableAdapterPro = new CheckListExpandableAdapter(checkTermGroupBeansPro, this, 0, illegalAdapter, illegalTermBeans);
                    }
                    adapters.add(checkListExpandableAdapterPro);
                } else if (type.equals(getResources().getString(R.string.generalbasement))) {
                    checkTermGroupBeansGen.addAll(getTermGroups(checklist,getResources().getString(R.string.generalbasement)));
                    if(bussinessType.contains(getResources().getString(R.string.other))||action.equals("checkRecordLocal")){
                        checkListExpandableAdapterGen = new CheckListExpandableAdapter(checkTermGroupBeansGen, this, 1, illegalAdapter, illegalTermBeans);
                    }else {
                        checkListExpandableAdapterGen = new CheckListExpandableAdapter(checkTermGroupBeansGen, this, 0, illegalAdapter, illegalTermBeans);
                    }
                    adapters.add(checkListExpandableAdapterGen);
                } else if (type.equals(getResources().getString(R.string.housesafeuse))) {
                    checkTermGroupBeansHouse.addAll(getTermGroups(checklist,getResources().getString(R.string.housesafeuse)));
                    if(bussinessType.contains(getResources().getString(R.string.other))||action.equals("checkRecordLocal")){
                        checkListExpandableAdapterHouse = new CheckListExpandableAdapter(checkTermGroupBeansHouse, this, 1, illegalAdapter, illegalTermBeans);
                    }else {
                        checkListExpandableAdapterHouse = new CheckListExpandableAdapter(checkTermGroupBeansHouse, this, 0, illegalAdapter, illegalTermBeans);
                    }
                    adapters.add(checkListExpandableAdapterHouse);
                }
            }



            List<PersonBean> beans = historyBean.getCheckBaseInfoBean().getPersonBeans();
            personBeans.addAll(beans);
            personAdapter.notifyDataSetChanged();

            DXSList = historyBean.getDXSList();
            FWList = historyBean.getFWList();
            String describe = historyBean.getCheckBaseInfoBean().getDescription();
            edit_describe.setText(describe);

            String[] genStr = DXSList.split(";");
            if(genStr.length!=0){
                for(int i=0;i<genStr.length;i++){
                    GeneralBean generalBean = new GeneralBean();
                    String objStr = genStr[i];
                    String[] attrs = objStr.split(",");
                    if(attrs.length==2){
                        generalBean.setBase_id(attrs[0]);
                        generalBean.setHouseLocate(attrs[1]);
                        generalBeans.add(generalBean);
                    }
                }
                previewDXSStr = generalBeans.size()+"处";
            }else{
                previewDXSStr = "该项目下的所有地下室";
            }

            String[] houseStr = FWList.split(";");

            if(houseStr.length!=0){
                StringBuilder sbb = new StringBuilder();
                for(int i=0;i<houseStr.length;i++){
                    HouseSafeBean houseBean = new HouseSafeBean();
                    String objStr = houseStr[i];
                    String[] attrs = objStr.split(",");
                    if(attrs.length==2){
                        houseBean.setBUILD_NO(attrs[0]);
                        houseBean.setBUILD_SITE(attrs[1]);
                        if(i==0){
                            sbb.append(attrs[1]);
                        }else{
                            sbb.append("、"+attrs[1]);
                        }
                        houseBeans.add(houseBean);
                    }
                }
                previewFWStr = sbb.toString();
            }else{
                previewFWStr = "该项目下的所有楼栋";
            }


            treatmentRecord = historyBean.getCheckBaseInfoBean().getTreatment();
            Contant.isXCZG = false;
            Contant.isXQZG = false;

//            if(treatmentRecord.contains(getResources().getString(R.string.xqzg))){
//                Contant.isXQZG = true;
//                cb_XQZG.setChecked(true);
//                lin_xqzgopinion.setVisibility(View.VISIBLE);
//                String xqDate = historyBean.getRectifDeadline();
//                String xqDate1 = xqDate.replace("年", "-");
//                String xqDate2 =xqDate1.replace("月","-");
//                String xqDate3 =xqDate2.replace("日","");
//                deadline = xqDate3;
//                text_xqzgdate.setText(deadline);
//            }
            String inspectTime = historyBean.getCheckBaseInfoBean().getInspectTime();
            String inspectTime1 = inspectTime.replace("年", "-");
            String inspectTime2 =inspectTime1.replace("月","-");
            String inspectTime3 =inspectTime2.replace("日", "");
            text_checkdate.setText(inspectTime3);
        }else{

            spotPhotoAdapter = new GridAdapter(this, spotPhotos,9,true);
            grid_spotphoto.setAdapter(spotPhotoAdapter);
            rectifyPhotoAdapter = new GridAdapter(this, rectifyPhotos,3,false);
            grid_rectifyphoto.setAdapter(rectifyPhotoAdapter);
            feedPhotoAdapter = new GridAdapter(this,feedphotos,9,true);
            grid_feedphoto.setAdapter(feedPhotoAdapter);
        }

        if(bussinessType.contains(getResources().getString(R.string.housesafeuse))){
            lin_rectifyphoto.setVisibility(View.VISIBLE);
        }else{
            lin_rectifyphoto.setVisibility(View.GONE);
        }

        if(action.equals("objectAddress")){
            lin_street.setVisibility(View.GONE);
            lin_location.setVisibility(View.VISIBLE);
            lin_topThree.setVisibility(View.GONE);
            lin_option.setVisibility(View.GONE);
            lin_creatword.setVisibility(View.GONE);
            String objectAddress = intent.getStringExtra("objectAddress");
            historyBean.setCheckBaseInfoBean(Contant.checkBaseInfoBean);
            historyBean.setName("");
            historyBean.setAddress(objectAddress);
            historyBean.setId("");
            if(Bimp.selectBitmaps.containsKey("spotphoto")){
                spotPhotos.addAll(Bimp.selectBitmaps.get("spotphoto"));
                spotPhotoAdapter.notifyDataSetChanged();
            }
            if(Bimp.selectBitmaps.containsKey("rectifyphoto")){
                rectifyPhotos.addAll(Bimp.selectBitmaps.get("rectifyphoto"));
                rectifyPhotoAdapter.notifyDataSetChanged();
            }
            if(Bimp.selectBitmaps.containsKey("feedphoto")){
                feedphotos.addAll(Bimp.selectBitmaps.get("feedphoto"));
                feedPhotoAdapter.notifyDataSetChanged();
            }


        }else {
            brokerId = historyBean.getId();
            if (DXSList == null || "".equals(DXSList) || generalBeans.size() == 0) {
                lin_DXSList.setVisibility(View.GONE);
            } else {
                lin_DXSList.setVisibility(View.VISIBLE);
                GeneralListAdapter generalListAdapter = new GeneralListAdapter(this, generalBeans, 2);
                list_DXS.setAdapter(generalListAdapter);
            }

            if (FWList == null || "".equals(FWList) || houseBeans.size() == 0) {
                lin_FWList.setVisibility(View.GONE);
            } else {
                lin_FWList.setVisibility(View.VISIBLE);
                HouseSafeListAdapter safeListAdapter = new HouseSafeListAdapter(this, houseBeans, 2);
                list_FW.setAdapter(safeListAdapter);
            }
            if("check".equals(action)){
                for (int i = 0; i < bussinessType.size(); i++) {
                    String type = bussinessType.get(i);
                    MyExpandableListView myExpandableListView = (MyExpandableListView) (expandLists.get(i));
                    if (type.equals(getResources().getString(R.string.primebroker))) {
                        myExpandableListView.setAdapter(checkListExpandableAdapterPrime);
                        myExpandableListView.expandGroup(0);
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansPrime));
                    } else if (type.equals(getResources().getString(R.string.propertymanage))) {
                        myExpandableListView.setAdapter(checkListExpandableAdapterPro);
                        myExpandableListView.expandGroup(0);
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansPro));
                    } else if (type.equals(getResources().getString(R.string.generalbasement))) {
                        myExpandableListView.setAdapter(checkListExpandableAdapterGen);
                        myExpandableListView.expandGroup(0);
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansGen));
                    } else if (type.equals(getResources().getString(R.string.housesafeuse))) {
                        myExpandableListView.setAdapter(checkListExpandableAdapterHouse);
                        myExpandableListView.expandGroup(0);
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansHouse));
                    }
                }
            }else{
                for (int i = 0; i < bussinessType.size(); i++) {
                    String type = bussinessType.get(i);
                    MyExpandableListView myExpandableListView = (MyExpandableListView) (expandLists.get(i));
                    if (type.equals(getResources().getString(R.string.primebroker))) {
                        if(checkTermGroupBeansPrime!=null){
                            if(!"checkRecordLocal".equals(action)){
                                List<CheckTermGroupBean> termGroupBeans = getChoosedTermGroup(checkTermGroupBeansPrime);
                                checkTermGroupBeansPrime.clear();
                                checkTermGroupBeansPrime.addAll(termGroupBeans);
                            }
                            myExpandableListView.setAdapter(checkListExpandableAdapterPrime);
                            if(checkTermGroupBeansPrime.size()>0) {
                                myExpandableListView.expandGroup(0);
                            }
                        }
                        checkListExpandableAdapterPrime.notifyDataSetChanged();
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansPrime));
                    } else if (type.equals(getResources().getString(R.string.propertymanage))) {
                        if(checkTermGroupBeansPro!=null){
                            if(!"checkRecordLocal".equals(action)) {
                                List<CheckTermGroupBean> termGroupBeans = getChoosedTermGroup(checkTermGroupBeansPro);
                                checkTermGroupBeansPro.clear();
                                checkTermGroupBeansPro.addAll(termGroupBeans);
                            }
                            myExpandableListView.setAdapter(checkListExpandableAdapterPro);
                            if(checkTermGroupBeansPro.size()>0) {
                                myExpandableListView.expandGroup(0);
                            }
                        }
                        checkListExpandableAdapterPro.notifyDataSetChanged();
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansPro));
                    } else if (type.equals(getResources().getString(R.string.generalbasement))) {
                        if(checkTermGroupBeansGen!=null){
                            if(!"checkRecordLocal".equals(action)) {
                                List<CheckTermGroupBean> termGroupBeans = getChoosedTermGroup(checkTermGroupBeansGen);
                                checkTermGroupBeansGen.clear();
                                checkTermGroupBeansGen.addAll(termGroupBeans);
                            }
                            myExpandableListView.setAdapter(checkListExpandableAdapterGen);
                            if(checkTermGroupBeansGen.size()>0){
                                myExpandableListView.expandGroup(0);
                            }
                        }
                        checkListExpandableAdapterGen.notifyDataSetChanged();
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansGen));
                    } else if (type.equals(getResources().getString(R.string.housesafeuse))) {
                        if(checkTermGroupBeansHouse!=null){
                            if(!"checkRecordLocal".equals(action)) {
                                List<CheckTermGroupBean> termGroupBeans = getChoosedTermGroup(checkTermGroupBeansHouse);
                                checkTermGroupBeansHouse.clear();
                                checkTermGroupBeansHouse.addAll(termGroupBeans);
                            }
                            myExpandableListView.setAdapter(checkListExpandableAdapterHouse);
                            if(checkTermGroupBeansHouse.size()>0) {
                                myExpandableListView.expandGroup(0);
                            }
                        }
                        checkListExpandableAdapterHouse.notifyDataSetChanged();
                        illegalTermBeans.addAll(getIllegalTerm(checkTermGroupBeansHouse));
                    }
                }
            }

            illegalAdapter.notifyDataSetChanged();

            if (!action.equals("check") ) {
//                Contant.isXCZG = true;
//                cb_XCZG.setChecked(true);
                if(treatmentRecord.contains(getResources().getString(R.string.zlgz))){
                    rg_opinion.check(R.id.rb_commit_zlgz);
                }else if(treatmentRecord.contains(getResources().getString(R.string.jyxzcf))){
                    rg_opinion.check(R.id.rb_commit_jyxzcf);
                }else if(treatmentRecord.contains(getResources().getString(R.string.ybxzcf))){
                    rg_opinion.check(R.id.rb_commit_ybxzcf);
                }else{
                    rg_opinion.check(R.id.rb_commit_noproblem);
                }
            }

            //区分开是否是反馈界面
            if("feed".equals(action)){
                lin_checkterm.setVisibility(View.GONE);
                lin_feedback.setVisibility(View.VISIBLE);
                lin_feedphoto.setVisibility(View.VISIBLE);
                lin_option.setVisibility(View.GONE);
                rel_checkdate.setVisibility(View.GONE);
                rel_person.setVisibility(View.GONE);
                lin_spotphoto.setVisibility(View.GONE);
                lin_rectifyphoto.setVisibility(View.GONE);
                lin_video.setVisibility(View.GONE);
                rel_imgButton.setVisibility(View.GONE);
                lin_checkperson.setVisibility(View.GONE);
                lin_checkedobject.setVisibility(View.GONE);
                String describe = historyBean.getCheckBaseInfoBean().getDescription();
                if(describe!=null&&!"".equals(describe)){
                    text_describeshow.setVisibility(View.VISIBLE);
                    edit_describe.setVisibility(View.GONE);
                    text_describeshow.setText(describe);
                }else{
                    lin_describe.setVisibility(View.GONE);
                }
                lin_feedupload.setVisibility(View.VISIBLE);
            }else{
                lin_checkterm.setVisibility(View.VISIBLE);
                lin_feedphoto.setVisibility(View.GONE);
                lin_feedback.setVisibility(View.GONE);
                lin_feedupload.setVisibility(View.GONE);
            }

            if (bussinessType.contains(getResources().getString(R.string.other))) {
                lin_topThree.setVisibility(View.GONE);
                lin_option.setVisibility(View.GONE);
                lin_creatword.setVisibility(View.GONE);
                if(isEnd.equals("true")){
                    rel_imgButton.setVisibility(View.GONE);
                    edit_describe.setEnabled(false);
                    text_insertperson.setEnabled(false);
                    text_checkdate.setClickable(false);
                    list_person.setEnabled(false);
//                    grid_spotphoto.setEnabled(false);
                }

                if(!"feed".equals(action)){
                    initRecordPhoto();
                }
                if(!"checkRecordLocal".equals(action)){
                    initNoEditView();
                }
            } else {

                if(Contant.infoAttributeBeans.size()!=0){
                    infoList.clear();
                    infoList.addAll(Contant.infoAttributeBeans);
                    if (infoListAdapter == null) {
                        infoListAdapter = new InfoListAdapter(CommitActivity.this, infoList,bussinessType,historyBean.getName(),brokerId);
                        listInfo.setAdapter(infoListAdapter);
                    } else {
                        infoListAdapter.notifyDataSetChanged();
                    }
                    if(bussinessType.contains(getResources().getString(R.string.primebroker))){
                        headList = Contant.primeHeadList;
                    }else{
                        headList = Contant.propertyHeadList;
                    }
                }else {
                    if ("check".equals(action)) {
                        if(bussinessType.contains(getResources().getString(R.string.primebroker))){
                            headList = Contant.primeHeadList;
                        }else{
                            headList = Contant.propertyHeadList;
                        }

                        if ("其他".equals(historyBean.getName())) {
                            infoList.clear();
                            infoList.addAll(getInfo(Contant.propertyHeadList));
                            if (infoListAdapter == null) {
                                infoListAdapter = new InfoListAdapter(CommitActivity.this, infoList,bussinessType,historyBean.getName(),brokerId);
                                listInfo.setAdapter(infoListAdapter);
                            } else {
                                infoListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (NetUtil.isNetworkAvailable(this)) {
                                if (progressDialog == null) {
                                    progressDialog = showProgressDialog();
                                } else {
                                    progressDialog.show();
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String result = "";
                                        Message msg = Message.obtain();
                                        if (bussinessType.size() == 1 && bussinessType.contains(getResources().getString(R.string.primebroker))) {
                                            if (action.equals("check")) {
                                                headList = Contant.primeHeadList;
                                            }
                                            Map<String, String> params = new HashMap<>();
                                            params.put("idlist", brokerId);
                                            result = RequestUtil.post(RequestUtil.GetJJJGDetail, params);
                                            msg.what = 1;
                                            //经纪机构
                                        } else {
                                            if (action.equals("check")) {
                                                headList = Contant.propertyHeadList;
                                            }
                                            Map<String, String> params = new HashMap<>();
                                            params.put("proID", brokerId);
                                            params.put("DXSIDList", "");
                                            params.put("FWList", "");
                                            result = RequestUtil.post(RequestUtil.GetProDXSFWHeadInfo, params);
                                            msg.what = 2;
                                            //物业管理
                                        }
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                }).start();
                            } else {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                ShowToast(noNetText);
                            }
                        }
                    } else {
                        String[] keys = null;
                        String[] names = null;
                        if (bussinessType.size() == 1 && bussinessType.contains(getResources().getString(R.string.primebroker))) {
                            keys = getResources().getStringArray(R.array.primeinfokey);
                            names = getResources().getStringArray(R.array.primeinfoname);
                        } else {
                            keys = getResources().getStringArray(R.array.propertyinfokey);
                            names = getResources().getStringArray(R.array.propertyinfoname);

                        }
                        infoList.clear();
                        infoList.addAll(getInfoList(headBeans, keys, names));
                        if (infoListAdapter == null) {
                            infoListAdapter = new InfoListAdapter(CommitActivity.this, infoList,bussinessType,historyBean.getName(),brokerId);
                            listInfo.setAdapter(infoListAdapter);
                        } else {
                            infoListAdapter.notifyDataSetChanged();
                        }
                        if(!"feed".equals(action)){
                            initRecordPhoto();
                        }
                        if(!"checkRecordLocal".equals(action)){
                            initNoEditView();
                        }
                    }
                }
            }
        }
    }



    private void initStreetData(){


        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("name", Contant.userid);
                    String result = RequestUtil.post(RequestUtil.GetStreetList, params);
                    Message msg = Message.obtain();
                    msg.what =17;
//                    msg.arg2=index;
                    msg.obj = result;
                    mHandler.sendMessage(msg);

                }
            }).start();

        }else{
            ShowToast(noNetText);
        }

    }


    private List<CheckTermGroupBean> getChoosedTermGroup(List<CheckTermGroupBean> checkTermGroupBeans){
        List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
        if(checkTermGroupBeans!=null&&checkTermGroupBeans.size()>0){
            for(CheckTermGroupBean checkTermGroupBean:checkTermGroupBeans){
                List<CheckTermBean> checkTermBeans = checkTermGroupBean.getCheckTermBeans();
                List<CheckTermBean> termBeans = new ArrayList<>();
                if(checkTermBeans!=null&&checkTermBeans.size()>0){
                    for(CheckTermBean termBean:checkTermBeans){
                        if(termBean.getValue()!=null&&!"".equals(termBean.getValue())){
                            termBeans.add(termBean);
                        }
                    }
                }
                if(termBeans.size()!=0){
                    checkTermGroupBean.setCheckTermBeans(termBeans);
                    termGroupBeans.add(checkTermGroupBean);
                }else{
                    termBeans = null;
                }
            }
        }
        return termGroupBeans;
    }


    /**
     * 请求视频列表
     */
    private void requestVideoList(){
        if(NetUtil.isNetworkAvailable(this)){

            if(progressDialog ==null){
                progressDialog = showProgressDialog();
                progressDialog.setTitle("加载视频...");
                progressDialog.setMessage("正在加载视频");
                progressDialog.setCanceledOnTouchOutside(false);
            }else{
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                progressDialog.setTitle("加载视频...");
                progressDialog.setMessage("正在加载视频");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("guid",checkGuid);
                    String videoResult = RequestUtil.post(RequestUtil.GetVideoList,params);
                    Message msg = Message.obtain();
                    msg.what = 14;
                    msg.obj = videoResult;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }
    }

    /**
     * 加载已经下载好的图片，并且适配到控件上
     */
    private void loadpic(){
        if(imageFileBeans.size()!=0){
            if("Video".equals(imageFileBeans.get(0).getType())){
                imageFileBeans.remove(0);
                loadpic();
            }else{
                savePhoto(imageFileBeans.get(0));
            }
        }else {

            getloadImage();
        }


    }


    private void getloadImage(){
        if(failImageBeans==null||failImageBeans.size()==0) {
            spotPhotos.clear();
            spotPhotos.addAll(ImageUtil.getImageItems(checkGuid, "IMG"));
            spotPhotoAdapter.notifyDataSetChanged();
            if (spotPhotos.size() == 0) {
                lin_spotphoto.setVisibility(View.GONE);
            }
            lastSpotPhotos.clear();
            lastSpotPhotos.addAll(spotPhotos);
            rectifyPhotos.clear();
            rectifyPhotos.addAll(ImageUtil.getImageItems(checkGuid, "PIC"));
            rectifyPhotoAdapter.notifyDataSetChanged();
            if (rectifyPhotos.size() == 0) {
                lin_rectifyphoto.setVisibility(View.GONE);
            }
            lastRectifyPhotos.clear();
            lastRectifyPhotos.addAll(rectifyPhotos);

            feedphotos.clear();
            feedphotos.addAll(ImageUtil.getImageItems(checkGuid, "FEDIMG"));
            feedPhotoAdapter.notifyDataSetChanged();

            if (feedphotos.size() == 0) {
                lin_feedphoto.setVisibility(View.GONE);
            }

            personSigns.addAll(ImageUtil.getImageItems(checkGuid, "CHKORSIGN"));
            objectSigns.addAll(ImageUtil.getImageItems(checkGuid, "OBJSIGN"));
            if (personSigns != null && personSigns.size() != 0) {
                lin_checkperson.setVisibility(View.VISIBLE);
//                    img_checkperson.setVisibility(View.VISIBLE);
//                    ImageItem imageItem = personSigns.get(0);
//                Contant.personBitmap = imageItem.getBitmapSelf(CommitActivity.this);
//                img_checkperson.setImageBitmap(imageItem.getBitmap(CommitActivity.this));

                adapterImage(personSigns, 1);
            } else {
                lin_checkperson.setVisibility(View.GONE);
                Contant.personBitmapList.clear();
            }

            if (objectSigns != null && objectSigns.size() != 0) {
                lin_checkedobject.setVisibility(View.VISIBLE);
//                    img_checkedobject.setVisibility(View.VISIBLE);
//                    ImageItem imageItem = objectSigns.get(0);
//                Contant.objectBitmap = imageItem.getBitmapSelf(CommitActivity.this);
//                img_checkedobject.setImageBitmap(imageItem.getBitmap(CommitActivity.this));


                adapterImage(objectSigns, 2);
            } else {
                lin_checkedobject.setVisibility(View.GONE);
                Contant.objectBitmapList.clear();
            }

            if(!CommitActivity.this.isFinishing()){
                requestVideoList();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }else{
            imageFileBeans.clear();
            imageFileBeans.addAll(failImageBeans);
            failImageBeans.clear();
            savePhoto(imageFileBeans.get(0));
        }
    }




    private void adapterImage(ArrayList<ImageItem> imageItems,int index){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if(index==1){
//            String url = ImageDownloader.Scheme.FILE.wrap(imageItem.getImagePath());
//            Contant.personBitmap = ImageLoader.getInstance().loadImageSync(url, options);
            grid_checkperson.setVisibility(View.VISIBLE);
            PersonSignAdapter personSignAdapter = new PersonSignAdapter(imageItems,this);
            grid_checkperson.setAdapter(personSignAdapter);
            Contant.personBitmapList.clear();
            if(imageItems!=null&&imageItems.size()!=0){
                for(int i=0;i<imageItems.size();i++){
                    ImageItem imageItem = imageItems.get(i);
                    String url = ImageDownloader.Scheme.FILE.wrap(imageItem.getImagePath());
                    Contant.personBitmapList.add(ImageLoader.getInstance().loadImageSync(url, options));
                }
            }

        }else if(index==2){
            grid_checkedObject.setVisibility(View.VISIBLE);
            PersonSignAdapter objectSignAdapter = new PersonSignAdapter(imageItems,this);
            grid_checkedObject.setAdapter(objectSignAdapter);
            Contant.objectBitmapList.clear();
            if(imageItems!=null&&imageItems.size()!=0){
                for(int i=0;i<imageItems.size();i++){
                    ImageItem imageItem = imageItems.get(i);
                    String url = ImageDownloader.Scheme.FILE.wrap(imageItem.getImagePath());
                    Contant.objectBitmapList.add(ImageLoader.getInstance().loadImageSync(url, options));
                }
            }
        }

    }



    /**
     * 对commitActivity中的控件进行分情况 不可编辑
     */
    private void initNoEditView(){

        lin_street.setVisibility(View.GONE);
        sp_street.setEnabled(false);

        if(historyBean.getFeedBackMan()==null||"".equals(historyBean.getFeedBackMan())){
            if(!"feed".equals(action)){
                lin_checkterm.setVisibility(View.VISIBLE);
                lin_feedphoto.setVisibility(View.GONE);
                lin_feedback.setVisibility(View.GONE);
                lin_feedupload.setVisibility(View.GONE);
            }else{
                text_preview.setVisibility(View.GONE);
            }
        }else{
            if(!"feed".equals(action)){
                edit_feedback.setText(historyBean.getFeedBackDes());
                edit_feedman.setText(historyBean.getFeedBackMan());
                text_feeddate.setText(historyBean.getFeedBackTime());

                edit_feedback.setEnabled(false);
                edit_feedman.setEnabled(false);
                text_feeddate.setEnabled(false);
            }

            lin_feedphoto.setVisibility(View.VISIBLE);
            lin_feedback.setVisibility(View.VISIBLE);
            lin_feedupload.setVisibility(View.GONE);
        }

        if(infoList!=null&&infoList.size()!=0){
            for(int i=0;i<infoList.size();i++){
                infoList.get(i).setIsEdit(false);
            }
        }
        if(infoListAdapter!=null){
            infoListAdapter.notifyDataSetChanged();
            listInfo.setEnabled(false);
        }
        if(!bussinessType.contains("其他")){
            if(expandLists!=null&&expandLists.size()!=0){
                for(int i=0;i<expandLists.size();i++){
                    MyExpandableListView myExpandableListView = (MyExpandableListView) expandLists.get(i);
                    if(myExpandableListView!=null){
                        myExpandableListView.collapseGroup(0);
                    }
                }
            }
        }


        text_noIllegal.setVisibility(View.GONE);
        text_setninepic.setVisibility(View.GONE);
        text_setthreepic.setVisibility(View.GONE);
        text_setvideosize.setVisibility(View.GONE);
        bt_video.setVisibility(View.GONE);
        text_feedsetnine.setVisibility(View.GONE);

//        rg_opinion.setEnabled(false);
        int count = rg_opinion.getChildCount();
        if(count>0){
            for(int i=0;i<count;i++){
                View view = rg_opinion.getChildAt(i);
                view.setClickable(false);
            }
        }
//        cb_XCZG.setClickable(false);
//        cb_XQZG.setClickable(false);
//        text_xqzgdate.setClickable(false);

        if(illegalTermBeans!=null&&illegalTermBeans.size()!=0){
            for(int i=0;i<illegalTermBeans.size();i++){
                illegalTermBeans.get(i).setIsEdit(false);
            }
        }else{
            list_illegal.setVisibility(View.GONE);
            text_noIllegal.setVisibility(View.VISIBLE);
        }
        if(illegalAdapter!=null){
            illegalAdapter.notifyDataSetChanged();
        }

        edit_describe.setEnabled(false);

        text_signobject.setVisibility(View.GONE);
        text_signperson.setVisibility(View.GONE);


        text_insertperson.setClickable(false);
        personAdapter = new PersonAdapter(this,personBeans,false);
        list_person.setAdapter(personAdapter);
        list_person.setEnabled(false);
        text_checkdate.setClickable(false);
        bt_video.setClickable(false);
        bt_checkperson.setClickable(false);
        bt_checkedobject.setClickable(false);
        bt_checkedobject.setVisibility(View.GONE);
        bt_checkperson.setVisibility(View.GONE);
        rel_imgButton.setVisibility(View.GONE);
    }




    /**
     * 加载本地的或者记录中的图片和视频
     */
    private void initRecordPhoto(){


        //记录中图片的加载

        if(action.equals("checkRecordLocal")){
            //本地记录图片加载
            String spotImgPaths = historyBean.getSpotImgPaths();
            String rectifyImgPaths = historyBean.getRectifyImgPath();
            if(!"".equals(spotImgPaths)){
                String[] spotPaths = spotImgPaths.split(",");
                List<ImageItem> spots = new ArrayList<>();
                for(String path:spotPaths){
                    ImageItem imageItem = new ImageItem();
                    imageItem.setImagePath(path);
                    spots.add(imageItem);
                }
                spotPhotos.addAll(spots);
                spotPhotoAdapter.notifyDataSetChanged();
            }
            if(!"".equals(rectifyImgPaths)){
                String[] rectifyPaths = rectifyImgPaths.split(",");
                List<ImageItem> rectifys = new ArrayList<>();
                for(String path:rectifyPaths){
                    ImageItem imageItem = new ImageItem();
                    imageItem.setImagePath(path);
                    rectifys.add(imageItem);
                }
                rectifyPhotos.addAll(rectifys);
                rectifyPhotoAdapter.notifyDataSetChanged();
            }

            String videoPaths = historyBean.getVideoPaths();
            if(videoPaths!=null&&!"".equals(videoPaths)){
                String[] videopatharray = videoPaths.split(",");
                List<VideoBean> videos = new ArrayList<>();
                for(String path:videopatharray){
                    VideoBean videoBean = new VideoBean();
                    videoBean.setIsLocal(true);
                    videoBean.setPath(path);
                    String name = getFileName(path);
                    videoBean.setName(name);
                    videos.add(videoBean);
                }
                videoBeans.addAll(videos);
                list_video.setVisibility(View.VISIBLE);
                videoListAdapter.notifyDataSetChanged();
            }

            String personsignPath =historyBean.getPeronSiagnPath();
            if(personsignPath!=null&&!"".equals(personsignPath)){
                String[] personStrs = personsignPath.split(";");
                ArrayList<ImageItem> imageItems = new ArrayList<>();
                if(personStrs!=null&&personStrs.length!=0){
                    for(int i=0;i<personStrs.length;i++){
                        ImageItem imageItem = new ImageItem();
                        imageItem.setImagePath(personStrs[i]);
                        imageItems.add(imageItem);
                    }
                }
                adapterImage(imageItems, 1);
//                Bitmap bitmap = imageItem.getBitmap(CommitActivity.this);
//                Contant.personBitmap = imageItem.getBitmapSelf(CommitActivity.this);
//                img_checkperson.setImageBitmap(bitmap);
            }

            String objectsignPath =historyBean.getObjectSignPath();
            if(objectsignPath!=null&&!"".equals(objectsignPath)){


                String[] objectStrs = objectsignPath.split(";");
                ArrayList<ImageItem> imageItems = new ArrayList<>();
                if(objectStrs!=null&&objectStrs.length!=0){
                    for(int i=0;i<objectStrs.length;i++){
                        ImageItem imageItem = new ImageItem();
                        imageItem.setImagePath(objectStrs[i]);
                        imageItems.add(imageItem);
                    }
                }
                adapterImage(imageItems, 2);


//                ImageItem imageItem = new ImageItem();
//                imageItem.setImagePath(objectsignPath);
//                ArrayList<ImageItem> imageItems = new ArrayList<>();
//                imageItems.add(imageItem);
//                img_checkedobject.setVisibility(View.VISIBLE);
//                adapterImage(imageItems,img_checkedobject,2);
//                Bitmap bitmap = imageItem.getBitmap(CommitActivity.this);
//                Contant.objectBitmap = imageItem.getBitmapSelf(CommitActivity.this);
//                img_checkedobject.setImageBitmap(bitmap);
            }



        }else {
            if (!"".equals(checkGuid)) {
                if (NetUtil.isNetworkAvailable(this)) {
                    if (progressDialog == null) {
                        progressDialog = showProgressDialog();
                        progressDialog.setTitle("加载图片...");
                        progressDialog.setMessage("正在加载图片...");
                        progressDialog.setCanceledOnTouchOutside(false);
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        progressDialog.setTitle("加载图片...");
                        progressDialog.setMessage("正在加载图片...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("inspectGuid", checkGuid);
                            String result = RequestUtil.post(RequestUtil.NewGetInspectAttachFiles, params);
                            Message msg = Message.obtain();
                            msg.what = 11;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    }).start();


                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    ShowToast(noNetText);
                }
            }
        }
    }

    /**
     * 请求附件图片的base64字符串后，保存到本地
     * @param imageFileBean
     */
    private void savePhoto(final ImageFileBean imageFileBean){
        final String fileGuid = imageFileBean.getGuid();

        if(!"".equals(fileGuid)){
            if(NetUtil.isNetworkAvailable(this)) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String result =  ImageUtil.downloadPic(imageFileBean.getUrl(),imageFileBean.getName(),imageFileBean.getGuid(),checkGuid,imageFileBean.getType());

//                        Map<String,String> params = new HashMap<String, String>();
//                        params.put("guid", fileGuid);
//                        String result = RequestUtil.post(RequestUtil.GetAttachFileData,params);
                        Message msg = Message.obtain();
                        msg.what =12;
                        msg.obj = result;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("imageFileBean", imageFileBean);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                }).start();

            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                ShowToast(noNetText);
            }
        }

    }




    private List<CheckTermGroupBean> getTermGroups(String checklist,String btype){
        List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();

        List<CheckResultBean> resultBeans = gson.fromJson(checklist, new TypeToken<List<CheckResultBean>>() {}.getType());
        Map<String,List<CheckResultBean>> map=new HashMap<>();
        for(CheckResultBean resultBean:resultBeans){
            if(btype.equals(resultBean.getBtype())){
                String inspectClass = resultBean.getInspectclass();
                List<CheckResultBean> list=null;
                if(!map.containsKey(inspectClass)){
                    list=new ArrayList<>();
                    list.add(resultBean);
                    map.put(inspectClass,list);

                }else{
                    map.get(resultBean.getInspectclass()).add(resultBean);
                }
            }
        }
        Set<String> mapstr = map.keySet();
        for(String key:mapstr){
            CheckTermGroupBean groupBean = new CheckTermGroupBean();
            groupBean.setGroupName(key);
            List<CheckResultBean> checkResultBeans =  map.get(key);
            List<CheckTermBean> terms = new ArrayList<>();
            for(CheckResultBean bean:checkResultBeans){
                terms.add(resultToTermBean(bean));
            }
            groupBean.setCheckTermBeans(terms);
            termGroupBeans.add(groupBean);
        }
        return termGroupBeans;
    }


    private CheckTermBean resultToTermBean(CheckResultBean resultBean){
        CheckTermBean termBean = new CheckTermBean();

        termBean.setGuid(resultBean.getItemguid());
        termBean.setBussinesstype(resultBean.getBtype());
        termBean.setTitle(resultBean.getTitle());
        termBean.setInspectClass(resultBean.getInspectclass());
        termBean.setOptions(resultBean.getOptions());
        termBean.setIsInLaw(resultBean.getIsinlaw());
        termBean.setTextOptions(resultBean.getTextoptions());
        termBean.setTextTitles(resultBean.getTexttitle());
//        termBean.setTextType(resultBean.getT);
        termBean.setIllegalOptions(resultBean.getIllegaloptions());
        termBean.setEnterprisePoints(resultBean.getEnterpricepoints());
        termBean.setPerInChargePoints(resultBean.getPerinchargepoints());
        termBean.setTextneednum(resultBean.getTextneednum());
        termBean.setValue(resultBean.getValue());
        if("1".equals(resultBean.getIscorrected())){
            termBean.setIsXCZG(true);
        }else{
            termBean.setIsXCZG(false);
        }

        return termBean;
    }


    private List<BranchBean> getPersonBeans(String result){
        List<BranchBean> beans = new ArrayList<>();
        if(!result.equals("")){
            beans = gson.fromJson(result, new TypeToken<List<BranchBean>>() {
            }.getType());
            if(beans!=null&&beans.size()!=0){
                for(BranchBean branchBean:beans){
                    branchBean.setListBranch();
                    for(PersonBean personBean:branchBean.getPersonList()){
                        personBean.setIsChecked(false);
                        if(personBeans!=null&&personBeans.size()!=0){
                            for(PersonBean bean:personBeans){
                                if(bean.getPID().equals(personBean.getPID())){
                                    personBean.setIsChecked(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return beans;
    }


    private List<InfoAttributeBean> getInfoList(List<CheckHeadBean> headBeans,String[] keys,String[] names){
        List<InfoAttributeBean> attributeBeans = new ArrayList<>();
        if(headBeans!=null&&headBeans.size()!=0) {
            for (int i = 0; i < headBeans.size(); i++) {
                InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
                CheckHeadBean headBean = headBeans.get(i);
                String dbField = headBean.getDbfield();
                int index = -1;
                String indexKey = "";
                for (int j = 0; j < keys.length; j++) {
                    String key = keys[j];
                    if (key.equals(dbField)) {
                        index = j;
                        indexKey = key;
                        break;
                    }
                }
                if (index != -1) {
                    infoAttributeBean.setName(names[index]);
                } else {
                    infoAttributeBean.setName("");
                }
                infoAttributeBean.setKey(indexKey);
                infoAttributeBean.setValue(headBean.getValue());
                attributeBeans.add(infoAttributeBean);
            }
        }
        return attributeBeans;
    }

    private List<InfoAttributeBean> getInfo(List<TableHeadBean> tableHeads){
        List<InfoAttributeBean> attributeBeans = new ArrayList<>();
        if(tableHeads!=null&&tableHeads.size()!=0) {
            for (int i = 0; i < tableHeads.size(); i++) {
                InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
                TableHeadBean tableHeadBean = tableHeads.get(i);
                infoAttributeBean.setName(tableHeadBean.getName());
                infoAttributeBean.setValue("");
                infoAttributeBean.setKey(tableHeadBean.getField());
                attributeBeans.add(infoAttributeBean);
            }
        }
        return attributeBeans;
    }

    private InspectBean getData(){

        InspectBean inspectBean = new InspectBean();
        CheckBaseInfoBean baseInfo = historyBean.getCheckBaseInfoBean();
        inspectBean.setState("2");
        inspectBean.setInspectType(getStringValue(baseInfo.getInspectType()));
        inspectBean.setSource(getStringValue(baseInfo.getSource()));
        inspectBean.setSourceDetail(getStringValue(baseInfo.getSourceDetail()));
        inspectBean.setObjectType(getStringValue(baseInfo.getObjectType()));
        inspectBean.setStreetId(streetId);
        StringBuilder bussSB = new StringBuilder();
        if(bussinessType!=null&&bussinessType.size()!=0){
            for(int i=0;i<bussinessType.size();i++){
                if(i==0){
                    bussSB.append(bussinessType.get(i));
                }else{
                    bussSB.append(","+bussinessType.get(i));
                }
            }
        } else {
            bussSB.append("");
        }
        inspectBean.setBussinesstype(bussSB.toString());
        if(illegalTermBeans.size()==0){
            inspectBean.setConclusion("没有违规项");
        }else {
            inspectBean.setConclusion("有违规项");
        }
        inspectBean.setConclusionOther("");

        String describe = edit_describe.getText().toString();
        inspectBean.setDescription(describe);
        StringBuilder treatSB = new StringBuilder();
        if(treatmentList!=null&&treatmentList.size()!=0){
            for(int i=0;i<treatmentList.size();i++){
                if(i==0){
                    treatSB.append(treatmentList.get(i));
                }else{
                    treatSB.append(","+treatmentList.get(i));
                }
            }
        } else {
            treatSB.append("");
        }
        inspectBean.setTreatment(getStringValue(treatSB.toString()));
        inspectBean.setInspectTime(getStringValue(text_checkdate.getText().toString()));
        inspectBean.setInspector(getStringValue(getPersonStr(personBeans)));
        inspectBean.setRectifDeadline(getStringValue(deadline));
        inspectBean.setDXSList(getStringValue(DXSList));
        inspectBean.setFWList(getStringValue(FWList));
        InspectItemsResultBean inspectResultBean = new InspectItemsResultBean();
        if(action.equals("objectAddress")){
        }else{
            inspectResultBean.setHead(getHeadList());
            inspectResultBean.setResult(getCheckResult());
        }
        inspectBean.setObjectName(getStringValue(historyBean.getName()));
        inspectBean.setObjectAddress(getStringValue(historyBean.getAddress()));
        inspectBean.setInspectItemsResult(inspectResultBean);

        return inspectBean;
    }


    private int getHaveCertCount(List<PersonBean> personBeans){
        int count= 0;
        if(personBeans!=null&&personBeans.size()!=0){
            for(int i=0;i<personBeans.size();i++){
                PersonBean personBean = personBeans.get(i);
                if(personBean.getCertNo()!=null&&!"".equals(personBean.getCertNo())){
                    count+=1;
                }
            }
        }
        return count;
    }

    private CheckDBBean getDBBean(InspectBean inspectBean){
        CheckDBBean checkDBBean = new CheckDBBean();
        checkDBBean.setState(inspectBean.getState());
        checkDBBean.setNoInCitySytem("");
        checkDBBean.setInspectType(inspectBean.getInspectType());
        checkDBBean.setSource(inspectBean.getSource());
        checkDBBean.setSourceDetail(inspectBean.getSourceDetail());
        checkDBBean.setObjectType(inspectBean.getObjectType());
        checkDBBean.setBussinesstype(inspectBean.getBussinesstype());
        checkDBBean.setInspectClasses("");
        checkDBBean.setObjectID(historyBean.getId());
        checkDBBean.setConclusion(inspectBean.getConclusion());
        checkDBBean.setConclusionOther(inspectBean.getConclusionOther());
        checkDBBean.setDescription(inspectBean.getDescription());
        checkDBBean.setTreatment(inspectBean.getTreatment());
        checkDBBean.setInspector(getPersonStr(personBeans));
        checkDBBean.setInspectTime(inspectBean.getInspectTime());
        checkDBBean.setInspector(inspectBean.getInspector());
        checkDBBean.setInspectBranch("");
        checkDBBean.setObjectName(inspectBean.getObjectName());
        checkDBBean.setObjectAddress(inspectBean.getObjectAddress());
        InspectItemsResultBean itemsResultBean = inspectBean.getInspectItemsResult();
        String resultsyr = gson.toJson(itemsResultBean);
        checkDBBean.setInspectItemsResult(getStringValue(resultsyr));
        checkDBBean.setAddUser(Contant.userid);
        checkDBBean.setAddTime(inspectBean.getInspectTime());
        checkDBBean.setSubmitTime(inspectBean.getInspectTime());
        checkDBBean.setRectifDeadline(inspectBean.getRectifDeadline());
        checkDBBean.setDXSList(inspectBean.getDXSList());
        checkDBBean.setFWList(inspectBean.getFWList());
        checkDBBean.setSpotImgPaths("");
        checkDBBean.setRectifyImgPath("");
        checkDBBean.setVideoPaths("");
        checkDBBean.setObjectSignPath("");
        checkDBBean.setPersonSignPath("");
        checkDBBean.setTest("");
        checkDBBean.setStreetID(inspectBean.getStreetId());
        checkDBBean.setStreetInfo(streetInfo);

        return checkDBBean;
    }

    private String getStringValue(String str){
        if(str==null){
            return "";
        }else{
            return str;
        }
    }


    private String getPersonStr(List<PersonBean> personBeans){
        StringBuilder sb = new StringBuilder();
        if(personBeans!=null&&personBeans.size()!=0){
            for(int i=0;i<personBeans.size();i++){
                PersonBean personBean = personBeans.get(i);
                if(i==0){
                    sb.append(personBean.getPID()+","+personBean.getName()+","+personBean.getCertNo()+","+personBean.getBranch());
                }else{
                    sb.append(";"+personBean.getPID()+","+personBean.getName()+","+personBean.getCertNo()+","+personBean.getBranch());
                }
            }
        }
        return sb.toString();
    }

    private List<CheckHeadBean> getHeadList() {
        List<CheckHeadBean> headBeans = new ArrayList<>();
        if (headList != null) {
            for (int i = 0; i < headList.size(); i++) {
                CheckHeadBean headBean = new CheckHeadBean();
                TableHeadBean tableHeadBean = headList.get(i);
                String name = tableHeadBean.getName();
                if (bussinessType.size() == 1 && bussinessType.contains(getResources().getString(R.string.primebroker))) {
                    headBean.setBtype(getResources().getString(R.string.primebroker));
                } else {
                    headBean.setBtype(getResources().getString(R.string.propertymanage));
                }
                headBean.setObjid(historyBean.getId());
                for (InfoAttributeBean attribute : infoList) {
                    if (attribute.getName().equals(name)) {
                        headBean.setDbfield(attribute.getKey());
                        headBean.setTitle(attribute.getName());
                        if("项目名称".equals(attribute.getName())){
                            historyBean.setName(attribute.getValue());
                        }else if("项目地址".equals(attribute.getName())){
                            historyBean.setAddress(attribute.getValue());
                        }

                        headBean.setValue(attribute.getValue());
                        break;
                    }
                }
                headBeans.add(headBean);
            }
        }
        return headBeans;
    }

    private List<CheckResultBean> getCheckResult(){
        List<CheckResultBean>  resultBeans = new ArrayList<>();
        for(int i=0;i<bussinessType.size();i++){
            List<CheckTermGroupBean> checkTermGroupBeans = null;
            String type = bussinessType.get(i);
            if(type.equals(getResources().getString(R.string.primebroker))){
                checkTermGroupBeans = checkTermGroupBeansPrime;
            }else if(type.equals(getResources().getString(R.string.propertymanage))){
                checkTermGroupBeans = checkTermGroupBeansPro;
            }else if(type.equals(getResources().getString(R.string.generalbasement))){
                checkTermGroupBeans = checkTermGroupBeansGen;
            }else if(type.equals(getResources().getString(R.string.housesafeuse))){
                checkTermGroupBeans = checkTermGroupBeansHouse;
            }
            if(checkTermGroupBeans!=null){
                for(CheckTermGroupBean groupBean:checkTermGroupBeans){
                    List<CheckTermBean> termBeans = groupBean.getCheckTermBeans();
                    for(CheckTermBean termBean:termBeans){
                        CheckResultBean resultBean = new CheckResultBean();
                        resultBean.setBtype(type);
                        resultBean.setEnterpricepoints(termBean.getEnterprisePoints());
                        resultBean.setIllegaloptions(termBean.getIllegalOptions());
                        resultBean.setInspectclass(termBean.getInspectClass());
                        resultBean.setIsinlaw(termBean.getIsInLaw());
                        resultBean.setItemguid(termBean.getGuid());
                        resultBean.setOptions(termBean.getOptions());
                        resultBean.setPerinchargepoints(termBean.getPerInChargePoints());
                        resultBean.setSortid((termBeans.size() + 1) + "");
                        resultBean.setText("");
                        resultBean.setTextneednum(termBean.getTextneednum());
                        resultBean.setTextoptions(termBean.getTextOptions());
                        resultBean.setTexttitle(termBean.getTextTitles());
                        resultBean.setTitle(termBean.getTitle());
                        resultBean.setValue(termBean.getValue());
                        resultBean.setIscorrected("0");
                        for(CheckTermBean checkTermBean:illegalTermBeans){
                            if(checkTermBean.getGuid().equals(termBean.getGuid())){
                                if(checkTermBean.isXCZG()){
                                    resultBean.setIscorrected("1");
                                }else{
                                    resultBean.setIscorrected("0");
                                }
                            }
                        }
                        resultBeans.add(resultBean);
                    }
                }
            }
        }
        return resultBeans;
    }

    /**
     * 记载检查人界面的dailog
     */
    private void initPersonDialog(){
        if(personAlertDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            personAlertDialog = builder.create();
            personAlertDialog.show();
            personAlertDialog.setCanceledOnTouchOutside(true);
            personAlertDialog.setContentView(R.layout.personchoose_dialog);
            listView_person= (ExpandableListView) personAlertDialog.findViewById(R.id.expandlist_persondialog);
            text_more = (TextView) personAlertDialog.findViewById(R.id.text_person_more);
            text_person_sure = (TextView) personAlertDialog.findViewById(R.id.text_persondialog_sure);
            text_person_cancel = (TextView) personAlertDialog.findViewById(R.id.text_persondialog_cancel);
        }else{
            personAlertDialog.show();
        }
        requestPerson(0);

        text_person_sure.setOnClickListener(this);
        text_person_cancel.setOnClickListener(this);
        text_more.setOnClickListener(this);
    }

    /**
     * 请求检查人数据
     * @param isAll
     */
    private void requestPerson(final int isAll){
        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,Object> params = new HashMap<String, Object>();
                    params.put("name", Contant.userid);
                    params.put("isAll", isAll);
                    String result = RequestUtil.postob(RequestUtil.GetCheckManList, params);
                    Message msg = Message.obtain();
                    msg.what = 8;
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

    }

    private void requestNotify(final String notifyDate){
        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Map<String,Object> params = new HashMap<String, Object>();
                    params.put("loginName", Contant.userid);
                    params.put("noticeDate",notifyDate);
                    params.put("inspectGuid",checkGuid);
                    String result = RequestUtil.postob(RequestUtil.UpdateInspectHistoryNoticed, params);
                    Message msg = Message.obtain();
                    msg.what = 9;
                    msg.obj = result;
                    msg.arg2=1;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            ShowToast(noNetText);
        }

    }

    /**
     * 加载日期选择的dialog
     * @param index
     */
    private void initDialog(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog= builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setContentView(R.layout.date_layout);
        datePicker = (DatePicker) alertDialog.findViewById(R.id.datepicker);


        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        datePicker.setLayoutParams(new RelativeLayout.LayoutParams(width * 2 / 3, height / 2));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        datePicker.setDate(year, month);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {

                if(index==1){

                    long day = Util.getDaynum(date,nowTime);
                    if(day>7){
                        ShowToast("你选择的检查日期在一月前，不能录入系统");
                    }else{
                        text_checkdate.setText(date);
                        alertDialog.dismiss();
                    }
                }else if(index==2){
                    deadline = date;
//                    text_xqzgdate.setText(date);
                    alertDialog.dismiss();
                }else if(index==3){
//                    text_notifydate.setText(date);
                }else if(index==4){
                    text_feeddate.setText(date);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private int getTermSize(){
        int size = 0;
        if(bussinessType.contains(getResources().getString(R.string.primebroker))){
            if(checkTermGroupBeansPrime.size()!=0) {
                for (CheckTermGroupBean groupBean : checkTermGroupBeansPrime) {
                    size += groupBean.getCheckTermBeans().size();
                }
            }
        }else{
            if(checkTermGroupBeansPro.size()!=0){
                for(CheckTermGroupBean groupBean:checkTermGroupBeansPro){
                    size+=groupBean.getCheckTermBeans().size();
                }
            }
            if(checkTermGroupBeansGen.size()!=0){
                for(CheckTermGroupBean groupBean:checkTermGroupBeansGen){
                    size+=groupBean.getCheckTermBeans().size();
                }
            }
            if(checkTermGroupBeansHouse.size()!=0){
                for(CheckTermGroupBean groupBean:checkTermGroupBeansHouse){
                    size+=groupBean.getCheckTermBeans().size();
                }
            }
        }
        return size;
    }


    /**
     * 设置地下室和房屋列表显示
     */
    private void setDXAANDFW(){
        if(bussinessType.contains(getResources().getString(R.string.primebroker))){
            FWList = "";
            DXSList = "";
        }else{
            PropertyBean propertyBean = Contant.propertyObjectList.get(0);
            List<GeneralBean> generalBeans = propertyBean.getGeneralBeans();
            List<HouseSafeBean> houseBeans = propertyBean.getHouseBeans();
            if(generalBeans!=null&&generalBeans.size()!=0){
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<generalBeans.size();i++){
                    GeneralBean bean = generalBeans.get(i);
                    if(i==0){
                        sb.append(bean.getBase_id()+","+bean.getHouseLocate());
                    }else{
                        sb.append(";"+bean.getBase_id()+","+bean.getHouseLocate());
                    }
                }
                DXSList = sb.toString();
            }else{
                DXSList = "";
            }
            if(houseBeans!=null&&houseBeans.size()!=0){
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<houseBeans.size();i++){
                    HouseSafeBean bean = houseBeans.get(i);
                    if(i==0){
                        sb.append(bean.getBUILD_NO()+","+bean.getBUILD_SITE());
                    }else{
                        sb.append(";"+bean.getBUILD_NO()+","+bean.getBUILD_SITE());
                    }
                }
                FWList = sb.toString();
            }else{
                FWList = "";
            }
        }
    }
    private TextView text_message;

    /**
     * 结束检查的dialog
     */
    private void initNotifyDailog(){
        if(alertDialogNotify==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialogNotify = builder.create();
            alertDialogNotify.show();
            alertDialogNotify.setCanceledOnTouchOutside(true);
            alertDialogNotify.setContentView(R.layout.notify_dialog);
            text_message= (TextView) alertDialogNotify.findViewById(R.id.text_notifydialog_message);

//            LinearLayout lin_notifydate = (LinearLayout) alertDialogNotify.findViewById(R.id.lin_notifydate);
            TextView text_notifysure = (TextView) alertDialogNotify.findViewById(R.id.text_notifydialog_sure);
            TextView text_notifycancle = (TextView) alertDialogNotify.findViewById(R.id.text_notifydialog_cancel);
            if(getHaveCertCount(personBeans)<2){
                text_message.setText("您选择的检查人中有执法证号的少于两个,提交后将不能上传市系统,确定结束检查吗？");
                text_message.setTextColor(getResources().getColor(R.color.red));
            }else{
                text_message.setText("检查结束后,检查单将不可修改,确定结束检查吗？");
            }
//            if(bussinessType.contains(getResources().getString(R.string.other))){
//                lin_notifydate.setVisibility(View.GONE);
//            }else{
//                lin_notifydate.setVisibility(View.VISIBLE);
//            }
//            text_notifydate.setText(nowTime);
//            text_notifydate.setOnClickListener(this);
            text_notifysure.setOnClickListener(this);
            text_notifycancle.setOnClickListener(this);

        }else{
            if(getHaveCertCount(personBeans)<2){
                text_message.setText("您选择的检查人中有执法证号的少于两个,提交后将不能上传市系统,确定结束检查吗？");
                text_message.setTextColor(getResources().getColor(R.color.red));
            }else{
                text_message.setText("检查结束后,检查单将不可修改,确定结束检查吗？");
            }
            alertDialogNotify.show();
        }

    }

    private void locationGPS(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            ShowToast("请打开Gps...");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String bestProvider = locationManager.getBestProvider(criteria,true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location!=null){
            Contant.longitube = location.getLongitude()+"";
            Contant.latitube = location.getLatitude()+"";
            text_loadinggps.setText("获取位置成功");
            ShowToast("已使用当前位置");
            text_choosedLoc.setVisibility(View.VISIBLE);
            text_choosedLoc.setText("已选择当前位置");
        }else{
            text_loadinggps.setText("获取位置失败");
            ShowToast("定位失败，请手动选择位置");
        }
    }



    private void saveSignDb(){

        if(Contant.personBitmapList.size()!=0||Contant.objectBitmapList.size()!=0) {
            if (Contant.personBitmapList.size() != 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Contant.personBitmapList.size(); i++) {
                    String fileName = "personsign" + nowTime + "-" + i + ".png";
                    String path = ImageUtil.saveMyBitmap(fileName, CommitActivity.this, Contant.personBitmapList.get(i), dbId + "", "CHKORSIGN");
                    if (i == 0) {
                        sb.append(path);
                    } else {
                        sb.append(";" + path);

                    }
                }
                checkDBBean.setPersonSignPath(sb.toString());
            } else {
                checkDBBean.setPersonSignPath("");
            }

            if (Contant.objectBitmapList.size() != 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Contant.objectBitmapList.size(); i++) {
                    String fileName = "objectsign" + nowTime + "-" + i + ".png";
                    String path = ImageUtil.saveMyBitmap(fileName, CommitActivity.this, Contant.objectBitmapList.get(i), dbId + "", "OBJSIGN");
                    if (i == 0) {
                        sb.append(path);
                    } else {
                        sb.append(";" + path);

                    }
                }
                checkDBBean.setObjectSignPath(sb.toString());
            } else {
                checkDBBean.setObjectSignPath("");
            }
            finalDb.update(checkDBBean, "id = " + dbId);
        }
    }

    boolean isFirstGo = true;

    private void uploadonePic(final String isSuccess, final List<ImageItem> photos){


        if("1".equals(isSuccess)) {

            if (NetUtil.isNetworkAvailable(this)) {
                if (progressDialog == null) {
                    progressDialog = showProgressDialog();
                    progressDialog.setTitle("上传图片...");
                    progressDialog.setMessage("正在上传图片...");
                    progressDialog.setCanceledOnTouchOutside(false);
                } else {
                    if (progressDialog.isShowing()) {
//                            if(isFirstGo){
//                                progressDialog.dismiss();
//                                progressDialog.setTitle("上传图片...");
//                                progressDialog.setMessage("正在上传图片...");
//                                progressDialog.setCanceledOnTouchOutside(false);
//                                progressDialog.show();
//                                isFirstGo = false;
//                            }
                    }else {
                        progressDialog.setTitle("上传图片...");
                        progressDialog.setMessage("正在上传图片...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }
                }

                if(photos!=null&&photos.size()>0){
                    final ImageItem imageItem = photos.get(0);
                    String filePath = imageItem.getImagePath();
                    String type = imageItem.getType();
                    new AsyncTask<String, String, String>() {

                        List<ImageItem> items = null;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            items = new ArrayList<ImageItem>();
                            items.addAll(photos);

                        }

                        @Override
                        protected String doInBackground(String... params) {

                            JSONArray jsonArray = new JSONArray();
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("Name", getFileName(params[0]));
                                jsonObject.put("Note", "test");
                                jsonObject.put("Type", params[1]);
                                String data = Util.picPathToBase64(params[0]);
                                jsonObject.put("Data", data);
                                data = null;
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Map<String, String> requestparams = new HashMap<String, String>();
                            requestparams.put("loginName", Contant.userid);
                            requestparams.put("inspectGuidList", checkGuid);
                            requestparams.put("filesString", jsonArray.toString());
                            String result = RequestUtil.post(RequestUtil.UploadInspectFiles, requestparams);
                            return result;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            JSONObject jsonObject = null;
                            String isSuccesssss = "";
                            String failReason = "";
                            String succeedString = "";

                            try {
                                jsonObject = new JSONObject(s);
                                isSuccesssss = jsonObject.getString("result");
                                failReason = jsonObject.getString("failReason");
                                succeedString = jsonObject.getString("succeedString");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if ("1".equals(isSuccesssss)) {
                                items.remove(0);
                                if (items.size() == 0) {
                                    new MyAsyncTask().execute();

                                } else {
                                    uploadonePic("1",items);
                                }
                            } else  {
                                if (failPhotos == null) {
                                    failPhotos = new ArrayList<ImageItem>();
                                }
                                failPhotos.add(items.get(0));
                                items.remove(0);
                                if (items.size() == 0) {
                                    new MyAsyncTask().execute();

                                } else {
                                    uploadonePic("1",items);
                                }
                            }

                        }
                    }.execute(new String[]{filePath,type});

                }else{
                    new MyAsyncTask().execute();
                }
            }else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                ShowToast(noNetText);
            }

        }else{
            if(checkDBBean!=null){
                if(spotPhotos.size()!=0){
                    StringBuilder spotBuild = new StringBuilder();
                    for(int i=0;i<spotPhotos.size();i++){
                        if(i==0){
                            spotBuild.append(spotPhotos.get(i).getImagePath());
                        }else{
                            spotBuild.append(","+spotPhotos.get(i).getImagePath());
                        }
                    }
                    if(checkDBBean!=null){
                        checkDBBean.setSpotImgPaths(spotBuild.toString());
                    }
                }

                if(rectifyPhotos.size()!=0){
                    StringBuilder rectifyBuild = new StringBuilder();
                    for(int i=0;i<rectifyPhotos.size();i++){
                        if(i==0){
                            rectifyBuild.append(rectifyPhotos.get(i).getImagePath());
                        }else{
                            rectifyBuild.append(","+rectifyPhotos.get(i).getImagePath());
                        }
                    }
                    if(checkDBBean!=null){
                        checkDBBean.setRectifyImgPath(rectifyBuild.toString());
                    }
                }

                if(videoBeans.size()!=0){
                    StringBuilder videoBuild = new StringBuilder();
                    for(int i=0;i<videoBeans.size();i++){
                        if(i==0){
                            videoBuild.append(videoBeans.get(i).getPath());
                        }else{
                            videoBuild.append(","+videoBeans.get(i).getPath());
                        }
                    }
                    checkDBBean.setVideoPaths(videoBuild.toString());
                }

                saveSignDb();

                if(finalDb==null){
                    finalDb = getFinalDb(CommitActivity.this);
                }

                if(dbId==-1){
                    finalDb.save(checkDBBean);
                    List<CheckDBBean> checkDBBeans = finalDb.findAll(CheckDBBean.class, "id DESC");
                    if(checkDBBeans!=null&&checkDBBeans.size()!=0){
                        dbId = checkDBBeans.get(0).getId();
                    }
                }else{
                    finalDb.update(checkDBBean, "id = " + dbId);
                }
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }


    class MyAsyncTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            JSONArray jsonArray = new JSONArray();
            try {
                if (Contant.personBitmapList.size()!=0) {
                    for(int i=0;i<Contant.personBitmapList.size();i++){
                        JSONObject jsonObject = new JSONObject();
                        String name = checkGuid + "CHKORSIGN" + nowTime +"-"+i+ ".png";
                        jsonObject.put("Name", name);
                        jsonObject.put("Note", "检查人签字");
                        jsonObject.put("Type", "CHKORSIGN");
                        String data = Util.bitmapToBase64(Contant.personBitmapList.get(i));
                        jsonObject.put("Data", data);
                        data = null;
                        jsonArray.put(jsonObject);
                    }
                }

                if (Contant.objectBitmapList.size()!=0) {
                    for(int i=0;i<Contant.objectBitmapList.size();i++) {
                        JSONObject jsonObject = new JSONObject();
                        String name = checkGuid + "OBJSIGN" + nowTime +"-"+i+ ".png";
                        jsonObject.put("Name", name);
                        jsonObject.put("Note", "受检单位签字");
                        jsonObject.put("Type", "OBJSIGN");
                        String data = Util.bitmapToBase64(Contant.objectBitmapList.get(i));
                        jsonObject.put("Data", data);
                        data = null;
                        jsonArray.put(jsonObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Map<String, String> requestparams = new HashMap<String, String>();
            requestparams.put("loginName", Contant.userid);
            requestparams.put("inspectGuidList", checkGuid);
            requestparams.put("filesString", jsonArray.toString());
            Util.saveStrToFile(jsonArray.toString());
            String result = RequestUtil.post(RequestUtil.UploadInspectFiles, requestparams);


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            JSONObject jsonObject = null;
            String isSuccess = "";
            String failReason = "";
            String succeedString = "";

            try {
                jsonObject = new JSONObject(s);
                isSuccess = jsonObject.getString("result");
                failReason = jsonObject.getString("failReason");
                succeedString = jsonObject.getString("succeedString");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
//                            ShowToast(failReason);
            if (failPhotos != null && failPhotos.size() != 0) {
                ShowToast("有" + failPhotos.size() + "张图片上传失败");
            } else {
                ShowToast("图片上传成功");
            }
            if (videoBeans != null && videoBeans.size() != 0) {
                uploadVideo();
            } else {
                if (dbId != -1) {
                    finalDb.deleteById(CheckDBBean.class, dbId);
                }
                Intent intent = new Intent(CommitActivity.this, MainActivity.class);
                CommitActivity.this.startActivity(intent);
                CommitActivity.this.finish();
            }
        }
    }



    /**
     * 上传图片，包括现场照片，整改带照片，签名照片
     * @param isSuccess
     * @param spotPhotos
     * @param rectifyPhotos
     */
//    private void upLoadPic(String isSuccess, final List<ImageItem> spotPhotos, final List<ImageItem> rectifyPhotos){
//        if(spotPhotos.size()!=0||rectifyPhotos.size()!=0) {
//            if("1".equals(isSuccess)){
//                if (NetUtil.isNetworkAvailable(this)) {
//                    if (progressDialog == null) {
//                        progressDialog = showProgressDialog();
//                        progressDialog.setTitle("上传图片...");
//                        progressDialog.setMessage("正在上传图片...");
//                        progressDialog.setCanceledOnTouchOutside(false);
//                    } else {
//                        if(progressDialog.isShowing()){
//                            progressDialog.dismiss();
//                        }
//                        progressDialog.setTitle("上传图片...");
//                        progressDialog.setMessage("正在上传图片...");
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.show();
//                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//                                if(spotPhotos.size()!=0) {
//                                    if (spotPhotos.size() < 5) {
//                                        JSONArray jsonArray = new JSONArray();
//                                        for (int i = 0; i < spotPhotos.size(); i++) {
//                                            ImageItem imageItem = spotPhotos.get(i);
//                                            JSONObject jsonObject = new JSONObject();
//                                            jsonObject.put("Name", getFileName(imageItem.getImagePath()));
//                                            jsonObject.put("Note", "test");
//                                            jsonObject.put("Type", "IMG");
//                                            String data = Util.picPathToBase64(imageItem.getImagePath());
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray.put(jsonObject);
//                                        }
//
//                                        if(Contant.personBitmap!=null){
//                                            JSONObject jsonObject = new JSONObject();
//                                            String name = checkGuid+"CHKORSIGN"+nowTime+".png";
//                                            jsonObject.put("Name", name);
//                                            jsonObject.put("Note", "检查人签字");
//                                            jsonObject.put("Type", "CHKORSIGN");
//                                            String data = Util.bitmapToBase64(Contant.personBitmap);
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray.put(jsonObject);
//                                        }
//
//                                        if(Contant.objectBitmap!=null){
//                                            JSONObject jsonObject = new JSONObject();
//                                            String name = checkGuid+"OBJSIGN"+nowTime+".png";
//                                            jsonObject.put("Name", name);
//                                            jsonObject.put("Note", "受检单位签字");
//                                            jsonObject.put("Type", "OBJSIGN");
//                                            String data = Util.bitmapToBase64(Contant.objectBitmap);
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray.put(jsonObject);
//                                        }
//
//
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("loginName", Contant.userid);
//                                        params.put("inspectGuidList", checkGuid);
//                                        params.put("filesString", jsonArray.toString());
//                                        Util.saveStrToFile(jsonArray.toString());
//                                        String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
//                                        Message msg = Message.obtain();
//                                        msg.what = 9;
//                                        msg.arg2 = 2;
//                                        msg.arg1 = 1;
//                                        msg.obj = result;
//                                        mHandler.sendMessage(msg);
//                                    } else {
//                                        JSONArray jsonArray = new JSONArray();
////
//                                        for (int i = 0; i < 4; i++) {
//                                            ImageItem imageItem = spotPhotos.get(i);
//                                            JSONObject jsonObject = new JSONObject();
//                                            jsonObject.put("Name", getFileName(imageItem.getImagePath()));
//                                            jsonObject.put("Note", "test");
//                                            jsonObject.put("Type", "IMG");
//                                            String data = Util.picPathToBase64(imageItem.getImagePath());
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray.put(jsonObject);
//                                        }
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("loginName", Contant.userid);
//                                        params.put("inspectGuidList", checkGuid);
//                                        params.put("filesString", jsonArray.toString());
//
//                                        String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
//                                        Message msg = Message.obtain();
//                                        msg.what = 9;
//                                        msg.arg2 = 2;
//                                        msg.arg1 = 0;
//                                        msg.obj = result;
//                                        mHandler.sendMessage(msg);
//
//
//                                        JSONArray jsonArray1 = new JSONArray();
//                                        for (int i = 4; i < spotPhotos.size(); i++) {
//                                            ImageItem imageItem = spotPhotos.get(i);
//                                            JSONObject jsonObject = new JSONObject();
//                                            jsonObject.put("Name", getFileName(imageItem.getImagePath()));
//                                            jsonObject.put("Note", "test");
//                                            jsonObject.put("Type", "IMG");
//                                            String data = Util.picPathToBase64(imageItem.getImagePath());
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray1.put(jsonObject);
//                                        }
//
//
//                                        if(Contant.personBitmap!=null){
//                                            JSONObject jsonObject = new JSONObject();
//                                            String name = checkGuid+"CHKORSIGN"+nowTime+".png";
//                                            jsonObject.put("Name", name);
//                                            jsonObject.put("Note", "检查人签字");
//                                            jsonObject.put("Type", "CHKORSIGN");
//                                            String data = Util.bitmapToBase64(Contant.personBitmap);
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray1.put(jsonObject);
//                                        }
//
//                                        if(Contant.objectBitmap!=null){
//                                            JSONObject jsonObject = new JSONObject();
//                                            String name = checkGuid+"OBJSIGN"+nowTime+".png";
//                                            jsonObject.put("Name", name);
//                                            jsonObject.put("Note", "受检单位签字");
//                                            jsonObject.put("Type", "OBJSIGN");
//                                            String data = Util.bitmapToBase64(Contant.objectBitmap);
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray1.put(jsonObject);
//                                        }
//                                        Map<String, String> params1 = new HashMap<String, String>();
//                                        params1.put("loginName", Contant.userid);
//                                        params1.put("inspectGuidList", checkGuid);
//                                        params1.put("filesString", jsonArray1.toString());
//                                        String result1 = RequestUtil.post(RequestUtil.UploadInspectFiles, params1);
//                                        Message msg1 = Message.obtain();
//                                        msg1.what = 9;
//                                        msg1.arg2 = 2;
//                                        msg1.arg1 = 1;
//                                        msg1.obj = result1;
//                                        mHandler.sendMessage(msg1);
//                                    }
//                                }
//                                if(bussinessType.contains("房屋安全")){
//                                    if(rectifyPhotos.size()!=0){
//                                        JSONArray jsonArray = new JSONArray();
//                                        for (int i = 0; i <rectifyPhotos.size() ; i++) {
//                                            ImageItem imageItem = rectifyPhotos.get(i);
//                                            JSONObject jsonObject = new JSONObject();
//                                            jsonObject.put("Name", getFileName(imageItem.getImagePath()));
//                                            jsonObject.put("Note", "test");
//                                            jsonObject.put("Type", "PIC");
//                                            String data = Util.picPathToBase64(imageItem.getImagePath());
//                                            jsonObject.put("Data", data);
//                                            data = null;
//                                            jsonArray.put(jsonObject);
//                                        }
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("loginName", Contant.userid);
//                                        params.put("inspectGuidList", checkGuid);
//                                        params.put("filesString", jsonArray.toString());
//
//                                        String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
//                                        Message msg = Message.obtain();
//                                        msg.what = 9;
//                                        msg.arg2 = 2;
//                                        msg.arg1 = 0;
//                                        msg.obj = result;
//                                        mHandler.sendMessage(msg);
//                                    }
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }).start();
//                } else {
//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }
//                    ShowToast(noNetText);
//                }
//            }else if("0".equals(isSuccess)){
//                if(spotPhotos.size()!=0){
//                    StringBuilder spotBuild = new StringBuilder();
//                    for(int i=0;i<spotPhotos.size();i++){
//                        if(i==0){
//                            spotBuild.append(spotPhotos.get(i).getImagePath());
//                        }else{
//                            spotBuild.append(","+spotPhotos.get(i).getImagePath());
//                        }
//                    }
//                    if(checkDBBean!=null){
//                        checkDBBean.setSpotImgPaths(spotBuild.toString());
//                    }
//                }
//
//                if(rectifyPhotos.size()!=0){
//                    StringBuilder rectifyBuild = new StringBuilder();
//                    for(int i=0;i<rectifyPhotos.size();i++){
//                        if(i==0){
//                            rectifyBuild.append(rectifyPhotos.get(i).getImagePath());
//                        }else{
//                            rectifyBuild.append(","+rectifyPhotos.get(i).getImagePath());
//                        }
//                    }
//                    if(checkDBBean!=null){
//                        checkDBBean.setRectifyImgPath(rectifyBuild.toString());
//                    }
//                }
//
//            }
//        }else{
//            if("1".equals(isSuccess)) {
//                if (NetUtil.isNetworkAvailable(this)) {
//                    if (progressDialog == null) {
//                        progressDialog = showProgressDialog();
//                        progressDialog.setTitle("上传图片...");
//                        progressDialog.setMessage("正在上传图片...");
//                        progressDialog.setCanceledOnTouchOutside(false);
//                    } else {
//                        if (progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                        progressDialog.setTitle("上传图片...");
//                        progressDialog.setMessage("正在上传图片...");
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.show();
//                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            JSONArray jsonArray = new JSONArray();
//                            try {
//                                if (Contant.personBitmap != null) {
//                                    JSONObject jsonObject = new JSONObject();
//                                    String name = checkGuid + "CHKORSIGN" + nowTime + ".png";
//
//                                    jsonObject.put("Name", name);
//                                    jsonObject.put("Note", "检查人签字");
//                                    jsonObject.put("Type", "CHKORSIGN");
//                                    String data = Util.bitmapToBase64(Contant.personBitmap);
//                                    jsonObject.put("Data", data);
//                                    jsonArray.put(jsonObject);
//                                }
//
//                                if (Contant.objectBitmap != null) {
//                                    JSONObject jsonObject = new JSONObject();
//                                    String name = checkGuid + "OBJSIGN" + nowTime + ".png";
//                                    jsonObject.put("Name", name);
//                                    jsonObject.put("Note", "受检单位签字");
//                                    jsonObject.put("Type", "OBJSIGN");
//                                    String data = Util.bitmapToBase64(Contant.objectBitmap);
//                                    jsonObject.put("Data", data);
//                                    jsonArray.put(jsonObject);
//                                }
//
//
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("loginName", Contant.userid);
//                                params.put("inspectGuidList", checkGuid);
//                                params.put("filesString", jsonArray.toString());
//
//                                String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
//                                Message msg = Message.obtain();
//                                msg.what = 9;
//                                msg.arg2 = 2;
//                                msg.arg1 = 1;
//                                msg.obj = result;
//                                mHandler.sendMessage(msg);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }).start();
//                }
//            }
//        }
//        if(finalDb==null){
//            finalDb = getFinalDb(CommitActivity.this);
//        }
//        if(checkDBBean!=null){
//            if(dbId==-1){
//                finalDb.save(checkDBBean);
//                List<CheckDBBean> checkDBBeans = finalDb.findAll(CheckDBBean.class, "id DESC");
//                if(checkDBBeans!=null&&checkDBBeans.size()!=0){
//                    dbId = checkDBBeans.get(0).getId();
//                }
//            }else{
//                finalDb.update(checkDBBean, "id = " + dbId);
//            }
//        }
//    }


    /**
     * 上传反馈照片
     */
    private void upLoadFeedPic(){
        if (NetUtil.isNetworkAvailable(this)) {
            if (progressDialog == null) {
                progressDialog = showProgressDialog();
                progressDialog.setTitle("上传图片...");
                progressDialog.setMessage("正在上传图片...");
                progressDialog.setCanceledOnTouchOutside(false);
            } else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog.setTitle("上传图片...");
                progressDialog.setMessage("正在上传图片...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (feedphotos.size() < 5) {
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < feedphotos.size(); i++) {
                                ImageItem imageItem = feedphotos.get(i);
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("Name", getFileName(imageItem.getImagePath()));
                                jsonObject.put("Note", "test");
                                jsonObject.put("Type", "FEDIMG");
                                String data = Util.picPathToBase64(imageItem.getImagePath());
                                jsonObject.put("Data", data);
                                data = null;
                                jsonArray.put(jsonObject);
                            }


                            Map<String, String> params = new HashMap<String, String>();
                            params.put("loginName", Contant.userid);
                            params.put("inspectGuidList", checkGuid);
                            params.put("filesString", jsonArray.toString());
                            Util.saveStrToFile(jsonArray.toString());
                            String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
                            Message msg = Message.obtain();
                            msg.what = 9;
                            msg.arg2 = 2;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        } else {
                            JSONArray jsonArray = new JSONArray();
                            JSONArray jsonArray1 = new JSONArray();
                            for (int i = 0; i < 4; i++) {
                                ImageItem imageItem = feedphotos.get(i);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("Name", getFileName(imageItem.getImagePath()));
                                jsonObject.put("Note", "test");
                                jsonObject.put("Type", "FEDIMG");
                                String data = Util.picPathToBase64(imageItem.getImagePath());
                                jsonObject.put("Data", data);
                                data = null;
                                jsonArray.put(jsonObject);
                            }
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("loginName", Contant.userid);
                            params.put("inspectGuidList", checkGuid);
                            params.put("filesString", jsonArray.toString());

                            String result = RequestUtil.post(RequestUtil.UploadInspectFiles, params);
                            Message msg = Message.obtain();
                            msg.what = 9;
                            msg.arg2 = 2;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                            for (int i = 4; i < feedphotos.size(); i++) {
                                ImageItem imageItem = feedphotos.get(i);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("Name", getFileName(imageItem.getImagePath()));
                                jsonObject.put("Note", "test");
                                jsonObject.put("Type", "FEDIMG");
                                String data = Util.picPathToBase64(imageItem.getImagePath());
                                jsonObject.put("Data", data);
                                jsonArray1.put(jsonObject);
                            }
                            Map<String, String> params1 = new HashMap<String, String>();
                            params1.put("loginName", Contant.userid);
                            params1.put("inspectGuidList", checkGuid);
                            params1.put("filesString", jsonArray1.toString());
                            String result1 = RequestUtil.post(RequestUtil.UploadInspectFiles, params1);
                            Message msg1 = Message.obtain();
                            msg1.what = 9;
                            msg1.arg2 = 2;
                            msg1.obj = result1;
                            mHandler.sendMessage(msg1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).start();


        }
    }

    /**
     * 通过路径path获取文件名
     * @param path
     * @return
     */
    private String getFileName(String path){
        String[] strs = path.split("\\/");
        String name = strs[strs.length-1];
        if(!name.contains("=guid=")){
            return name;
        }else{
            String fileName = name.split("=guid=")[1];
            return fileName;
        }
    }


    private void dismiss() {
        Animation animationexit = AnimationUtils.loadAnimation(this,
                R.anim.activity_translate_out);
        view_takephoto.setAnimation(animationexit);
        view_takephoto.setVisibility(View.GONE);
    }

    /**
     * 跳转到拍照界面
     */
    public void photo() {


        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        String imgName = format.format(new Date());
        values.put("_display_name", imgName);
        values.put("title", imgName);
        values.put("description", imgName);
        values.put("picasa_id", imgName);

        if(selectItem==0){
            startActivityForResult(intent2, TAKE_PICTURE);
        }else if(selectItem==1){
            startActivityForResult(intent2, TAKE_RECTIFYPICTURE);
        }else if(selectItem==2){
            startActivityForResult(intent2, TAKE_FEEDPICTURE);
        }
    }

    /**
     * 选择从相册选择或者拍照
     */
    private void show() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if(isOpen){
            imm.hideSoftInputFromWindow(CommitActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.activity_translate_in);
        view_takephoto.setAnimation(animation);
        view_takephoto.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.gc();

        if(Contant.isSign){

            if(Contant.signIndex==1){
//                img_checkperson.setVisibility(View.VISIBLE);
//                img_checkperson.setImageBitmap(Contant.personBitmap);
                grid_checkperson.setVisibility(View.VISIBLE);
                if(personSignAdapter == null){
                    personSignAdapter = new PersonSignAdapter(this,Contant.personBitmapList);
                    grid_checkperson.setAdapter(personSignAdapter);
                }else{
                    personSignAdapter.notifyDataSetChanged();
                }

            }else if(Contant.signIndex==2){
                grid_checkedObject.setVisibility(View.VISIBLE);
                if(objectSignAdapter == null){
                    objectSignAdapter = new PersonSignAdapter(this,Contant.objectBitmapList);
                    grid_checkedObject.setAdapter(objectSignAdapter);
                }else{
                    objectSignAdapter.notifyDataSetChanged();
                }
//                img_checkedobject.setVisibility(View.VISIBLE);
//                img_checkedobject.setImageBitmap(Contant.objectBitmap);
            }
        }

        if (Contant.isChooseMap) {
            text_choosedLoc.setVisibility(View.VISIBLE);
            text_choosedLoc.setText("已手动选择位置");
        }

        if (!Bimp.isCarame&&("check".equals(action)||"objectAddress".equals(action)||"checkRecordLocal".equals(action)||"feed".equals(action))) {
            if (selectItem == 0 && spotPhotoAdapter != null) {
                spotPhotos.clear();
                spotPhotos.addAll(Bimp.tempSelectBitmap0);
                spotPhotoAdapter.notifyDataSetChanged();
            } else if (selectItem == 1 && rectifyPhotoAdapter != null) {
                rectifyPhotos.clear();
                rectifyPhotos.addAll(Bimp.tempSelectBitmap0);
                rectifyPhotoAdapter.notifyDataSetChanged();
            } else if(selectItem==2 && feedPhotoAdapter!=null){
                feedphotos.clear();
                feedphotos.addAll(Bimp.tempSelectBitmap0);
                feedPhotoAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if("check".equals(action)){
            Contant.infoAttributeBeans.clear();
            Contant.infoAttributeBeans.addAll(infoList);
            Bimp.selectBitmaps.put("spotphoto", spotPhotos);
            Bimp.selectBitmaps.put("rectifyphoto", rectifyPhotos);
            Bimp.selectBitmaps.put("feedphoto", feedphotos);
        }






//        else{
//            Bimp.selectBitmaps.clear();
//            spotPhotos.clear();
//            spotPhotos = null;
//            rectifyPhotos.clear();
//            rectifyPhotos = null;
//            feedphotos.clear();
//            feedphotos = null;
//        }

    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (spotPhotos.size() < 9 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());


                    Cursor cursor = this.getContentResolver().query(data.getData(), null,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String videoPath = cursor.getString(cursor
                                .getColumnIndex("_data"));// 获取绝对路径
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(videoPath);
                        spotPhotos.add(takePhoto);
                        spotPhotoAdapter.notifyDataSetChanged();
                    }

                }
                break;
            case TAKE_RECTIFYPICTURE:
                if (rectifyPhotos.size() < 9 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());


                    Cursor cursor = this.getContentResolver().query(data.getData(), null,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String videoPath = cursor.getString(cursor
                                .getColumnIndex("_data"));// 获取绝对路径
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(videoPath);
                        rectifyPhotos.add(takePhoto);
                        rectifyPhotoAdapter.notifyDataSetChanged();
                    }

                }
                break;
            case TAKE_FEEDPICTURE:
                if (feedphotos.size() < 9 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());


                    Cursor cursor = this.getContentResolver().query(data.getData(), null,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String videoPath = cursor.getString(cursor
                                .getColumnIndex("_data"));// 获取绝对路径
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(videoPath);
                        feedphotos.add(takePhoto);
                        feedPhotoAdapter.notifyDataSetChanged();
//                        feedPhotoAdapter = new GridAdapter(this,feedphotos,9,true);
//                        grid_feedphoto.setAdapter(feedPhotoAdapter);
                    }

                }
                break;
            case TAKE_VIDEO:
                if(data!=null){
                    Uri uri = data.getData();
                    Cursor cursor = this.getContentResolver().query(uri, null, null,
                            null, null);
                    if (cursor != null && cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                        videoPath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                        cursor.close();
                        list_video.setVisibility(View.VISIBLE);

                        VideoBean videoBean = new VideoBean();
                        videoBean.setPath(videoPath);
                        String name = getFileName(videoPath);
                        videoBean.setName(name);
                        videoBean.setIsLocal(true);
                        videoBeans.add(videoBean);
                        videoListAdapter.notifyDataSetChanged();
                    }
                }

                break;
        }
    }


    /**
     * 上传视频
     */
    private void uploadVideo(){

        if(NetUtil.isNetworkAvailable(this)){


            if(progressDialog!=null&&progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            initVideoDialog();


            new Thread(new Runnable() {
                @Override
                public void run() {

                    requestVideo(0,videoBeans.size());

                }
            }).start();
        }else{
            ShowToast("无网络连接");
        }
    }

    /**
     * 请求上传video，通过getVideo方法，进行分段上传，在通过UpdateVideoHT方法整合分段是屁为一个视频
     * @param index
     * @param size
     */
    private void requestVideo(int index,int size){
        VideoBean videoBean = videoBeans.get(index);
        String path = videoBean.getPath();
        String fileName = getFileName(path);
        File file = new File(path);
        ByteArrayOutputStream ops = null;
        String currFileName = "";

        try {
            FileAccessI fileAccessI = new FileAccessI(file,0);

            int mstartnum = 0;
            long length = fileAccessI.getFileLength();
            byte[] buffer ;
            FileAccessI.Detail detail;
            int nRead = 1;
            int startNum = mstartnum;
            int i=0;
            while(startNum<length){
                i++;
                currFileName = fileName+"("+i+")";
                //设置每次传的大小
                detail = fileAccessI.getContent(startNum);
                nRead = detail.length;
                buffer = detail.b;
                ops = new ByteArrayOutputStream();
                ops.write(buffer,0,nRead);

                String bufferString = new String(Base64.encode(ops.toByteArray()));
                Map<String,String> params = new HashMap<String, String>();
                params.put("VideoName",currFileName);
                params.put("VideoStream",bufferString);
                params.put("guid",checkGuid);
                String result = RequestUtil.post(RequestUtil.GetVideo,params);
                if(result==null||"no".equals(result)||"".equals(result)){
                    Message message = Message.obtain();
                    message.what = 13;
                    message.obj = result;
                    mHandler.sendMessage(message);
                    break;
                }else{
                    long total = getbeforeVideoLength(videoBeans.size(),0);
                    long current = getbeforeVideoLength(index,startNum);
                    int progress = (int) ((current*100)/total);
                    Message message = Message.obtain();
                    message.what = 16;
                    message.obj = progress;
                    mHandler.sendMessage(message);
                    startNum+=nRead;
                }
            }
            if(startNum>=length){
                Map<String,String> params = new HashMap<String, String>();
                params.put("VideoName",fileName);
                params.put("guid",checkGuid);
                String result = RequestUtil.post(RequestUtil.UpdateVideoHT,params);
                if("yes".equals(result)){
                    index++;
                    if(index<size){
                        requestVideo(index,size);
                    }else{
                        Message message = Message.obtain();
                        message.what = 13;
                        message.obj = result;
                        mHandler.sendMessage(message);
                    }
                }else{
                    Message message = Message.obtain();
                    message.what = 13;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                ops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取已经上传的视频的length
     * @param index
     * @param mstart
     * @return
     */
    private long getbeforeVideoLength(int index,int mstart){
        long num = 0;

        for(int i=0;i<index;i++){
            VideoBean videoBean = videoBeans.get(i);
            File file = new File(videoBean.getPath());
            try {
                FileAccessI fileAccessI = new FileAccessI(file,0);
                num =  num+(int)fileAccessI.getFileLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        num = num+mstart;

        return num;
    }

    /**
     * 上传视频的dialog
     */
    private void initVideoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialogProgressBar = builder.create();
        alertDialogProgressBar.show();
        alertDialogProgressBar.setCanceledOnTouchOutside(false);
        alertDialogProgressBar.setContentView(R.layout.video_progressbar);
        saundProgressBar = (SaundProgressBar) alertDialogProgressBar.findViewById(R.id.regularprogressbar);

        saundProgressBar.setMax(100);

        Drawable indicator = getResources().getDrawable(
                R.drawable.progress_indicator);
        Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 5,
                indicator.getIntrinsicHeight());
        indicator.setBounds(bounds);

        saundProgressBar.setProgressIndicator(indicator);
        saundProgressBar.setProgress(0);
    }


    private String getCheckBranch(List<PersonBean> personBeans){
        StringBuilder sb = new StringBuilder();
        if(personBeans!=null&&personBeans.size()!=0){
            for(PersonBean personBean:personBeans){
                String branch = personBean.getBranch();
                if("".equals(sb.toString())){
                    sb.append(branch);
                }else{
                    if(!sb.toString().contains(branch)){
                        sb.append(";"+branch);
                    }
                }
            }
        }
        return sb.toString();
    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
//            case R.id.rel_allview:
//                jianpandelete();
//                break;
            case R.id.text_commit_preview:


                String type = historyBean.getBussinesstype();
//                headList = getTableHead(historyBean.getInspectItemsResultBean().getHead());
//                 brokerId = historyBean.getId();
                DXSList = historyBean.getDXSList();
                FWList = historyBean.getFWList();
//                String xqDate = historyBean.getRectifDeadline();
//                 checkGuid= historyBean.getInspectGuid();
                String inspectTime = historyBean.getCheckBaseInfoBean().getInspectTime();
//                List<CheckHeadBean>  checkHeadBeans =  historyBean.getInspectItemsResultBean().getHead();

//                String[] keys1 = null;
//                String[] names1 = null;
//                if (type.contains(getResources().getString(R.string.primebroker))) {
//                    keys1 = getResources().getStringArray(R.array.primeinfokey);
//                    names1 = getResources().getStringArray(R.array.primeinfoname);
//                } else {
//                    keys1 = getResources().getStringArray(R.array.propertyinfokey);
//                    names1 = getResources().getStringArray(R.array.propertyinfoname);
//                }

//                if(infoList==null){
//                 infoList = new ArrayList<>();
//                }else{
//                    infoList.clear();
//                }
//                infoList.addAll(getInfoList(checkHeadBeans,keys1,names1));

                String info1 = gson.toJson(infoList);
                List<CheckResultBean> resultBeans1 = historyBean.getInspectItemsResultBean().getResult();
                List<CheckTermBean> illegalTerm1 = getIllegalTerm(getTermGroups(resultBeans1));
                String illegaltermstr1 = gson.toJson(illegalTerm1);
                ArrayList<String> bussinessList1 = new ArrayList<>();
                String[] busstr1 = type.split(",");
                if (busstr1 != null && busstr1.length != 0) {
                    for (String str : busstr1) {
                        bussinessList1.add(str);
                    }
                } else {
                    bussinessList1.add(type);
                }

                String personSign = historyBean.getCHKORSIGN();
                String objectSign = historyBean.getOBJSIGN();

                if(personSign==null){
                    personSign = "";
                }

                if(objectSign==null){
                    objectSign = "";
                }
//                String inspectStr = gson.toJson(inspectBean);


                String preBranch = historyBean.getBranchs();
                Intent wordIntent1 = new Intent(CommitActivity.this, PreviewActivity.class);
                wordIntent1.setAction("checkRecord");
                wordIntent1.putExtra("info", info1);
                wordIntent1.putExtra("illegalTerm", illegaltermstr1);
                wordIntent1.putExtra("termCount", getTermSize(getTermGroups(resultBeans1)));
                wordIntent1.putExtra("xqDate", deadline);
                wordIntent1.putExtra("inspectTime", inspectTime);
                wordIntent1.putExtra("inspectGuid", checkGuid);
//                wordIntent1.putExtra("inspect",inspectStr);
                wordIntent1.putExtra("decribe", historyBean.getCheckBaseInfoBean().getDescription());
                wordIntent1.putExtra("objectName", historyBean.getName());

                wordIntent1.putExtra("DXSList", DXSList);
                wordIntent1.putExtra("FWList", FWList);
                wordIntent1.putExtra("inspectNo", historyBean.getInspectNo());
                wordIntent1.putExtra("branchs",preBranch);
                wordIntent1.putExtra("personSign",personSign);
                wordIntent1.putExtra("objectSign",objectSign);
                wordIntent1.putExtra("treatments", historyBean.getCheckBaseInfoBean().getTreatment());
                wordIntent1.putStringArrayListExtra("bussinessType", bussinessList1);
                startActivity(wordIntent1);

                break;
            case R.id.bt_commit_video:
                Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intentVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intentVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
                startActivityForResult(intentVideo,TAKE_VIDEO);
                break;
//            case R.id.img_commit_checkperson:
//
//                Intent imgIntent = new Intent(this,SignShowActivity.class);
//                imgIntent.setAction("commit");
//                imgIntent.putExtra("index",1);
//
//                startActivity(imgIntent);
//
//                break;
//            case R.id.img_commit_checkedobject:
//
//
//                Intent imgIntentobject = new Intent(this,SignShowActivity.class);
//                imgIntentobject.setAction("commit");
//                imgIntentobject.putExtra("index",2);
//
//                startActivity(imgIntentobject);
//                break;
            case R.id.bt_commit_checkperson:

                Intent intentSign = new Intent(this,SignActivity.class);
                intentSign.putExtra("index",1);
                startActivity(intentSign);

                break;
            case R.id.bt_commit_checkedobject:
                Intent intentSignobject = new Intent(this,SignActivity.class);
                intentSignobject.putExtra("index",2);
                startActivity(intentSignobject);
                break;
            case R.id.bt_commit_mapchoose:
                ShowToast("请选择一个位置后,点击返回按钮继续操作");
                Intent loactionIntent = new Intent(this,LocationMapActivity.class);
                this.startActivity(loactionIntent);
                break;
            case R.id.bt_commit_useCurLoc:
                text_loadinggps.setVisibility(View.VISIBLE);
                locationGPS();
                break;
            case R.id.include_commit_takephote:
                view_takephoto.setVisibility(View.GONE);
                break;
            case R.id.title_lefttext:
//                Bimp.selectBitmaps.put("spotphoto",spotPhotos);
//                Bimp.selectBitmaps.put("rectifyphoto", rectifyPhotos);
                finish();
                break;
            case R.id.title_righttext:
                Intent intent = new Intent(this,MainActivity.class);
                this.startActivity(intent);
                break;

            case R.id.input_popupwindows_camera:
                Bimp.isCarame = true;
                photo();
                dismiss();
                break;
            case R.id.input_popupwindows_Photo:

                Bimp.isCarame = false;
                if(selectItem==0){
                    PublicWay.num=9;
                }else if(selectItem==1){
                    PublicWay.num=3;
                }else if(selectItem==2){
                    PublicWay.num=9;
                }
                ImageUtil.sendBroadCaseRemountSDcard(CommitActivity.this);
                Intent intent_ablum = new Intent(CommitActivity.this,
                        AlbumActivity.class);
                startActivity(intent_ablum);
                dismiss();
                break;
            case R.id.input_popupwindows_cancel:
                dismiss();
                break;
            case R.id.include_takephote:
                dismiss();
                break;
            case R.id.imgbt_commit_feedupload:
                final String feedbackman = edit_feedman.getText().toString();
                if("".equals(feedbackman)){
                    ShowToast("请输入反馈人");
                }else {
                    if (NetUtil.isNetworkAvailable(this)) {
                        if (progressDialog == null) {
                            progressDialog = showProgressDialog();
                        } else {
                            progressDialog.show();
                        }

                        final String feedbackdes = edit_feedback.getText().toString();

                        final String feedbacktime = text_feeddate.getText().toString();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("guid", checkGuid);
                                params.put("feedbackdes", feedbackdes);
                                params.put("feedbackman", feedbackman);
                                params.put("feedbacktime", feedbacktime);
                                String result = RequestUtil.post(RequestUtil.InspectFeekBack, params);
                                Message msg = Message.obtain();
                                msg.what = 15;
                                msg.obj = result;
                                mHandler.sendMessage(msg);

                            }
                        }).start();
                    } else {
                        ShowToast(noNetText);
                    }
                }
                break;
            case R.id.text_notifydialog_sure:
//                notifyDate = text_notifydate.getText().toString();
//                String checkDate = text_checkdate.getText().toString();
//                if("".equals(notifyDate)){
//                    ShowToast("请选择下发日期");
//                }else{
//                    if(Util.compare_date(checkDate,notifyDate)){
                alertDialogNotify.dismiss();
//                        if("".equals(checkGuid)){
//                            if(NetUtil.isNetworkAvailable(this)) {
                inspectBean = getData();
//                                progressDialog.show();
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        String tableInfo = gson.toJson(inspectBean);
//                                        Log.i("TAG", "info" + tableInfo);
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("loginName", Contant.userid);
//                                        params.put("tableInfo", tableInfo);
//                                        params.put("x",Contant.longitube);
//                                        params.put("y",Contant.latitube);
//                                        String result = RequestUtil.post(RequestUtil.SaveNewCheckTable, params);
//                                        Message msg = Message.obtain();
//                                        msg.what = 5;
//                                        msg.obj = result;
//                                        msg.arg1=2;
//                                        mHandler.sendMessage(msg);
//                                    }
//                                }).start();
//                            }
//                        }else{
//                            requestNotify(notifyDate);
//                        }

                String checkdateweb = text_checkdate.getText().toString();
                if("".equals(streetId)){
                    ShowToast("请在对象信息下方选择对象所在街道");
                }else if(personBeans==null||personBeans.size()<2){
                    ShowToast("请至少选择两个检查人员");
                }else if(Contant.isXQZG&&!Util.compare_date(checkdateweb,deadline)){
                    ShowToast("你选择的限期整改日期在检查日期之前");
                }else {
                    inspectBean = getData();

                    boolean isHaveName = false;
                    boolean isHaveAddress = false;
                    if(bussinessType.contains("其他")||bussinessType.contains("经纪机构")){
                        isHaveName = true;
                        isHaveAddress = true;
                    }else {
                        List<CheckHeadBean> checkHeadBeans = inspectBean.getInspectItemsResult().getHead();
                        for (CheckHeadBean headBean : checkHeadBeans) {
                            if ("项目名称".equals(headBean.getTitle())) {
                                if (headBean.getValue() == null || "".equals(headBean.getValue())) {
                                    isHaveName = false;
                                } else {
                                    isHaveName = true;
                                }
                            } else if ("项目地址".equals(headBean.getTitle())) {
                                if (headBean.getValue() == null || "".equals(headBean.getValue())) {
                                    isHaveAddress = false;
                                } else {
                                    isHaveAddress = true;
                                }
                            }
                        }
                    }

                    if (!isHaveName) {
                        ShowToast("请输入项目名称");
                    } else if (!isHaveAddress) {
                        ShowToast("请输入项目地址");
                    }else if(Contant.objectBitmapList.size()==0){
                        ShowToast("请受检单位签字");
                    }else {
                        if (NetUtil.isNetworkAvailable(this)) {

                            progressDialog = null;
                            if (progressDialog == null) {
                                progressDialog = showProgressDialog();
                            } else {
                                progressDialog.show();
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    String tableInfo = gson.toJson(inspectBean);
                                    tableInfo.split(",");
//                                    if ("".equals(checkGuid)) {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("loginName", Contant.userid);
                                    params.put("tableInfo", tableInfo);
                                    params.put("x", Contant.longitube);
                                    params.put("y", Contant.latitube);
                                    String result = RequestUtil.post(RequestUtil.SaveNewCheckTable, params);
                                    Message msg = Message.obtain();
                                    msg.what = 5;
                                    msg.obj = result;
                                    msg.arg1 = 1;
                                    mHandler.sendMessage(msg);
//                                    } else {
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("inspectGuid", checkGuid);
//                                        params.put("opName", "保存");
//                                        params.put("tableInfo", tableInfo);
//                                        String result = RequestUtil.post(RequestUtil.UpdateInspectHistroyInfo, params);
//                                        Message msg = Message.obtain();
//                                        msg.what = 5;
//                                        msg.obj = result;
//                                        msg.arg1 = 3;
//                                        mHandler.sendMessage(msg);
//                                    }
                                }
                            }).start();
                        }else{
                            checkDBBean = getDBBean(inspectBean);
                            if (finalDb == null) {
                                finalDb = getFinalDb(CommitActivity.this);
                            }
                            if(checkDBBean!=null){
                                if(dbId==-1){
                                    finalDb.save(checkDBBean);
                                    List<CheckDBBean> checkDBBeans = finalDb.findAll(CheckDBBean.class, "id DESC");
                                    if(checkDBBeans!=null&&checkDBBeans.size()!=0){
                                        dbId = checkDBBeans.get(0).getId();
                                    }
                                }else{
                                    finalDb.update(checkDBBean,"id = "+dbId);
                                }
                            }
                            ShowToast("由于网络原因，上传服务器失败，已为您保存到本地，可在本地记录中查看");
                            uploadonePic("0", spotPhotos);
                        }
                    }
                }
//                    }else{
//                        ShowToast("您选择的下发日期在检查日期之前");
//                    }
//                }
                break;
            case R.id.text_notifydialog_cancel:
                alertDialogNotify.dismiss();
                break;
//            case R.id.text_notifydailog_date:
//                initDialog(3);
//                break;
            case R.id.imgbt_commit_endcheck:
                initNotifyDailog();
                break;
            case R.id.createword:

                String checkdate = text_checkdate.getText().toString();

                if(personBeans==null||personBeans.size()<2){
                    ShowToast("请至少选择两个检查人员");
                }else if(Contant.isXQZG&&!Util.compare_date(checkdate,deadline)){
                    ShowToast("你选择的限期整改日期在检查日期之前");
                }else {
                    if (infoList != null && infoList.size() != 0) {
//                        if(action.equals("check")){
//                            setDXAANDFW();
//                        }
                        inspectBean = getData();
                        checkDBBean= getDBBean(inspectBean);
                        String info = gson.toJson(infoList);
                        String illegalTerm = gson.toJson(illegalTermBeans);
                        int termCount = getTermSize();
                        String xqDate = "";
                        String inspect = gson.toJson(inspectBean);
                        String DB = gson.toJson(checkDBBean);
                        String branchs = getCheckBranch(personBeans);
                        String inspectTime1 = inspectBean.getInspectTime();

                        Intent wordIntent = new Intent(CommitActivity.this, PreviewActivity.class);
                        wordIntent.setAction("check");
                        wordIntent.putExtra("info", info);
                        wordIntent.putExtra("illegalTerm", illegalTerm);
                        wordIntent.putExtra("termCount", termCount);
                        wordIntent.putExtra("xqDate", xqDate);
                        wordIntent.putExtra("inspect",inspect);
                        wordIntent.putExtra("DB",DB);
                        wordIntent.putExtra("inspectTime",inspectTime1);
                        wordIntent.putExtra("previewDXSStr",previewDXSStr);
                        wordIntent.putExtra("previewFWStr",previewFWStr);
                        wordIntent.putExtra("inspectNo","");
                        wordIntent.putExtra("inspectGuid",checkGuid);
                        wordIntent.putExtra("branchs",branchs);
                        wordIntent.putStringArrayListExtra("bussinessType",bussinessType);
                        CommitActivity.this.startActivity(wordIntent);
                    } else {
                        ShowToast("被检对像信息缺失，请返回重新进入该界面进行加载");
                    }
                }

                break;
            case R.id.uploadtoweb:
                inspectBean = getData();
                checkDBBean = getDBBean(inspectBean);
                if (finalDb == null) {

                    finalDb = getFinalDb(CommitActivity.this);
//
                }

                if(checkDBBean!=null){

                    if(spotPhotos.size()!=0){
                        StringBuilder spotBuild = new StringBuilder();
                        for(int i=0;i<spotPhotos.size();i++){
                            if(i==0){
                                spotBuild.append(spotPhotos.get(i).getImagePath());
                            }else{
                                spotBuild.append(","+spotPhotos.get(i).getImagePath());
                            }
                        }
                        if(checkDBBean!=null){
                            checkDBBean.setSpotImgPaths(spotBuild.toString());
                        }
                    }

                    if(rectifyPhotos.size()!=0){
                        StringBuilder rectifyBuild = new StringBuilder();
                        for(int i=0;i<rectifyPhotos.size();i++){
                            if(i==0){
                                rectifyBuild.append(rectifyPhotos.get(i).getImagePath());
                            }else{
                                rectifyBuild.append(","+rectifyPhotos.get(i).getImagePath());
                            }
                        }
                        checkDBBean.setRectifyImgPath(rectifyBuild.toString());
                    }

                    if(videoBeans.size()!=0){
                        StringBuilder videoBuild = new StringBuilder();
                        for(int i=0;i<videoBeans.size();i++){
                            if(i==0){
                                videoBuild.append(videoBeans.get(i).getPath());
                            }else{
                                videoBuild.append(","+videoBeans.get(i).getPath());
                            }
                        }
                        checkDBBean.setVideoPaths(videoBuild.toString());
                    }


                    if(dbId==-1){
                        finalDb.save(checkDBBean);
                        List<CheckDBBean> checkDBBeans = finalDb.findAll(CheckDBBean.class, "id DESC");
                        if(checkDBBeans!=null&&checkDBBeans.size()!=0){
                            dbId = checkDBBeans.get(0).getId();
                        }
                    }else{
                        finalDb.update(checkDBBean, "id = " + dbId);
                    }

                    saveSignDb();
                    finalDb.update(checkDBBean, "id = " + dbId);
                }
                ShowToast("已保存到本地，在本地记录中查看");

                break;
            case R.id.text_checkresult_insertperson:
                initPersonDialog();
                break;
            case R.id.text_checkresult_date:
                initDialog(1);
                break;
            case R.id.text_commit_feeddate:
                initDialog(4);
                break;
//            case R.id.text_commit_xqzgdate:
//                initDialog(2);
//                break;
            case R.id.text_persondialog_cancel:
                personAlertDialog.dismiss();
                break;
            case R.id.text_person_more:
                requestPerson(1);
                break;
            case R.id.text_persondialog_sure:
                personAlertDialog.dismiss();
                List<PersonBean> persons = new ArrayList<>();
                for(BranchBean branchBean:personDialogBeans){
                    for(PersonBean bean :branchBean.getPersonList()){
                        if(bean.isChecked()){
                            persons.add(bean);
                        }
                    }
                }
                personBeans.clear();
                personBeans.addAll(persons);
                personAdapter.notifyDataSetChanged();
                break;
        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()){
//            case R.id.cb_xczg:
//                Contant.isXCZG = isChecked;
//                String xczg = getResources().getString(R.string.xczg);
//                if(isChecked){
//                    treatmentList.add(xczg);
//                    if(illegalTermBeans!=null&&illegalTermBeans.size()!=0){
//                        for(CheckTermBean termBean:illegalTermBeans){
//                            termBean.setIsShow(true);
//                        }
//                    }
//                    illegalAdapter.notifyDataSetChanged();
//
//                }else{
//                    treatmentList.remove(xczg);
//                    if(illegalTermBeans!=null&&illegalTermBeans.size()!=0){
//                        for(CheckTermBean termBean:illegalTermBeans){
//                            termBean.setIsShow(false);
//                        }
//                    }
//                    illegalAdapter.notifyDataSetChanged();
//                }
//                break;
//            case R.id.cb_xqzg:
//                Contant.isXQZG = isChecked;
//                String xqzg = getResources().getString(R.string.xqzg);
//                if(isChecked){
//                    treatmentList.add(xqzg);
//                    lin_xqzgopinion.setVisibility(View.VISIBLE);
//                }else{
//                    treatmentList.remove(xqzg);
//                    lin_xqzgopinion.setVisibility(View.GONE);
//                }
//                break;
//
//        }
//    }


    class updateDB implements FinalDb.DbUpdateListener{

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }


}
