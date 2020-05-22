package com.taian.floatingballmatrix.structures;


import com.taian.floatingballmatrix.entity.Reason;
import com.taian.floatingballmatrix.structures.message.Message;
import com.taian.floatingballmatrix.structures.message.MessageReadQueen;
import com.taian.floatingballmatrix.structures.message.MessageWriteQueen;

/**
 * author       :   long
 * created on   :   2017/11/30
 * description  :   客户连接对象
 */

public abstract class BaseClient {

    //读队列
    public MessageReadQueen mReadMessageQueen = new MessageReadQueen();

    //收队列
    public MessageWriteQueen mWriteMessageQueen = new MessageWriteQueen();

    protected BaseMessageProcessor mMessageProcessor;

    protected OnSendMsgStateLisntener onSendMsgStateLisntener;

    public BaseClient(BaseMessageProcessor mMessageProcessor) {
        this.mMessageProcessor = mMessageProcessor;
    }

    //--------------------------------------------------------------------------------------
    public void clearUnreachableMessages() {
        Message msg = pollWriteMessage();
        while (null != msg) {
            removeWriteMessage(msg);
            msg = pollWriteMessage();
        }
    }

    //--------------------------------------------------------------------------------------
    public void onReceiveData(byte[] src, int offset, int length) {
        Message msg = mReadMessageQueen.build(src, offset, length);
        mReadMessageQueen.add(msg);
    }

    public void onReceiveMessageClear() {
        Message msg = mReadMessageQueen.mReadQueen.poll();
        while (null != msg) {
            mReadMessageQueen.remove(msg);
            msg = mReadMessageQueen.mReadQueen.poll();
        }
    }

    //--------------------------------------------------------------------------------------
    public void onSendMessage(byte[] src, int offset, int length) {
        Message msg = mWriteMessageQueen.build(src, offset, length);
        mWriteMessageQueen.add(msg);
        onCheckConnect();
    }

    protected Message pollWriteMessage() {
        return mWriteMessageQueen.mWriteQueen.poll();
    }

    protected void removeWriteMessage(Message msg) {
        mWriteMessageQueen.remove(msg);
    }

    //--------------------------------------------------------------------------------------
    public abstract void onCheckConnect();

    public abstract void onClose();

    public abstract boolean onRead();

    public abstract boolean onWrite();

    public abstract boolean isConnected();

    public abstract void connect();

    public abstract void disconnect();

    public abstract void setOnSendMsgStateLisntener(OnSendMsgStateLisntener lisntener);

    public interface OnSendMsgStateLisntener {
        void onSendFailed(String reason);
    }

}
