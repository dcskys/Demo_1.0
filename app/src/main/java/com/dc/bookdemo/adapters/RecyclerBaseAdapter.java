package com.dc.bookdemo.adapters;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dc.bookdemo.beans.MenuItem;

import java.util.ArrayList;
import java.util.List;

import frontier.listeners.OnItemClickListener;

/**
 * 重构第一步 ：  相似功能的adapter   使用泛型
 * 适用于RecyclerView的抽象Adapter，封装了数据集、ViewHolder的创建与绑定过程,简化子类的操作
 *
 *  @param <D> 数据集中的类型，例如Article等
 * @param <V> ViewHolder类型
 *
 */
public  abstract   class RecyclerBaseAdapter<D,V extends ViewHolder> extends Adapter<V> {

    protected  final List<D>  mDataSet = new ArrayList<D>();

    private OnItemClickListener<D> mItemClickListener; //接口

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    protected D getItem(int position) {
        return mDataSet.get(position);
    }


    public void addItems(List<D> items) {
        // 移除已经存在的数据,避免数据重复
        items.removeAll(mDataSet) ;
        // 添加新数据
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    /*
    * 绑定数据,主要分为两步,绑定数据与设置每项的点击事件处理
    * @see
    * android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android
    * .support.v7.widget.RecyclerView.ViewHolder, int)
    */
    @Override
    public final void onBindViewHolder(V viewHolder, int position) {
        final D item = getItem(position);
        bindDataToItemView(viewHolder, item); //把具体的数据呈现抽象出去
        setupItemViewClickListener(viewHolder, item);
    }


    public void setOnItemClickListener(OnItemClickListener<D> mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * ItemView的点击事件
     *
     * @param viewHolder
     * @param item
     */
    protected void setupItemViewClickListener(V viewHolder, final D item) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(item);
                }
            }
        });
    }


    /**
     * 将数据绑定到ItemView上
     */
    protected abstract void bindDataToItemView(V viewHolder, D item);


    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

}
