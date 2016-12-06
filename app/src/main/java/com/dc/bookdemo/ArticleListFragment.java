/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.dc.bookdemo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dc.bookdemo.adapters.ArticleAdapter;
import com.dc.bookdemo.beans.Article;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import frontier.db.DatabaseHelper;
import frontier.listeners.DataListener;
import frontier.listeners.OnItemClickListener;
import frontier.net.ArticleParser;
import frontier.net.HttpFlinger;
import frontier.widgets.AutoLoadRecyclerView;

/**
 * 文章列表主界面,包含自动滚动广告栏、文章列表
 * 
 * @author mrsimple
 */
public class ArticleListFragment extends Fragment implements OnRefreshListener,
        AutoLoadRecyclerView.OnLoadListener {

    protected ArticleAdapter mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout; //刷新控件
    protected AutoLoadRecyclerView mRecyclerView;

    private int mPageIndex = 1;

    ArticleParser mArticleParser = new ArticleParser(); //json解析器 ，解析网络返回


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        initRefreshView(rootView);

        initAdapter();

        mSwipeRefreshLayout.setRefreshing(true); //刷新
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadArticlesFromDB();
    }


    private void loadArticlesFromDB() {
        mAdapter.addItems(DatabaseHelper.getInstance().loadArticles());
    }

    /**
     * 初始化控件
     * @param rootView
     */
    protected void initRefreshView(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (AutoLoadRecyclerView) rootView.findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                .getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);
    }

    protected void initAdapter() {
        mAdapter = new ArticleAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {

            @Override
            public void onClick(Article article) {
                if (article != null) {
                    jumpToDetailActivity(article);
                }
            }
        });
        // 设置Adapter
        mRecyclerView.setAdapter(mAdapter);

        fetchArticles(1); //加载第一页
    }




    private void fetchArticles(final int page) {
        mSwipeRefreshLayout.setRefreshing(true);

        HttpFlinger.get(prepareRequestUrl(), mArticleParser, new DataListener<List<Article>>() {
            @Override
            public void onComplete(List<Article> result) {
                // 添加心数据
                mAdapter.addItems(result);
                mSwipeRefreshLayout.setRefreshing(false);
                // 存储文章列表
                DatabaseHelper.getInstance().saveArticles(result);
                if (result.size() > 0) {
                    mPageIndex++;
                }
            }
        });
    }

    private String prepareRequestUrl() {
        return "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex
                + "&count=20&category=1";
    }

    protected void jumpToDetailActivity(Article article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        fetchArticles(1);
    }

    @Override
    public void onLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        fetchArticles(mPageIndex);
    }
}
