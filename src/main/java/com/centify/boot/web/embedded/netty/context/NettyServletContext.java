package com.centify.boot.web.embedded.netty.context;

import lombok.extern.log4j.Log4j2;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

/**
 * <pre>
 * <b>实现MockServletContext，设置addServlet、addFilter为空</b>
 * <b>Describe:
 * 1、只需要有ServletContext空对象，否则Springboot启动时抛NPE
 * 2、SpringBoot-> Netty-->重新赋值addServlet、addFilter</b>
 *
 * <b>Author: tanlin [2020/5/24 11:49]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/24 11:49        tanlin            new file.
 * <pre>
 */
@Log4j2
public class NettyServletContext extends MockServletContext{

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter){
        return null;
    }
}
