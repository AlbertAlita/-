package com.taian.floatingballmatrix.fragment;
/*
 Created by baotaian on 2020/5/19 0019.
*/


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.taian.floatingballmatrix.BR;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.SettingActivity;
import com.taian.floatingballmatrix.base.BaseFragment;
import com.taian.floatingballmatrix.bus.RxBus;
import com.taian.floatingballmatrix.bus.RxSubscriptions;
import com.taian.floatingballmatrix.databinding.FragmentSettingBinding;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.view.SelectSocketModeDialog;
import com.taian.floatingballmatrix.viewmodel.SettingViewModel;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SettingFragment extends BaseFragment<FragmentSettingBinding, SettingViewModel> implements SelectSocketModeDialog.OnViewClickListenter {
    private SelectSocketModeDialog dialog;
    private Disposable subscribe;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_setting;
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
                dialog = new SelectSocketModeDialog(getActivity());
                dialog.setOnViewClickListenter(SettingFragment.this);
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
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null) RxSubscriptions.remove(subscribe);
    }
}
