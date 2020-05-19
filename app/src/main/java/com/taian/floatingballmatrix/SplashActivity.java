package com.taian.floatingballmatrix;
/*
 Created by baotaian on 2020/5/19 0019.
*/


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.databinding.ActivitySplashBinding;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, BaseViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        handler.sendEmptyMessageDelayed(0,1500);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler =null;
    }
}
