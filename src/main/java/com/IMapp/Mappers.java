package com.IMapp;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author funmiayinde
 **/
public class Mappers {

    public static class NamesVal {
        public final String[] names;
        public final Object[] vals;

        public NamesVal(String[] names, Object[] vals) {
            this.names = names;
            this.vals = vals;
        }
    }

    /***
     * <p> Create  a HashMap of a list of [name,values,name,values] array</p>
     *
     * if the array miss the last "value" for the last "name" then the value will
     * be set to null for this property
     ****/
    static final public Map mapOf(Object... obj) {
        HashMap<Object, Object> map = new HashMap<Object, Object>();

        for (int i = 0; i < obj.length; i += 2) {
            Object key = obj[i];
            if (i + 1 < obj.length) {
                Object val = obj[i + 1];
                map.put(key, val);
            } else {
                map.put(key, null);
            }
        }
        return map;
    }

    /**
     * Similar to mapOf,but will get the string from keys
     * to return a Map<String,Object>
     * **/
    static final public Map<String,Object> provMapOf(Object objArgs[]){
        HashMap<String,Object> map = new HashMap<String, Object>();

        for (int i = 0; i < objArgs.length; i += 2){
            Object keyObj = objArgs[i];
            String key = (keyObj instanceof String) ? (String) keyObj: keyObj.toString();
            if (i + 1 < objArgs.length){
                Object val = objArgs[i +1];
                map.put(key,val);
            }else {
                map.put(key,null);
            }
        }
        return map;
    }

    static final public <T> Set<T> setMapOf(T... objArgs){
        Set<T>  set = new HashSet<T>();
        Collections.addAll(set,objArgs);
        return set;
    }

    /**
     * Nest the flattenMapper following me "." notation scheme
     * A flattenMapper with the value like {"company.name":"Frank"} will result
     * in the returned map like {"company":{"name":"Nike"}}
     * **/
    static final public Map<String,Object> asNestedMapper(Map<? extends Object,? extends Object> flattenMap){
        Map<String,Object> nestedMap = new HashMap<String,Object>();

        Map<String,Map<String,Object>> mapByPath = new HashMap<String, Map<String, Object>>();

        for (Map.Entry entry : flattenMap.entrySet()){
            Object keyObject = entry.getKey();
            String key = (keyObject instanceof String) ? (String) keyObject: keyObject.toString();

            Object value  = entry.getValue();

            Object proValue = value;

            Map<String,Object> childProMapp = null;

            if (key.indexOf('.') != -1){
                String[] names = key.split("\\.");
                int lastIndex = names.length - 1;
                for (int i = lastIndex; i >=0; i--){
                    String proName = names[i];

                    String mapByPathKey = String.join(".", Arrays.copyOfRange(names,0,i));

                    // if i == 0, then , we take the base propMapp
                    Map<String,Object> proMapp = (i == 0) ? nestedMap : mapByPath.get(mapByPathKey);

                    boolean newPropMap = false;

                    if (proMapp ==null){
                        proMapp = new HashMap<String,Object>();
                        mapByPath.put(mapByPathKey,proMapp);
                        newPropMap = true;
                    }

                    // put the value
                    proMapp.put(proName,proValue);

                    // determine if the need set childProMapp for next iteration
                    if (newPropMap){
                        proValue = proMapp;
                    }else {
                        break;
                    }
                }
            }else{
                nestedMap.put(key,value);
            }
        }
        return nestedMap;
    }
}
