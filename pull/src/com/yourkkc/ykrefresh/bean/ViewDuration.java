package com.yourkkc.ykrefresh.bean;

import android.view.View;

/**
 * Created by Administrator on 2017/11/7.
 */
public class ViewDuration {

    private View view;
    private long duration;




    public ViewDuration(View view, long duration){
        this.view = view;
        this.duration = duration;
    }


    public long getDuration() {
        return duration;
    }

    public View getView() {
        return view;
    }
}
