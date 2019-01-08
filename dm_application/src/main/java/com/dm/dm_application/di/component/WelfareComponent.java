package com.dm.dm_application.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dm.dm_application.di.module.WelfareModule;
import com.dm.dm_application.mvp.contract.WelfareContract;

import com.jess.arms.di.scope.FragmentScope;
import com.dm.dm_application.mvp.ui.fragment.WelfareFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/26/2018 10:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = WelfareModule.class, dependencies = AppComponent.class)
public interface WelfareComponent {
    void inject(WelfareFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        WelfareComponent.Builder view(WelfareContract.View view);

        WelfareComponent.Builder appComponent(AppComponent appComponent);

        WelfareComponent build();
    }
}