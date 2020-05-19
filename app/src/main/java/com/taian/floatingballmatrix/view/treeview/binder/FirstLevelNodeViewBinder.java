package com.taian.floatingballmatrix.view.treeview.binder;
/*
 Created by baotaian on 2020/5/18 0018.
*/


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.view.treeview.TreeNode;
import com.taian.floatingballmatrix.view.treeview.base.CheckableNodeViewBinder;

public class FirstLevelNodeViewBinder extends CheckableNodeViewBinder {
    TextView name;
    CheckBox checkBox;

    public FirstLevelNodeViewBinder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_button_name);
        checkBox = itemView.findViewById(getCheckableViewId());
    }

    @Override
    public int getCheckableViewId() {
        return R.id.btn_switch;
    }

    @Override
    public int getLayoutId() {
        return R.layout.button_setting_item;
    }

    @Override
    public void bindView(TreeNode treeNode, Context context) {
        ButtonEntity value = (ButtonEntity) treeNode.getValue();
        name.setText(value.buttonName);
        checkBox.setChecked(value.isSwitchOn);
    }

    @Override
    public void onNodeToggled(int position, TreeNode treeNode, boolean expand, Context context) {
        Log.e("TAG", "onNodeToggled: " );
        checkBox.setChecked(expand);
    }

    @Override
    public void onNodeSelectedChanged(Context context, TreeNode treeNode, boolean selected) {
        ButtonEntity value = (ButtonEntity) treeNode.getValue();
        Log.e("TAG", "onNodeSelectedChanged: " + value.toString()  + selected);
        value.isSwitchOn = selected;
        if (selected) treeView.expandNode(treeNode);
        else treeView.collapseNode(treeNode);
    }
}
