package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.decoration.RecyclerItemDecoration;
import com.taian.floatingballmatrix.decoration.RecyclerViewDecoration;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.entity.SettingEntity;
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

/**
 * @ClassName: com.taian.floatingballmatrix.viewmodel
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class SettingViewModel extends BaseViewModel implements SelectSocketModeDialog.OnViewClickListenter {

    public SettingEntity settingEntity;

    public int MAX_BUTTON = 9;

    public TreeNode root = TreeNode.root();

    public TreeView treeView;

    private List<ButtonEntity> entities = new LinkedList<>();

    private String[] buttonName;

    public String socketMode = "UDP";

    public SettingViewModel(@NonNull Application application) {
        super(application);
        initSettingEntity();
    }

    private void initSettingEntity() {
        String setting = RxSPTool.getString(getApplication(), Constant.SETTING);
        buttonName = getApplication().getResources().getStringArray(R.array.button_name);
        if (TextUtils.isEmpty(setting)) {
            settingEntity = new SettingEntity();
            settingEntity.title = getApplication().getString(R.string.default_title);
        } else settingEntity = GsonUtil.fromJson(setting, SettingEntity.class);
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
            Log.e(TAG, ": " + value.buttonName + "--" + value.isSwitchOn + "---" + secondryValue.assignedName);
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
            entities.add(value);
        }
        if (isSuccessCommit) {
            RxSPTool.putString(getApplication(), Constant.BUTTON_SETTING, GsonUtil.toJson(entities));
            finishForReslut();
        }
    });

    public BindingCommand onConnentClickCommand = new BindingCommand(() -> {
        RxSPTool.putString(getApplication(), Constant.SETTING, GsonUtil.toJson(settingEntity));

    });

    private SelectSocketModeDialog dialog;

    public BindingCommand onSetProtcalClickCommand = new BindingCommand(() -> {
        if (dialog == null) {
            dialog = new SelectSocketModeDialog(getApplication());
            dialog.setOnViewClickListenter(this);
            dialog.show();
        } else {
            if (!dialog.isShowing()) dialog.show();
        }
    });

    @Override
    public void onViewClicked(boolean isUDP) {
        socketMode = isUDP ? "UDP" : "TCP";
        settingEntity.protocal = socketMode;
    }
}
