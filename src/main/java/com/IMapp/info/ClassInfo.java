package com.IMapp.info;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static java.lang.String.format;

/**
 * @author funmiayinde
 * */
public class ClassInfo {

    private static Set<String> excludePropClassName = new HashSet<>(Arrays.asList(new String[]{"class"}));
    private final Class objClass;

    Map<String,PropertyClassInfo> propertyClassInfoMap = new HashMap<>();

    public ClassInfo(Class clazz){
        this.objClass = clazz;
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(objClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null && !excludePropClassName.contains(propertyDescriptor.getName())){
                    // get the name and type
                    String propName = propertyDescriptor.getName();
                    Class propType = propertyDescriptor.getPropertyType();

                    // get the readMethod actual genericType
                    Class propGenericType = null;
                    Type returnType  = readMethod.getGenericReturnType();
                    if (returnType instanceof ParameterizedType){
                        ParameterizedType type = (ParameterizedType) returnType;
                        Type[] typeArguments = type.getActualTypeArguments();
                        for (Type typeArgs: typeArguments){
                            if (typeArgs instanceof Class){
                                propGenericType = (Class) typeArgs;
                            }else if (typeArgs instanceof TypeVariable){
                                // check generic name
                            }
                        }
                    }

                    // get the writeMethod
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    if (writeMethod == null){
                        String readMethodName = readMethod.getName();
                        if (readMethodName.startsWith("get")){
                            String writMethodName = "set" + readMethodName.substring(3);
                            try {
                                writeMethod = objClass.getMethod(writMethodName,propType);
                            }catch (NoSuchMethodException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    PropertyClassInfo propertyClassInfo = new PropertyClassInfo(propName,propType,propGenericType,writeMethod,readMethod);
                    propertyClassInfoMap.put(propName, propertyClassInfo);
                }

            }

        }catch (Exception e){
            throw new RuntimeException();
        }
    }


    /**
     * @return the objectClass of this ClassInfo
     * */
    public Class getObjClass(){
        return objClass;
    }

    /**
    * Set the value of an object given the prop name
     * @param object
     * @param propertyName
     * @param value
    * **/


    public boolean setValue(Object object,String propertyName,Object value){
        boolean done = false;
        PropertyClassInfo propertyClassInfo = propertyClassInfoMap.get(propertyName);
        Method writeMethod = (propertyClassInfo != null) ? propertyClassInfo.getCanWriteMethod(): null;
        if (writeMethod != null){
            try{
                if (value == null){
                    writeMethod.invoke(object,new Object[]{null});
                }else {
                    // if value is null, i.e fails to convert
                    if (value != null){
                        writeMethod.invoke(object,value);
                        done = true;
                    }
                }
            }catch (Throwable e){
                throw new RuntimeException();
            }
        }
        return done;
    }

    /**
     * @param object
     * @param propertyName
    * **/

    public Object getValue(Object object,String propertyName){
        Object value = null;
        PropertyClassInfo propertyClassInfo = propertyClassInfoMap.get(propertyName);
        if (propertyClassInfo != null){
            Method readMethod = propertyClassInfo.getCanReadMethod();
            if (readMethod == null){
                throw new RuntimeException(format("propName %s does not have readMethod",propertyName));
            }
            try {
                value = readMethod.invoke(object);
            }catch (Throwable e){
                throw new RuntimeException(format("Error While referencing readMethod %s for property %s on the object %s",readMethod.getName(),propertyName,object) + "\n"+e,e);
            }
        }
        return value;
    }


    public Set<String> getPropertyNames(){
        return propertyClassInfoMap.keySet();
    }

    public PropertyClassInfo getPropertyInfo(String name){
        return propertyClassInfoMap.get(name);
    }

    public boolean hasProperty(String name){
        return propertyClassInfoMap.containsKey(name);
    }
}
