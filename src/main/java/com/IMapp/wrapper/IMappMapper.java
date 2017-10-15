package com.IMapp.wrapper;

import com.IMapp.IMapp;
import com.IMapp.info.ClassInfo;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author funmiayinde
 * */
public class IMappMapper<T> {

    public enum SetterRule{
        notNull,noOverride
    }

    private final Object object;
    private final Map map;
    private final ClassInfo classInfo;
    private final CustomMapper mapper;

    public IMappMapper(T obj, CustomMapper mapper){
        if (obj instanceof Map){
            map = (Map) obj;
            object = null;
        }else{
            map = null;
            object = obj;
        }
        this.mapper = mapper;
        classInfo = (obj != null) ? mapper.getClassInfo(obj.getClass()): null;
    }

    public T get(){
        return (T) ((object != null) ? object:map);
    }

    // --------Convert to map ----------//
    public Map asMap(){
        return mapper.asMap(get());
    }

    public <T> T as(Class<T> targetedClass){
        return mapper.as(targetedClass,get());
    }

    public <T> T convertInto(Supplier<T> supplier){
        T target = supplier.get();
        mapper.iMappMapper(target);
        return target;
    }

    public IMappMapper<T> putAll(Object sourceObj){
        if (classInfo == null || sourceObj == null){
            return this;
        }

        IMappMapper iSource = (sourceObj instanceof )
    }



}
