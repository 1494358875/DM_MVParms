package com.dm.dm_application.di.module;

import dagger.Binds;
import dagger.Module;

import com.dm.dm_application.mvp.contract.CollectContract;
import com.dm.dm_application.mvp.model.CollectModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/26/2018 10:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class CollectModule {

    @Binds
    abstract CollectContract.Model bindCollectModel(CollectModel model);
}