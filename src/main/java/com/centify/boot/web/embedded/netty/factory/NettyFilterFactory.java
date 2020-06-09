/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.centify.boot.web.embedded.netty.factory;

import com.centify.boot.web.embedded.netty.config.NettyFilterConfig;
import com.centify.boot.web.embedded.netty.filter.NettyFilterConfigRegistry;
import com.centify.boot.web.embedded.netty.filter.NettyServletFilterChain;
import com.centify.boot.web.embedded.netty.utils.SpringContextUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 * <b>Netty Filter 工厂服务</b>
 * <b>Describe:
 * Factory for the creation and caching of Filters and creation
 * of Filter Chains
 * </b>
 *
 * <b>Author: tanlin [2020/5/26 11:26]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/26 11:26        tanlin            new file.
 * <pre>
 */
@Log4j2
public final class NettyFilterFactory {

    private NettyFilterFactory() {
    }

    /**
     * <pre>
     * <b>创建Netty Servlet 过滤链</b>
     * <b>Describe:
     * Construct and return a FilterChain implementation that will wrap the
     * execution of the specified servlet instance.  If we should not execute
     * a filter chain at all, return
     * </b>
     *
     * <b>Author: tanlin [2020/5/26 11:28]</b>
     *
     * @param ctx Netty 管道上下文对项
     * @param request Http请求对象
     * @return com.centify.starter.netty.core.filter.NettyServletFilterChain
     * <pre>
     */
    public static NettyServletFilterChain createFilterChain(ChannelHandlerContext ctx, ServletRequest request) {
        NettyServletFilterChain filterChain = new NettyServletFilterChain();
        Optional.ofNullable(SpringContextUtils.getBean(NettyFilterConfigRegistry.class)
                .getApplicationFilterConfigs()).ifPresent((filterItems) -> {
            filterItems.stream().forEach((item) -> {
                //TODO 可以增加其他Path 匹配逻辑
                /**这里加入path的判断*/
                if (matchFiltersURL(item.getUrlPatterns(), ((MockHttpServletRequest) request).getRequestURI())) {
                    filterChain.addFilter(item);
                }
            });
        });
        /**执行完所有filter后，调用ctx将请求传递下去*/
        filterChain.setChc(ctx);

        /**Return the completed filter chain*/
        return filterChain;
    }

    /**
     * <pre>
     * <b>请求路径规则匹配</b>
     * <b>Describe:TODO</b>
     *
     * <b>Author: tanlin [2020/5/26 11:33]</b>
     *
     * @param urlPatterns 全局配置的URL 规则
     * @param requestPath 请求URL路径
     * @return boolean
     * <pre>
     */
    private static boolean matchFiltersURL(String urlPatterns, String requestPath) {
        /**urlPatterns NPE return false*/
        if (urlPatterns == null) {
            return false;
        }
        /**Case 1 - Exact Match*/
        if (urlPatterns.equals(requestPath)) {
            return true;
        }
        /**Case 2 - Path Match ("/.../*")*/
        if (urlPatterns.equals("/*")) {
            return true;
        }
        if (urlPatterns.endsWith("/*")) {
            if (urlPatterns.regionMatches(0, requestPath, 0,
                    urlPatterns.length() - 2)) {
                if (requestPath.length() == (urlPatterns.length() - 2)) {
                    return true;
                } else if ('/' == requestPath.charAt(urlPatterns.length() - 2)) {
                    return true;
                }
            }
            return false;
        }
        /**Case 3 - Extension Match*/
        if (urlPatterns.startsWith("*.")) {
            int slash = requestPath.lastIndexOf('/');
            int period = requestPath.lastIndexOf('.');
            if ((slash >= 0) && (period > slash)
                    && (period != requestPath.length() - 1)
                    && ((requestPath.length() - period)
                    == (urlPatterns.length() - 1))) {
                return (urlPatterns.regionMatches(2, requestPath, period + 1,
                        urlPatterns.length() - 2));
            }
        }
        /**Case 4 - "Default" Match (NOTE - Not relevant for selecting filters)*/
        return false;
    }
}
