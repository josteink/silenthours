package net.kjonigsen.silenthours;

import java.util.Date;

/**
 * Created by jostein on 25/09/13.
 */
public class DateUtil {

    private final static long DU_Second = 1000;
    private final static long DU_Minute = DU_Second * 60;
    private final static long DU_Hour = DU_Minute * 60;
    private final static long DU_Day = DU_Hour * 24;

    public static Date addDays(int days, Date source)
    {
        Date result = new Date();
        result.setTime(source.getTime() + days * DU_Day);
        return result;
    }

    public static Date addSeconds(int seconds, Date source)
    {
        Date result = new Date();
        result.setTime(source.getTime() - DU_Second * seconds);
        return result;
    }

    public static Date getDateOnly(Date source)
    {
        Date result = new Date(source.getTime());
        result.setHours(0);
        result.setMinutes(0);
        result.setSeconds(0);
        return result;
    }
}
