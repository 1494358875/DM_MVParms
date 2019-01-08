package com.dm.dm_application.mvp.model;

import android.app.Application;

import com.dm.dm_application.mvp.contract.CategoryContract;
import com.dm.dm_application.mvp.model.api.service.CommonService;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/27/2018 15:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class CategoryModel extends BaseModel implements CategoryContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    public static final int USERS_PER_PAGESIZE = 10;
    @Inject
    public CategoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<GankEntity> gank(String type, String page) {
        return mRepositoryManager.obtainRetrofitService(CommonService.class)
                .gank(type, USERS_PER_PAGESIZE, page);
    }
}