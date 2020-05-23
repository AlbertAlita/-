package com.taian.floatingballmatrix;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.bus.RxBus;
import com.taian.floatingballmatrix.bus.RxSubscriptions;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.databinding.ActivityMainBinding;
import com.taian.floatingballmatrix.entity.Reason;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.facotry.SocketFactory;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.taian.floatingballmatrix.view.CheckCodeDialog;
import com.taian.floatingballmatrix.viewmodel.MainViewModel;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxSPTool;
import com.tamsiree.rxkit.view.RxToast;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements CheckCodeDialog.OnViewClickListenter {

    private CheckCodeDialog dialog;
    private Disposable subscribe;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        binding.ivBanner.getLayoutParams().height = RxDeviceTool.getScreenWidth(this) / 2;
    }

    @Override
    public void initViewObservable() {
        viewModel.dialogEvent.observe(this, (v) -> {
            if (dialog == null) {
                dialog = new CheckCodeDialog(MainActivity.this);
                dialog.setOnViewClickListenter(MainActivity.this);
                dialog.show();
            } else {
                if (!dialog.isShowing()) dialog.show();
            }
        });
        RxSubscriptions.add(RxBus.getDefault().toObservable(SettingEntity.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((settingEntity) -> {
                    Log.e(TAG, "initViewObservable: " + settingEntity.toString());
                    binding.connectState.setText("连接状态：" + settingEntity.getConnecStr());
                    if (settingEntity.getConnecStatus() == SettingEntity.CONNECTED) {
                        RxToast.success("连接成功");
                        viewModel.setConnected();
                    } else {
//                        RxToast.warning("连接断开");
                        viewModel.setDisConnect();
                    }
                }));
        RxSubscriptions.add(RxBus.getDefault().toObservable(Reason.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((reason) -> {
                    Log.e(TAG, "initViewObservable: ");
//                    RxToast.warning("连接断开");
                    RxToast.warning(getString(R.string.send_failed_reason2));
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE) {
            String setting = RxSPTool.getString(getApplication(), Constant.SETTING);
            if (!TextUtils.isEmpty(setting)) {
                binding.title.setText(GsonUtil.fromJson(setting, SettingEntity.class).getTitle());
            }
            viewModel.setOnItemChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) RxSubscriptions.clear();
        SocketFactory.getInstance().mClient.disconnect();
        SocketFactory.getInstance().mUdpClient.disconnect();
        String setting = RxSPTool.getString(this, Constant.SETTING);
        if (!TextUtils.isEmpty(setting)) {
            SettingEntity entity = GsonUtil.fromJson(setting, SettingEntity.class);
            entity.setConnecString(getString(R.string.connect));
            entity.setConnecStatus(SettingEntity.DISCONNECT);
            RxSPTool.putString(this, Constant.SETTING, GsonUtil.toJson(entity));
        }
    }

    @Override
    public void onBackPressed() {
        RxActivityTool.AppExit(this);
    }

    @Override
    public void onViewClicked(String psd) {
        if (TextUtils.equals(Constant.PSD, psd)) {
            startActivityForResult(SettingActivity.class, Constant.REQUEST_CODE);
            dialog.dismiss();
        } else {
            RxToast.warning("密码错误！");
        }
    }
}
