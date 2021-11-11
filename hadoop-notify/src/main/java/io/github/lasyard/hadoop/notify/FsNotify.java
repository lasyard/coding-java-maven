package io.github.lasyard.hadoop.notify;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSInotifyEventInputStream;
import org.apache.hadoop.hdfs.client.HdfsAdmin;
import org.apache.hadoop.hdfs.inotify.Event;
import org.apache.hadoop.hdfs.inotify.EventBatch;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

@Slf4j
public final class FsNotify {
    private FsNotify() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
            System.setProperty("hadoop.home.dir", "/");
            Configuration configuration = new Configuration();
            configuration.addResource("server.xml");
            HdfsAdmin admin = new HdfsAdmin(URI.create(configuration.get("fs.defaultFS")), configuration);
            DFSInotifyEventInputStream stream = admin.getInotifyEventStream();
            while (true) {
                EventBatch events = stream.take();
                for (Event event : events.getEvents()) {
                    log.info("Event type: " + event.getEventType());
                    switch (event.getEventType()) {
                        case CREATE:
                            Event.CreateEvent createEvent = (Event.CreateEvent) event;
                            log.info("\tPath: " + createEvent.getPath());
                            log.info("\tOwner:" + createEvent.getOwnerName());
                            break;
                        case RENAME:
                            Event.RenameEvent renameEvent = (Event.RenameEvent) event;
                            log.info("\tSrcPath: " + renameEvent.getSrcPath());
                            log.info("\tDstPath: " + renameEvent.getDstPath());
                            break;
                        case CLOSE:
                            Event.CloseEvent closeEvent = (Event.CloseEvent) event;
                            log.info("\tPath: " + closeEvent.getPath());
                            break;
                        case UNLINK:
                            Event.UnlinkEvent unlinkEvent = (Event.UnlinkEvent) event;
                            log.info("\tPath: " + unlinkEvent.getPath());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
