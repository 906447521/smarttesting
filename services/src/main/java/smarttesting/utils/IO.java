package smarttesting.utils;

import java.io.Closeable;

/**
 * @author
 */
public class IO {

    public static void closeQuietly(Closeable... closeable) {
        if(closeable != null) {
            for(Closeable closeable1 : closeable) {
                try {
                    closeable1.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
