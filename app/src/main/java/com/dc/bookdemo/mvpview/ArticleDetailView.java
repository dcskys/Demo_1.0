package com.dc.bookdemo.mvpview;

/**
 * Created by zcits on 2016/12/6.
 */

public interface ArticleDetailView extends MvpView {

    void onFetchedArticleContent(String html);

}
