package com.centify.boot.web.embedded.netty.factory;

import com.centify.boot.web.embedded.netty.context.NettyServletContext;
import com.centify.boot.web.embedded.netty.core.NettyContainer;
import com.centify.boot.web.embedded.netty.utils.ReflectionUtils;
import io.netty.bootstrap.Bootstrap;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.InetSocketAddress;

/**
 * <pre>
 * <b>获取Netty Web 容器</b>
 * <b>Describe:
 * 1、Netty Servlet WebServer 工厂服务类
 * 2、SpringBoot 自动注入并获取web应用的容器</b>
 *
 * <b>Author: tanlin [2020/6/8 9:53]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/8 9:53        tanlin            new file.
 * <pre>
 */
@Log4j2
public class NettyServletServletWebServerFactory extends AbstractServletWebServerFactory implements ConfigurableNettyServletWebServerFactory, ResourceLoaderAware {

    /**资源加载器*/
    private ResourceLoader resourceLoader;

    /**环境配置对象*/
    private final Environment environment;

    /**WebServer 配置属性*/
    private final ServerProperties serverProperties;

    /**应用服务上下文对象*/
    private ServletWebServerApplicationContext servletWebServerApplicationContext;

    public NettyServletServletWebServerFactory(Environment environment, ServerProperties serverProperties, ServletWebServerApplicationContext servletWebServerApplicationContext) {
        this.environment = environment;
        this.serverProperties = serverProperties;
        this.servletWebServerApplicationContext = servletWebServerApplicationContext;
    }


    @SneakyThrows
    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        /**Netty启动环境相关信息*/
        Package nettyPackage = Bootstrap.class.getPackage();
        String title = nettyPackage.getImplementationTitle();
        String version = nettyPackage.getImplementationVersion();
        log.info("Running with " + title + " " + version);
        /**是否支持默认Servlet*/
        if (isRegisterDefaultServlet()) {
            log.warn("This container does not support a default servlet");
        }
        log.info("Running end " + title + " " + version);

        /**Servlet 上下文 设置*/
        ServletContext context = setServletContext();

        /**容器初始化工厂 上下文设置*/
        onStartup(context, initializers);

        /**Servlet 分发器初始化*/
        dispatcherServletInit();

        /**从SpringBoot配置中获取端口，如果没有则随机生成*/
        int port = getPort() > 0 ? getPort() : 8080;
        InetSocketAddress address = new InetSocketAddress(port);
        log.info("Server initialized with port: {}" ,port);
        /**初始化容器并返回*/
        return new NettyContainer(address, context);
    }

    private void dispatcherServletInit() throws ServletException {
        /**从spring上下文获取DispatcherServlet，对其进行处理*/
        DispatcherServlet dispatcherServlet = servletWebServerApplicationContext.getBean(DispatcherServlet.class);
        MockServletConfig myServletConfig = new MockServletConfig();
        ReflectionUtils.setFieldValue(dispatcherServlet, "config", myServletConfig);
        /**初始化servlet*/
        dispatcherServlet.init();
    }

    private void onStartup(ServletContext context, ServletContextInitializer[] initializers) {
        for (ServletContextInitializer initializer : initializers) {
            try {
                initializer.onStartup(context);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ServletContext setServletContext() {
        ServletContext context = new NettyServletContext();
        servletWebServerApplicationContext.setServletContext(context);
        return context;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
