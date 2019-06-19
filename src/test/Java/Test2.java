import com.shine.model.Source;
import org.beetl.ext.fn.Print;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Test2 {

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


    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();
        for (int i=-3 ; i<3; i++){
            set.add(i);
            list.add(i);
        }
        for (int i =0;i<3 ; i++){
            set.remove(i);
            list.remove(i);
        }
        System.out.println(set + "," + list);
        /*Map map = new HashMap();
        map.put("a","a1");
        map.put("b","b1");
        TestBean t = new TestBean();
        t.setA("df");
        t.setB("es");
        try {
            Map<String, Object> toMap = Test2.beanToMap(t);
            System.out.println(toMap.get("a"));
            System.out.println(toMap.get("b"));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }
}
class TestBean{
    private String a;
    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
class Test_a{
    public static void main(String[] args) {
      List<String> list = Arrays.asList("a","2");
      list.add(2,"3");
      list.forEach(System.out::println);
    }
}