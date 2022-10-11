package io.github.lasyard.jetty.servlet;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletExample extends HttpServlet {
    private static final int PORT = 8080;
    private static final long serialVersionUID = 4570203527331054198L;

    public static void main(String[] args) throws Exception {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setResourceBase(".");
        handler.addServlet(DefaultServlet.class, "/");

        Server server = new Server(PORT);
        server.setHandler(handler);

        handler.addServlet(ServletExample.class, "/servlet");

        server.start();
        server.join();
    }

    @Override
    public void doGet(HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body><h1>I am a servlet.</h1></body></html>");
    }
}
