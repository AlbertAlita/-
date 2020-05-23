package com.taian.floatingballmatrix;

import android.os.Bundle;
import android.util.Log;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.bus.RxBus;
import com.taian.floatingballmatrix.bus.RxSubscriptions;
import com.taian.floatingballmatrix.databinding.ActivitySettingBinding;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.view.SelectSocketModeDialog;
import com.taian.floatingballmatrix.viewmodel.SettingViewModel;
import com.tamsiree.rxkit.view.RxToast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements SelectSocketModeDialog.OnViewClickListenter {

    private SelectSocketModeDialog dialog;
    private Disposable subscribe;

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
        subscribe = RxBus.getDefault().toObservable(SettingEntity.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((settingEntity) -> {
                    binding.tvConnent.setText(settingEntity.getConnecString());
                    binding.tvConnent.setEnabled(settingEntity.isEnabled());
                    viewModel.setEntity(settingEntity);
                    if (settingEntity.getConnecStatus() == SettingEntity.CONNECTED)
                        RxToast.success("连接成功");
                    else {
                        if (!settingEntity.isClickForDisconnent())
                            RxToast.warning("连接失败");
                    }
                });
        RxSubscriptions.add(subscribe);
    }

    @Override
    public void onViewClicked(boolean isUDP) {
        viewModel.setEntity(isUDP ? "UDP" : "TCP");
        if (isUDP) viewModel.initUDPCilent();
        else viewModel.initTCPCilent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) RxSubscriptions.remove(subscribe);
    }
}
