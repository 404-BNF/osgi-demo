package dev.sk;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProvisionActivator implements BundleActivator {

    private final static String B_PATH = "/WEB-INF/bundles/";

    private final ServletContext servletContext;

    public ProvisionActivator(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void start(BundleContext context) throws Exception {
        System.out.println("Starting OSGI Proxy Framework...");
        servletContext.setAttribute(BundleContext.class.getName(), context);
        ArrayList<Bundle> installed = new ArrayList<Bundle>();
        for (URL url : findBundles()) {
            this.servletContext.log("Installing bundle [" + url + "]");
            Bundle bundle = context.installBundle(url.toExternalForm());
            installed.add(bundle);
        }

        for (Bundle bundle : installed) {
            if (!isFragment(bundle)) {
                bundle.start();
            }
        }
    }

    public void stop(BundleContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    private boolean isFragment(Bundle bundle) {
        return bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
    }

    private List<URL> findBundles() throws Exception {
        System.out.println("Finding Bundles");
        ArrayList<URL> list = new ArrayList<URL>();
        for (Object o : this.servletContext.getResourcePaths(B_PATH)) {
            String name = (String) o;
            if (name.endsWith(".jar")) {
                URL url = this.servletContext.getResource(name);
                if (url != null) {
                    list.add(url);
                }
            }
        }

        return list;
    }

}