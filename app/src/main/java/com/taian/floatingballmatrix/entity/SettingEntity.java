package com.taian.floatingballmatrix.entity;
/*
 Created by baotaian on 2020/5/18 0018.
*/


import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class SettingEntity extends BaseObservable {

    public SettingEntity() {
    }

    public static final int CONNECTED = 1, CONNECTING = 2, DISCONNECT = 3;

    private String title;
    private String protocal = "UDP";
    ;
    private String ip;
    private String port;
    private String connecString = "连接";
    private String connecStr ;
    private boolean enabled = true,clickForDisconnent;
    private int connecStatus = DISCONNECT;


    public String getConnecString() {
        return connecString;
    }

    public void setConnecString(String connecString) {
        this.connecString = connecString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getConnecStatus() {
        return connecStatus;
    }

    public void setConnecStatus(int connecStatus) {
        this.connecStatus = connecStatus;
    }

    public String getConnecStr() {
        return connecStr;
    }

    public void setConnecStr(String connecStr) {
        this.connecStr = connecStr;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setClickForDisconnent(boolean clickForDisconnent) {
        this.clickForDisconnent = clickForDisconnent;
    }

    public boolean isClickForDisconnent() {
        return clickForDisconnent;
    }

    @Override
    public String toString() {
        return "SettingEntity{" +
                "title='" + title + '\'' +
                ", protocal='" + protocal + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", connecString='" + connecString + '\'' +
                ", enabled=" + enabled +
                ", connecStatus=" + connecStatus +
                ", connecStr=" + connecStr +
                '}';
    }
}
