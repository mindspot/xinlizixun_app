package com.kuangji.paopao.util;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.kuangji.paopao.constant.CommonConstant;

public class DateUtils {
	/**
	 * 指定日期加上天数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 */
	public static String plusDay(int num, String newDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date currdate = format.parse(newDate);
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}

	public static Long getTimestamp(String time) {
		Long timestamp = null;
		try {
			timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	
	/**
	 * @desc  时间戳转字符串
	 * @param Long timestamp
	 * @example timestamp=1558322327000
	**/
	public static String getStringTime(Long timestamp) {
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);  // 获取年月日时分秒
		return datetime; 
	}
	public static String secondToTime(long second) {

		long hours = second / 3600; // 转换小时
		second = second % 3600; // 剩余秒数
		long minutes = second / 60;
		String time = hours + ":" + minutes;// 转换分钟
		if (minutes < 10) {
			time = hours + ":" + minutes + "0";
		}
		if (time.equals(CommonConstant.TWELVE_P_M_STR)) {
			time = CommonConstant.BEFORE_DAWN;
		}
		return time;

	}

	public static Integer yearInt() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String num = format.format(new Date());

		return Integer.valueOf(num);

	}

	/**
	 * 当前日期加上天数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @return
	 */
	public static String plusDay(int num) {
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		d = ca.getTime();
		String enddate = format.format(d);
		return enddate;
	}

	public static String formatDate(final Date date, final String pattern) {

		final SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
		return formatter.format(date);
	}

	public static boolean isTime(final String time) {
		if (checkHHMM(time)) {
			String[] temp = time.split(":");
			if ((temp[0].length() == 2 || temp[0].length() == 1) && temp[1].length() == 2) {
				int h, m;
				try {
					h = Integer.parseInt(temp[0]);
					m = Integer.parseInt(temp[1]);
				} catch (NumberFormatException e) {
					return false;
				}

				if (h >= 0 && h <= 24 && m <= 60 && m >= 0) {
					return true;
				}
			}
		}
		return false;

	}

	public static long formatSecond(final String time) {
		String[] m = time.split(":");
		int hour = Integer.parseInt(m[0]);
		int min = Integer.parseInt(m[1]);
		return hour * 3600 + min * 60;
	}

