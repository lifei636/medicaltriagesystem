package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeansUtil {

    /**
     *  map转对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static  <T> T mapToBean(Map map, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method[] methods = clazz.getMethods();
        Field[] declaredFields = clazz.getDeclaredFields();
        T t = clazz.newInstance();
        for (Field field:declaredFields){
            if(map.containsKey(field.getName())){
                String value = map.get(field.getName()).toString();
                String setName = "set"+field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
                Class<?> type = field.getType();
                Method method = clazz.getMethod(setName, type);

                switch (type.getName()){
                    case "java.lang.String":
                        method.invoke(t,value);
                        break;
                    case "java.lang.int":
                        method.invoke(t,Integer.parseInt(value));
                        break;
                    case "java.lang.double":
                        method.invoke(t,Double.parseDouble(value));
                        break;
                    case "java.lang.float":
                        method.invoke(t,Float.parseFloat(value));
                        break;
                    case "java.lang.boolean":
                        method.invoke(t,Boolean.parseBoolean(value));
                        break;
                    default:
                        method.invoke(t,value);
                }

            }
        }
        return t;
    }

    /**
     * bean对象转Map
     * @param t
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public  static <T> Map<String,Object> beanToMap(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map map = new HashMap();
        for(Field f :fields){
            String getter = "get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
            Class<?> type = f.getType();
            Method method = clazz.getMethod(getter, new Class[]{});
            Object o = method.invoke(t, new Class[]{});
            map.put(f.getName(),o);
        }
        return map;
    }

}
