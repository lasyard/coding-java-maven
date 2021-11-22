package io.github.lasyard.jmx;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

/**
 * JMX demo.
 *
 * <p>Run it and do
 *
 * <p>{@code
 * curl localhost:8082
 * }
 */
@Slf4j
public final class HelloAgent {
    private HelloAgent() {
    }

    public static void main(String[] args) throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName helloName = new ObjectName("lasy:name=Hello");
        server.registerMBean(new Hello(), helloName);

        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        server.registerMBean(adapter, adapterName);

        adapter.start();
        log.info("Server started ...");
    }
}
