package com.dm.dm_application.mvp.presenter;

import android.app.Application;

import com.dm.dm_application.mvp.contract.CategoryContract;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.dm.dm_application.mvp.ui.adapter.CategoryAdapter;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
public class CategoryPresenter extends BasePresenter<CategoryContract.Model, CategoryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    List<GankEntity.ResultsBean> mDate =new ArrayList<>();
    private CategoryAdapter adapter;
    private int lastUserId;

    private int preEndIndex;
    @Inject
    public CategoryPresenter(CategoryContract.Model model, CategoryContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void requestDate(String type, boolean pullToRefresh){
        if (adapter == null) {
            adapter = new CategoryAdapter(mDate);
            //设置Adapter
            mRootView.setAdapter(adapter);
        }

        if (pullToRefresh){
            //上拉刷新默认只请求第一页
            lastUserId = 1;
        }else {
            lastUserId++;
        }
        mModel.gank(type,String.valueOf(lastUserId))
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示上拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示下拉加载更多的进度条
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏上拉刷新的进度条
                    } else {
                        mRootView.soptLoadMore();//隐藏下拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用Rxlifecycle,使Disposable和Activity一起销毁
                .subscribe(new ErrorHandleSubscriber<GankEntity>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull GankEntity gankEntity) {
                        preEndIndex = mDate.size();
                        if (pullToRefresh) {
                            mDate.clear();
                        }
                        mDate.addAll(gankEntity.results);
                        if (pullToRefresh) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.notifyItemRangeInserted(preEndIndex, gankEntity.results.size());
                        }
                    }
                });
    }
}
