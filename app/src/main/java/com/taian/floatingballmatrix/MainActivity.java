package com.taian.floatingballmatrix;


import android.content.Intent;
import android.os.Bundle;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.databinding.ActivityMainBinding;
import com.taian.floatingballmatrix.viewmodel.MainViewModel;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE) {
            viewModel.setOnItemChanged();
        }
    }
}
