package com.dc.bookdemo.mvpview;

import com.dc.bookdemo.beans.Article;

import java.util.List;

/**
 *
 * 展示文章列表的MVP View接口
 */

public interface ArticleListView extends MvpView {


    //从网络或数据库获取网络列表
    void onFetchedArticles(List<Article> result);

    //清空缓存数据
     void clearCacheArticles();
}

