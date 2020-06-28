package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.utils.NettyChannelUtil;
import com.centify.boot.web.embedded.netty.utils.SpringContextUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

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
public class DispatcherServletHandler extends SimpleChannelInboundHandler<MockHttpServletRequest> {
	private ServletContext context;
	public DispatcherServletHandler(ServletContext context) {
		this.context = context;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext chc, MockHttpServletRequest rrw) throws Exception {
//		System.out.println("4");
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

		SpringContextUtil.getApplication().getBean(DispatcherServlet.class)
				.service(rrw, servletResponse);

		NettyChannelUtil.sendResultByteBuf(
				chc,
				HttpResponseStatus.valueOf(servletResponse.getStatus()),
				rrw,
				Unpooled.wrappedBuffer(servletResponse.getContentAsByteArray())
		);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
