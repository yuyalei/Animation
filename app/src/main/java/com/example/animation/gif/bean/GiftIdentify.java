package com.example.animation.gif.bean;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/29 17:18
 */
public interface GiftIdentify extends Comparable<GiftIdentify>{
    /**
     * 是否为接受的礼物
     *
     * @return
     */
    boolean isReceive();

    /**
     * 礼物Id
     *
     * @return
     */
    long getTheGiftId();

    /**
     *
     * @param gid
     */
    void setTheGiftId(long gid);

    /**
     * 用户Id
     *
     * @return
     */
    long getTheUserId();

    void setTheUserId(long uid);

    /**
     * 礼物累计数
     *
     * @return
     */
    int getTheGiftCount();

    void setTheGiftCount(int count);

    /**
     * 单次礼物赠送数目
     *
     * @return
     */
    int getTheSendGiftSize();

    void setTheSendGiftSize(int size);

    /**
     * 礼物停留时间
     *
     * @return
     */
    long getTheGiftStay();

    void setTheGiftStay(long stay);

    /**
     * 礼物最新一次刷新时间戳
     *
     * @return
     */
    long getTheLatestRefreshTime();

    void setTheLatestRefreshTime(long time);

    /**
     * 礼物索引
     *
     * @return
     */
    int getTheCurrentIndex();

    void setTheCurrentIndex(int index);
}
