package com.hb.basemodel.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * create by hb
 * activity 管理类
 */
public class AppManager {
    public static Stack<WeakReference<Activity>> activityStack;
    public volatile static AppManager instance = new AppManager();

    public static AppManager init(){
        if(instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    public AppManager(){
        activityStack = new Stack<>();
    }

    /**
     * 把activity 压入栈
     * @param activity
     */
    public void addActivity(Activity activity){
        activityStack.add(new WeakReference<>(activity));
    }

    /**
     * 获取当前的activity
     */
    public static Activity currentActivity() {
        return activityStack.peek().get();
    }

    /**
     * 通过cls来获取Activity的实例
     */
    public Activity returnToActivity(Class<?> cls){
        Activity activity = null;
        for(WeakReference<Activity> act:activityStack){
            if(act.get().getClass() == cls){
                activity = act.get();
                break;
            }
        }
        return activity;
    }

    /**
     * 获取栈顶的activity并移除
     */
    public Activity popAndRemoveActivity(){
        try{
            return activityStack.pop().get();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 移除activity的实例
     * @param activity
     */
    public void removeActivity(Activity activity){
        activityStack.remove(activity);
    }

    /**
     * 移除并删除activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if(activity != null){
            activityStack.remove(activity);
            activity.finish();
        }
    }
    /**
     * 检查弱引用,则冲栈里面删除掉
     */
    public void killActivity(Activity activity){
        try {
            Iterator<WeakReference<Activity>> iterator = activityStack.iterator();
            while (iterator.hasNext()){
                WeakReference<Activity> stackActivity = iterator.next();
                Activity act = stackActivity.get();
                if(act == null){
                    iterator.remove();
                    continue;
                }
                if(act.getClass().getName().equals(activity.getClass().getName())){
                    iterator.remove();
                    act.finish();
                }
            }
        }catch (Exception e){
            LoggerUtil.e(e.getMessage());
        }
    }

    /**
     * 通过cls移除activity
     */
    public void finishActivity(Class<?> cls){
        try {
            ListIterator<WeakReference<Activity>> listIterator = activityStack.listIterator();
            while (listIterator.hasNext()){
                Activity activity = listIterator.next().get();
                if(activity == null){
                    listIterator.remove();
                    continue;
                }
                if(activity.getClass() == cls){
                    listIterator.remove();
                    if(activity != null){
                        activity.finish();
                    }
                }
            }
        }catch (Exception e){
            LoggerUtil.e(e.getMessage());
        }
    }

    /**
     * 关闭所有的activity
     */
    public void finishAllActivity(){
        try {
            ListIterator<WeakReference<Activity>> listIterator = activityStack.listIterator();
            while (listIterator.hasNext()){
                Activity activity = listIterator.next().get();
                if(activity != null){
                    activity.finish();
                }
                listIterator.remove();
            }
        }catch (Exception e){
            LoggerUtil.e(e.getMessage());
        }
    }

}
