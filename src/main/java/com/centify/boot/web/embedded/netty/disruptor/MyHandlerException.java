package com.centify.boot.web.embedded.netty.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.log4j.Log4j2;

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

@Log4j2
public class MyHandlerException implements ExceptionHandler {
  /**
   * <pre>
   * <b>TODO</b>
   * <b>Describe:TODO</b>
   * (non-Javadoc) 运行过程中发生时的异常
   *
   * @see
   * com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable
   * , long, java.lang.Object)
   * <b>Author: tanlin [2020/6/11 13:42]</b>
   *
   * @param ex TODO
   * @param sequence TODO
   * @param event TODO
   * @return
   * <pre>
   */
  @Override
  public void handleEventException(Throwable ex, long sequence, Object event) {
    ex.printStackTrace();
    log.error("process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event.toString(), ex.getMessage());
  }

  /**
   * <pre>
   * <b>(non-Javadoc) 启动时的异常</b>
   * <b>Describe:com.lmax.disruptor.ExceptionHandler</b>
   *
   * <b>Author: tanlin [2020/6/11 13:43]</b>
   *
   * @param ex TODO
   * @return void
   * <pre>
   */
  @Override
  public void handleOnStartException(Throwable ex) {
    log.error("start disruptor error ==[{}]!", ex.getMessage());
  }

  /**
   * <pre>
   * <b>TODO</b>
   * <b>Describe:TODO</b>
   * @see com.lmax.disruptor.ExceptionHandler
   * .Throwable)
   * <b>Author: tanlin [2020/6/11 13:42]</b>
   *
   * @param ex TODO
   * @return void
   * <pre>
   */
  @Override
  public void handleOnShutdownException(Throwable ex) {
    log.error("shutdown disruptor error ==[{}]!", ex.getMessage());
  }

}