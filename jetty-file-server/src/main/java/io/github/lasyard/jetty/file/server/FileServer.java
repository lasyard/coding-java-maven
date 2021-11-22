package io.github.lasyard.jetty.file.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

public final class FileServer {
    private static final int PORT = 8080;

    private FileServer() {
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setResourceBase(".");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
