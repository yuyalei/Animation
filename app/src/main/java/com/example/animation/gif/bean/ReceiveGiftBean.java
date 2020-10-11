package com.example.animation.gif.bean;

/**
 * Desc: <功能简述>
 * Author: JS-Dugu
 * Created On: 2019/10/24 20:50
 */
public class ReceiveGiftBean extends BaseGiftBean {

    private long msgId = 0;
    private int receiveGiftCount = 0;

    public ReceiveGiftBean(int userId, int giftId, String userName, String giftName, String giftImg, long time) {
        super(userId, giftId, userName, giftName, giftImg, time);
    }

    public int getReceiveGiftCount() {
        return receiveGiftCount;
    }

    public void setReceiveGiftCount(int receiveGiftCount) {
        this.receiveGiftCount = receiveGiftCount;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "ReceiveGiftBean{" +
                "receiveGiftCount=" + receiveGiftCount +
                ", userId=" + userId +
                ", giftId=" + giftId +
                ", userName='" + userName + '\'' +
                ", giftName='" + giftName + '\'' +
                ", giftImg='" + giftImg + '\'' +
                ", giftStayTime=" + giftStayTime +
                ", giftSendSize=" + giftSendSize +
                ", giftCount=" + giftCount +
                ", latestRefreshTime=" + latestRefreshTime +
                ", currentIndex=" + currentIndex +
                '}';
    }

    @Override
    public boolean isReceive() {
        return false;
    }
}
