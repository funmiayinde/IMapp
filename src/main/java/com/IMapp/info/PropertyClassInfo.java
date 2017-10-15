package com.IMapp.info;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author funmiayinde
 * */
public class PropertyClassInfo {

    private final String name;
    private final Method canReadMethod;
    private final Method canWriteMethod;
    private final Class classType;
    private final Optional<Class> genericType;

    public PropertyClassInfo(String name, Class classType, Class genericType, Method canReadMethod, Method canWriteMethod){
        this.name  = name;
        this.canReadMethod = canReadMethod;
        this.canWriteMethod = canWriteMethod;
        this.classType = classType;
        this.genericType = Optional.ofNullable(genericType);
    }

    public Method getCanReadMethod() {
        return canReadMethod;
    }

    public Method getCanWriteMethod() {
        return canWriteMethod;
    }

    public String getName() {
        return name;
    }

    public Class getClassType() {
        return classType;
    }

    public Optional<Class> getGenericType() {
        return genericType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[").append(classType.getSimpleName());
        genericType.ifPresent((gen) -> sb.append("<").append(gen.getSimpleName()).append(">"));
        return sb.append(" ").append(name).append(" ").
                append(canReadMethod.getName())
                .append(canWriteMethod.getName())
                .append("]").toString();
    }
}
