//package com.centify.boot.web.embedded.netty.core;
//
//import com.centify.boot.web.embedded.netty.context.NettyServletContext;
//import com.centify.boot.web.embedded.netty.utils.NettyChannelUtil;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.FullHttpRequest;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
///**
// * <pre>
// * <b>FullHttpRequest 转换为MockHttpServletRequest </b>
// * <b>Describe:</b>
// *
// * <b>Author: tanlin [2020/5/24 15:13]</b>
// * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
// * <b>Changelog:</b>
// *   Ver   Date                  Author           Detail
// *   ----------------------------------------------------------------------------
// *   1.0   2020/5/24 15:13        tanlin            new file.
// * <pre>
// */
//@Log4j2
//@ChannelHandler.Sharable
//public class FullHttpTransformServletHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
//    private final NettyServletContext servletContext;
//
//    public FullHttpTransformServletHandler(NettyServletContext servletContext) {
//        this.servletContext = servletContext;
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext chc, FullHttpRequest fullHttpRequest) throws Exception {
//        chc.fireChannelRead(NettyChannelUtil.createServletRequest(chc,servletContext,fullHttpRequest));
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
//    }
//}
