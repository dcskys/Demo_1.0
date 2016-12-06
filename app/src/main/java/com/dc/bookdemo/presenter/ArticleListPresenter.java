package com.dc.bookdemo.presenter;

import com.dc.bookdemo.beans.Article;
import com.dc.bookdemo.mvpview.ArticleListView;

import java.util.List;

import frontier.db.DatabaseHelper;
import frontier.listeners.DataListener;
import frontier.net.ArticleParser;
import frontier.net.HttpFlinger;

/**
 * Created by dc on 2016/12/6.
 * 文章列表的Presenter,负责从网络上加载最新的文章列表。第一次加载最新文章列表时先从数据库中加载缓存，然后再从网络上加载最新的数据。
 *    mView  实际是ArticleListView  接口了
 */

public class ArticleListPresenter extends BasePresenter<ArticleListView>{

    public static final int FIRST_PAGE = 1;  //第一次加载的页数
    private int mPageIndex = FIRST_PAGE;

    ArticleParser mArticleParser = new ArticleParser();  //网络解析器

    private boolean isCacheLoaded = false;



    /**
     * 第一次先从数据库中加载缓存,然后再从网络上获取数据
     */
    public void fetchLastestArticles() {
        if (!isCacheLoaded) { //接口传递
            mView.onFetchedArticles(DatabaseHelper.getInstance().loadArticles());
        }
        // 从网络上获取最新的数据
        fetchArticlesAsync(FIRST_PAGE);
    }


    /**
     * 加载更多页
     */
    public void loadNextPageArticles() {
        fetchArticlesAsync(mPageIndex);
    }


    private void fetchArticlesAsync(final int page) {
        mView.onShowLoding(); //接口传递
        HttpFlinger.get(prepareRequestUrl(page), mArticleParser, new DataListener<List<Article>>() {
            @Override
            public void onComplete(List<Article> result) {
                mView.onHideLoding();
                if (!isCacheLoaded && result != null) {
                    mView.clearCacheArticles();//接口传递
                    isCacheLoaded = true;
                }

                if (result == null) {  //数据获取失败时需要退出
                    return;
                }
                mView.onFetchedArticles(result);//接口传递
                // 存储文章列表
                DatabaseHelper.getInstance().saveArticles(result);
                updatePageIndex(page, result);
            }
        });
    }



    /**
     * 更新下一页的索引,当请求成功且不是第一次请求最新数据时更新索引值。
     *
     * @param curPage
     * @param result
     */
    public void updatePageIndex(int curPage, List<Article> result) {
        if (result.size() > 0
                && shouldUpdatePageIndex(curPage)) {
            mPageIndex++;
        }
    }


    /**
     * 是否应该更新Page索引。更新索引值的时机有两个，一个是首次成功加载最新数据时mPageIndex需要更新;另一个是每次加载更多数据时需要更新.
     *
     * @param curPage
     * @return
     */
    private boolean shouldUpdatePageIndex(int curPage) {
        return (mPageIndex > 1 && curPage > 1)
                || (curPage == 1 && mPageIndex == 1);
    }


    private String prepareRequestUrl(int page) {
        return "http://www.devtf.cn/api/v1/?type=articles&page=" + page
                + "&count=20&category=1";
    }


    public int getPageIndex() {
        return mPageIndex;
    }


}
