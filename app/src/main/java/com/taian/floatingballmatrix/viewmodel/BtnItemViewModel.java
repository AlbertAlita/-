package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseApplication;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.base.ItemViewModel;
import com.taian.floatingballmatrix.binding.command.BindingAction;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.utils.Utils;
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

    public ButtonEntity entity;

    public BtnItemViewModel(@NonNull MainViewModel viewModel) {
        super(viewModel);
        entity = new ButtonEntity(Utils.getContext().getResources().getString(R.string.default_btn_name));
    }

    public void setEntity(ButtonEntity entity) {
        if (TextUtils.isEmpty(entity.assignedName))
            entity.assignedName = Utils.getContext().getResources().getString(R.string.default_btn_name);
        this.entity = entity;
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        String hexCommand = RxSPTool.getString(viewModel.getApplication(), String.valueOf(getPosition()));

    });

    public int getPosition() {
        return viewModel.getItemPosition(this);
    }
}