	/**
	 * 
	 * @doc 日期转换星期几
	 * @param datetime
	 *            日期 例:2017-10-17
	 * @return String 例:星期二
	 * @author lzy
	 * @history 2017年10月17日 上午9:55:30 Create by 【lzy】
	 */
	public static String dateToWeek(String date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(date);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	
	/**
	 * 
	 * 年 月 日
	 * @throws ParseException 
	 */
	public static String StringDate(String dateStr) {
		SimpleDateFormat dateDtf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = dateDtf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateDtf.format(date);
	}
	
	
	/**
	 * 
	 * 年 月 日
	 * @throws ParseException 
	 */
	public static String StringDateTime(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = new Date();
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format.format(date);
	}
	/**
	 * 
	 * @doc 日期转换 工作时间 周末时间
	 * @param datetime
	 *            日期 例:2017-10-17
	 * @return String 例:星期二
	 * @author lzy
	 * @history 2017年10月17日 上午9:55:30 Create by 【lzy】
	 */
	public static long bookTimeCode(String time) {
		long secondTime=DateUtils.formatSecond(time);
		if (secondTime==CommonConstant.TWELVE_P_M_SECOND||secondTime<=CommonConstant.BEFOR_DAWN) {
			
			return CommonConstant.BEFOR_DAWN;
		}
		if (secondTime>CommonConstant.BEFOR_DAWN&&secondTime<=CommonConstant.MORNING) {
			
			return CommonConstant.MORNING;
		}
		if (secondTime>CommonConstant.MORNING&&secondTime<=CommonConstant.AFTERNOON) {
			
			return CommonConstant.AFTERNOON;
		}
		if (secondTime>CommonConstant.AFTERNOON&&secondTime<=CommonConstant.NIGHT) {
			
			return CommonConstant.NIGHT;
		}
	
		return CommonConstant.MORNING;
	}

	/**
	 * 
	 * @doc传入一个 yyyy-MM-dd字符串日期 判断是不是大于服务时间
	 * @param paramDate        
	 */
	public static boolean timeContrast(String paramDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String serverTimeStr = simpleDateFormat.format(new Date());
		try {
			Date paramDateTime = simpleDateFormat.parse(paramDate);
			Date serverTime = simpleDateFormat.parse(serverTimeStr);
			
			return paramDateTime.getTime() >= serverTime.getTime();
		} catch (Exception e) {
			return false;
		}
	}

	
	/**
	 * 
	 * @doc 日期转换星期几
	 * @param datet
	 *            当前日期 start预约开始时间 日期 例:2017-10-17
	 * @return String 例:星期二
	 * @author lzy
	 * @history 2017年10月17日 上午9:55:30 Create by 【lzy】
	 */
	public static boolean greaterThanService(String date, String start) {
		 SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd H:mm");
		  DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
		if (DateUtils.formatSecond(start) >= CommonConstant.TWELVE_P_M_SECOND) {
			start = CommonConstant.BEFORE_DAWN;
		}
		StringBuffer stringBuffer = new StringBuffer(date);
		stringBuffer.append(" ").append(start);
		LocalDateTime localDateTime = LocalDateTime.parse(stringBuffer.toString(), timeDtf);
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		long tTime = instant.getEpochSecond();

		String strDate = ft.format(new Date());
		LocalDateTime nlocalDateTime = LocalDateTime.parse(strDate, timeDtf);
		ZoneId nzone = ZoneId.systemDefault();
		Instant ninstant = nlocalDateTime.atZone(nzone).toInstant();
		long nowTime = ninstant.getEpochSecond();

		return tTime - CommonConstant.LEAD_TIME >= nowTime;
	}

	
	/**
	 * 
	 * 当天剩余秒 
	 */
	public static long  toDayRemainingTime() {
		
		  LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
	      long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
	      return seconds;
	}

	
	/**
	 * A factory for {@link SimpleDateFormat}s. The instances are stored in a
	 * threadlocal way because SimpleDateFormat is not threadsafe as noted in
	 * {@link SimpleDateFormat its javadoc}.
	 *
	 */
	final static class DateFormatHolder {

		private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>();

		/**
		 * creates a {@link SimpleDateFormat} for the requested format string.
		 *
		 * @param pattern
		 *            a non-{@code null} format String according to
		 *            {@link SimpleDateFormat}. The format is not checked
		 *            against {@code null} since all paths go through
		 *            {@link DateUtils}.
		 * @return the requested format. This simple dateformat should not be
		 *         used to {@link SimpleDateFormat#applyPattern(String) apply}
		 *         to a different pattern.
		 */
		public static SimpleDateFormat formatFor(final String pattern) {
			final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
			Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
			if (formats == null) {
				formats = new HashMap<String, SimpleDateFormat>();
				THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
			}

			SimpleDateFormat format = formats.get(pattern);
			if (format == null) {
				format = new SimpleDateFormat(pattern, Locale.US);
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				formats.put(pattern, format);
			}

			return format;
		}

		public static void clearThreadLocal() {
			THREADLOCAL_FORMATS.remove();
		}

	}

	// 仅仅格式验证
	public static boolean checkHHMM(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
		try {
			@SuppressWarnings("unused")
			Date t = dateFormat.parse(time);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	// 返回 0:00 到 24:00的时间表
	// intervalTime间隔时间 单位秒
	public static Map<String, String> getTimetable(int intervalTime) {
		Map<String, String> map = new LinkedHashMap<String, String>(24);
		long startTime = 0;
		long endTime = CommonConstant.TWELVE_P_M_SECOND;
		while (startTime < endTime) {
			String s = DateUtils.secondToTime(startTime);
			if (s.equals(CommonConstant.TWELVE_P_M_STR)) {
				s = CommonConstant.BEFORE_DAWN;
			}
			long bookEnd = startTime + intervalTime;
			String e = DateUtils.secondToTime(bookEnd);
			startTime = startTime + 3600;
			map.put(s, e);
		}
		return map;

	}

	
	public static Set<String>  getTimetable(String start, String end, int intervalTime) {
		Set<String> list = new LinkedHashSet<String>(6);
		long startTime = DateUtils.formatSecond(start);
		long endTime = DateUtils.formatSecond(end);
		while (startTime < endTime) {
			String s = DateUtils.secondToTime(startTime);
			if (s.equals(CommonConstant.TWELVE_P_M_STR)) {
				s = CommonConstant.BEFORE_DAWN;
			}
			long bookEnd = startTime + intervalTime;
			String e = DateUtils.secondToTime(bookEnd);
			startTime = startTime + 3600;
			list.add(s);
			list.add(e);
		}
		return list;

	}
	

}