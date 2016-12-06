

package com.dc.bookdemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.dc.bookdemo.beans.ArticleDetail;
import com.dc.bookdemo.mvpview.ArticleDetailView;
import com.dc.bookdemo.presenter.ArticleDetailPresenter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import frontier.db.DatabaseHelper;
import frontier.listeners.DataListener;
import frontier.net.ArticleParser;
import frontier.net.HtmlUtls;
import frontier.net.HttpFlinger;
import frontier.net.StringParser;

/**
 * 文章阅读页面,使用WebView加载文章。
 * 
 * @author mrsimple
 */
public class ArticleDetailActivity extends BaseActionBarActivity implements ArticleDetailView {

    ProgressBar mProgressBar;  //没有进行分装
    WebView mWebView;
    private String mPostId;
    private String mTitle;

    String mJobUrl;

    ArticleDetailPresenter mPresenter = new ArticleDetailPresenter();


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_detail;
    }


    @Override
    protected void initWidgets() {

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        mWebView = (WebView) findViewById(R.id.articles_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings = mWebView.getSettings();
                settings.setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    protected void afterOnCreate() {

        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null && !extraBundle.containsKey("job_url")) {
            mPostId = extraBundle.getString("post_id");
            mTitle = extraBundle.getString("title");
        } else {
            mJobUrl = extraBundle.getString("job_url");
        }

        mPresenter.attach(this); //绑定接口


        // 从数据库上获取文章内容缓存，如果没有缓存则从网络获取
        if (!TextUtils.isEmpty(mPostId)) {
            mPresenter.fetchArticleContent(mPostId, mTitle);
        } else {
            mWebView.loadUrl(mJobUrl);
        }


    }




    @Override
    public void onFetchedArticleContent(String html) {
        mWebView.loadDataWithBaseURL("", html,
                "text/html", "utf8", "404");
    }

    @Override
    public void onShowLoding() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoding() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();  //防止内存泄漏
    }
}
