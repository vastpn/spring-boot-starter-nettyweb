package com.centify.boot.web.embedded.netty.filter;


import com.centify.boot.web.embedded.netty.config.NettyFilterConfig;
import io.netty.channel.ChannelHandlerContext;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <b>Netty Servlet FilterChain 过滤器链配置</b>
 * <b>Describe:
 * Implementation of <code>javax.servlet.FilterChain</code> used to manage
 * the execution of a set of filters for a particular request.  When the
 * set of defined filters has all been executed, the next call to
 * <code>doFilter()</code> will execute the servlet's <code>service()</code>
 * method itself.
 * </b>
 *
 * <b>Author: tanlin [2020/5/26 10:07]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/26 10:07        tanlin            new file.
 * <pre>
 */
public final class NettyServletFilterChain implements FilterChain {
    /**
     * 过滤器集合
     */
    private List<NettyFilterConfig> filters = new ArrayList<>();

    /**
     * The int which is used to maintain the current position in the filter chain.
     */
    private int position = 0;

    private ChannelHandlerContext chc;

    /**
     * <pre>
     * <b>过滤器链顺序执行</b>
     * <b>Describe:
     * Invoke the next filter in this chain, passing the specified request
     * and response.  If there are no more filters in this chain, invoke
     * the <code>service()</code> method of the servlet itself.
     * </b>
     *
     * <b>Author: tanlin [2020/5/26 10:35]</b>
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     * <pre>
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        internalDoFilter(request, response);
    }

    private void internalDoFilter(ServletRequest request,
                                  ServletResponse response)
            throws IOException, ServletException {

        // Call the next filter if there is one
        if (position < filters.size()) {
            NettyFilterConfig filterConfig = filters.get(position);
            position++;
            Filter filter = null;
            try {
                filter = filterConfig.getFilter();
                filter.doFilter(request, response, this);
            } catch (IOException | ServletException | RuntimeException e) {

                throw e;
            } catch (Throwable e) {
                throw new ServletException();
            }
            return;
        }

        // We fell off the end of the chain -- call the servlet instance
        try {
            chc.fireChannelRead(new NettyServletWrapper().setServletRequest(request).setServletResponse(response));
        } catch (Throwable e) {
            throw new ServletException();
        }
    }

    /**
     * <pre>
     * <b>增加过滤器到过滤器链中</b>
     * <b>Describe:Add a filter to the set of filters that will be executed in this chain</b>
     *
     * <b>Author: tanlin [2020/5/26 10:43]</b>
     *
     * @param filterConfig The FilterConfig for the servlet to be executed
     * @return void
     * <pre>
     */
    public void addFilter(NettyFilterConfig filterConfig) {

        // Prevent the same filter being added multiple times
        for (NettyFilterConfig filter : filters) {
            if (filter.equals(filterConfig)) {
                return;
            }
        }
        filters.add(filterConfig);
    }

    public void setChc(ChannelHandlerContext chc) {
        this.chc = chc;
    }
}
