package com.centify.boot.web.embedded.netty.config;


import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * <pre>
 * <b>Netty Servlet FilterConfig</b>
 * <b>Describe:
 * Implementation of a <code>javax.servlet.FilterConfig</code> useful in
 * managing the filter instances instantiated when a web application
 * is first started
 * </b>
 *
 * <b>Author: tanlin [2020/5/26 11:25]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/26 11:25        tanlin            new file.
 * <pre>
 */
@Data
@Log4j2
public final class NettyFilterConfig implements FilterConfig {
    /**
     * filter对象
     */
    private Filter filter;

    /**
     * 过滤器名称
     */
    private String filterName;

    /**
     * 过滤器的url模式；遵循传统web.xml中的语法
     * 参考：https://www.cnblogs.com/51kata/p/5152400.html
     */
    private String urlPatterns;

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
