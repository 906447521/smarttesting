package smarttesting.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 */
public class UnsafeObject {


    private static final Map beanDescriptorsCacheMap = new ConcurrentHashMap();
    private static final Map beanPropertiesCacheMap  = new ConcurrentHashMap();
    private static final Map excludePropertyNameMap  = new ConcurrentHashMap() {{
        put("class", true);
        put("this$0", true);
        put("log", true);
    }};

    public static List<PropertyDescriptor> readMethodPropertyDescriptors(Class beanClass) {
        Preconditions.checkNotNull(beanClass, "No bean class specified");
        Method[] methods = (Method[]) beanDescriptorsCacheMap.get(beanClass);
        if (methods != null) {
            return Lists.newArrayList();
        }
        BeanInfo beanInfo;// 读取类信息
        try {
            beanInfo = Introspector.getBeanInfo(beanClass, Introspector.USE_ALL_BEANINFO);
        } catch (IntrospectionException e) {
            return Lists.newArrayList();
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }
        List list = Lists.newArrayList();
        for (PropertyDescriptor descriptor : descriptors) {
            list.add(descriptor);
        }
        return list;

    }

    /**
     * 循环添加一个对象
     *
     * @param beanClass
     * @return
     */
    public static List<Field> declaredFieldPropertyFields(Class beanClass) {
        Preconditions.checkNotNull(beanClass, "No bean class specified");
        Field[] fields = (Field[]) beanPropertiesCacheMap.get(beanClass);  // 缓存获取
        if (fields != null) {
            return Lists.newArrayList();
        }
        List list = Lists.newArrayList();
        while (beanClass != null) {
            Field[] declaredFields = beanClass.getDeclaredFields();
            beanClass = beanClass.getSuperclass();
            if (declaredFields.length == 0)
                continue;
            for (Field field : declaredFields) {
                list.add(field);
            }
        }
        return list;

    }

    public static Set readMethodPropertyKeys(Class clazz) {
        Set arrays = Sets.newLinkedHashSet();
        List<PropertyDescriptor> descriptors = readMethodPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            if (excludePropertyNameMap.containsKey(descriptor.getName()))
                continue;
            arrays.add(descriptor.getName());
        }
        return arrays;
    }

    public static Set declaredFieldPropertyKeys(Class clazz) {
        Set arrays = Sets.newLinkedHashSet();
        List<Field> descriptors = declaredFieldPropertyFields(clazz);
        for (Field field : descriptors) {
            if (excludePropertyNameMap.containsKey(field.getName()))
                continue;
            arrays.add(field.getName());
        }
        return arrays;
    }

    public static Map readMethodPropertyValues(Object object) {
        Map map = Maps.newLinkedHashMap();
        List<PropertyDescriptor> descriptors = readMethodPropertyDescriptors(object.getClass());
        for (PropertyDescriptor descriptor : descriptors) {
            if (excludePropertyNameMap.containsKey(descriptor.getName()))
                continue;
            try {
                if (descriptor.getReadMethod() != null)
                    map.put(descriptor.getName(), descriptor.getReadMethod().invoke(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public static Map declaredFieldPropertyValues(Object object) {
        Map map = Maps.newLinkedHashMap();
        List<Field> fields = declaredFieldPropertyFields(object.getClass());
        for (Field field : fields) {
            if (excludePropertyNameMap.containsKey(field.getName()))
                continue;
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String prettyPrint(Class clazz, Map map) {
        StringBuilder string = new StringBuilder();
        string.append("\t");
        string.append(clazz.getSimpleName());
        string.append(": ");
        Set<Map.Entry> set = map.entrySet();
        for (Map.Entry e : set) {
            string.append("\n\t\t").append(e.getKey()).append("=").append(e.getValue()).append(", ");
        }
        return string.toString();
    }

    public static String writeObjectReadMethodValues(Object object) {
        return prettyPrint(object.getClass(), readMethodPropertyValues(object));
    }


    public static String writeObjectDeclaredFieldValues(Object object) {
        return prettyPrint(object.getClass(), declaredFieldPropertyValues(object));
    }
}
