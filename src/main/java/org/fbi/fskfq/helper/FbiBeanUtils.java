package org.fbi.fskfq.helper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanrui on 13-12-31.
 */
public class FbiBeanUtils {
    private static String FIELD_DATE_FORMAT = "yyyy-MM-dd";
    private static String[] FIELD_TYPE_SIMPLE = {"java.lang.Integer", "int", "java.util.Date", "java.math.BigDecimal"};
    private static String FIELD_TYPE_INTEGER = "java.lang.Integer,int";
    private static String FIELD_TYPE_DATE = "java.util.Date";
    private static String FIELD_TYPE_BIGDECIMAL = "java.math.BigDecimal";

    public static void copyProperties(Map srcMap, Object targetObj) throws Exception {
        BeanInfo targetBean = Introspector.getBeanInfo(targetObj.getClass());
        PropertyDescriptor[] propertyDescriptors = targetBean.getPropertyDescriptors();

        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor prop = propertyDescriptors[i];
            Method writeMethod = prop.getWriteMethod();
            if (writeMethod != null) {
                Iterator ite = srcMap.keySet().iterator();
                while (ite.hasNext()) {
                    String mapkey = (String) ite.next();
                    //TODO 可先判断name中是否存在下划线
                    String mapkeyTmp = toCamelStr(mapkey.toLowerCase());
                    if (mapkeyTmp.equals(prop.getName())) {
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        Object value = srcMap.get(mapkey);
                        //类型不匹配则转换
                        if (!(prop.getPropertyType().getName().equals(value.getClass().getName()))) {
                            value = parseByType(prop.getPropertyType(), value.toString());
                        }
                        writeMethod.invoke((Object) targetObj, new Object[]{value});
                        break;
                    }
                }
            }
        }
    }

    public static void copyProperties(Object source, Object target) {
        try {
            BeanInfo targetbean = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] propertyDescriptors = targetbean.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor prop = propertyDescriptors[i];
                Method writeMethod = prop.getWriteMethod();
                if (writeMethod != null) {
                    BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass());
                    PropertyDescriptor[] sourcepds = sourceBean.getPropertyDescriptors();
                    for (int j = 0; j < sourcepds.length; j++) {
                        if (sourcepds[j].getName().equals(prop.getName())) {
                            Method rm = sourcepds[j].getReadMethod();
                            if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
                                rm.setAccessible(true);
                            }
                            Object value = rm.invoke(source, new Object[0]);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke((Object) target, new Object[]{value});
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Bean copy 错误.",e);
        }
    }

    //带下划线的字段名改为camel型
    //map->bean时使用
    private static String toCamelStr(String srcStr) {
        StringBuilder sb = new StringBuilder();
        boolean match = false;
        for (int i = 0; i < srcStr.length(); i++) {
            char ch = srcStr.charAt(i);
            if (match && ch >= 97 && ch <= 122)
                ch -= 32;
            if (ch != '_') {
                match = false;
                sb.append(ch);
            } else {
                match = true;
            }
        }
        return sb.toString();
    }


    private static Object parseObject(Class clazz, String str) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Object obj;
        if (str == null || str.equals("")) {
            obj = null;
        } else {
            obj = clazz.newInstance();
            Method m = clazz.getMethod("setId", str.getClass());
            m.invoke(obj, str);
        }
        return obj;
    }


    private static Object parseByType(Class clazz, String str) throws ParseException, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        Object obj = "";
        String clazzName = clazz.getName().trim();
        if (isSimpleType(clazzName)) {
            if (FIELD_TYPE_INTEGER.contains(clazzName)) {
                obj = parseInteger(str);
            } else if (FIELD_TYPE_DATE.contains(clazzName)) {
                obj = parseDate(str);
            } else if (FIELD_TYPE_BIGDECIMAL.contains(clazzName)) {
                obj = parseBigDecimal(str);
            }
        } else {
            obj = parseObject(clazz, str);
        }
        return obj;
    }

    private static boolean isSimpleType(String type) {
        for (int i = 0; i < FIELD_TYPE_SIMPLE.length; i++) {
            if (type.equals(FIELD_TYPE_SIMPLE[i])) {
                return true;
            }
        }
        return false;
    }

    private static Integer parseInteger(String str) {
        if (str == null || str.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    private static Date parseDate(String str) throws ParseException {
        if (str == null || str.equals("")) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(FIELD_DATE_FORMAT);
            Date date = sdf.parse(str);
            return date;
        }
    }
    private static BigDecimal parseBigDecimal(String str) throws ParseException {
        if (str == null || str.equals("")) {
            return null;
        } else {
            return new BigDecimal(str);
        }
    }

    //====

    public static String getDateFormat() {
        return FIELD_DATE_FORMAT;
    }

    public static void setDateFormat(String DATE_FORMAT) {
        FbiBeanUtils.FIELD_DATE_FORMAT = DATE_FORMAT;
    }
}
