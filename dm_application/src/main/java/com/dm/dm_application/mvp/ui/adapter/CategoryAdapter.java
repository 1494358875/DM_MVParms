package com.dm.dm_application.mvp.ui.adapter;

import android.view.View;

import com.dm.dm_application.R;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.dm.dm_application.mvp.ui.holder.CategoryItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

public class CategoryAdapter extends DefaultAdapter<GankEntity.ResultsBean> {
    public CategoryAdapter(List<GankEntity.ResultsBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<GankEntity.ResultsBean> getHolder(View v, int viewType) {
        return new CategoryItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_android;
    }
}
