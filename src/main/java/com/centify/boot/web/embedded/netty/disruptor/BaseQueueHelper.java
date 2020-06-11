package com.centify.boot.web.embedded.netty.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import net.minidev.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: admin [2019/12/24 15:59]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 15:59        admin            new file.
 * <pre>
 */
public abstract class BaseQueueHelper<D, E extends ValueWrapper<D>, H extends WorkHandler<E>> {

    private static List<BaseQueueHelper> queueHelperList = new ArrayList<BaseQueueHelper>();
    /**
     * Disruptor 对象
     */
    private Disruptor<E> disruptor;
    /**
     * initQueue
     */
    private List<D> initQueue = new ArrayList<D>();
    /**
     * RingBuffer
     */
    private RingBuffer<E> ringBuffer;

    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 初始化
     */
    public void init() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("DisruptorThreadPool").build();
        disruptor = new Disruptor<E>(eventFactory(), getQueueSize(), namedThreadFactory, ProducerType.SINGLE, getStrategy());
        disruptor.setDefaultExceptionHandler(new MyHandlerException());
        disruptor.handleEventsWithWorkerPool(getHandler());
        ringBuffer = disruptor.start();

        //初始化数据发布
//    for (D data : initQueue) {
//      ringBuffer.publishEvent(new
//
//                                  EventTranslatorOneArg<E, D>() {
//
//                                    @Override
//                                    public void translateTo(E event, long sequence, D data) {
//                                      event.setValue(data);
//                                    }
//
//
//                                  }, data);
//    }
        initQueue.stream()
                .forEach((data) ->
                        ringBuffer.publishEvent((e, s, d) ->
                                e.setValue(d), data));

        //加入资源清理钩子
        synchronized (queueHelperList) {
            if (queueHelperList.isEmpty()) {
                Runtime.getRuntime()
                        .addShutdownHook(new Thread(() ->
                                queueHelperList.stream()
                                        .forEach((item) -> item.shutdown())));
            }
            queueHelperList.add(this);
        }
    }

    /**
     * 事件工厂
     *
     * @return EventFactory
     */
    protected abstract EventFactory<E> eventFactory();

    /**
     * 队列大小
     *
     * @return 队列长度，必须是2的幂
     */
    protected abstract int getQueueSize();

    /**
     * 如果要改变线程执行优先级，override此策略. YieldingWaitStrategy会提高响应并在闲时占用70%以上CPU，
     * 慎用SleepingWaitStrategy会降低响应更减少CPU占用，用于日志等场景.
     *
     * @return WaitStrategy
     */
    protected abstract WaitStrategy getStrategy();

    /**
     * 事件消费者
     *
     * @return WorkHandler[]
     */
    protected abstract WorkHandler[] getHandler();

    /**
     * 关闭队列
     */
    public void shutdown() {
        disruptor.shutdown();
    }

    /**
     * 插入队列消息，支持在对象init前插入队列，则在队列建立时立即发布到队列处理.
     */
    public synchronized void publishEvent(D data) {
        if (ringBuffer == null) {
            initQueue.add(data);
            return;
        }
        ringBuffer.publishEvent((e, s, d) -> e.setValue(d), data);
    }
}
