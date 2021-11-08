package io.github.lasyard.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HadoopIoIT {
    private static final Path PATH = new Path("test.txt");

    private static FileSystem openHdfs() throws IOException {
        System.setProperty("hadoop.home.dir", "/");
        Configuration configuration = new Configuration();
        configuration.addResource("server.xml");
        return FileSystem.get(configuration);
    }

    @AfterAll
    public static void tearDownAll() throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
            try (FileSystem fileSystem = openHdfs()) {
                if (fileSystem.exists(PATH)) {
                    fileSystem.delete(PATH, true);
                }
            }
            return null;
        });
    }

    @Test
    public void readWriteTest() throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
            try (FileSystem fileSystem = openHdfs()) {
                FSDataOutputStream out = fileSystem.create(PATH, true);
                String string = UUID.randomUUID().toString();
                out.writeUTF(string);
                out.close();
                FSDataInputStream in = fileSystem.open(PATH);
                assertThat(in.readUTF()).isEqualTo(string);
                in.close();
            }
            return null;
        });
    }

    @Test
    public void reCreateTest() throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        assertThrows(FileAlreadyExistsException.class, () -> {
            ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
                try (FileSystem fileSystem = openHdfs()) {
                    FSDataOutputStream out = fileSystem.create(PATH, true);
                    out.close();
                    out = fileSystem.create(PATH, false);
                    out.close();
                }
                return null;
            });
        });
    }

    @Test
    public void openNonExistTest() throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        assertThrows(FileNotFoundException.class, () -> {
            ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
                try (FileSystem fileSystem = openHdfs()) {
                    if (fileSystem.exists(PATH)) {
                        fileSystem.delete(PATH, true);
                    }
                    FSDataInputStream in = fileSystem.open(PATH);
                    in.close();
                }
                return null;
            });
        });
    }

    @Test
    public void appendTest() throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
            try (FileSystem fileSystem = openHdfs()) {
                FSDataOutputStream out = fileSystem.create(PATH, true);
                String string = UUID.randomUUID().toString();
                out.writeUTF(string);
                out.close();
                out = fileSystem.append(PATH);
                String string1 = UUID.randomUUID().toString();
                out.writeUTF(string1);
                out.close();
                FSDataInputStream in = fileSystem.open(PATH);
                assertThat(in.readUTF()).isEqualTo(string);
                assertThat(in.readUTF()).isEqualTo(string1);
                in.close();
            }
            return null;
        });
    }

    @Test
    public void makeQualifiedTest() throws Exception {
        try (FileSystem fileSystem = openHdfs()) {
            String root = fileSystem.getConf().get("fs.defaultFS");
            String user = System.getProperty("user.name");
            final String testPath = "test";
            Path path = fileSystem.makeQualified(new Path(testPath));
            assertThat(path.toString()).isEqualTo(root + "/user/" + user + "/" + testPath);
        }
    }
}
