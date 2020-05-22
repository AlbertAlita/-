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
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.facotry.SocketFactory;
import com.taian.floatingballmatrix.utils.CommonUtils;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.taian.floatingballmatrix.utils.Utils;
import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxSPTool;
import com.tamsiree.rxkit.view.RxToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class BtnItemViewModel extends ItemViewModel<MainViewModel> {

    private Map<Integer, ButtonEntity> map;
    public ButtonEntity entity;
    public String defaultName = Utils.getContext().getResources().getString(R.string.default_btn_name);

    public BtnItemViewModel(@NonNull MainViewModel viewModel) {
        super(viewModel);
        entity = new ButtonEntity(defaultName);
    }

    public void setEntity(ButtonEntity entity) {
        if (TextUtils.isEmpty(entity.assignedName))
            entity.assignedName = defaultName;
        this.entity = entity;
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        String json = RxSPTool.getString(Utils.getContext(), Constant.BUTTON_SETTING);
        map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        ButtonEntity buttonEntity = map.get(getPosition());
        String setting = RxSPTool.getString(Utils.getContext(), Constant.SETTING);
        SettingEntity settingEntity = GsonUtil.fromJson(setting, SettingEntity.class);
        if (buttonEntity != null && settingEntity != null) {
            String hexCommand = buttonEntity.hexCommand;
            if (TextUtils.isEmpty(hexCommand)) {
                RxToast.warning("请设置十六位进制码");
                return;
            }
            String s = hexCommand.replaceAll(" ", "");
            if (s.length() % 2 != 0) {
                RxToast.warning("指令位数必须为偶数");
                return;
            }
            Log.e(TAG, ": " + hexCommand );
            if (!buttonEntity.isSwitchOn) {
                RxToast.warning("请在设置中打开按键开关");
                return;
            }
            if (settingEntity.getConnecStatus() == SettingEntity.DISCONNECT) {
                RxToast.warning("请在设置中打开连接");
                return;
            }

            String protocal = settingEntity.getProtocal();
            Log.e(TAG, ": " + protocal);
            byte[] bytes = hexStrToBinaryStr(hexCommand);
            if (TextUtils.equals(protocal, "UDP")) {
                if (SocketFactory.getInstance().mUdpClient.getAdress() == null) {
                    RxToast.warning("请在设置中打开连接");
                    return;
                }
                SocketFactory.getInstance().mMessageProcessor.send
                        (SocketFactory.getInstance().mUdpClient, bytes);
            } else if (TextUtils.equals(protocal, "TCP")) {
                if (SocketFactory.getInstance().mClient.getAdress() == null) {
                    RxToast.warning("请在设置中打开连接");
                    return;
                }
                SocketFactory.getInstance().mMessageProcessor.send
                        (SocketFactory.getInstance().mClient, bytes);
            }
        }
    });

    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    private Map<Integer, ButtonEntity> convertToMap(List<ButtonEntity> entities) {
        Map<Integer, ButtonEntity> map = new HashMap<>();
        for (ButtonEntity entity : entities) {
            map.put(entity.index, entity);
        }
        return map;
    }

    public static byte[] hexStrToBinaryStr(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.replaceAll(" ", "");

        int len = hexString.length();
        int index = 0;

        byte[] bytes = new byte[len / 2];

        while (index < len) {

            String sub = hexString.substring(index, index + 2);

            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);

            index += 2;
        }
        return bytes;
    }
}
