package smarttesting;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

/**
 * @author
 */
public class Main {

    static Logger log;
    static Tomcat  container = new Tomcat();
    static Integer port      = 8080;


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
            // 打包后的路径下面的home目录，classpath目录
            String home;
            // webapp的路径
            String webapp = "";
            // 打包后的路径
            String basedir = System.getProperty("basedir");
            String injectPort = System.getProperty("port");

            // 本地启动的
            if (basedir == null) {
                home = new ClassPathResource("version.prop").getFile().getParent();
                basedir = home;
                webapp = basedir + "/../../../web/src/main/webapp";
                try {
                    ResourceClasspath.add(new File(basedir + "/../../../web/src/main/resources"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 打包后启动
            else {
                basedir = basedir + "";
                home = basedir + "/home";
                webapp = home + "/frontend";
            }


            log = LoggerFactory.getLogger(Main.class);
            log.info("==================================");
            log.info("DIR: " + basedir);
            if (injectPort != null) {
                try {
                    port = Integer.parseInt(injectPort);
                } catch (Exception e) {
                    log.error("", e);
                }
            }


            log.info("home: " + home);
            String contextPath = "";

            Context.baseDir = basedir;
            container.setPort(port);
            container.setBaseDir(basedir);

            StandardServer server = (StandardServer) container.getServer();
            AprLifecycleListener listener = new AprLifecycleListener();
            server.addLifecycleListener(listener);
            server.setParentClassLoader(Thread.currentThread().getContextClassLoader());
            container.addWebapp(contextPath, webapp);
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
