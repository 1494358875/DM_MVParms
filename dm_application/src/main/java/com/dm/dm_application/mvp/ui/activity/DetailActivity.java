package com.dm.dm_application.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dm.dm_application.R;
import com.dm.dm_application.app.ARouterPaths;
import com.dm.dm_application.app.EventBusTags;
import com.dm.dm_application.di.component.DaggerDetailComponent;
import com.dm.dm_application.mvp.contract.DetailContract;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.dm.dm_application.mvp.presenter.DetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import org.parceler.Parcels;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
@Route(path = ARouterPaths.MAIN_DETAIL)
public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View {
    @BindView(R.id.imageView)
    ImageView imageView;
    private GankEntity.ResultsBean entity;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        entity=Parcels.unwrap(getIntent().getExtras().getParcelable(EventBusTags.EXTRA_DETAIL));
        mPresenter.getGirl();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setData(String url) {
        ArmsUtils.obtainAppComponentFromContext(this).imageLoader().loadImage(this,
                ImageConfigImpl
                        .builder()
                        .url(url)
                        .imageView(imageView)
                        .build());
    }

    @Override
    public void onFavoriteChange(boolean isFavorite) {

    }
}
