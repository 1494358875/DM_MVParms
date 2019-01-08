package com.dm.dm_application.mvp.model;

import android.app.Application;

import com.dm.dm_application.mvp.contract.DetailContract;
import com.dm.dm_application.mvp.model.api.service.CommonService;
import com.dm.dm_application.mvp.model.entity.DaoGankEntity;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/28/2018 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class DetailModel extends BaseModel implements DetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<GankEntity> getRandomGirl() {
        return mRepositoryManager.obtainRetrofitService(CommonService.class)
                .getRandomGirl();
    }

    @Override
    public List<DaoGankEntity> queryById(String id) {
        return null;
    }

    @Override
    public void removeById(String id) {

    }

    @Override
    public void addGankEntity(DaoGankEntity entity) {

    }
}