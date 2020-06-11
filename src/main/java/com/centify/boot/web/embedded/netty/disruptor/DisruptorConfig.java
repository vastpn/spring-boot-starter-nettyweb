package com.centify.boot.web.embedded.netty.disruptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: admin [2019/12/24 15:59]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 15:59        admin            new file.
 * <pre>
 */

@Configuration
public class DisruptorConfig {

  /**
   * smsParamEventHandler1
   *
   * @return SeriesDataEventHandler
   */
  @Bean
  public SeriesDataEventHandler seriesDataEventHandler() {
    return new SeriesDataEventHandler();
  }
}