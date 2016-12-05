package com.dc.bookdemo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dc.bookdemo.adapters.MenuAdapter;
import com.dc.bookdemo.beans.MenuItem;

import java.util.ArrayList;
import java.util.List;

import frontier.listeners.OnItemClickListener;

public class MainActivity extends AppCompatActivity {


    protected FragmentManager mFragmentManager;
    Fragment mArticleFragment = new WenzhangFragment();
    Fragment mAboutFragment;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mMenuRecyclerView;
    protected Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFragmentManager = getFragmentManager();

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


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(getString(R.string.article), R.drawable.home));
        menuItems.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        menuItems.add(new MenuItem(getString(R.string.exit), R.drawable.exit));
        MenuAdapter menuAdapter = new MenuAdapter(menuItems);

        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem item) {  //接口实现
                clickMenuItem(item);
            }
        });

        mMenuRecyclerView.setAdapter(menuAdapter);

        mFragmentManager.beginTransaction().add(R.id.articles_container, mArticleFragment)
                .commitAllowingStateLoss();

    }

    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home: // 全部
                mFragmentManager.beginTransaction()
                        .replace(R.id.articles_container, mArticleFragment)
                        .commit();
                break;

            case R.drawable.about: // 招聘信息
                if (mAboutFragment == null) {
                    mAboutFragment = new GuanyuFragment();
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.articles_container, mAboutFragment)
                        .commit();
                break;

            case R.drawable.exit: // 退出
                isQuit();
                break;

            default:
                break;
        }
    }
    private void isQuit() {
        new AlertDialog.Builder(this)
                .setTitle("确认退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", null).create().show();
    }


}
