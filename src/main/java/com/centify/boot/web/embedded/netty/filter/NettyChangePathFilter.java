package com.centify.boot.web.embedded.netty.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import javax.servlet.*;

/**
 * <pre>
 * <b>Netty 管道 过滤器（从）</b>
 * <b>Describe:
 * filter需要手动在{@link com.centify.boot.web.embedded.netty}进行注册
 * netty会将请求传入{@link com.centify.boot.web.embedded.netty.core.NettyFilterHandler},该handler内部会创建 filter 链，执行这些filter
 * </b>
 *
 * <b>Author: tanlin [2020/5/26 11:18]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/26 11:18        tanlin            new file.
 * <pre>
 */
@Log4j2
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class NettyChangePathFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) {
        MockHttpServletRequest mockRequest = (MockHttpServletRequest) request;
        try {
            chain.doFilter(mockRequest, response);
        } catch (Exception ex) {
            log.error("path changed Error："+ex.getCause().getMessage(),ex);
        }
    }

    @Override
    public void destroy() {

    }

}