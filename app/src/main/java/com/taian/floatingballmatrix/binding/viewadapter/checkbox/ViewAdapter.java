package com.taian.floatingballmatrix.binding.viewadapter.checkbox;


import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.taian.floatingballmatrix.binding.command.BindingCommand;

import androidx.databinding.BindingAdapter;

/**
 * Created by baotaian on 2017/6/16.
 */

public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bindingCommand.execute(b);
            }
        });
    }
}
