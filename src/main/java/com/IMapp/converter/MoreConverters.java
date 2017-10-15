package com.IMapp.converter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author funmiayinde
 *
 **/
public class MoreConverters {

    static private DateTimeFormatter localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    static private DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static String localDateTimeToString(LocalDateTime localDateTime){
        return localDateTimeFormatter.format(localDateTime);
    }

    public static LocalDate stringToLocalDate(String str){
        return LocalDate.parse(str,localDateFormatter);
    }

    public static String localDateString(LocalDate localDate){
        return localDateFormatter.format(localDate);
    }

    public static LocalDateTime dateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
    }

    public static <T extends Object> T toNumber(Number number,Class<T> clazz){
        if (number == null)
            return null;

        try{
            return clazz.getConstructor(String.class).newInstance(number.toString());
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public static BigDecimal toBigDecimal(Number number){
        return (number == null) ? null : new BigDecimal(number.toString());
    }

    public static Boolean toBoolean(Character character){
        if (character == null)
            return false;

        return character.charValue() == '1' || character.charValue() == 'y'
                || character.charValue() == 'Y';
    }

    public static Boolean toBoolean(Number number){
        if (number == null)
            return false;

        return number.intValue() == 1;
    }

    public static Boolean toBoolean(String str){
        if (str == null)
            return false;

        return str.equalsIgnoreCase("y") || str.equalsIgnoreCase("1") || str.equals("y")
                || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("true");
    }

    public static Boolean toBoolean(Long l){
        if (l == null)
            return false;

        return l.longValue() == 1;
    }



}
