package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.constant.NettyConstant;
import com.centify.boot.web.embedded.netty.utils.NettyChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * <pre>
 * <b>FullHttpRequest 转换为MockHttpServletRequest </b>
 * <b>Describe:线程非安全，不能使用@Component、@ChannelHandler.Sharable
 * </b>
 *
 * <b>Author: tanlin [2020/5/24 15:13]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/24 15:13        tanlin            new file.
 * <pre>
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class FaviconHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext chc, FullHttpRequest fullHttpRequest) throws Exception {
//        System.out.println("1");
        /**验证解码*/
        if (!fullHttpRequest.decoderResult().isSuccess()) {
            NettyChannelUtil.sendResult(chc, HttpResponseStatus.BAD_REQUEST, fullHttpRequest, null);
            return;
        }
        //TODO 后期支持 YML配置 动态启用、禁用 /favicon.ico 请求
        if(NettyConstant.HTTP_REQUEST_FAVICON.equalsIgnoreCase(fullHttpRequest.uri())){
            chc.close();
            return ;
        }
        chc.fireChannelRead(fullHttpRequest);
    }

    /**
     * <pre>
     * <b>获取远程客户端IP</b>
     * <b>Describe:</b>
     *
     * <b>Author: tanlin [2020/5/24 15:28]</b>
     *
     * @param httpRequest http请求对象
     * @param channelHandlerContext 数据管道上下文对象
     * @return String 客户端IP
     * <pre>
     */
    public String getRemoteIP(FullHttpRequest httpRequest, ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        String ip = "";
        try {
            String ipForwarded = httpRequest.headers().get("x-forwarded-for");
            if (StringUtils.isBlank(ipForwarded) || "unknown".equalsIgnoreCase(ipForwarded)) {
                InetSocketAddress insocket = (InetSocketAddress) channel.remoteAddress();
                ip = insocket.getAddress().getHostAddress();
            } else {
                ip = ipForwarded;
            }
        } catch (Exception e) {
//            log.error("getRemoteIP(): get remote ip fail!", e);
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
