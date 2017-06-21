package net.xapxinh.forex.server.util.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MarketSession {
	
	public class Status {
		
		private boolean open;
		private int left;
		private boolean inWinter; 
		
		public boolean isOpen() {
			return open;
		}
		public int getLeft() {
			return left;
		}
		public boolean isInWinter() {
			return inWinter;
		}
	}
	
	private static final SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private static final SimpleDateFormat YYYYMMDD_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
	
	static {
		YYYYMMDD_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	
	private static final int SUNDAY = 1;
	private static final int MONDAY = 2;
	private static final int TUESDAY = 3;
	private static final int WEDNESDAY = 4;
	private static final int THURSDAY = 5;
	private static final int FRIDAY = 6;
	private static final int SATURDAY = 7;
	
	public static final MarketSession NEWYORK = new MarketSession();
	public static final MarketSession LONDON = new MarketSession();
	public static final MarketSession TOKYO = new MarketSession();
	public static final MarketSession SYDNEY = new MarketSession();
	
	static {
		NEWYORK.name = "NewYork";
		NEWYORK.open = 12;
		NEWYORK.close = 21;
		NEWYORK.startDay = MONDAY;
		NEWYORK.summerStart = "03/12";
		NEWYORK.summerEnd = "10/05";
		
		LONDON.name = "London";
		LONDON.open = 7;
		LONDON.close = 16;
		LONDON.startDay = MONDAY;
		LONDON.summerStart = "03/26";
		LONDON.summerEnd = "10/29";
		
		TOKYO.name = "Tokyo";
		TOKYO.open = 0;
		TOKYO.close = 9;
		TOKYO.startDay = MONDAY;
		TOKYO.summerStart = null;
		TOKYO.summerEnd = null;
		
		SYDNEY.name = "Sydney";
		SYDNEY.open = 21;
		SYDNEY.close = 6;
		SYDNEY.startDay = SUNDAY;
		SYDNEY.summerStart = "04/02";
		SYDNEY.summerEnd = "10/01";
	}
	
	private String name;
	private int open;
	private int close;
	private int startDay;
	private String summerStart;
	private String summerEnd;
	private Status status;
	
	public String getName() {
		return name;
	}
	public int getOpen() {
		return open;
	}
	public int getClose() {
		return close;
	}
	public int getStartDay() {
		return startDay;
	}
	public String getSummerStart() {
		return summerStart;
	}
	public String getSummerEnd() {
		return summerEnd;
	}
	
	public int getTimeLeft() {
		return 0;
	}
	
	public static void main(String[] args) {
		MarketSession.LONDON.getStatus();
	}
	
	public Status getStatus() {
		if (status == null) {
			status = new Status();
		}
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		boolean isInWinter = isInWinter(date);
		
		Calendar openTimeCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		openTimeCal.set(Calendar.HOUR_OF_DAY, open);
		openTimeCal.set(Calendar.MINUTE, 0);
		openTimeCal.set(Calendar.SECOND, 0);		
		
		while (openTimeCal.get(Calendar.DAY_OF_WEEK) + 1 != startDay) {
			openTimeCal.set(Calendar.DAY_OF_MONTH, openTimeCal.get(Calendar.DAY_OF_MONTH) - 1);
	    }
		
		Calendar closeTimeCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		closeTimeCal.set(Calendar.YEAR, openTimeCal.get(Calendar.YEAR));
		closeTimeCal.set(Calendar.MONTH, openTimeCal.get(Calendar.MONTH));
		closeTimeCal.set(Calendar.DAY_OF_MONTH, openTimeCal.get(Calendar.DAY_OF_MONTH));
		closeTimeCal.set(Calendar.HOUR_OF_DAY, close);
		closeTimeCal.set(Calendar.MINUTE, 0);
		closeTimeCal.set(Calendar.SECOND, 0);
		
		while (closeTimeCal.before(openTimeCal)) {
			closeTimeCal.set(Calendar.DAY_OF_MONTH, closeTimeCal.get(Calendar.DAY_OF_MONTH) + 1);
	    }	    
	   
	    if (isInWinter) {
	    	openTimeCal.set(Calendar.HOUR_OF_DAY, openTimeCal.get(Calendar.HOUR_OF_DAY) + 1);
	    	closeTimeCal.set(Calendar.HOUR_OF_DAY, closeTimeCal.get(Calendar.HOUR_OF_DAY) + 1);
	    }
	    
	    status.inWinter = isInWinter;
	    
	    while (date.getTime().after(closeTimeCal.getTime()) || 
	    		(openTimeCal.get(Calendar.DAY_OF_WEEK) + 1) > FRIDAY || 
	    		(openTimeCal.get(Calendar.DAY_OF_WEEK) + 1) < startDay || 
	    		((openTimeCal.get(Calendar.DAY_OF_WEEK) + 1) == FRIDAY) && 
	    		openTimeCal.get(Calendar.HOUR_OF_DAY) >= close) {
	    	openTimeCal.set(Calendar.DAY_OF_MONTH, openTimeCal.get(Calendar.DAY_OF_MONTH) + 1);
	        closeTimeCal.set(Calendar.DAY_OF_MONTH, closeTimeCal.get(Calendar.DAY_OF_MONTH) + 1);
	    }
	    if (!date.getTime().before(openTimeCal.getTime()) && date.getTime().before(closeTimeCal.getTime())) {
	        status.open = true;
	        int left = (int) (closeTimeCal.getTime().getTime() - date.getTime().getTime());
	        status.left = left;
	    } else {
	    	status.open = false;
	    	int left = (int) (openTimeCal.getTime().getTime() - date.getTime().getTime());
	    	status.left = left;
	    	
	    }
	    return status;
	}
	
	public boolean isInWinter(Calendar date) {
		if (summerStart == null || summerEnd == null) {
			return false;
		}
		try {
			Date summerStartDate = YYYYMMDD_DATE_FORMAT.parse(date.get(Calendar.YEAR) + "/" + summerStart);
			Date summerEndDate = YYYYMMDD_DATE_FORMAT.parse(date.get(Calendar.YEAR) + "/" + summerEnd);
			return date.before(summerStartDate) || date.after(summerEndDate);
		} catch (ParseException e) {
			return false;
		}
	}
}
