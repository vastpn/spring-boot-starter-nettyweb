package com.centify.boot.web.embedded.netty.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: tanlin [2020/6/11 14:01]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/11 14:01        tanlin            new file.
 * <pre>
 */
@Data
@Accessors(chain = true)
public class ChannelServletEvent {
    private ChannelHandlerContext chc;
    private MockHttpServletRequest request;

    public ChannelServletEvent(ChannelHandlerContext chc, MockHttpServletRequest request) {
        this.chc = chc;
        this.request = request;
    }
}
