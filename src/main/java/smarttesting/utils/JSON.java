package smarttesting.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author
 */
public class JSON {

    private static final Logger log = LoggerFactory.getLogger(JSON.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public final static String write(Object value) {
        String jsonValue = null;
        try {
            if (value == null) {
                return null;
            }
            jsonValue = mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
        }
        return jsonValue;
    }

    public final static String writerPretty(Object value) {
        String jsonValue = null;
        try {
            if (value == null) {
                return null;
            }
            jsonValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
        }
        return jsonValue;
    }


    public static <V> V read(String json, Class<V> clazz) {
        V value = null;
        try {
            if (json == null) {
                return null;
            }
            value = mapper.readValue(json, clazz);
        } catch (Exception e) {

        }
        return value;
    }

    public static <V> V readUnsafe(Object object, Class<V> clazz) throws IOException {
        String str = valueOf(object);
        if (str == null)
            return null;
        V value = mapper.readValue(str, clazz);
        return value;
    }

    public static <V> V readUnsafe(Object object, TypeReference<V> typeReference) throws IOException {
        String str = valueOf(object);
        if (str == null)
            return null;
        V value = mapper.readValue(str, typeReference);
        return value;
    }

    private static String valueOf(Object s) {
        if (s == null)
            return null;

        return s.toString();
    }

    public static <V> V read(String json, TypeReference<V> typeReference) {
        V value = null;
        try {
            if (json == null)
                return null;
            value = mapper.readValue(json, typeReference);
        } catch (Exception e) {
        }
        return value;
    }

}
