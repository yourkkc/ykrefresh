package com.yourkkc.ykrefresh.adapter;


import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public  abstract class YKBaseAdapter<E> extends BaseAdapter {

    protected Context context;

    protected List<E> data;
    public YKBaseAdapter(Context context, List<E> data){
        this.context = context;
        this.data = data;
    }
    public YKBaseAdapter(Context context){
        this.context = context;
        this.data = new ArrayList<E>();
    }
    @Override
    public abstract E getItem(int i);



    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public List<E> getData() {
        return data;
    }
    public void setData(List<E> data) {
        this.data = data;
    }
}
