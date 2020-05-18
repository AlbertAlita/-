package com.taian.floatingballmatrix;

import android.os.Bundle;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.databinding.ActivitySettingBinding;
import com.taian.floatingballmatrix.viewmodel.SettingViewModel;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        binding.headerView.setText(R.id.header_title, getString(R.string.setting));
        binding.container.addView(viewModel.initButton());
    }
}
