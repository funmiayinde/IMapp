package com.IMapp.converter;


import com.IMapp.mappers.PairMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.IMapp.mappers.PairMapper.newPair;

/**
 * @author funmiayinde
 *
 * Inspired from https://github.com/jexenberger/lambda-tuples/ project
 * */
public class ConvertersRepo {

    private final Map<PairMapper<Class<?>,Class<?>>,ClassTypeConverter<?,?>> repoMap;

    public ConvertersRepo(){
        repoMap = new HashMap<>();
    }

   public void init() {
        addClassTypeConverter(String.class, Long.class, Long::valueOf);
        addClassTypeConverter(String.class, Long.TYPE, Long::valueOf);
        addClassTypeConverter(String.class, Integer.class, Integer::valueOf);
        addClassTypeConverter(String.class, Integer.TYPE, Integer::valueOf);
        addClassTypeConverter(String.class, Byte.class, Byte::valueOf);
        addClassTypeConverter(String.class, Byte.TYPE, Byte::valueOf);
        addClassTypeConverter(String.class, Short.class, Short::valueOf);
        addClassTypeConverter(String.class, Short.TYPE, Short::valueOf);
        addClassTypeConverter(String.class, Boolean.class, MoreConverters::toBoolean);
        addClassTypeConverter(String.class, Boolean.TYPE, MoreConverters::toBoolean);
        addClassTypeConverter(String.class, Float.class, Float::valueOf);
        addClassTypeConverter(String.class, Float.TYPE, Float::valueOf);
        addClassTypeConverter(String.class, Double.class, Double::valueOf);
        addClassTypeConverter(String.class, Double.TYPE, Double::valueOf);
        addClassTypeConverter(String.class, Character.class, (instance) -> (instance != null) ? instance.charAt(0) : (char) (byte) 0);
        addClassTypeConverter(String.class, Character.TYPE, (instance) -> (instance != null) ? instance.charAt(0) : (char) (byte) 0);
        addClassTypeConverter(String.class, Double.TYPE, Double::valueOf);
        addClassTypeConverter(String.class, BigDecimal.class, BigDecimal::new);
        addClassTypeConverter(Number.class, Long.class, (instance) -> MoreConverters.toNumber(instance, Long.class));
        addClassTypeConverter(Number.class, Long.TYPE, (instance) -> MoreConverters.toNumber(instance, Long.class));
        addClassTypeConverter(Number.class, Integer.class, (instance) -> MoreConverters.toNumber(instance, Integer.class));
        addClassTypeConverter(Number.class, Integer.TYPE, (instance) -> MoreConverters.toNumber(instance, Integer.class));
        addClassTypeConverter(Number.class, Byte.class, (instance) -> MoreConverters.toNumber(instance, Byte.class));
        addClassTypeConverter(Number.class, Byte.TYPE, (instance) -> MoreConverters.toNumber(instance, Byte.class));
        addClassTypeConverter(Number.class, Short.class, (instance) -> MoreConverters.toNumber(instance, Short.class));
        addClassTypeConverter(Number.class, Short.TYPE, (instance) -> MoreConverters.toNumber(instance, Short.class));
        addClassTypeConverter(Number.class, Boolean.class, MoreConverters::toBoolean);
        addClassTypeConverter(Number.class, Boolean.TYPE, MoreConverters::toBoolean);
        addClassTypeConverter(Number.class, Float.class, (instance) -> MoreConverters.toNumber(instance, Float.class));
        addClassTypeConverter(Number.class, Float.TYPE, (instance) -> MoreConverters.toNumber(instance, Float.class));
        addClassTypeConverter(Number.class, Double.class, (instance) -> MoreConverters.toNumber(instance, Double.class));
        addClassTypeConverter(Number.class, Double.TYPE, (instance) -> MoreConverters.toNumber(instance, Double.class));
        addClassTypeConverter(Number.class, BigDecimal.class, MoreConverters::toBigDecimal);
        addClassTypeConverter(Object.class, String.class, Object::toString);
        addClassTypeConverter(Character.class, Boolean.class, MoreConverters::toBoolean);
        addClassTypeConverter(Number.class, Boolean.TYPE, MoreConverters::toBoolean);
        addClassTypeConverter(LocalDateTime.class, LocalDate.class, (LocalDateTime instance) -> instance.toLocalDate());
        addClassTypeConverter(LocalDate.class, LocalDateTime.class, (LocalDate instance) -> instance.atStartOfDay());
        addClassTypeConverter(LocalDateTime.class, Date.class, MoreConverters::localDateTimeToDate);
        addClassTypeConverter(Date.class, LocalDateTime.class, MoreConverters::dateToLocalDateTime);
//        addClassTypeConverter(String.class, LocalDateTime.class,MoreConverters::stringToLocalDateTime);
        addClassTypeConverter(LocalDateTime.class, String.class,MoreConverters::localDateTimeToString);
        addClassTypeConverter(String.class, LocalDate.class,MoreConverters::stringToLocalDate);
//        addClassTypeConverter(LocalDate.class, String.class,MoreConverters::localDateToString);

    }


    <K,V> void addClassTypeConverter(Class<K> source,Class<V> clazzTarget,ClassTypeConverter<K,V> typeConverter){
        repoMap.put(newPair(source,clazzTarget),typeConverter);
    }


    public Map<PairMapper<Class<?>,Class<?>>,ClassTypeConverter<?,?>> getRepoMap(){
        return repoMap;
    }


    private ClassTypeConverter<?,?> findWideningClassTypeConverter(Class<?> source,Class<?> classTargetType){
        ClassTypeConverter<?,?> typeConverter = repoMap.get(newPair(source,classTargetType));
        if (typeConverter != null){
            return typeConverter;
        }

        PairMapper<Class<?>,Class<?>> convertKey = repoMap
                .keySet()
                .stream()
                .filter(key -> {
                    boolean sourceAssignable = key.getK().isAssignableFrom(source);
                    boolean classTargetAssignable = key.getV().isAssignableFrom(classTargetType);
                    return sourceAssignable && classTargetAssignable;
                }).findFirst().orElse(null);

        return repoMap.get(convertKey);
    }


    public ClassTypeConverter converterHelper(Class<?> source, Class<?> classTarget){
        ClassTypeConverter<?,?> typeConverter = findWideningClassTypeConverter(source,classTarget);

        if (typeConverter == null &&  classTarget.isEnum()){
            if (classTarget.isEnum()){
                return (val) ->{
                  Class enumClassTarget = (Class<Enum>) classTarget;
                  try{
                      return Enum.valueOf(enumClassTarget,val.toString());
                  }catch (Throwable e){
                      e.printStackTrace();
                      throw e;
                  }
                };
            }

            if (typeConverter == null){
                if (classTarget.isPrimitive()){
                    return (instance) -> {
                       if (classTarget.isPrimitive()){
                           try {
                               return classTarget.getField("TYPE").get(null);
                           }catch (Exception e){
                               throw new RuntimeException(e);
                           }
                       }else{
                            return null;
                       }
                    };
                }
            }
        }

        return typeConverter;

    }




}
