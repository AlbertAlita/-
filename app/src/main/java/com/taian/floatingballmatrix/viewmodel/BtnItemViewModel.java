package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.taian.floatingballmatrix.base.BaseApplication;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.base.ItemViewModel;
import com.taian.floatingballmatrix.binding.command.BindingAction;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxSPTool;

import androidx.annotation.NonNull;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class BtnItemViewModel extends ItemViewModel<MainViewModel> {

    public BtnItemViewModel(@NonNull MainViewModel viewModel) {
        super(viewModel);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        String hexCommand = RxSPTool.getString(viewModel.getApplication(), String.valueOf(getPosition()));
        Log.e(TAG, ": " + getPosition() );
    });

    public int getPosition() {
        return viewModel.getItemPosition(this);
    }
}
