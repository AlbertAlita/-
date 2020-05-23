package com.taian.floatingballmatrix.facotry;

import android.util.Log;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.bus.RxBus;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.entity.Reason;
import com.taian.floatingballmatrix.entity.SettingEntity;
import com.taian.floatingballmatrix.structures.BaseClient;
import com.taian.floatingballmatrix.structures.BaseMessageProcessor;
import com.taian.floatingballmatrix.structures.IConnectListener;
import com.taian.floatingballmatrix.structures.message.Message;
import com.taian.floatingballmatrix.tcp.bio.BioClient;
import com.taian.floatingballmatrix.tcp.nio.NioClient;
import com.taian.floatingballmatrix.udp.bio.UdpBioClient;
import com.taian.floatingballmatrix.udp.nio.UdpNioClient;
import com.taian.floatingballmatrix.utils.Utils;

import java.util.LinkedList;

/*
 Created by baotaian on 2020/5/19 0019.
*/


public class SocketFactory implements BaseClient.OnSendMsgStateLisntener {

    public NioClient mClient;
    public UdpNioClient mUdpClient;
    private String TAG = getClass().getSimpleName();

    private static class SingletonHolder {
        private static SocketFactory INSTANCE = new SocketFactory();
    }

    public static SocketFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public BaseMessageProcessor mMessageProcessor = new BaseMessageProcessor() {

        @Override
        public void onReceiveMessages(BaseClient mClient, final LinkedList<Message> mQueen) {
            for (int i = 0; i < mQueen.size(); i++) {
                Message msg = mQueen.get(i);
                final String s = new String(msg.data, msg.offset, msg.length);
                Log.e(TAG, "onReceiveMessages: " + s);
            }
        }
    };

    private IConnectListener mConnectResultListener = new IConnectListener() {
        @Override
        public void onConnectionSuccess() {
            Log.e(TAG, "onConnectionSuccess: ");
            SettingEntity settingEntity = new SettingEntity();
            settingEntity.setEnabled(true);
            settingEntity.setConnecString(Utils.getContext().getString(R.string.connected));
            settingEntity.setConnecStr(Utils.getContext().getString(R.string.connected_str));
            settingEntity.setConnecStatus(SettingEntity.CONNECTED);
            RxBus.getDefault().postSticky(settingEntity);
        }

        @Override
        public void onConnectionFailed() {
            Log.e(TAG, "onConnectionFailed: ");
            SettingEntity settingEntity = new SettingEntity();
            settingEntity.setEnabled(true);
            settingEntity.setConnecString(Utils.getContext().getString(R.string.connect));
            settingEntity.setConnecStr(Utils.getContext().getString(R.string.non_connect));
            settingEntity.setConnecStatus(SettingEntity.DISCONNECT);
            settingEntity.setClickForDisconnent(false);
            RxBus.getDefault().postSticky(settingEntity);
        }
    };

    @Override
    public void onSendFailed(String reason) {
        Log.e(TAG, "onSendFailed: " + reason);
        RxBus.getDefault().postSticky(new Reason(reason));
    }

    /*
        注意我只写了nio链接的连接setOnSendMsgStateLisntener方法，
        未重写 bio 连接setOnSendMsgStateLisntener方法未重写，维护者记得重写
     */
    private SocketFactory() {
        mClient = new NioClient(mMessageProcessor, mConnectResultListener);
        mUdpClient = new UdpNioClient(mMessageProcessor, mConnectResultListener);
        mClient.setOnSendMsgStateLisntener(this);
        mUdpClient.setOnSendMsgStateLisntener(this);
    }

    public static void resetSocket() {
        SingletonHolder.INSTANCE = null;
        SingletonHolder.INSTANCE = new SocketFactory();
    }


}
