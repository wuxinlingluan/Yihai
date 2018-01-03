package com.github.yihai.base.recycle;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzl on 2017/12/7.
 */

public class BaseRecycleAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    BaseRecycleAdapter.Builder builder;

    private BaseRecycleAdapter(BaseRecycleAdapter.Builder builder) {
        this.builder = builder;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        for (RecycleTypeBean typeBean : builder.typeDatas) {
            if (typeBean.getType() == viewType) {
                View view = inflater.inflate(typeBean.getLayout(), parent, false);
                try {
                    Class c = typeBean.getViewHolderClass();
                    Constructor con = null;
                    con = c.getConstructor(Context.class, View.class);
                    Object obj = con.newInstance(builder.context, view);
                    holder = (BaseViewHolder) obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return builder.itemDatas != null ? builder.itemDatas.get(position).getType() : -1;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (builder.listener != null) {
            holder.itemView.setOnClickListener(new ItemClickListener(builder, position));
        }
        holder.onBindViewHolder(position, builder.itemDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return builder.itemDatas == null ? 0 : builder.itemDatas.size();
    }

    public List<RecycleItemBean> getData() {
        return builder.itemDatas;
    }

    public void add(RecycleItemBean bean) {
        builder.itemDatas.add(bean);
        notifyDataSetChanged();
    }

    public RecycleItemBean remove(int position){
        RecycleItemBean remove = builder.itemDatas.remove(position);
        notifyDataSetChanged();
        return remove;
    }

    public void addAll(List<RecycleItemBean> bean) {
        builder.addItems(bean);
        notifyDataSetChanged();
    }

    public void setDatas(List<RecycleItemBean> beans) {
        builder.itemDatas = beans;
        notifyDataSetChanged();
    }

    public void clear() {
        builder.itemDatas.clear();
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.builder.addOnItemClickListener(onItemClickListener);
        notifyDataSetChanged();
    }


    public static class Builder {
        private Context context;
        public List<RecycleItemBean> itemDatas;
        public List<RecycleTypeBean> typeDatas;
        public OnItemClickListener listener;

        public Builder(Context context) {
            this.context = context;
            itemDatas = new ArrayList<>();
            typeDatas = new ArrayList<>();
        }

        public Builder(Context context, List<RecycleTypeBean> typeDatas) {
            this(context);
            this.context = context;
            this.typeDatas = typeDatas;
        }

        public <VH extends BaseViewHolder> Builder addType(
                int type, @LayoutRes int layout, Class<VH> viewHolderClass) {
            RecycleTypeBean<VH> typeBean = new RecycleTypeBean<>();
            typeBean.setType(type);
            typeBean.setLayout(layout);
            typeBean.setViewHolderClass(viewHolderClass);
            return addType(typeBean);
        }

        public Builder addType(RecycleTypeBean typeBean) {
            if (typeBean != null) {
                typeDatas.add(typeBean);
            }
            return this;
        }
        public <T> Builder addItem(int type, T data) {
            RecycleItemBean<T> itemBean = new RecycleItemBean<>();
            itemBean.setType(type);
            itemBean.setData(data);
            return addItem(itemBean);
        }

        public Builder removeItem(int position) {
            if (itemDatas != null) {
                itemDatas.remove(position);
            }
            return this;
        }

        public Builder addItem(RecycleItemBean itemBean) {
            if (itemBean != null) {
                itemDatas.add(itemBean);
            }
            return this;
        }

        public Builder addItems(List<RecycleItemBean> itemBean) {
            if (itemBean != null) {
                itemDatas.addAll(itemBean);
            }
            return this;
        }

        public Builder addOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public BaseRecycleAdapter build() {
            return new BaseRecycleAdapter(this);
        }
    }

    class ItemClickListener implements View.OnClickListener {
        Builder builder;
        int position;

        ItemClickListener(Builder builder, int position) {
            this.builder = builder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            builder.listener.onItemClick(position, builder.itemDatas.get(position));
        }
    }

}
