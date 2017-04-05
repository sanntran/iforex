package net.xapxinh.forex.server.util.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateTimeUtil {

	public static final String DAY_DLM = "/";
	public static final String TIME_DLM = ":";
	public static final String HOUR = "Hour";
	public static final String MINUTE = "Minute";
	public static final String SECOND = "Second";
	public static final String DAY_OF_WEEK = "Day";
	public static final String AM = "AM";
	public static final String PM = "PM";
	public static final String AM_PM = "AmPm";
	public static final double timeZone = 7.0;
	public static String HHmmddMMyyyyFormatter = "HH/mm/dd/MM/yyyy";
	// Lunar - Solar date time	
	public static final SimpleDateFormat yyyyMMdd_HHmmssFormatter =  new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static final SimpleDateFormat yyyyMMddHHmmssFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	// Date time
	public static HashMap<String, Integer> getHour_Mimute_AmPm_DayOfWeek() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		Calendar cal = Calendar.getInstance();

		result.put(HOUR, cal.get(Calendar.HOUR));
		result.put(MINUTE, cal.get(Calendar.MINUTE));
		result.put(AM_PM, cal.get(Calendar.AM_PM));
		result.put(DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));
		result.put(SECOND, cal.get(Calendar.SECOND));
		return result;
	}

	public static HashMap<String, Integer> getHour_Mimute_AmPm_DayOfWeek(long milisecond) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milisecond);
		result.put(HOUR, cal.get(Calendar.HOUR));
		result.put(MINUTE, cal.get(Calendar.MINUTE));
		result.put(AM_PM, cal.get(Calendar.AM_PM));
		result.put(DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));
		result.put(SECOND, cal.get(Calendar.SECOND));
		return result;
	}

	public static String getTimeHHMM(HashMap<String, Integer> hhMMAmPmDay) {
		String h = hhMMAmPmDay.get(HOUR) + "";
		if (hhMMAmPmDay.get(HOUR) < 10)
			h = "0" + hhMMAmPmDay.get(HOUR);
		String m = hhMMAmPmDay.get(MINUTE) + "";
		if (hhMMAmPmDay.get(MINUTE) < 10)
			m = "0" + hhMMAmPmDay.get(MINUTE);
		return (h + ":" + m);
	}

	public static List<String> getCurrentLunarSolarDateTime() {
		List<String> result = getCurrentSolarDateTime();
		result.add(getLunarDateTime(result));
		return result;
	}

	/**
	 * @return empty list if error, else return a list of String contains date
	 *         and time with format: hour, minute, am-pm, day week, day month,
	 *         year.
	 */
	public static List<String> getCurrentSolarDateTime() {
		List<String> result = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(HHmmddMMyyyyFormatter);
		String dateTime = sdf.format(cal.getTime());

		String dt[] = dateTime.split("/");

		if (dt.length != 5)
			return result;
		String amPm = AM;
		int hour = Integer.parseInt(dt[0]);
		if (hour >= 12) {
			if (hour > 12) {
				hour = hour - 12;
			}
			amPm = PM;
		}

		else if (hour == 0) {
			hour = 12;
		}

		result.add(hour + "");
		result.add(dt[1]);
		result.add(amPm);

		String day = cal.get(Calendar.DAY_OF_WEEK) + "";
		result.add(day);

		result.add(dt[2]);
		result.add(dt[3]);
		result.add(dt[4]);

		return result;
	}

	public static String getLunarDateTime(List<String> solar) {
		String lunar = "";

		if (solar.size() != 7)
			return lunar;
		int d = Integer.parseInt(solar.get(4));
		int m = Integer.parseInt(solar.get(5));
		int y = Integer.parseInt(solar.get(6));
		int[] lun = CalendarConverter.convertSolar2Lunar(d, m, y, timeZone);
		if (lun.length != 4)
			return lunar;
		String day = "";
		String mon = "";
		if (lun[0] < 10)
			day = "0" + lun[0];
		else
			day = lun[0] + "";
		if (lun[1] < 10)
			mon = "0" + lun[1];
		else
			mon = lun[1] + "";

		lunar = day + DAY_DLM + mon + DAY_DLM + lun[2];
		return lunar;
	}

	public static String getCurrentLunarDateTime() {
		List<String> solar = getCurrentSolarDateTime();
		return getLunarDateTime(solar);
	}

	public static String getDateTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date now = new Date();
		String strDate = sdfDate.format(now);

		return strDate;
	}	

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
}
