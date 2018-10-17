package com.yjtoon.school.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd"; // 年-月-日
	public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年-月-日 时:分:秒
	
	public static String date2String(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date string2Date(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date result = null;
		try {
			result = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

	
	public static void main(String[] args) {
        System.out.println(DateUtils.dateToWeek("2018-05-02"));
    }
	
}
