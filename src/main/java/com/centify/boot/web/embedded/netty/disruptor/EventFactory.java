package com.centify.boot.web.embedded.netty.disruptor;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: admin [2019/12/24 16:00]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 16:00        admin            new file.
 * <pre>
 */

public class EventFactory<T> implements com.lmax.disruptor.EventFactory {

  @Override
  public SeriesDataEvent newInstance() {
    return new SeriesDataEvent();
  }
}