package com.dm.dm_application.mvp.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    int a = 0;
    private GankEntity.ResultsBean entity;
    private boolean isFavorite;

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
        ArmsUtils.statuInScreen(this);
        entity = Parcels.unwrap(getIntent().getExtras().getParcelable(EventBusTags.EXTRA_DETAIL));
        mPresenter.getGirl();
        mPresenter.getQuery(entity._id);
        if (toolbar != null) {
            if (this instanceof AppCompatActivity) {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.setActionBar(this.findViewById(R.id.toolbar));
                    this.getActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        }
        //增加到收藏夹
        fab.setOnClickListener(v -> {
            if (isFavorite) {
                ArmsUtils.snackbarText("已移除收藏夹");
                mPresenter.removeById(entity);
            } else {
                ArmsUtils.snackbarText("已添加到收藏夹");
                mPresenter.addToFavorites(entity);
            }
        });

        initWebView();
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        //设置WebView是否应该启用对“视图端口”的支持
        settings.setUseWideViewPort(true);
        //设置WebView是否以overview模式加载页面
        settings.setLoadWithOverviewMode(true);
        //设置WebView是否应该支持使用屏幕缩放控件和手势进行缩放
        settings.setSupportZoom(true);
        //设置WebView是否应该使用其内置的缩放机制
        settings.setBuiltInZoomControls(true);
        //设置WebView在使用内置缩放机制时是否应该显示屏幕缩放控件
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.loadUrl(entity.url);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
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
        this.isFavorite = isFavorite;
        if (isFavorite) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.C4)));
        }
    }
}
