package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.context.NettyServletContext;
import com.centify.boot.web.embedded.netty.servlet.NettyHttpServletRequest;
import com.centify.boot.web.embedded.netty.servlet.NettyRequestDispatcher;
import com.centify.boot.web.embedded.netty.utils.NettyChannelUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * <pre>
 * <b>Http请求转发处理</b>
 * <b>Describe:
 * 1、转交给dispatcherServlet处理（核心为MockHttpServletRequest 、MockHttpServletResponse）
 * 2、netty handler链的初始化在{@link DispatcherServletChannelInitializer#initChannel(io.netty.channel.socket.SocketChannel)}</b>
 *
 * <b>Author: tanlin [2020/6/8 9:49]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/8 9:49        tanlin            new file.
 * <pre>
 */
@Log4j2
@ChannelHandler.Sharable
public class DispatcherServletHandler extends ChannelInboundHandlerAdapter {
    private final NettyServletContext servletContext;

    public DispatcherServletHandler(NettyServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        NettyRequestDispatcher dispatcherServlet = (NettyRequestDispatcher) servletContext.getRequestDispatcher(((MockHttpServletRequest) msg).getRequestURI());
        dispatcherServlet.dispatch((MockHttpServletRequest) msg, servletResponse);
        NettyChannelUtil.sendResultByteBuf(
                ctx,
                HttpResponseStatus.valueOf(servletResponse.getStatus()),
                msg,
                Unpooled.wrappedBuffer(servletResponse.getContentAsByteArray())
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
