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

public class MainActivity extends BaseActionBarActivity {


    protected FragmentManager mFragmentManager;
    Fragment mArticleFragment = new ArticleListFragment();
    Fragment mAboutFragment;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mMenuRecyclerView;
    protected Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }


    //重写父类的方法
    @Override
    protected void initWidgets() {
        mFragmentManager = getFragmentManager();
        setupDrawerToggle();
        setupMenuRecyclerView();

        // 显示文章列表Fragment
        addFragment(mArticleFragment);

    }


    private void setupDrawerToggle() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    private void setupMenuRecyclerView() {
        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // 初始化菜单Adapter
        MenuAdapter menuAdapter = new MenuAdapter();
        menuAdapter.addItems(prepareMenuItems());
        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mMenuRecyclerView.setAdapter(menuAdapter);
    }


    private List<MenuItem> prepareMenuItems() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(getString(R.string.article), R.drawable.home));
        menuItems.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        menuItems.add(new MenuItem(getString(R.string.exit), R.drawable.exit));
        return menuItems;
    }


    protected void addFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().add(R.id.articles_container, fragment).commit();
    }

    protected void replaceFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.articles_container, fragment).commit();
    }




    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home: // 全部
                replaceFragment(mArticleFragment);
                break;

            case R.drawable.about: // 招聘信息
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
                replaceFragment(mAboutFragment);
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
