package com.centify.boot.web.embedded.netty.core;

import com.centify.boot.web.embedded.netty.context.NettyServletContext;
import com.centify.boot.web.embedded.netty.utils.SpringContextUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * <pre>
 * <b>Netty 通讯管道初始化</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: tanlin [2020/5/24 12:31]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/5/24 12:31        tanlin            new file.
 * <pre>
 */
@Log4j2
@Component
public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**请求粘包最大长度（256KB）*/
    private static final Integer REQUEST_DATA_MAXCONTENTLENGTH = 256 * 1024;
    public DispatcherServletChannelInitializer() {
    }

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        /**必须第一步对通信数据进行编解码 (已包含HttpRequestDecoder/HttpResponseEncoder)*/
        channel.pipeline()
                /**读取超时*/
                .addLast("RTimeout",new ReadTimeoutHandler(1))
                /**写入超时*/
                .addLast("WTimeout",new WriteTimeoutHandler(1))
                /**转码*/
                .addLast("HttpCodec",new HttpServerCodec())
                /**请求数据粘包设置*/
                .addLast("HttpObject",new HttpObjectAggregator(REQUEST_DATA_MAXCONTENTLENGTH))
                /**用于处理大的数据流*/
                .addLast("ChunkedWrite",new ChunkedWriteHandler())

                /**过滤 favicon.ico 请求*/
                .addLast("Favicon",SpringContextUtil.getApplication().getBean(FaviconHandler.class))
                /**FullHttpRequest转换为MockHttpServletRequest*/
                .addLast("MockTransform", SpringContextUtil.getApplication().getBean( MockTransformFullHttpHandler.class))
                /**转交给SpringMVC dispatcherServlet 处理业务逻辑，可正常使用Spring RestController 等注解*/
                .addLast( "DispatcherServlet", SpringContextUtil.getApplication().getBean(DispatcherServletHandler.class));

    }


}
