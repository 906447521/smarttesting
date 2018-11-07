package smarttesting;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author
 */
public class Main {

    static Logger  log       = LoggerFactory.getLogger(Main.class);
    static Tomcat  container = new Tomcat();
    static Integer port      = 6060;
    static String home;

    public static void main(String[] args) {

        if (args != null && args.length > 0) {
            if (args[0].equals("start")) {
                start();
            } else if (args[0].equals("restart")) {
                restart();
            } else if (args[0].equals("stop")) {
                stop();
            }
        } else {
            try {
                PropertyConfigurator.configure(new ClassPathResource("log4j_debug.xml").getFile().getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            start();
        }


    }

    private static void stop() {
        try {
            container.stop();
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            container.notifyAll();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void restart() {
        stop();
        start();
    }

    private static void start() {
        try {

            String basedir = System.getProperty("basedir");
            String injectPort = System.getProperty("port");
            log.info("==================================");
            log.info("DIR: " + basedir);
            if (injectPort != null) {
                try {
                    port = Integer.parseInt(injectPort);
                } catch (Exception e) {
                    log.error("", e);
                }
            }


            if (basedir == null) {
                ClassPathResource resource = new ClassPathResource("log4j.xml");
                home = resource.getFile().getParent();
                basedir = home;
            } else {
                home = basedir + "/home";
            }
            log.info("home: " + home);
            String contextPath = "";

            Context.baseDir = basedir;
            container.setPort(port);
            container.setBaseDir(basedir);

            StandardServer server = (StandardServer) container.getServer();
            AprLifecycleListener listener = new AprLifecycleListener();
            server.addLifecycleListener(listener);

            container.addWebapp(contextPath, home + "/frontend");
            container.start();


        } catch (Exception e) {
            log.error("", e);
        }

        while (true) {
            synchronized (container) {
                try {
                    container.wait();
                } catch (Throwable e) {
                }
            }
        }
    }
}
