package com.centify.boot.web.embedded.netty.filter;

import com.centify.boot.web.embedded.netty.config.NettyFilterConfig;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <b>Netty Http 过滤器注册表</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: tanlin [2020/5/25 16:38]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/25 16:38        tanlin            new file.
 * <pre>
 */
@Data
@Log4j2
public class NettyFilterConfigRegistry {

    /**过滤器配置列表*/
    private List<NettyFilterConfig> applicationFilterConfigs = new ArrayList<>();

    public void register(NettyFilterConfig nettyFilterConfig){
        applicationFilterConfigs.add(nettyFilterConfig);
    }


}
