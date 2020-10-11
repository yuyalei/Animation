package com.example.animation.gif;

import android.util.Log;

import com.example.animation.gif.bean.GiftIdentify;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Desc: <礼物仓库队列>
 * Author: JS-Dugu
 * Created On: 2019/10/24 19:50
 */
public class GiftBasket {

    private String TAG = "GiftBasket";

    BlockingQueue<GiftIdentify> queue = new LinkedBlockingQueue<>();

    /**
     * 将礼物放入队列
     *
     * @param bean
     */
    public void putGift(GiftIdentify bean) throws InterruptedException {
        //添加元素到队列，如果队列已满,线程进入等待，直到有空间继续生产
        queue.put(bean);
        Log.d(TAG, "puted size:" + queue.size());
        //添加元素到队列，如果队列已满，抛出IllegalStateException异常，退出生产模式
//        queue.add(bean);
        //添加元素到队列，如果队列已满或者说添加失败，返回false，否则返回true，继续生产
//        queue.offer(bean);
        //添加元素到队列，如果队列已满，就等待指定时间，如果添加成功就返回true，否则false，继续生产
//        queue.offer(bean,5, TimeUnit.SECONDS);
    }

    /**
     * 从队列取出礼物
     *
     * @return
     */
    public GiftIdentify takeGift() throws InterruptedException {
        //检索并移除队列头部元素，如果队列为空,线程进入等待，直到有新的数据加入继续消费
        GiftIdentify bean = queue.take();
        Log.d(TAG, "taked size:" + queue.size());
        //检索并删除队列头部元素，如果队列为空，抛出异常，退出消费模式
//        GiftIdentify bean = queue.remove();
        //检索并删除队列头部元素，如果队列为空，返回false，否则返回true，继续消费
//        GiftIdentify bean = queue.poll();
        //检索并删除队列头部元素，如果队列为空，则等待指定时间，成功返回true，否则返回false，继续消费
//        GiftIdentify bean = queue.poll(3, TimeUnit.SECONDS);
        return bean;
    }

}
