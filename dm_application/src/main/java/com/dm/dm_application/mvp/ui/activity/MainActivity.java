package com.dm.dm_application.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.dm_application.R;
import com.dm.dm_application.app.utils.FragmentUtils;
import com.dm.dm_application.di.component.DaggerMainComponent;
import com.dm.dm_application.mvp.contract.MainContract;
import com.dm.dm_application.mvp.presenter.MainPresenter;
import com.dm.dm_application.mvp.ui.fragment.CollectFragment;
import com.dm.dm_application.mvp.ui.fragment.HomeFragment;
import com.dm.dm_application.mvp.ui.fragment.WelfareFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.dm.dm_application.app.EventBusTags.ACTIVITY_FRAGMENT_REPLACE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/24/2018 16:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@SuppressWarnings("ALL")
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    /**
     * 标题文字
     */
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    /**
     * 标题返回按钮
     */
    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    /**
     * 底部导航栏
     */
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    /**
     * 标题集合
     */
    private List<Integer> mTitles;
    /**
     * 导航栏集合
     */
    private List<Integer> mNavIds;
    /**
     * framgent集合
     */
    private List<Fragment> mFragments;
    private int mReplace=0;

    /**
     * 底部导航栏的监听器
     */
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=menuItem -> {
        switch (menuItem.getItemId()){
            case R.id.tab_home:
                mReplace=0;
                break;
            case R.id.tab_dashboard:
                mReplace=1;
                break;
            case R.id.tab_mycenter:
                mReplace=2;
                break;
            default:break;
        }
        toolbarTitle.setText(mTitles.get(mReplace));
        FragmentUtils.hideAllShowFragment(mFragments.get(mReplace));
      return true;
    };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArmsUtils.statuInScreen(this);
        //隐藏ToolBar返回键
        toolbarBack.setVisibility(View.GONE);
        //初始化标题集合
        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add(R.string.title_home);
            mTitles.add(R.string.title_dashboard);
            mTitles.add(R.string.title_mecenter);
        }
        //初始化导航栏集合
        if (mNavIds == null) {
            mNavIds = new ArrayList<>();
            mNavIds.add(R.id.tab_home);
            mNavIds.add(R.id.tab_dashboard);
            mNavIds.add(R.id.tab_mycenter);
        }
        //初始化fragment
        HomeFragment homeFragment;
        WelfareFragment welfareFragment;
        CollectFragment collectFragment;
        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            welfareFragment = WelfareFragment.newInstance();
            collectFragment = CollectFragment.newInstance();
        } else {
            mReplace = savedInstanceState.getInt(ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = (HomeFragment) FragmentUtils.findFragment(fm, HomeFragment.class);
            welfareFragment = (WelfareFragment) FragmentUtils.findFragment(fm, WelfareFragment.class);
            collectFragment = (CollectFragment) FragmentUtils.findFragment(fm, CollectFragment.class);
        }
        //初始化framgent集合
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(homeFragment);
            mFragments.add(welfareFragment);
            mFragments.add(collectFragment);
        }
        FragmentUtils.addFragments(getSupportFragmentManager(),mFragments,R.id.main_frame,0);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前Activity显示的Fragment索引
        outState.putInt(ACTIVITY_FRAGMENT_REPLACE,mReplace);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mTitles=null;
        this.mNavIds=null;
        this.mFragments=null;
    }
}
