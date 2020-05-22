package com.taian.floatingballmatrix.view.treeview.binder;
/*
 Created by baotaian on 2020/5/18 0018.
*/


import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.view.treeview.TreeNode;
import com.taian.floatingballmatrix.view.treeview.base.CheckableNodeViewBinder;

import static android.content.ContentValues.TAG;

public class SecondLevelNodeViewBinder extends CheckableNodeViewBinder {

    EditText etName, etHex;

    public SecondLevelNodeViewBinder(View itemView) {
        super(itemView);
        etName = itemView.findViewById(R.id.et_name);
        etHex = itemView.findViewById(R.id.et_hex);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkbox;
    }

    @Override
    public int getLayoutId() {
        return R.layout.button_setting_item2;
    }

    @Override
    public void bindView(TreeNode treeNode, Context context) {
        ButtonEntity value = (ButtonEntity) treeNode.getValue();
        etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                value.assignedName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etHex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                value.hexCommand = string;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        String assignedName = value.assignedName;
        String hexCommand = value.hexCommand;
        if (!TextUtils.isEmpty(assignedName)) {
            etName.setText(assignedName);
        }
        if (!TextUtils.isEmpty(hexCommand)) {
            etHex.setText(hexCommand);
        }
    }

//    private String formatHex(String hexCommand) {
//        String newHex = "";
//        char[] chars = hexCommand.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            if (i % 2 == 0) {
//                if (i < 3) newHex += (chars[i] + " ");
//            } else {
//                newHex += chars[i];
//            }
//        }
//        return newHex;
//    }
}
