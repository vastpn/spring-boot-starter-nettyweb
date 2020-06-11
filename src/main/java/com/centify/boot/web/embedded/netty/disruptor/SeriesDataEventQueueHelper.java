package com.centify.boot.web.embedded.netty.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: admin [2019/12/24 16:01]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 16:01        admin            new file.
 * <pre>
 */

@Component
public class SeriesDataEventQueueHelper extends BaseQueueHelper<SeriesData, SeriesDataEvent, SeriesDataEventHandler> implements InitializingBean {

  private static final int QUEUE_SIZE = 1024;

  @Autowired
  private List<SeriesDataEventHandler> seriesDataEventHandler;


  @Override
  protected int getQueueSize() {
    return QUEUE_SIZE;
  }

  @Override
  protected EventFactory eventFactory() {
    return new EventFactory();
  }

  @Override
  protected WorkHandler[] getHandler() {
    return seriesDataEventHandler.toArray(new WorkHandler[seriesDataEventHandler.size()]);
  }

  @Override
  protected WaitStrategy getStrategy() {
    return new BlockingWaitStrategy();
    //return new YieldingWaitStrategy();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.init();
  }
}