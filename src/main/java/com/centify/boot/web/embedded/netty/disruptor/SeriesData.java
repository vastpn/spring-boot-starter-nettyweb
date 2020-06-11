package com.centify.boot.web.embedded.netty.disruptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * <pre>
 * <b>数据传输对象[disruptor]</b>
 * <b>Describe:用于在disruptor 中事件传输数据对象</b>
 *
 * <b>Author: admin [2019/12/24 16:00]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2019/12/24 16:00        admin            new file.
 * <pre>
 */
@Data
@Accessors(chain = true)
public class SeriesData implements Serializable {

  /**消息内容*/
  private Object message;
  /**消息唯一编号*/
  private String reqSeqNo = UUID.randomUUID().toString();
  /**时间戳*/
  private Long stampNo = System.currentTimeMillis();

  public SeriesData(Object message) {
    this.message = message;
  }

  //
//  public static enum EventEnum{
//    event_service("service",Class.class);
//    private String service;
//    private Class<?> requiredType;
//    private Obje
//    EventEnum(String service, Class<Class> requiredType) {
//      this.service = service;
//      this.requiredType = requiredType;
//    }
//    public String getService() {
//      return service;
//    }
//    public void setService(String service) {
//      this.service = service;
//    }
//    public Class<?> getRequiredType() {
//      return requiredType;
//    }
//    public void setRequiredType(Class<?> requiredType) {
//      this.requiredType = requiredType;
//    }
//  }
}
