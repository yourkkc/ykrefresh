package com.yourkkc.ykrefresh.factory;


import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yourkkc.ykrefresh.adapter.YKBaseAdapter;
import com.yourkkc.ykrefresh.anim.AnimUtils;
import com.yourkkc.ykrefresh.bean.ViewDuration;
import com.yourkkc.ykrefresh.ilistener.YKQueryListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yourkkc on 2017/11/7.
 */
public class YKRefreshModelFactory<E> implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private int page;
    private boolean nextMode;//翻页模式
    private PullToRefreshListView lv;
    private YKQueryListener mQueryListener;
    private List<E> data;
    private int dataCount;
    private YKBaseAdapter adapter;
    private View endView;
    private Activity context;
    private View returnTop;
    private long delayDismissTip = 200;
    private int everyPage = 20;
    private int animStyle = AnimUtils.TYPE_SCALE;
    private long animDuration = 1000;
    private int currentFirstItemIndex;

    /**
     * 构造方法
     *
     * @param lv        传入的PullToRefreshListView
     * @param adapter   需要传入YKBaseAdapter实现的子类
     * @param endLayout 当ListView显示最后一条数据的时候应该显示的背景layout
     * @param context   上下文对象
     */
    public YKRefreshModelFactory(PullToRefreshListView lv, YKBaseAdapter adapter, int endLayout, Activity context) {
        this.lv = lv;
        this.adapter = adapter;
        this.data = this.adapter.getData();
        this.lv.setAdapter(adapter);
        this.lv.setScrollingWhileRefreshingEnabled(true);
        this.context = context;
        this.lv.setOnRefreshListener(this);
        this.endView = LayoutInflater.from(context).inflate(endLayout, null);
        this.lv.getRefreshableView().addFooterView(endView);
        initScroll();
        reset();
    }

    /**
     * 构造方法
     *
     * @param lv        传入的PullToRefreshListView
     * @param adapter   需要传入YKBaseAdapter实现的子类
     * @param endLayout 当ListView显示最后一条数据的时候应该显示的背景layout
     * @param context   上下文对象
     * @param returnTop 如果需要返回顶部功能，传入该组件的view视图即可
     */
    public YKRefreshModelFactory(PullToRefreshListView lv, YKBaseAdapter adapter, int endLayout, Activity context, View returnTop) {
        this.lv = lv;
        this.adapter = adapter;
        this.data = this.adapter.getData();
        this.lv.setAdapter(adapter);
        this.lv.setScrollingWhileRefreshingEnabled(true);
        this.context = context;
        this.lv.setOnRefreshListener(this);
        this.endView = LayoutInflater.from(context).inflate(endLayout, null);
        this.lv.getRefreshableView().addFooterView(endView);
        this.returnTop = returnTop;
        setReturnTopBtn();
        initScroll();
        reset();
    }

    /**
     * 重置
     */
    private void reset() {
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        if (data == null) {
            data = new ArrayList<E>();
        } else {
            if (!nextMode) {
                data.clear();
            }
        }
    }

    /**
     * 设置载入提示延时消失的时间
     * @param delayDismissTip ms
     */
    public void setDelayDismissTip(long delayDismissTip) {
        this.delayDismissTip = delayDismissTip;
    }

    /**
     * 设置动画类型
     * @param animStyle int
     */
    public void setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
    }
    /**
     * 设置动画时长
     * @param animDuration ms
     */
    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }
    /**
     * 设置每一页的数据数量
     * @param everyPage
     */
    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public PullToRefreshListView getLv() {
        return lv;
    }

    /**
     * 翻页
     */
    public void nextPage() {
        this.page = this.page + 1;
    }


    public int getDataCount() {
        return dataCount;
    }

    /**
     * 设置数据总条数
     * @param dataCount 总条数
     */
    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }
    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public YKQueryListener getmQueryListener() {
        return mQueryListener;
    }
    /**
     * 设置查询数据的监听
     * @param mQueryListener
     */
    public void setmQueryListener(YKQueryListener mQueryListener) {
        this.mQueryListener = mQueryListener;
    }

    /**
     * 设置返回顶部 监听事件
     */
    private void setReturnTopBtn() {
        this.returnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.getRefreshableView().smoothScrollToPosition(0);
            }
        });
    }

    /**
     * 刷新列表
     */
    public void flushData() {
        adapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(delayDismissTip);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lv.isRefreshing()) {
                            lv.onRefreshComplete();
                        }
                    }
                });
            }
        }).start();
    }

    private void initScroll() {
        this.lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                setSingleDirectionMode(true);
            }
        });

        this.lv.setOnScrollListener(new AbsListView.OnScrollListener() {


            private boolean isReturnTopShow = false;
            private boolean isBottom;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (!YKRefreshModelFactory.this.getLv().isRefreshing()) {//没有刷新或者加载更多时，进行显示
                        if (data != null && data.size() < dataCount) {
                            setSingleDirectionMode(false);
                        } else {
                            setSingleDirectionMode(true);
                        }
                    } else {
                        setSingleDirectionMode(false);
                    }
                }
                if (YKRefreshModelFactory.this.getData().size() == 0) {
                    setSingleDirectionMode(true);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                currentFirstItemIndex = firstVisibleItem;

                if (returnTop != null) {
                    if (firstVisibleItem == 0) {
                        AnimUtils.animOut(animStyle, new ViewDuration(returnTop, animDuration));
                        isReturnTopShow = false;
                    } else {
                        if (!isReturnTopShow) {
                            returnTop.setVisibility(View.VISIBLE);
                            AnimUtils.animIn(animStyle, new ViewDuration(returnTop, animDuration));
                            isReturnTopShow = true;
                        }
                    }
                }

                //  判断是否在屏幕底部
                if ((firstVisibleItem + visibleItemCount) >= (totalItemCount)) {//由于本身底部自带一个feetView和一个顶部所以要减去1
                    isBottom = true;
                    if (!lv.isRefreshing()) {//没有刷新或者加载更多时，进行显示
                        if (data != null && data.size() < dataCount) {
                            setSingleDirectionMode(false);
                        } else {
                            setSingleDirectionMode(true);
                        }
                    } else {

                    }
                } else {
                    isBottom = false;
                    setSingleDirectionMode(false);
                }
                if (YKRefreshModelFactory.this.getData().size() == 0) {
                    setSingleDirectionMode(true);
                } else {

                }
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {//下滑
        nextMode = false;//刷新模式
        page = 0;//当前页码归零
        if (mQueryListener != null) {
            reset();
            mQueryListener.query();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {//上滑
        if (data != null && data.size() >= dataCount) {
            setSingleDirectionMode(true);
        } else {
            nextMode = true;//翻页模式
            if (mQueryListener != null) {
                reset();
                mQueryListener.query();
            }
        }
    }

    /**
     * 设置  是否单向滑动
     *
     * @param flag
     */
    private void setSingleDirectionMode(boolean flag) {
        if (flag) {
            endView.setVisibility(View.VISIBLE);
            lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            endView.setVisibility(View.GONE);
            lv.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.lv.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.lv.getRefreshableView().setOnItemLongClickListener(listener);
    }


}
