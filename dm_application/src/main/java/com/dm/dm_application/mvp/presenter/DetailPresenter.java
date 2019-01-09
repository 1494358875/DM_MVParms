package com.dm.dm_application.mvp.presenter;

import android.app.Application;

import com.dm.dm_application.mvp.contract.DetailContract;
import com.dm.dm_application.mvp.model.entity.DaoGankEntity;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
public class DetailPresenter extends BasePresenter<DetailContract.Model, DetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private DaoGankEntity daoGankEntity;

    @Inject
    public DetailPresenter(DetailContract.Model model, DetailContract.View rootView) {
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

    public void getGirl() {
        mModel.getRandomGirl()
                //在指定的{@link调度程序}上异步订阅此ObservableSource的观察者。
                //返回默认的共享{@link调度器}实例，用于io绑定的工作。
                .subscribeOn(Schedulers.io())
                //返回一个可观察对象，该可观察对象发出的值与源可观察对象相同,否则，这个观察源将重新订阅源观察源
                //对输入值应用一些计算并返回一些其他值。重试次数 3  重试延迟秒数 2
                .retryWhen(new RetryWithDelay(3, 2))
                //在指定的{@链接调度程序}上异步订阅此ObservableSource的观察者。
                //一个{@链接调度程序}，它在Android主线程上执行操作
                .subscribeOn(AndroidSchedulers.mainThread())
                //修改可观察对象以在指定的{@链接调度程序}上执行它的排放和通知
                //一个{@链接调度程序}，它在Android主线程上执行操作
                .observeOn(AndroidSchedulers.mainThread())
                //通过对可观察对象应用一个特定的转变函数来转换它。
                //使用Rxlifecycle,使Disposable和Activity一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                //订阅
                //提供一种接收基于推送的通知的机制。
                .subscribe(new ErrorHandleSubscriber<GankEntity>(mErrorHandler) {
                    @Override
                    public void onNext(GankEntity gankEntity) {
                        mRootView.setData(gankEntity.results.get(0).url);
                    }
                });
    }

    public void getQuery(String id) {
        mRootView.onFavoriteChange(mModel.queryById(id).size() > 0);
    }

    public void removeById(GankEntity.ResultsBean entity){
        DaoGankEntity daoGankEntity = entityToDao(entity);
        mModel.removeById(daoGankEntity._id);
        mRootView.onFavoriteChange(false);
    }

    public void addToFavorites(GankEntity.ResultsBean entity){
        DaoGankEntity daoGankEntity = entityToDao(entity);
        mModel.addGankEntity(daoGankEntity);
        mRootView.onFavoriteChange(true);
    }

    private DaoGankEntity entityToDao(GankEntity.ResultsBean entity) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String format = simpleDateFormat.format(date);
        if (daoGankEntity == null) {
            daoGankEntity = new DaoGankEntity();
        }
        daoGankEntity._id=entity._id;
        daoGankEntity.createdAt=entity.createdAt;
        daoGankEntity.desc=entity.desc;
        daoGankEntity.publishedAt=entity.publishedAt;
        daoGankEntity.source=entity.source;
        daoGankEntity.type=entity.type;
        daoGankEntity.url=entity.url;
        daoGankEntity.used=entity.used;
        daoGankEntity.who=entity.who;
        daoGankEntity.addtime=format;
        return daoGankEntity;
    }


}
