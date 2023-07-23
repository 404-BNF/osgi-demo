package dev.sk;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {
    private FrameworkService service;

    public StartupListener() {
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Destroying Context OSGI Proxy Framework...");
        this.service.stop();

    }

    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Initializing Context OSGI Proxy Framework...");
        this.service = new FrameworkService(arg0.getServletContext());
        this.service.start();

    }
}