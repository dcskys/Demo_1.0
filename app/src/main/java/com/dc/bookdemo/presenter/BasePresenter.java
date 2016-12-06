package com.dc.bookdemo.presenter;

import com.dc.bookdemo.mvpview.MvpView;

/**
 * Created by dc on 2016/12/6.
 */

public abstract class BasePresenter<T extends MvpView> {

    protected T mView;  //这里实际绑定的  其实是接口

    public void attach(T view) {
        mView = view;
    }

    public void detach() {
        mView = null;
    }

}
