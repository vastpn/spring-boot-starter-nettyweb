package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.factory.NettyFilterFactory;
import com.centify.boot.web.embedded.netty.filter.NettyServletFilterChain;
import com.centify.boot.web.embedded.netty.utils.NettyChannelUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import java.util.Optional;

/**
 * <pre>
 * <b>Servlet Filter 处理链器</b>
 * <b>Describe:
 * 1、通过Netty 加入 ServletFilter处理器链，以支持Servlet Filter
 * 2、netty handler链的初始化在{@link DispatcherServletChannelInitializer#initChannel(io.netty.channel.socket.SocketChannel)}</b>
 *
 * <b>Author: tanlin [2020/6/8 9:50]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/8 9:50        tanlin            new file.
 * <pre>
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class NettyFilterHandler extends SimpleChannelInboundHandler<MockHttpServletRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MockHttpServletRequest httpServletRequest) throws Exception {
        System.out.println("3");
        NettyServletFilterChain filterChain = NettyFilterFactory.createFilterChain(ctx,httpServletRequest);
        Optional.ofNullable(filterChain).ifPresent((chain)->{
            try {
                chain.doFilter(httpServletRequest, new MockHttpServletResponse());
            } catch (Exception ex) {
                NettyChannelUtils.sendResult(ctx, HttpResponseStatus.BAD_REQUEST, httpServletRequest, null);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
