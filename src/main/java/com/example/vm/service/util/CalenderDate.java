package com.example.vm.service.util;

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

}
