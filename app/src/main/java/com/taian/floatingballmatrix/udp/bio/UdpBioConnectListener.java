package com.taian.floatingballmatrix.udp.bio;


import com.taian.floatingballmatrix.udp.bio.processor.UdpBioReadWriteProcessor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


/**
 * author       :   Administrator
 * created on   :   2017/12/4
 * description  :
 */

public interface UdpBioConnectListener {

    void onConnectSuccess(UdpBioReadWriteProcessor mSocketProcessor, DatagramSocket mSocket, DatagramPacket mWriteDatagramPacket, DatagramPacket mReadDatagramPacket);

    void onConnectFailed(UdpBioReadWriteProcessor mSocketProcessor);

}
