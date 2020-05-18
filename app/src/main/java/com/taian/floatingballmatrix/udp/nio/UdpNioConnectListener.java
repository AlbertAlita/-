package com.taian.floatingballmatrix.udp.nio;


import com.taian.floatingballmatrix.udp.nio.processor.UdpNioReadWriteProcessor;

import java.io.IOException;
import java.nio.channels.DatagramChannel;

/**
 * author       :   long
 * created on   :   2017/11/30
 * description  :   连接状态回调
 */

public interface UdpNioConnectListener {

    void onConnectSuccess(UdpNioReadWriteProcessor mSocketProcessor, DatagramChannel socketChannel) throws IOException;

    void onConnectFailed(UdpNioReadWriteProcessor mSocketProcessor);

}