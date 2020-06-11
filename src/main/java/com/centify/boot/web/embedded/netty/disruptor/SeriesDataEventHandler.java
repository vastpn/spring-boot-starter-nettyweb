package com.centify.boot.web.embedded.netty.disruptor;

import com.centify.boot.web.embedded.netty.core.ChannelServletEvent;
import com.centify.boot.web.embedded.netty.utils.NettyChannelUtil;
import com.centify.boot.web.embedded.netty.utils.SpringContextUtil;
import com.lmax.disruptor.WorkHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: admin [2019/12/24 16:01]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 16:01        admin            new file.
 * <pre>
 */
@Log4j2
public class SeriesDataEventHandler implements WorkHandler<SeriesDataEvent> {

    private static final AtomicLong DISRUPTOR_SIZE = new AtomicLong(0);


    @Override
    public void onEvent(SeriesDataEvent event) {
        Optional.ofNullable(event.getValue())
                /*SeriesData 对象不为空，执行业务操作（SeriesData 包含 数据对象，数据即是待消费的真实数据对象）*/
                .ifPresent((message) ->
                        Optional.ofNullable(message.getMessage())
                                /*数据对象 对象不为空，执行业务操作*/
                                .ifPresent((data) -> {
                                    log.info("消息总队列：{} ,交易数据：{}", DISRUPTOR_SIZE.addAndGet(1), event.getValue().getMessage());
                                }));
    }
}
