package com.taian.floatingballmatrix;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.databinding.ActivityMainBinding;
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

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements CheckCodeDialog.OnViewClickListenter {

    private CheckCodeDialog dialog;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE) {
            viewModel.setOnItemChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        Log.e(TAG, "onViewClicked: " + psd);
        if (TextUtils.equals(Constant.PSD, psd)) {
            startActivityForResult(SettingActivity.class, Constant.REQUEST_CODE);
            dialog.dismiss();
        } else {
            RxToast.warning("密码错误！");
        }
    }
}
