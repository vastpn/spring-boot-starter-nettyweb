package com.centify.boot.embedded.netty;

import com.centify.boot.embedded.netty.core.DynamicProtocolChannelHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 * <b>TODO</b>
 * <b>Describe:TODO</b>
 *
 * <b>Author: tanlin [2020/6/11 10:02]</b>
 * <b>Copyright:</b> Copyright 2008-2026 http://www.jinvovo.com Technology Co., Ltd. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   Date                  Author           Detail
 *   ----------------------------------------------------------------------------
 *   1.0   2020/6/11 10:02        tanlin            new file.
 * <pre>
 */
@Data
@ConfigurationProperties(prefix = "server.netty", ignoreUnknownFields = true)
public class NettyProperties {
    /**
     * 服务端 - TCP级别最大同时在线的连接数
     */
    private int maxConnections = 10000;
    /**
     * 服务端 - 是否tcp数据包日志
     */
    private boolean enableTcpPackageLog = false;
    /**
     * 服务端 - 第一个客户端包的超时时间 (毫秒)
     */
    private long firstClientPacketReadTimeoutMs = 800;
    /**
     * 服务端-IO线程数  注: (0 = cpu核数 * 2 )
     */
    private int serverIoThreads = 0;
    /**
     * 服务端-io线程执行调度与执行io事件的百分比. 注:(100=每次只执行一次调度工作, 其他都执行io事件), 并发高的时候可以设置最大
     */
    private int serverIoRatio = 100;

    /**
     * 是否禁用Nagle算法，true=禁用Nagle算法. 即数据包立即发送出去 (在TCP_NODELAY模式下，假设有3个小包要发送，第一个小包发出后，接下来的小包需要等待之前的小包被ack，在这期间小包会合并，直到接收到之前包的ack后才会发生)
     */
    private boolean tcpNodelay = false;
    /**
     * 动态协议处理器,是在进入所有协议之前的入口- 使用者可以继承它加入自己的逻辑 比如:(处理超出最大tcp连接数时的逻辑, 处理遇到不支持的协议时的逻辑等..)
     */
    private Class<?extends DynamicProtocolChannelHandler> channelHandler = DynamicProtocolChannelHandler.class;

    @NestedConfigurationProperty
    private final HttpServlet httpServlet = new HttpServlet();

    public static class HttpServlet{
        /**
         * 请求体最大字节
         */
        private int maxContentSize = 5 * 1024 * 1024;
        /**
         * 请求头每行最大字节
         */
        private int maxHeaderLineSize = 4096;
        /**
         * 请求头最大字节
         */
        private int maxHeaderSize = 8192;
        /**
         * 大于这个字节则进行分段传输
         */
        private int maxChunkSize = 5 * 1024 * 1024;
        /**
         * 服务端 - servlet线程执行器
         */
        private Class<?extends Executor> serverHandlerExecutor = null;
        /**
         * 服务端 - servlet3异步特性。 异步dispatch的线程执行器 (默认用的是netty的IO线程)
         */
        private Class<?extends ExecutorService> asyncExecutorService = null;
        /**
         * 服务端 - servlet3的异步特性。 异步回调是否切换至新的线程执行任务, 如果没有异步嵌套异步的情况,建议开启.因为只有给前端写数据的IO损耗.
         * (设置false会减少一次线程切换, 用回调方的线程执行. 提示:tomcat是true，用新线程执行)
         */
        private boolean asyncSwitchThread = true;
        /**
         * session存储 - 是否开启本地文件存储
         */
        private boolean enablesLocalFileSession = false;

        /**
         * session存储 - session远程存储的url地址, 注: 如果不设置就不会开启
         */
        private String sessionRemoteServerAddress;

        /**
         * 每次调用servlet的 OutputStream.Writer()方法写入的最大堆字节,超出后用堆外内存
         */
        private int responseWriterChunkMaxHeapByteLength = 4096 * 10;

        /**
         * servlet文件存储的根目录。(servlet文件上传下载) 如果未指定，则使用临时目录。
         */
        private File basedir;

        /**
         * 是否开启DNS地址查询. true=开启 {@link javax.servlet.ServletRequest#getRemoteHost}
         */
        private boolean enableLookup = false;

        public boolean isAsyncSwitchThread() {
            return asyncSwitchThread;
        }

        public void setAsyncSwitchThread(boolean asyncSwitchThread) {
            this.asyncSwitchThread = asyncSwitchThread;
        }

        public Class<? extends ExecutorService> getAsyncExecutorService() {
            return asyncExecutorService;
        }

        public void setAsyncExecutorService(Class<? extends ExecutorService> asyncExecutorService) {
            this.asyncExecutorService = asyncExecutorService;
        }

        public boolean isEnableLookup() {
            return enableLookup;
        }

        public void setEnableLookup(boolean enableLookup) {
            this.enableLookup = enableLookup;
        }

        public int getMaxContentSize() {
            return maxContentSize;
        }

        public void setMaxContentSize(int maxContentSize) {
            this.maxContentSize = maxContentSize;
        }

        public int getMaxHeaderLineSize() {
            return maxHeaderLineSize;
        }

        public void setMaxHeaderLineSize(int maxHeaderLineSize) {
            this.maxHeaderLineSize = maxHeaderLineSize;
        }

        public int getMaxHeaderSize() {
            return maxHeaderSize;
        }

        public void setMaxHeaderSize(int maxHeaderSize) {
            this.maxHeaderSize = maxHeaderSize;
        }

        public int getMaxChunkSize() {
            return maxChunkSize;
        }

        public void setMaxChunkSize(int maxChunkSize) {
            this.maxChunkSize = maxChunkSize;
        }

        public Class<?extends Executor> getServerHandlerExecutor() {
            return serverHandlerExecutor;
        }

        public void setServerHandlerExecutor(Class<?extends Executor> serverHandlerExecutor) {
            this.serverHandlerExecutor = serverHandlerExecutor;
        }

        public boolean isEnablesLocalFileSession() {
            return enablesLocalFileSession;
        }

        public void setEnablesLocalFileSession(boolean enablesLocalFileSession) {
            this.enablesLocalFileSession = enablesLocalFileSession;
        }

        public String getSessionRemoteServerAddress() {
            return sessionRemoteServerAddress;
        }

        public void setSessionRemoteServerAddress(String sessionRemoteServerAddress) {
            this.sessionRemoteServerAddress = sessionRemoteServerAddress;
        }

        public int getResponseWriterChunkMaxHeapByteLength() {
            return responseWriterChunkMaxHeapByteLength;
        }

        public void setResponseWriterChunkMaxHeapByteLength(int responseWriterChunkMaxHeapByteLength) {
            this.responseWriterChunkMaxHeapByteLength = responseWriterChunkMaxHeapByteLength;
        }

        public File getBasedir() {
            return basedir;
        }

        public void setBasedir(File basedir) {
            this.basedir = basedir;
        }
    }
}
