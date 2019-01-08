package com.dm.dm_application.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dm.dm_application.di.module.CollectModule;
import com.dm.dm_application.mvp.contract.CollectContract;

import com.jess.arms.di.scope.FragmentScope;
import com.dm.dm_application.mvp.ui.fragment.CollectFragment;


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
@FragmentScope
@Component(modules = CollectModule.class, dependencies = AppComponent.class)
public interface CollectComponent {
    void inject(CollectFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CollectComponent.Builder view(CollectContract.View view);

        CollectComponent.Builder appComponent(AppComponent appComponent);

        CollectComponent build();
    }
}