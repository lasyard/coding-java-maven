package io.github.lasyard.camel.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class CamelFileXmlIT extends CamelSpringTestSupport {
    @AfterEach
    public void tearDown() {
        Helper.cleanUp();
    }

    @Override
    public AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("test-camel-file.xml");
    }

    @Test
    public void copyFilesTest() throws Exception {
        Helper.testCopyFile(this);
    }
}
