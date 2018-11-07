package smarttesting.data.model;

import java.lang.annotation.*;

/**
 * @author
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String value();
}
