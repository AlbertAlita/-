package com.taian.floatingballmatrix;

import android.os.Bundle;
import android.util.Log;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.databinding.ActivitySettingBinding;
import com.taian.floatingballmatrix.view.SelectSocketModeDialog;
import com.taian.floatingballmatrix.viewmodel.SettingViewModel;
import com.tamsiree.rxkit.view.RxToast;

import androidx.lifecycle.Observer;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements SelectSocketModeDialog.OnViewClickListenter {

    private SelectSocketModeDialog dialog;

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

    @Override
    public void initViewObservable() {
        viewModel.showDateDialogObservable.observe(this, (v) -> {
            if (dialog == null) {
                dialog = new SelectSocketModeDialog(SettingActivity.this);
                dialog.setOnViewClickListenter(SettingActivity.this);
                dialog.show();
            } else {
                if (!dialog.isShowing()) dialog.show();
            }
        });
        viewModel.toastObservable.observe(this, (b -> {
            if (b) RxToast.success("连接成功");
            else RxToast.warning("连w接失败");
        }));
    }

    @Override
    public void onViewClicked(boolean isUDP) {
        viewModel.setEntity(isUDP ? "UDP" : "TCP");
        if (isUDP) viewModel.initUDPCilent();
        else viewModel.initTCPCilent();
    }

}
