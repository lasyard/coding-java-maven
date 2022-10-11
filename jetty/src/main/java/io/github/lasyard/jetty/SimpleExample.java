package io.github.lasyard.jetty;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleExample extends AbstractHandler {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);
        server.setHandler(new SimpleExample());
        server.start();
        server.join();
    }

    @Override
    public void handle(
        String target,
        @NonNull Request baseRequest,
        HttpServletRequest request,
        @NonNull HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body><h1>Hello world!</h1></body></html>");
        baseRequest.setHandled(true);
    }
}
