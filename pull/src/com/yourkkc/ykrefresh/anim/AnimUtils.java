package com.yourkkc.ykrefresh.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yourkkc.ykrefresh.bean.ViewDuration;


/**
 * Created by Administrator on 2017/11/6.
 */
public class AnimUtils {

    public static final int TYPE_SCALE = 1;
    public static final int TYPE_ALPHA = 2;
    public static void animIn(int type,ViewDuration... vds){
        switch (type){
            case TYPE_ALPHA:
                for(ViewDuration vd : vds){
                    alphaIn(vd);
                }
                break;
            case TYPE_SCALE:
                for(ViewDuration vd : vds){
                    scaleIn(vd);
                }
                break;
        }
    }
    public static void animOut(int type,ViewDuration... vds){

        switch (type){

            case TYPE_ALPHA:
                for(ViewDuration vd : vds){
                    alphaOut(vd);
                }
                break;
            case TYPE_SCALE:
                for(ViewDuration vd : vds){
                    scaleOut(vd);
                }
                break;
        }

    }
    //缩放动画--进入
    private static void scaleIn(ViewDuration vd){
        ObjectAnimator oa = ObjectAnimator.ofFloat(vd.getView(),"scaleX",0,1);
        ObjectAnimator ob = ObjectAnimator.ofFloat(vd.getView(),"scaleY",0,1);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.setDuration(vd.getDuration());
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(oa).with(ob);
        animatorSet.start();
    }
    //缩放动画--进入
    private static void scaleOut(ViewDuration vd){
        ObjectAnimator oa = ObjectAnimator.ofFloat(vd.getView(),"scaleX",1,0);
        ObjectAnimator ob = ObjectAnimator.ofFloat(vd.getView(),"scaleY",1,0);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.setDuration(vd.getDuration());
        animatorSet.setInterpolator(new AnticipateInterpolator());
        animatorSet.play(oa).with(ob);
        animatorSet.start();

    }



    //淡入动画
    private static void alphaIn(ViewDuration vd){
        ObjectAnimator oa = ObjectAnimator.ofFloat(vd.getView(),"alpha",0,1);
        oa.setDuration(vd.getDuration());
        oa.start();
    }

    //淡入动画
    private static void alphaOut(ViewDuration vd){
        ObjectAnimator oa = ObjectAnimator.ofFloat(vd.getView(),"alpha",1,0);
        oa.setDuration(vd.getDuration());
        oa.start();
    }
}
