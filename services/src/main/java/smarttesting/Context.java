package smarttesting;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author
 */
public class Context {

    static String baseDir;

    public static String getBaseDir() {
        if (baseDir == null) {
            ClassPathResource resource = new ClassPathResource("log4j.xml");
            try {
                baseDir = resource.getFile().getParent();
            } catch (IOException e) {

            }
        }
        return baseDir;
    }
}
