package com.centify.boot.web.embedded.netty.filter;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <pre>
 * <b>Servlet 请求响应对象包装类，用于Handler参数传递</b>
 * <b>Describe:
 * 1、包含 ServletRequest、ServletResponse 对象
 * (可以继承ServletRequestWrapper ，然后再补充ServletResponse 对象。但需要验证ServletRequestWrapper 所有方法是否能获取、设置数据)
 * </b>
 *
 * <b>Author: tanlin [2020/5/25 16:41]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/25 16:41        tanlin            new file.
 * <pre>
 */
@Data
@Accessors(chain = true)
public class NettyServletWrapper {
    /**ServletRequest 请求对象*/
    private ServletRequest servletRequest;
    /**ServletResponse 响应对象*/
    private ServletResponse servletResponse;
}
