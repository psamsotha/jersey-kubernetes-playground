package org.example;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String HOST = "0.0.0.0";
    public static final int PORT = 8080;
    public static final String BASE_URI = String.format("http://%s:%d", HOST, PORT);

    /**
     * Starts Jetty HTTP server exposing JAX-RS resources defined in this application.
     * @return Jetty HTTP server.
     */
    public static Server startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("org.example");
        return JettyHttpContainerFactory.createServer(URI.create(BASE_URI), rc, true);
    }

    /**
     * Main method.
     */
    public static void main(String[] args) throws InterruptedException {
        final Server server = startServer();
        System.out.format("Jersey started and listening at %s ... %n", BASE_URI);
        server.join();
    }
}

