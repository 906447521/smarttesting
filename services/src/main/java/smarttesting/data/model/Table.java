package smarttesting.data.model;


import java.lang.annotation.*;

/**
 * @author
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value();
}
