package io.github.lasyard.camel.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class CamelFileJavaIT extends CamelTestSupport {
    @AfterEach
    public void tearDown() {
        CamelFileTestUtils.cleanUp();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new EndpointRouteBuilder() {
            @Override
            public void configure() {
                from(file(CamelFileTestUtils.inputDir).noop(true))
                    .autoStartup(false)
                    .process(exchange -> log.info("Copy file: {}.", exchange.getIn().getHeader(Exchange.FILE_NAME)))
                    .to(file(CamelFileTestUtils.outputDir));
            }
        };
    }

    @Test
    public void copyFilesTest() throws Exception {
        CamelFileTestUtils.testCopyFile(this);
    }
}
