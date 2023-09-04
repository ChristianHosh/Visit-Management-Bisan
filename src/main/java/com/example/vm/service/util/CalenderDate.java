package com.example.vm.service.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class CalenderDate {

    public static Date getYesterdayUtil(){
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, -1);
        todayDate.setTime(calendar.getTime().getTime());

        return todayDate;
    }

    public static java.sql.Date getYesterdaySql(){
        return new java.sql.Date(getYesterdayUtil().getTime());
    }

    public static Date getTodayUtil(){
        return new Date();
    }

    public static Date getTodayUtil(int offset){
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, offset);
        todayDate.setTime(calendar.getTime().getTime());

        return todayDate;
    }

    public static java.sql.Date getTodaySql() {
        return new java.sql.Date(getTodayUtil().getTime());
    }

    public static java.sql.Date getTodaySql(int offset){
        return new java.sql.Date(getTodayUtil(offset).getTime());
    }

    public static Timestamp asTimestamp(java.sql.Date date){
        return Timestamp.valueOf(date.toLocalDate().atStartOfDay());
    }
}
