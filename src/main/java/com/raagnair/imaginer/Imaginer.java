package com.raagnair.imaginer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Easy Jetty Server.
 *
 * Example usage:
 *         Imaginer.create()
 *                 .withPort(8080)
 *                 .withApi("/api/*", "com.raagnair.endpoints")
 *                 .withUI("./src/frontend/")
 *                 .go();
 */
public class Imaginer {
    private final static String[] DUMMY_STR_ARR = new String[0];

    private int port = 8080;
    private final Map<String, List<String>> apis = new HashMap<>();
    private String ui = null;
    private Server server;

    public static Imaginer create() {
        return new Imaginer();
    }

    public Imaginer withPort(int port) {
        this.port = port;
        return this;
    }

    public Imaginer withApi(String extension, String pkg) {
        this.apis.computeIfAbsent(extension, e -> new LinkedList<>()).add(pkg);
        return this;
    }

    public Imaginer withApi(String extension, List<String> pkgs) {
        this.apis.put(extension, pkgs);
        return this;
    }

    public Imaginer withUI(String directory) {
        this.ui = directory;
        return this;
    }

    public void go() {
        this.go(null);
    }

    public void go(Consumer<Exception> withCatch) {
        if (this.server == null) {
            this.build();
        }

        this.run(withCatch);
    }

    public Imaginer build() {
        Server server = new Server(this.port);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        this.apis.entrySet().forEach(e -> {
            ResourceConfig apiConfig = new ResourceConfig();
            apiConfig.packages(e.getValue().toArray(DUMMY_STR_ARR));
            handler.addServlet(new ServletHolder(new ServletContainer(apiConfig)), e.getKey());
        });

        if (this.ui != null) {
            ServletHolder uiHolder = new ServletHolder(new DefaultServlet());
            uiHolder.setInitParameter("resourceBase", this.ui);
            handler.addServlet(uiHolder, "/");
        }

        server.setHandler(handler);
        this.server = server;
        return this;
    }

    public Server getServer() {
        return this.server;
    }

    public void run(Consumer<Exception> exc) {
        try {
            this.server.start();
            this.server.join();
        } catch (Exception e) {
            if (exc != null) {
                exc.accept(e);
            }
        } finally {
            this.server.destroy();
        }
    }
}
