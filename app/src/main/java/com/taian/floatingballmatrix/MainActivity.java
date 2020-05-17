package com.taian.floatingballmatrix;


import android.os.Bundle;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.databinding.ActivityMainBinding;
import com.taian.floatingballmatrix.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
