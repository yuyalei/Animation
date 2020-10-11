package com.example.animation.lottie.bean;

/**
 * Desc:
 * Author: JS-Kylo
 * Created On: 2019/10/31 11:36
 */
public interface AnimationIdentify extends Comparable<AnimationIdentify>{
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
}
