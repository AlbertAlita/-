package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.taian.floatingballmatrix.BR;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.SettingActivity;
import com.taian.floatingballmatrix.adapter.ButtonAdapter;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.taian.floatingballmatrix.bus.SingleLiveEvent;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.facotry.SocketFactory;
import com.taian.floatingballmatrix.structures.TcpAddress;
import com.taian.floatingballmatrix.structures.UdpAddress;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxSPTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.GridLayoutManager;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class MainViewModel extends BaseViewModel {

    public SettingEntity settingEntity;
    private int BTN_MAX_NUM = 9;
    private Map<Integer, ButtonEntity> map;

    public SingleLiveEvent<Void> dialogEvent = new SingleLiveEvent<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        String json = RxSPTool.getString(getApplication(), Constant.BUTTON_SETTING);
        map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        for (int i = 0; i < BTN_MAX_NUM; i++) {
            BtnItemViewModel btnItemViewModel = new BtnItemViewModel(this);
            if (map.get(i) != null) btnItemViewModel.setEntity(map.get(i));
            observableList.add(btnItemViewModel);
        }
        String setting = RxSPTool.getString(getApplication(), Constant.SETTING);
        if (!TextUtils.isEmpty(setting)) {
            settingEntity = GsonUtil.fromJson(setting, SettingEntity.class);
        } else {
            settingEntity = new SettingEntity();
            settingEntity.setTitle(getApplication().getString(R.string.default_title));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        autoConnect();
    }

    private void autoConnect() {
        if (TextUtils.isEmpty(settingEntity.getIp()) || TextUtils.isEmpty(settingEntity.getPort()))
            return;
        if (TextUtils.equals(settingEntity.getProtocal(), "UDP")) {
            Log.e(TAG, "udpOpen: " + settingEntity.getIp() + "---" + settingEntity.getPort());
            SocketFactory.getInstance().mUdpClient.setConnectAddress(new UdpAddress[]
                    {new UdpAddress(settingEntity.getIp(), Integer.valueOf(settingEntity.getPort()))});
            SocketFactory.getInstance().mUdpClient.connect();
//            setConnected();
        } else if (TextUtils.equals(settingEntity.getProtocal(), "TCP")) {
            Log.e(TAG, "tcpOpen: " + settingEntity.getIp() + "---" + settingEntity.getPort());
            SocketFactory.getInstance().mClient.setConnectAddress(new TcpAddress[]
                    {new TcpAddress(settingEntity.getIp(), Integer.valueOf(settingEntity.getPort()))});
            SocketFactory.getInstance().mClient.connect();
            if (!SocketFactory.getInstance().mClient.isConnected()) {
                notifyConnecting();
            }
        }
    }

    private void notifyConnecting() {
        settingEntity.setEnabled(false);
        settingEntity.setConnecStr("连接状态：" + getApplication().getString(R.string.connectting));
        settingEntity.notifyChange();
    }


    public void setConnected() {
        settingEntity.setEnabled(true);
        settingEntity.setConnecString(getApplication().getString(R.string.connected));
        settingEntity.setConnecStr("连接状态：" + getApplication().getString(R.string.connected_str));
        settingEntity.setConnecStatus(SettingEntity.CONNECTED);
        RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
        settingEntity.notifyChange();
    }

    public void setDisConnect() {
        settingEntity.setEnabled(true);
        settingEntity.setConnecString(getApplication().getString(R.string.connect));
        settingEntity.setConnecStr("连接状态：" + getApplication().getString(R.string.non_connect));
        settingEntity.setConnecStatus(SettingEntity.DISCONNECT);
        RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
        settingEntity.notifyChange();
    }

    public GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(), 3);

    public ItemBinding<BtnItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.btn_item);

    public ButtonAdapter adapter = new ButtonAdapter(itemBinding);

    public ObservableList<BtnItemViewModel> observableList = new ObservableArrayList<>();

    public BindingCommand settinigClicked = new BindingCommand(() -> {
        dialogEvent.call();
    });

    public int getItemPosition(BtnItemViewModel btnItemViewModel) {
        return observableList.indexOf(btnItemViewModel);
    }

    public void setOnItemChanged() {
        String json = RxSPTool.getString(getApplication(), Constant.BUTTON_SETTING);
        map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        for (int i = 0; i < observableList.size(); i++) {
            ButtonEntity buttonEntity = map.get(i);
            if (buttonEntity != null) {
                BtnItemViewModel itemViewModel = observableList.get(i);
                itemViewModel.setEntity(buttonEntity);
                adapter.notifyItemChanged(i);
            }
        }
    }

    private Map<Integer, ButtonEntity> convertToMap(List<ButtonEntity> entities) {
        Map<Integer, ButtonEntity> map = new HashMap<>();
        for (ButtonEntity entity : entities) {
            map.put(entity.index, entity);
        }
        return map;
    }
}
