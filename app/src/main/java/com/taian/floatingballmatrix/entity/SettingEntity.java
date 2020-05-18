package com.taian.floatingballmatrix.entity;
/*
 Created by baotaian on 2020/5/18 0018.
*/


import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class SettingEntity extends BaseObservable implements Parcelable {

    public SettingEntity() {
    }

    public static final int CONNECTED = 1, CONNECTING = 2, DISCONNECT = 3;

    private String title;
    private String protocal = "UDP";
    ;
    private String ip;
    private String port;
    private String connecString = "连接";
    private int connecStatus = DISCONNECT;


    protected SettingEntity(Parcel in) {
        title = in.readString();
        protocal = in.readString();
        ip = in.readString();
        port = in.readString();
        connecStatus = in.readInt();
    }

    public static final Creator<SettingEntity> CREATOR = new Creator<SettingEntity>() {
        @Override
        public SettingEntity createFromParcel(Parcel in) {
            return new SettingEntity(in);
        }

        @Override
        public SettingEntity[] newArray(int size) {
            return new SettingEntity[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(protocal);
        parcel.writeString(ip);
        parcel.writeString(port);
        parcel.writeInt(connecStatus);
    }
}
