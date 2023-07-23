package dev.sk;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class FrameworkService
{
    private final ServletContext context;
    private Felix felix;

    public FrameworkService(ServletContext context)
    {
        this.context = context;
    }

    public void start()
    {
        try {
            doStart();
        } catch (Exception e) {
            System.err.println("Failed to start framework"+ e);
        }
    }

    public void stop()
    {
        try {
            doStop();
        } catch (Exception e) {
            System.err.println("Error stopping framework:"+ e);
        }
    }

    private void doStart()
            throws Exception
    {
        System.out.println("Starting OSGI...");
        Felix tmp = new Felix(createConfig());
        tmp.start();
        this.felix = tmp;
    }

    private void doStop()
            throws Exception
    {
        System.out.println("Stopping OSGI...");
        if (this.felix != null) {
            this.felix.stop();
        }
    }

    private Map<String, Object> createConfig()throws Exception{
        Properties props = new Properties();
        props.load(this.context.getResourceAsStream("/WEB-INF/framework.properties"));

        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Object key : props.keySet()) {
            map.put(key.toString(), props.get(key));
        }
        map.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, Arrays.asList(new ProvisionActivator(this.context)));
        return map;
    }
}