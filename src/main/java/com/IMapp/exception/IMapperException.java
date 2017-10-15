package com.IMapp.exception;

import java.util.Objects;

/**
 * @author funmiayinde
 *
 * */
public class IMapperException extends RuntimeException{

    public enum Error{
        CANNOT_FIND_HELPER
    }

    private final Error error;

    private IMapperException(Error error,String msg){
            super(msg);
            this.error = error;
    }

    public Error getError() {
        return error;
    }

    public boolean isError(){
        return Objects.equals(error,this.error);
    }

    public static IMapperException noHelperFoundErr(Class sourceClass,Class targetClass){
        String msg = String.format("Can't find helper for sourceClass %s to targetClass %s",
                sourceClass.getName(),targetClass.getName());
        return new IMapperException(Error.CANNOT_FIND_HELPER,msg);
    }
}
