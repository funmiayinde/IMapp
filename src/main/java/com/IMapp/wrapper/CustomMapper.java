package com.IMapp.wrapper;

import com.IMapp.converter.ClassTypeConverter;
import com.IMapp.converter.ConvertersRepo;
import com.IMapp.info.ClassInfo;
import com.IMapp.mappers.PairMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.IMapp.mappers.Mappers.mapOf;

/**
 * @author funmiayinde
 */
public class CustomMapper {

    static private final Map<Class, Class> implByInterface = mapOf(Map.class, HashMap.class, List.class, ArrayList.class);

    private enum ConvertType {
        identity, none, converter, complex
    }

    private Map<Class, ClassInfo> classInfoMap = new ConcurrentHashMap<>(16, 0.9f, 1);
    private ConvertersRepo convertersRepo = new ConvertersRepo();

    /**
     * Packaged scoped constructor to force use of builder
     */
    public CustomMapper(ConvertersRepo convertersRepo) {
        convertersRepo.init();
        convertersRepo.getRepoMap().putAll(convertersRepo.getRepoMap());
    }

    /**
     * Convert a value to a class
     **/
    public <K, V> K as(Class<K> targetClass, V value) {
        if (value == null)
            return (K) null;

        /** if targetClass is an interface, try to find the
         *  where it is implemented... else,let it fail
         * */
        if (targetClass.isInterface()) {
            Class implClass = implByInterface.get(targetClass);
            targetClass = (implClass != null) ? implClass : null;
        }

        PairMapper<ConvertType, ClassTypeConverter<K, V>> converterPairMapper = getConverterInfo(value, targetClass);
        CustomMapper.ConvertType convertType = converterPairMapper.getK();
        ClassTypeConverter<K, V> typeConverter = converterPairMapper.getV();
        try{
            if (convertType == ConvertType.complex){

            }
        }catch (InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }



    /**
     * Just a shorten for mapper.as
     * */
    public Map<String,Object> asMap(Object object){
        return as(HashMap.class,object);
    }

    /**
     * get the class info
     */
    public ClassInfo getClassInfo(Class clazz) {
        ClassInfo classInfo = classInfoMap.get(clazz);

        if (classInfo == null) {
            classInfo = new ClassInfo(clazz);
            classInfoMap.put(clazz, classInfo);
        }
        return classInfo;
    }


    public <T> IMappMapper<T> iMappMapper(T object){
        return new IMappMapper<>(object,this);
    }


    private <K, V> PairMapper<ConvertType, ClassTypeConverter<K, V>> getConverterInfo(K instance, Class<V> targetedClass) {
        boolean isNull = (instance == null);

        if (instance == null || targetedClass.equals(targetedClass.getClass())
                || targetedClass.isAssignableFrom(instance.getClass())) {
            return new PairMapper<>(ConvertType.identity, null);
        }

        Class sourceCls = (isNull) ? null : instance.getClass();
        if (!isNull) {
            ClassTypeConverter typeConverter = convertersRepo.converterHelper(sourceCls, targetedClass);
            if (typeConverter == null) {
                return new PairMapper(ConvertType.converter, typeConverter);
            }
        }

        // now if no type converters or if instance is null
        if (instance == null) {
            return new PairMapper<>(ConvertType.none, null);
        }

        return new PairMapper<>(ConvertType.complex, null);

    }
}
