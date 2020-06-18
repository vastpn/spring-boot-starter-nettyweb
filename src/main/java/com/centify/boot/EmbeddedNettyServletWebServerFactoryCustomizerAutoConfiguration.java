package com.centify.boot;

import com.centify.boot.web.embedded.netty.factory.NettyServletWebServerFactory;
import io.netty.bootstrap.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;


/**
 * <pre>
 * <b>Netty 容器</b>
 * <b>Describe:
 * ConditionalOnWebApplication 在Web环境下才会起作用
 * ConditionalOnClass({Bootstrap.class}) // Netty的Bootstrap类必须在classloader中存在，才能启动Netty容器
 * ConditionalOnMissingBean(value = EmbeddedNettyFactory.class, search = SearchStrategy.CURRENT) //当前Spring容器中不存在EmbeddedServletContainerFactory接口的实例
 * 上述条件注解成立的话就会构造EmbeddedNettyFactory这个EmbeddedServletContainerFactory
 * </b>
 *
 * <b>Author: tanlin [2020/6/8 9:55]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/8 9:55        tanlin            new file.
 * <pre>
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(ServerProperties.class)
public class EmbeddedNettyServletWebServerFactoryCustomizerAutoConfiguration {

    @Configuration
    @ConditionalOnClass({Bootstrap.class})
    @ConditionalOnMissingBean(value = NettyServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    public static class NettyServletWebServerFactoryCustomizerConfiguration {
        private static final Logger LOGGER = LoggerFactory.getLogger(NettyServletWebServerFactoryCustomizerConfiguration.class);
        @Bean
        public NettyServletWebServerFactory embeddedNettyFactory(Environment environment,
                                                                 ServerProperties serverProperties) {
            LOGGER.info("[Container] Embedded Netty Servlet WebServer :{} ,{},context = {}", environment, serverProperties);
            return new NettyServletWebServerFactory(environment, serverProperties);
        }

//        /**
//         * <pre>
//         * <b>创建NettyFilterConfigRegistry 对象，由Spring容器管理</b>
//         * <b>Describe:
//         * Filter过滤所有请求</b>
//         *
//         * <b>Author: tanlin [2020/5/26 11:15]</b>
//         *
//         * @return com.centify.starter.netty.core.filter.NettyFilterConfigRegistry
//         * <pre>
//         */
//        @Bean
//        @Order(Integer.MAX_VALUE)
//        public NettyFilterConfigRegistry getNettyFilterConfigRegistry(List<FilterRegistrationBean> registFilters) {
//            NettyFilterConfigRegistry registry = new NettyFilterConfigRegistry();
//
//            Optional.ofNullable(registFilters).ifPresent((items) -> {
//                items.stream().sorted(Comparator.comparingInt(FilterRegistrationBean::getOrder)).forEach((item) -> {
//                    NettyFilterConfig nfc = new NettyFilterConfig();
//                    nfc.setFilter(item.getFilter());
//                    nfc.setFilterName(item.getFilter().getClass().getSimpleName());
//                    nfc.setUrlPatterns("/*");
//                    registry.register(nfc);
//                });
//            });
//            return registry;
//        }
    }

//    /**
//     * <pre>
//     * <b>项目 过滤器注册 DEMO</b>
//     * <b>Describe:
//     * 1、过滤器实现Filter 且实现Order
//     * 2、过滤注册器注册，并设置顺序</b>
//     *
//     * <b>Author: tanlin [2020/6/9 16:10]</b>
//     *
//     * @param nettyChangePathFilter TODO
//     * @return org.springframework.boot.web.servlet.FilterRegistrationBean<com.centify.boot.web.embedded.netty.filter.NettyChangePathFilter>
//     * <pre>
//     */
//    @Bean
//    @Order(100)
//    public FilterRegistrationBean<NettyChangePathFilter> filterRegistrationDemo2(NettyChangePathFilter nettyChangePathFilter) {
//
//        FilterRegistrationBean<NettyChangePathFilter> myFilterFilterRegistrationBean = new FilterRegistrationBean<>();
//        myFilterFilterRegistrationBean.setFilter(nettyChangePathFilter);
//        myFilterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
//        return myFilterFilterRegistrationBean;
//    }
}
