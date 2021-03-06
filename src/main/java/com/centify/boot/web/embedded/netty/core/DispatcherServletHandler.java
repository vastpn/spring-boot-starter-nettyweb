package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.filter.NettyServletWrapper;
import com.centify.boot.web.embedded.netty.utils.NettyChannelUtils;
import com.centify.boot.web.embedded.netty.utils.SpringContextUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;
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
@Component
@ChannelHandler.Sharable
public class DispatcherServletHandler extends SimpleChannelInboundHandler<NettyServletWrapper> {

	@Override
	protected void channelRead0(ChannelHandlerContext chc, NettyServletWrapper rrw) throws Exception {
		System.out.println("4");
//        MockHttpServletRequest servletRequest = (MockHttpServletRequest) rrw.getServletRequest();
        MockHttpServletResponse servletResponse = (MockHttpServletResponse) rrw.getServletResponse();

		SpringContextUtils.getApplicationContext().getBean(DispatcherServlet.class)
				.service(rrw.getServletRequest(), servletResponse);

		NettyChannelUtils.sendResult(
				chc,
				HttpResponseStatus.valueOf(servletResponse.getStatus()),
				rrw,
				servletResponse.getContentAsByteArray()
		);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
