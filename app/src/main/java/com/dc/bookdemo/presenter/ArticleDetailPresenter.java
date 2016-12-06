package com.dc.bookdemo.presenter;

import android.text.TextUtils;

import com.dc.bookdemo.beans.ArticleDetail;
import com.dc.bookdemo.mvpview.ArticleDetailView;

import frontier.db.DatabaseHelper;
import frontier.listeners.DataListener;
import frontier.net.HtmlUtls;
import frontier.net.HttpFlinger;

/**
 *  文章详情页面的Presenter,负责加载文章内容。如果数据库中有缓存，那么使用缓存，否则从网络上下载内容到本地，并存储。
 */

public class ArticleDetailPresenter extends BasePresenter<ArticleDetailView>  {

    /**
     * 加载文章的具体内容,先从数据库中加载,如果数据库中有，那么则不会从网络上获取
     *
     * @param postId
     */
    public void fetchArticleContent(final String postId,String title) {

        String articleContent = loadArticleContentFromDB(postId);

        if (!TextUtils.isEmpty(articleContent)) {
            String htmlContent = HtmlUtls.wrapArticleContent(title, articleContent);
            mView.onFetchedArticleContent(htmlContent); //接口传递
        } else {
            fetchContentFromServer(postId, title);
        }
    }


    public String loadArticleContentFromDB(String postId) {

        return DatabaseHelper.getInstance().loadArticleDetail(postId).content;
    }


    protected void fetchContentFromServer(final String postId,final String title) {
        mView.onShowLoding(); //接口，开始加载

        String reqURL = "http://www.devtf.cn/api/v1/?type=article&post_id=" + postId;

        HttpFlinger.get(reqURL,
                new DataListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        mView.onHideLoding();

                        if (TextUtils.isEmpty(result)) { //防止数据获取失败
                            result = "未获取到文章内容~";
                        }

                        mView.onFetchedArticleContent(HtmlUtls.wrapArticleContent(title, result));
                        DatabaseHelper.getInstance().saveArticleDetails(
                                new ArticleDetail(postId, result));
                    }
                });
    }




}
