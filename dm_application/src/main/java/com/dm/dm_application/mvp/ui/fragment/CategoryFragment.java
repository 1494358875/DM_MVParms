package com.dm.dm_application.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dm.dm_application.R;
import com.dm.dm_application.app.ARouterPaths;
import com.dm.dm_application.app.EventBusTags;
import com.dm.dm_application.app.base.BaseFragment;
import com.dm.dm_application.di.component.DaggerCategoryComponent;
import com.dm.dm_application.mvp.contract.CategoryContract;
import com.dm.dm_application.mvp.model.entity.GankEntity;
import com.dm.dm_application.mvp.presenter.CategoryPresenter;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.parceler.Parcels;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class CategoryFragment extends BaseFragment<CategoryPresenter> implements CategoryContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String type;
    private boolean isLoadingMore;
    private Paginate mPaginate;

    public static CategoryFragment newInstance(String type) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCategoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        type = getArguments().getString("type");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        //设置列表刷新
        Observable.just(1)//返回一个可观察对象，该对象发出给定(常量引用)项的信号，然后完成
                .observeOn(AndroidSchedulers.mainThread())//修改ObservableSource以在指定的{@link调度器}上执行它的排放和通知
                .subscribe(integer -> mSwipeRefreshLayout.setRefreshing(true));//订阅ObservableSource并提供回调来处理它发出的项。
    }

    @Override
    public void hideLoading() {
        //取消列表刷新
        mSwipeRefreshLayout.setRefreshing(false);
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

    }

    /**
     * 下拉刷新事件
     */
    @Override
    public void onRefresh() {
        mPresenter.requestDate(type, true);
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    @Override
    public void soptLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, viewType, data, position) -> {
            GankEntity.ResultsBean bean = (GankEntity.ResultsBean) data;
            ARouter.getInstance()
                    .build(ARouterPaths.MAIN_DETAIL)
                    .withParcelable(EventBusTags.EXTRA_DETAIL, Parcels.wrap(bean))
                    .navigation();
        });
        initPaginate();
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                /**
                 * 载入下一页资料(例如网络或资料库)
                 */
                @Override
                public void onLoadMore() {
                    mPresenter.requestDate(type, false);
                }

                /**
                 * 指示新页面加载是否正在进行
                 * @return
                 */
                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                /**
                 * 指示是否加载了所有数据(页)
                 * @return
                 */
                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };
            //在可回收视图上创建分页功能
            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    //设置从需要触发加载更多事件的列表末尾开始的偏移量
                    .setLoadingTriggerThreshold(0)
                    .build();
            //是否需要加载更多数据
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        this.mPaginate = null;
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
        mPresenter.requestDate(type, true);
    }
}
