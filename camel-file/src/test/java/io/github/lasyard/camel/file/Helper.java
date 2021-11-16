package io.github.lasyard.camel.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.test.junit5.CamelTestSupport;

import java.io.File;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;

import static org.apache.camel.test.junit5.TestSupport.deleteDirectory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
final class Helper {
    private static final ResourceBundle res = ResourceBundle.getBundle("test-camel-file");

    private static final String testFileName = res.getString("test.file.name");
    private static final String inputDir = res.getString("file.inbox");
    static final String fileInputUri = "file:" + inputDir + "?noop=true";
    private static final String outputDir = res.getString("file.outbox");
    static final String fileOutputUri = "file:" + outputDir;

    private Helper() {
    }

    static void cleanUp() {
        deleteDirectory(inputDir);
        log.info(inputDir + " deleted.");
        deleteDirectory(outputDir);
        log.info(outputDir + " deleted.");
    }

    static void testCopyFile(@Nonnull CamelTestSupport cts) throws Exception {
        log.debug("fileInputUri = {}.", fileInputUri);
        log.info("{}", cts.context());
        cts.context().start();
        cts.context().getRouteController().startAllRoutes();
        String testString = Double.toString(Math.random());
        cts.template().sendBodyAndHeader(fileInputUri, testString, Exchange.FILE_NAME, testFileName);
        File srcFile = new File(inputDir + File.separator + testFileName);
        assertTrue(srcFile.exists());
        // By default, camel checks the directory twice a second.
        Thread.sleep(3000);
        File dstFile = new File(outputDir + File.separator + testFileName);
        assertTrue(dstFile.exists());
        String content = cts.context().getTypeConverter().convertTo(String.class, dstFile);
        assertThat(content).isEqualTo(testString);
        cts.context().stop();
    }
}
