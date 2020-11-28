package com.sky.android.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by sky on 11/28/20.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<T> mItems;

    public BaseListAdapter(@NonNull Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

    public void setItems(List<T> items) {
        mItems = items;
    }

    public List<T> getItems() {
        return mItems;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems == null ? null : mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);

        if (convertView == null) {

            // 创建View
            convertView = onCreateView(mLayoutInflater, parent, viewType);

            // 创建ViewHolder
            ViewHolder<T> viewHolder = onCreateViewHolder(convertView, viewType);

            // 初始化操作
            if (viewHolder != null) viewHolder.onInitialize();

            // 保存
            convertView.setTag(viewHolder);
        }

        ViewHolder<T> viewHolder = (ViewHolder)convertView.getTag();

        if (viewHolder != null) {
            // 进行绑定
            viewHolder.mPosition = position;
            viewHolder.onBind(position, viewType);
        }

        return convertView;
    }

    /**
     * 实例显示的View
     * @param layoutInflater
     * @param parent
     * @param viewType
     * @return
     */
    public abstract View onCreateView(
            @NonNull LayoutInflater layoutInflater,
            ViewGroup parent,
            int viewType
    );

    /**
     * 初始化View
     * @param view
     * @return
     */
    public abstract ViewHolder<T> onCreateViewHolder(
            @NonNull View view, int viewType
    );


    public abstract static class ViewHolder<T> {

        protected final View mItemView;
        private final BaseListAdapter<T> mBaseListAdapter;
        private int mPosition;

        public ViewHolder(View itemView, BaseListAdapter<T> baseListAdapter) {
            mItemView = itemView;
            mBaseListAdapter = baseListAdapter;
        }

        public void onInitialize() {

        }

        /**
         * 绑定View，用于处理数据跟View进行关联
         * @param position 数据索引id
         * @param viewType View类型
         */
        public abstract void onBind(int position, int viewType);

        /**
         * 通过控件id查找相应的View
         * @param id 控件id
         * @return 返回View
         */
        public View findViewById(int id) {
            return mItemView == null ? null : mItemView.findViewById(id);
        }

        /**
         * 获取当前绑定的View
         * @return 返回View
         */
        public View getItemView() {
            return mItemView;
        }

        /**
         * 返回List的适配器
         * @return
         */
        public BaseListAdapter<T> getBaseListAdapter() {
            return mBaseListAdapter;
        }

        /**
         * 获取适配器的Item的数量
         * @return
         */
        public int getCount() {
            return mBaseListAdapter.getCount();
        }

        /**
         * 获取指定索引id的内容信息
         * @param position 索引id
         * @return 指定id的内容信息
         */
        public T getItem(int position) {
            return mBaseListAdapter.getItem(position);
        }

        /**
         * 获取适配器中数据下标
         * @return
         */
        public int getAdapterPosition() {
            return mPosition;
        }
    }
}
