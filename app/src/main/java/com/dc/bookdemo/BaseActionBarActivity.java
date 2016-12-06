package com.dc.bookdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 *重构2  消除  toolbar的 重复使用
 *
 * 提取一个公共类   封装activity 初始化流程
 *
 */

public abstract  class BaseActionBarActivity  extends AppCompatActivity {

    protected Toolbar mToolbar;


     //final  不让被继承
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewResId());

        setupToolbar();
        initWidgets();
        afterOnCreate();

    }

    /**
     * 获取Activity的布局id
     *
     * @return
     */
    protected abstract int getContentViewResId();

    /**
     * 初始化toolbar
     */
    protected void setupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected void initWidgets() {
    }

    protected void afterOnCreate() {
    }

}
