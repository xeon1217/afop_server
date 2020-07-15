package com.example.afop_server.Config

import com.example.afop_server.Common.Log
import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class GracefulShutdown : TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    private val tag = GracefulShutdown::class.simpleName
    private val TIMEOUT: Long = 60 // 강제종료까지 걸리는 시간
    @Volatile
    private var connector: Connector? = null

    override fun customize(connector: Connector?) {
        this.connector = connector
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        connector?.let {
            it.pause()
            val executor = it.protocolHandler.executor

            if (executor is ThreadPoolExecutor) {
                try {
                    val threadPoolExecutor: ThreadPoolExecutor = executor as ThreadPoolExecutor
                    threadPoolExecutor.shutdown()
                    if(!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                        Log.warn(tag, "Tomcat thread pool did not shut down gracefully within $TIMEOUT seconds. Proceeding with forceful shutdown")
                        threadPoolExecutor.shutdownNow()

                        if(!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                            Log.error(tag, "Tomcat thread pool did not terminate")
                        }
                    } else {
                        Log.info(tag, "Tomcat thread pool has been gracefully shutdown")
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }
}