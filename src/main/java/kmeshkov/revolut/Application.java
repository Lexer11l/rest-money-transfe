package kmeshkov.revolut;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

@Log4j2
class Application {
    public static void main(String[] args) {
        Properties configuration = new Properties();
        try {
            configuration.load(new FileInputStream(new File("resources/application.properties")));
        } catch (IOException e) {
            log.error("Cannot load properties", e);
        }
        int port = Integer.parseInt(configuration.getProperty("port", "80"));
        String contextPath = configuration.getProperty("contextPath", "/");
        String prefixPath = configuration.getProperty("apiPrefixPath", "/api/*");
        String jettyPackages = configuration.getProperty("jettyPackages", "jersey.config.server.provider.packages");
        String rootPath = configuration.getProperty("rootPath", "kmeshkov.revolut");
        String isDebug = configuration.getProperty("isDebug", "false");
        System.setProperty("isDebug", isDebug);

        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(port);

        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath(contextPath);

        server.setHandler(servletContextHandler);

        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, prefixPath);
        servletHolder.setInitOrder(0);

        servletHolder.setInitParameter(jettyPackages, rootPath);
        log.info("Initialization is finished. Starting server");
        try {
            server.start();
        } catch (Exception ex) {
            log.error("Cannot start server", ex);
        }
    }

}
