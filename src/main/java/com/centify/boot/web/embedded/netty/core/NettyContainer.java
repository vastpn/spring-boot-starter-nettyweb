package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.context.NettyServletContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;

import java.net.InetSocketAddress;

/**
 * <pre>
 * <b>Netty servle容器</b>
 * <b>Describe:只支持 前后端分离模式。即不支持JSP</b>
 *
 * <b>Author: tanlin [2020/6/8 9:47]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/8 9:47        tanlin            new file.
 * <pre>
 */
@Log4j2
public class NettyContainer implements WebServer {
    /**ServletContext 全局上下文*/
    private final NettyServletContext servletContext;

    /**监听端口地址*/
    private final InetSocketAddress address;

    /**Netty事件接收线程池组*/
    private EventLoopGroup acceptGroup;

    /**Netty事件处理线程池组*/
    private EventLoopGroup workerGroup;


    public NettyContainer(InetSocketAddress address, NettyServletContext servletContext) {
        this.address = address;
        this.servletContext = servletContext;
    }

    @Override
    public void start() throws WebServerException {
        /**服务启动对象*/
        ServerBootstrap bootstrap = new ServerBootstrap();
        /**接收请求工作组*/
        acceptGroup = new NioEventLoopGroup(1,
                new DefaultThreadFactory("acceptGroup"));
        /**处理请求工作组*/
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("workerGroup"));
        try {
            /**绑定接收请求、处理请求工作组，并设置HTTP/TCP通讯参数*/
            bootstrap.group(acceptGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address.getPort())
                    /*是否允许端口占用*/
                    .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    /*设置可处理队列数量*/
                    .option(ChannelOption.SO_BACKLOG, 4096)
                    .option(NioChannelOption.SO_RCVBUF, 4*1024)
                    /*响应时间有高要求的场景 禁用nagle 算法*/
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.FALSE)
                    /*ByteBuf重用缓冲区*/
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

                    /*响应时间有高要求的场景 禁用nagle 算法*/
                    .childOption(NioChannelOption.TCP_NODELAY, Boolean.TRUE)
                    /*是否允许端口占用*/
                    .childOption(NioChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    /*是否设置长连接*/
                    .childOption(NioChannelOption.SO_KEEPALIVE, Boolean.FALSE)
                    /*设置接收数据大小 设置为4K*/
                    .childOption(NioChannelOption.SO_RCVBUF, 4*1024)
                    /*设置发送数据大小 设置为16K*/
                    .childOption(NioChannelOption.SO_SNDBUF, 16*1024)
                    /*设置ByteBuf重用缓冲区*/
                    .childOption(NioChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new DispatcherServletChannelInitializer(servletContext));

            /**绑定端口，并打印端口信息*/
            ChannelFuture channelFuture = bootstrap.bind(address).syncUninterruptibly().addListener(future -> {
                StringBuilder logBanner = new StringBuilder();
                logBanner.append("\n\n")
                        .append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n")
                        .append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n")
                        .append("* *                                                                               * *\n")
                        .append("                     Netty Http Server started on port {}.                     \n")
                        .append("* *                                                                               * *\n")
                        .append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n")
                        .append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
                log.info(logBanner.toString(), address.getPort());
            });
            System.out.println(""+address.getHostString()+"" +address.getPort());
            /**通过引入监听器对象监听future状态，当future任务执行完成后会调用-》{}内的方法*/
            channelFuture.channel().closeFuture().addListener(future -> {
                log.info("Netty Http服务停止开始!");
                /**优雅关闭*/
                acceptGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                log.info("Netty Http服务停止完成!");
            });
        } catch (Exception e) {
            log.error("Netty Start Error " ,e);
            acceptGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.error("Netty Start Error，资源释放完成！");
        }

        log.info(" started on port: " + getPort());
    }

    /**
     * 优雅地关闭各种资源
     *
     * @throws WebServerException
     */
    @Override
    public void stop() throws WebServerException {
        log.info("Embedded Netty Servlet Container shuting down.");
        try {
            if (null != acceptGroup) {
                acceptGroup.shutdownGracefully().await();
            }
            if (null != workerGroup) {
                workerGroup.shutdownGracefully().await();
            }
        } catch (InterruptedException e) {
            throw new WebServerException("Container stop interrupted", e);
        }
    }

    @Override
    public int getPort() {
        return address.getPort();
    }
}
