package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.taian.floatingballmatrix.bus.RxBus;
import com.taian.floatingballmatrix.bus.SingleLiveEvent;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.decoration.RecyclerItemDecoration;
import com.taian.floatingballmatrix.decoration.RecyclerViewDecoration;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.facotry.SocketFactory;
import com.taian.floatingballmatrix.structures.BaseClient;
import com.taian.floatingballmatrix.structures.BaseMessageProcessor;
import com.taian.floatingballmatrix.structures.IConnectListener;
import com.taian.floatingballmatrix.structures.TcpAddress;
import com.taian.floatingballmatrix.structures.UdpAddress;
import com.taian.floatingballmatrix.structures.message.Message;
import com.taian.floatingballmatrix.tcp.nio.NioClient;
import com.taian.floatingballmatrix.udp.nio.UdpNioClient;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.taian.floatingballmatrix.view.SelectSocketModeDialog;
import com.taian.floatingballmatrix.view.treeview.TreeNode;
import com.taian.floatingballmatrix.view.treeview.TreeView;
import com.taian.floatingballmatrix.view.treeview.factroy.NodeViewFactory;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxSPTool;
import com.tamsiree.rxkit.view.RxToast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

/**
 * @ClassName: com.taian.floatingballmatrix.viewmodel
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class SettingViewModel extends BaseViewModel {

    public SettingEntity settingEntity;

    public static final int MAX_BUTTON = 9, UDP_MODE = 1, TCP_MODE = 2;

    public int socketMode = UDP_MODE;

    public TreeNode root = TreeNode.root();

    public TreeView treeView;

    private List<ButtonEntity> entities = new LinkedList<>();

    private String[] buttonName;

    public SingleLiveEvent<Void> showDateDialogObservable = new SingleLiveEvent();

    public SettingViewModel(@NonNull Application application) {
        super(application);
        initSettingEntity();
    }

    private void initSettingEntity() {
        String setting = RxSPTool.getString(getApplication(), Constant.SETTING);
        Log.e(TAG, "initSettingEntity: " + setting);
        buttonName = getApplication().getResources().getStringArray(R.array.button_name);
        if (TextUtils.isEmpty(setting)) {
            settingEntity = new SettingEntity();
            settingEntity.setTitle(getApplication().getString(R.string.default_title));
        } else {
            settingEntity = GsonUtil.fromJson(setting, SettingEntity.class);
            socketMode = TextUtils.equals(settingEntity.getProtocal(), "UDP") ? UDP_MODE : TCP_MODE;
            settingEntity.notifyChange();
        }
    }

    public View initButton() {
        treeView = new TreeView(root, getApplication(), new NodeViewFactory());
        String json = RxSPTool.getString(getApplication(), Constant.BUTTON_SETTING);
        Map<Integer, ButtonEntity> map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        for (int i = 0; i < MAX_BUTTON; i++) {
            ButtonEntity buttonEntity = map.get(i);
            if (buttonEntity == null) {
                buttonEntity = new ButtonEntity();
                buttonEntity.buttonName = buttonName[i];
            }
            //一级item
            TreeNode treeNode = new TreeNode(buttonEntity);
            treeNode.setLevel(0);
            //二级item
            if (buttonEntity == null)
                buttonEntity = new ButtonEntity();
            TreeNode secondryButtonTreeNode = new TreeNode(buttonEntity);
            secondryButtonTreeNode.setLevel(1);
            treeNode.addChild(secondryButtonTreeNode);
            root.addChild(treeNode);
        }
        View view = treeView.getView();
        for (TreeNode node : treeView.getAllNodes()) {
            ButtonEntity value = (ButtonEntity) node.getValue();
            if (value.isSwitchOn) treeView.expandNode(node);
        }
        RecyclerViewDecoration decoration = new RecyclerViewDecoration(RxImageTool.dip2px(1));
        decoration.setBottomSpace(true);
        treeView.addItemDecoration(decoration);
        return view;
    }

    private Map<Integer, ButtonEntity> convertToMap(List<ButtonEntity> entities) {
        Map<Integer, ButtonEntity> map = new HashMap<>();
        for (ButtonEntity entity : entities) {
            map.put(entity.index, entity);
        }
        return map;
    }

    public BindingCommand onCmtClickCommand = new BindingCommand(() -> {
        boolean isSuccessCommit = true;
        List<TreeNode> allNodes = treeView.getLevelOneNodes();
        entities.clear();
        for (int i = 0; i < allNodes.size(); i++) {
            TreeNode node = allNodes.get(i);
            ButtonEntity value = (ButtonEntity) node.getValue();
            if (node.getChildren().isEmpty()) break;
            ButtonEntity secondryValue = (ButtonEntity) node.getChildren().get(0).getValue();
            boolean empty = TextUtils.isEmpty(secondryValue.assignedName);
            if (value.isSwitchOn && empty) {
                RxToast.warning(getApplication().getString(R.string.pls_input_name, buttonName[i]));
                isSuccessCommit = false;
            }
            if (!empty && value.isSwitchOn && TextUtils.isEmpty(secondryValue.hexCommand)) {
                RxToast.warning(getApplication().getString(R.string.pls_input_protocal, buttonName[i]));
                isSuccessCommit = false;
            }
            value.index = i;
            if (!empty) value.assignedName = secondryValue.assignedName;
            if (!TextUtils.isEmpty(secondryValue.hexCommand))
                value.hexCommand = secondryValue.hexCommand;
            Log.e(TAG, ": " + value.toString());
            entities.add(value);
        }
        if (isSuccessCommit) {
            Log.e(TAG, ": " + GsonUtil.toJson(settingEntity));
            RxSPTool.putString(getApplication(), Constant.BUTTON_SETTING, GsonUtil.toJson(entities));
            RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
            finishForReslut();
        }
    });

    public BindingCommand onConnentClickCommand = new BindingCommand(() -> {
        int connecStatus = settingEntity.getConnecStatus();
        if (connecStatus == SettingEntity.DISCONNECT) {
            if (TextUtils.isEmpty(settingEntity.getIp())) {
                RxToast.warning(getApplication().getString(R.string.input_service_ip));
                return;
            }
            if (TextUtils.isEmpty(settingEntity.getPort())) {
                RxToast.warning(getApplication().getString(R.string.input_port));
                return;
            }
            if (socketMode == UDP_MODE) udpOpen();
            else tcpOpen();
        } else if (connecStatus == SettingEntity.CONNECTED) {
            if (socketMode == UDP_MODE) udpDisconnect();
            else tcpDisconnect();
            settingEntity.setConnecString(getApplication().getString(R.string.connect));
            settingEntity.setConnecStr(getApplication().getString(R.string.non_connect));
            settingEntity.setConnecStatus(SettingEntity.DISCONNECT);
            settingEntity.setClickForDisconnent(true);
            settingEntity.notifyChange();
            RxBus.getDefault().post(settingEntity);
            RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
        }

    });

    public BindingCommand onSetProtcalClickCommand = new BindingCommand(() -> {
        showDateDialogObservable.call();
    });

    public BindingCommand textClickCommand = new BindingCommand(() -> {
    });


    public void initTCPCilent() {
        socketMode = TCP_MODE;
    }

    public void initUDPCilent() {
        socketMode = UDP_MODE;
    }

    private void tcpOpen() {
        Log.e(TAG, "tcpOpen: " + settingEntity.getIp() + "---" + settingEntity.getPort());
        SocketFactory.getInstance().mClient.setConnectAddress(new TcpAddress[]
                {new TcpAddress(settingEntity.getIp(), Integer.valueOf(settingEntity.getPort()))});
        SocketFactory.getInstance().mClient.connect();
        if (!SocketFactory.getInstance().mClient.isConnected()) {
            notifyConnecting();
        }
        Log.e(TAG, "tcpOpen: " + SocketFactory.getInstance().mClient.getAdress());
    }

    private void udpOpen() {
        SocketFactory.getInstance().mUdpClient.setConnectAddress(new UdpAddress[]
                {new UdpAddress(settingEntity.getIp(), Integer.valueOf(settingEntity.getPort()))});
        SocketFactory.getInstance().mUdpClient.connect();
        if (!SocketFactory.getInstance().mUdpClient.isConnected()) {
            Log.e(TAG, "udpOpen: ");
            notifyConnecting();
        }
    }

    private void udpDisconnect() {
        SocketFactory.getInstance().mUdpClient.disconnect();
    }

    private void tcpDisconnect() {
        SocketFactory.getInstance().mClient.disconnect();
    }

    private void notifyConnecting() {
        settingEntity.setEnabled(false);
        settingEntity.setConnecString(getApplication().getString(R.string.connectting));
        settingEntity.notifyChange();
    }

    public void setEntity(String protocal) {
        if (TextUtils.equals(settingEntity.getProtocal(), protocal)) return;
        if (TextUtils.equals(settingEntity.getProtocal(), "UDP")) udpDisconnect();
        else if (TextUtils.equals(settingEntity.getProtocal(), "TCP")) tcpDisconnect();
        settingEntity.setEnabled(true);
        settingEntity.setProtocal(protocal);
        settingEntity.setConnecStatus(SettingEntity.DISCONNECT);
        settingEntity.setConnecString(getApplication().getString(R.string.connect));
        RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
        settingEntity.notifyChange();
    }

    public void setEntity(SettingEntity entity) {
        settingEntity.setEnabled(entity.isEnabled());
        settingEntity.setConnecStatus(entity.getConnecStatus());
        settingEntity.setConnecString(entity.getConnecString());
        RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));
        settingEntity.notifyChange();
    }

}
