package com.example.admin.housemanage;

import android.view.View;

import util.ActivityManage;

/**
 * Created by Administrator on 2016/4/18.
 */
public class DetailInfoActivity extends BaseActivity {
    @Override
    protected void initView() {
//        hideTitleView();
        setTitle(getResources().getString(R.string.detaiinfo));
        hideRightView();
        setContentLayout(R.layout.detailinfo_activity);
        ActivityManage.getInstance().addActivity(this);
    }

    @Override
    protected void initData() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_lefttext:
                this.finish();
                break;
        }

    }
}
