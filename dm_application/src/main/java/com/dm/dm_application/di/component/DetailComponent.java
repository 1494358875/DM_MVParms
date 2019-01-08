package com.dm.dm_application.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dm.dm_application.di.module.DetailModule;
import com.dm.dm_application.mvp.contract.DetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dm.dm_application.mvp.ui.activity.DetailActivity;


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
@Component(modules = DetailModule.class, dependencies = AppComponent.class)
public interface DetailComponent {
    void inject(DetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DetailComponent.Builder view(DetailContract.View view);

        DetailComponent.Builder appComponent(AppComponent appComponent);

        DetailComponent build();
    }
}