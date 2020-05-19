package com.taian.floatingballmatrix.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.facotry.SocketFactory;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.taian.floatingballmatrix.utils.Utils;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxSPTool;
import com.tamsiree.rxkit.RxTool;

import androidx.annotation.NonNull;

public class BaseApplication extends Application {
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        setApplication(this);
        SocketFactory.resetSocket();

        String setting = RxSPTool.getString(this, Constant.SETTING);
        if (!TextUtils.isEmpty(setting)) {
            SettingEntity entity = GsonUtil.fromJson(setting, SettingEntity.class);
            entity.setConnecString(getString(R.string.connect));
            entity.setConnecStatus(SettingEntity.DISCONNECT);
            RxSPTool.putString(this, Constant.SETTING, GsonUtil.toJson(entity));
        }
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    public static synchronized void setApplication(@NonNull Application application) {
        sInstance = application;
        Utils.init(application);
        //初始化工具类
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                RxActivityTool.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                RxActivityTool.removeActivity(activity);
            }
        });
    }

    /**
     * 获得当前app运行的Application
     */
    public static Application getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }
}
