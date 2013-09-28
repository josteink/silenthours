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
        result.setTime(source.getTime() + DU_Second * seconds);
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

    public static boolean isPast(Date source)
    {
        Date now = new Date();
        return isPast(source, now);
    }

    public static boolean isPast(Date source, Date now)
    {
        Date sourceClean = eraseMillis(source);
        Date nowClean = eraseMillis(now);
        // "now" will be past very, very soon.
        return nowClean.getTime() >= sourceClean.getTime();
    }

    public static boolean isFuture(Date source)
    {
        Date now = new Date();
        return isFuture(source, now);
    }

    public static boolean isFuture(Date source, Date now)
    {
        Date sourceClean = eraseMillis(source);
        Date nowClean = eraseMillis(now);
        return nowClean.getTime() < sourceClean.getTime();
    }

    public static boolean isWeekday(Date source)
    {
        int dayOfWeek = source.getDay();
        return (dayOfWeek > 0) && (dayOfWeek < 6);
    }

    public static Date changeDate(Date timeSource, Date dateSource)
    {
        Date result = new Date(timeSource.getTime());

        result.setYear(dateSource.getYear());
        result.setMonth(dateSource.getMonth());
        result.setDate(dateSource.getDate());

        return result;
    }

    public static Date eraseMillis(Date source)
    {
        long millis = (source.getTime()/1000)*1000;
        Date result = new Date(millis);
        return result;
    }

}
