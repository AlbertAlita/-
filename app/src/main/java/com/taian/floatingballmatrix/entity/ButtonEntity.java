package com.taian.floatingballmatrix.entity;
/*
 Created by baotaian on 2020/5/18 0018.
*/


import com.google.gson.annotations.SerializedName;

import androidx.databinding.BaseObservable;

public class ButtonEntity extends BaseObservable {

    public ButtonEntity() {
    }

    public ButtonEntity(String assignedName) {
        this.assignedName = assignedName;
    }

    public int index;
    public String buttonName;
    public String assignedName;
    public String hexCommand;
    public boolean isSwitchOn;

    @Override
    public String toString() {
        return "ButtonEntity{" +
                "index=" + index +
                ", buttonName='" + buttonName + '\'' +
                ", assignedName='" + assignedName + '\'' +
                ", hexCommand='" + hexCommand + '\'' +
                ", isSwitchOn=" + isSwitchOn +
                '}';
    }
}
