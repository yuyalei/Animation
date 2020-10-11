package com.example.animation.gif.bean;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/29 17:22
 */
public abstract class BaseGiftBean implements GiftIdentify, Cloneable {
    /**
     * 用户id
     */
    protected long userId;
    /**
     * 礼物id
     */
    protected long giftId;
    /**
     * 用户名称
     */
    protected String userName;
    /**
     * 礼物名称
     */
    protected String giftName;

    /**
     * 礼物本地图片也可以定义为远程url
     */
    protected String giftImg;

    /**
     * 礼物持续时间
     */
    protected long giftStayTime;

    /**
     * 单次礼物数目
     */
    protected int giftSendSize = 1;


    /**
     * 礼物计数
     */
    protected int giftCount;
    /**
     * 礼物刷新时间
     */
    protected long latestRefreshTime;
    /**
     * 当前index
     */
    protected int currentIndex;


    public BaseGiftBean(int userId, int giftId, String userName, String giftName, String giftImg, long time) {
        this.userId = userId;
        this.giftId = giftId;
        this.userName = userName;
        this.giftName = giftName;
        this.giftImg = giftImg;
        this.giftStayTime = time;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

    @Override
    public long getTheGiftId() {
        return giftId;
    }

    @Override
    public void setTheGiftId(long gid) {
        this.giftId = gid;
    }

    @Override
    public long getTheUserId() {
        return userId;
    }

    @Override
    public void setTheUserId(long uid) {
        this.userId = uid;
    }

    @Override
    public int getTheSendGiftSize() {
        return giftSendSize;
    }

    @Override
    public void setTheSendGiftSize(int size) {
        giftSendSize = size;
    }

    @Override
    public long getTheGiftStay() {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay(long stay) {
        giftStayTime = stay;
    }


    @Override
    public int getTheGiftCount() {
        return giftCount;
    }

    @Override
    public long getTheLatestRefreshTime() {
        return latestRefreshTime;
    }

    @Override
    public int getTheCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void setTheGiftCount(int count) {
        giftCount = count;
    }

    @Override
    public void setTheLatestRefreshTime(long time) {
        latestRefreshTime = time;
    }

    @Override
    public void setTheCurrentIndex(int index) {
        currentIndex = index;
    }

    @Override
    public int compareTo(GiftIdentify o) {
        return (int) (this.getTheLatestRefreshTime() - o.getTheLatestRefreshTime());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "BaseGiftBean{" +
                "userId=" + userId +
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
}
