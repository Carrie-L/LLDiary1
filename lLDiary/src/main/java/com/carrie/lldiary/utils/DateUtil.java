package com.carrie.lldiary.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Reminders;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Ann;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.R.attr.description;
import static android.R.attr.track;

/**
 * 日期工具类
 *
 * @author Carrie
 */
public class DateUtil {
    public static final int WEEKDAYS = 7;
    private static final String tag = "DateAndWeek";
    private static final String TAG = "DateUtil";
    private static final String CAL_NAME = "LLDiary";//日历名称
    private static final String CAL_ACCOUNT_NAME = "com.android.exchange";
    private static final String CAL_ACCOUNT_TYPE = CalendarContract.ACCOUNT_TYPE_LOCAL;//本地日历
    public static String[] WEEK = {"Mon", "Tues", "Wed", "Thr", "Fri", "Sat",
            "Sun"};
    public static String[] WEEK2 = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    };
    private static String date;
    private static Calendar calendar;
    private static SimpleDateFormat sformat;
    private static String d;
    /**
     * 时+分+秒
     */
    private static String time;
    /**
     * 时+分
     */
    private static String time2;
    private static String week;
    private static long lDate;
    private static boolean alreadyExistdDiaryCalendars;

    /**
     * 格式： Mon, 07-26, 2015
     *
     * @return week+" , "+date;
     */
    public static String date1() {
        date();
        sformat = new SimpleDateFormat("MM-dd , yyyy", Locale.getDefault());

        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);// 锟矫碉拷锟斤拷锟斤拷锟斤拷锟节硷拷锟斤拷锟斤拷锟斤拷锟斤拷6锟斤拷
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        String week = WEEK[dayIndex - 1];// Saturday
        String d = week + " , " + date;
        return d;

    }

    /**
     * 格式： 2015年07月06日 星期一
     *
     * @return date+" "+week;
     */
    public static String date2() {
        date();
        week();
        d = date + " " + week;
        return d;
    }

    /**
     * 2016年6月5日 星期日
     *
     * @return
     */
    public static String getDateWeek() {
        week();
        return getDateOnly() + " " + week;
    }


    /**
     * Sat Jul 09 00:00:00 GMT+08:00 2016 星期日
     *
     * @return
     */
    public static String getDateWeek2() {
        week();
        Date date = string2Date2(getDateOnly());
        return date + " " + week;
    }


    /**
     * 仅显示日期 格式：yyyy年MM月dd日
     *
     * @return date
     */
    public static String dateOnly() {
        date();
        return date;
    }

    /**
     * 仅显示日期 格式：MM月dd日
     *
     * @return String
     */
    public static String dateOnly2() {
        sformat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        date = sformat.format(time);
        calendar.setTime(time);
        return date;
    }

    /**
     * 获取系统时间  : 4:18:27
     *
     * @return
     */
    public static String timeOnly() {
        time();
        return time;
    }

    /**
     * 获取系统时间  : 4:18
     *
     * @return
     */
    public static String timeOnly2() {
        time();
        return time2;
    }

    /**
     * 日期+时间: 2015年8月1日 12:27:09
     *
     * @return String date
     */
    public static String date3() {
        date();
        time();
        return date + " " + time;
    }

    /**
     * 日期+星期+时间： 2015年8月1日 星期六 12:27:09
     *
     * @return
     */
    public static String date4() {
        date();
        week();
        time();
        return date + " " + week + " " + time;
    }

    /**
     * 将String类型的日期转换为long型
     *
     * @param date
     */
    public static long string2Long(String date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date time = format.parse(date);
            lDate = time.getTime() / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lDate;
    }

    /**
     * 将String类型的日期转换为long型
     *
     * @param date
     */
    public static long string2Long2(String date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date time = format.parse(date);
            lDate = time.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lDate;
    }

    public static Date string2Date(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * yyyy年MM月dd日 ——> Date
     *
     * @param time
     * @return
     */
    public static Date string2Date2(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间+1s
     *
     * @param
     * @return calendar.getTime()
     */
    public static Date dateAdd1Second(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(string2Date(time));
        calendar.add(Calendar.SECOND, 1);
        return calendar.getTime();
    }

    /**
     * 日期公共方法
     */
    public static void date() {
        sformat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        date = sformat.format(time);
        calendar.setTime(time);
    }

    /**
     * 2016年6月5日
     *
     * @return
     */
    public static String getDateOnly() {
        //月份要加1
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "年" + monthOfYear + "月" + dayOfMonth + "日";
    }

    /**
     * 时间公共方法
     */
    public static void time() {
        Time t = new Time();
        t.setToNow();// 获取当前系统时间
        int hour = t.hour;
        int minute = t.minute;
        int second = t.second;
        time = hour + ":" + minute + ":" + second;
        if (minute < 10) {
            time2 = hour + ":0" + minute;
        } else {
            time2 = hour + ":" + minute;
        }

    }

    /**
     * 星期公共方法
     */
    public static void week() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);// 锟矫碉拷锟斤拷锟斤拷锟斤拷锟节硷拷锟斤拷锟斤拷锟斤拷锟斤拷6锟斤拷
        if (dayIndex > 0 || dayIndex <= WEEKDAYS) {
            week = WEEK2[dayIndex - 1];// Saturday
        }
    }

    /**
     * 获取当前系统时间
     *
     * @return <font color="#f97798">23:54:32</font>
     */
    public static String getCurrentTime() {
        Time t = new Time();
        t.setToNow();// 获取当前系统时间
        int hour = t.hour;
        int minute = t.minute;
        int second = t.second;
        return hour + ":" + minute + ":" + second;
    }

    /**
     * <font color="#f97798">获取当前系统时间</font>
     *
     * @return String 2016-05-05 23:28:03
     * @version 创建时间：2016年4月28日 上午9:02:50
     */
    public static String getCurrentTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static long getCurrentTimeLong() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * @return yyyy年MM月dd日
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * <font color="#f97798">获取当前系统时间</font>
     *
     * @return String yyyy年MM月dd日 HH:mm
     */
    public static String getCurrentTime3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }


    /**
     * <font color="#f97798">时间分段</font> <font color="#f97798"></font>
     *
     * @return void
     * @version 创建时间：2016年4月28日 上午9:02:07
     */
    public static String timePeriod() {
        Time t = new Time();
        t.setToNow();// 获取当前系统时间
        int hour = t.hour;//凌晨：01:00:00----04:59:59

        LogUtil.i(TAG, "HOUR=" + hour);

//早上：05:00:00----07:59:59
//
//上午：08:00:00----10:59:59
//
//中午：11:00:00----12:59:59
//
//下午：13:00:00----16:59:59
//
//傍晚：17:00:00----18:59:59
//
//晚上：19:00:00----22:59:59
//
//子夜：23:00:00----00:59:59
        if (hour >= 5 && hour < 8) {
            return "早上好";
        } else if (hour >= 8 && hour < 11) {
            return "上午好";
        } else if (hour >= 11 && hour < 13) {
            return "中午好";
        } else if (hour >= 13 && hour < 17) {
            return "下午好";
        } else if (hour >= 17 && hour < 19) {
            return "傍晚好";
        } else if (hour >= 19 && hour < 23) {
            return "晚上好";
        } else if (hour < 1) {
            return "子夜好";
        } else if (hour >= 1 && hour < 5) {
            return "凌晨好";
        }
        return "";
    }

    public static String[] WEEK0 = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    };

    /**
     * 根据日期获取星期
     */
    public static String getWeekAccordingDate(String date, Calendar calendar) {
        String week = "";
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        try {
            calendar.setTime(sformat.parse(date));
            int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
            LogUtil.i(TAG, "dayIndex=" + dayIndex);
            if (dayIndex > 0 || dayIndex <= 7) {
                week = WEEK0[dayIndex - 1];// Saturday
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }

    /**
     * @return 星期日 2:0:25
     */
    public static String getWeenAndTime() {
        String week = "";
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        String date = getDateOnly();
        LogUtil.i(TAG, "getDateOnly=" + date);
        try {
            calendar.setTime(sformat.parse(getDateOnly()));
            int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
            LogUtil.i(TAG, "dayIndex=" + dayIndex);
            if (dayIndex > 0 || dayIndex <= 7) {
                week = WEEK0[dayIndex - 1];// Saturday
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week + " " + getCurrentTime();
    }

    public static String getWeek() {
        calendar = Calendar.getInstance();
        sformat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        String date = getDateOnly();
        LogUtil.i(TAG, "getDateOnly=" + date);
        try {
            calendar.setTime(sformat.parse(getDateOnly()));
            int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
            LogUtil.i(TAG, "dayIndex=" + dayIndex);
            if (dayIndex > 0 || dayIndex <= 7) {
                week = WEEK0[dayIndex - 1];// Saturday
                return week;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrent_Date_Time_Week() {
        String time = "";
        return getCurrentTime2() + " " + getWeek();
    }

    /**
     * 给定年和月，计算一个月最多有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMaxDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);//7月
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        LogUtil.i(TAG, year + "年" + month + "月最多有" + maxDate + "天");
        return maxDate;
    }

    /**
     * 获取这周的起始日期，如今天是周日，2016年6月19日，则这周的起始日期为2016年5月30日
     *
     * @return 2016年5月30日
     */
    public static String getLastWeekDate(int year, int month, int day) {
        int lastWeekDay = 1;
        int lastWeekMonth = month;
        int lastWeekYear = year;

        if (day >= 7) {
            lastWeekDay = day - 6;
            LogUtil.e(TAG, "day>=7:lastWeek=" + lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日");
            return lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日";
        } else {
            if (month > 1) {
                lastWeekMonth = month - 1;
                int maxDayOfMonth = DateUtil.getMaxDaysOfMonth(year, month - 1);
                lastWeekDay = maxDayOfMonth - (7 - day) + 1;
                lastWeekYear = year;
                LogUtil.e(TAG, "month>1:lastWeek=" + lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日");
                return lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日";
            } else {
                lastWeekMonth = 12;
                int maxDayOfMonth = DateUtil.getMaxDaysOfMonth(year, month - 1);
                lastWeekDay = maxDayOfMonth - (7 - day) + 1;
                lastWeekYear = year - 1;
                LogUtil.e(TAG, "month=1:lastWeek=" + lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日");

            }
        }
        return lastWeekYear + "年" + lastWeekMonth + "月" + lastWeekDay + "日";
    }

    /**
     * 返回友好的日期格式
     *
     * @return Today, Yesterday, Tomorrow , 2016年7月9日 星期六
     */
    public static String friendlyDate(String dateStr) {
        Date date = string2Date2(dateStr);
        LogUtil.i(TAG, "dateStr=" + dateStr);
        LogUtil.i(TAG, "date=" + date);
        int dayDiff = compareWithTodayDate(date);
        LogUtil.i(TAG, "dayDiff=" + dayDiff);

        if (dayDiff == 0) {
            return "Today";
        } else if (dayDiff == 1) {
            return "Tomorrow";
        }
        if (dayDiff == -1) {
            return "Yesterday";
        } else {
            return dateStr;
        }
    }

    public static int compareWithTodayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        return day - today;
    }

    public static String fixDate(String date) {//2016年07月09日
//		String date1=date.substring(5,7);//07
//		String date2=date.substring(6,8);//7月
//		String date3=date.substring(4,6);//年0
//		String date4=date.substring(4,7);//年07
//		LogUtil.i(TAG,"fixDate_date1="+date1+",date2="+date2);
//		LogUtil.i(TAG,"fixDate_date3="+date3+",date4="+date4);
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        LogUtil.i(TAG, "fixDate_year=" + year + ",day=" + day);
        return year + "年" + month + "月" + day + "日";

    }


    /**
     * 从记录的日期到现在的时间间隔
     */
    public static String getDurTime(String date) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());//:ss
        try {
            Date d1 = df.parse(DateUtil.date3());// 当前时间
            Date d2 = df.parse(date); // 存储时间
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
//			dTime = String.valueOf(days);
//			System.out.println(mTime);
            LogUtil.i(TAG, "getDurTime=" + days + "天" + hours + "小时" + minutes + "分");
            return days + "天" + hours + "小时" + minutes + "分";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //===========时间、日期选择对话框====================

    public interface OnDateClickListener {
        void getDate(String date);
    }

    public interface OnTimeClickListener {
        void getTime(String time);
    }

    private static OnDateClickListener mDateListener;
    private static OnTimeClickListener mTimeListener;

    public static void setOnDateClickListener(OnDateClickListener listener) {
        mDateListener = listener;
    }

    public static void setOnTimeClickListener(OnTimeClickListener listener) {
        mTimeListener = listener;
    }

    /**
     * 返回格式：2016年7月10日 星期日
     *
     * @param context
     */
    public static void dateWeekPickerDialog(Context context) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //月份+1
                monthOfYear = monthOfYear + 1;
                String date = year + "年" + monthOfYear + "月" + dayOfMonth + "日";
                String week = DateUtil.getWeekAccordingDate(date, calendar);
                date = date + " " + week;
                LogUtil.e(TAG, "date=" + date);

                mDateListener.getDate(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 返回格式：2016年7月10日
     *
     * @param context
     */
    public static void datePickerDialog(Context context) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //月份+1
                monthOfYear = monthOfYear + 1;
                String date = year + "年" + monthOfYear + "月" + dayOfMonth + "日";
                LogUtil.e(TAG, "date=" + date);

                mDateListener.getDate(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 返回格式：2016年7月10日 星期日
     *
     * @param context
     */
    public static void dateTimePickerDialog(Context context) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//		DatePick datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//			@Override
//			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//				//月份+1
//				monthOfYear = monthOfYear + 1;
//				String date = year + "年" + monthOfYear + "月" + dayOfMonth + "日";
//				String week = DateUtil.getWeekAccordingDate(date, calendar);
//				date = date + " " + week;
//				LogUtil.e(TAG, "date=" + date);
//
//				mDateListener.getDate(date);
//			}
//		}, year, month, day);
//		datePickerDialog.show();
    }

    public static void timePickerDialog(Context context) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);


                        String mTime = format(hourOfDay) + ":" + format(minute);
                        System.out.println("mTime=" + mTime);

                        mTimeListener.getTime(mTime);

                    }
                }, mHour, mMinute, true);
        dialog.show();
    }

    /**
     * 格式化字符串(7:3->07:03)
     */
    public static String format(int x) {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    /**
     * 毫秒级的long转换成String
     *
     * @param mills long 毫秒
     * @return String yyyy年MM月dd日 HH:mm
     */
    public static String millLongToString(long mills) {
//        2、由long类型转换成Date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        java.util.Date dt = new Date(mills);
        return sdf.format(dt);
    }


    /**
     * 日历操作
     *
     * @param eventId 活动ID，亦即item项的id，唯一
     * @return bool, true, 添加成功；false,添加失败
     */
    public static boolean addToCalendar(Activity activity, long eventId, String title, String location, String description, long startMillis, long endMillis, boolean allDay) {
        int calId = getLocalCalendarId(activity);
        LogUtil.i(TAG, "addToCal: getLocalCalendarId: calId =" + calId);
        if (calId == -1 || !alreadyExistdDiaryCalendars) {
            calId = initCalendars(activity, calId == -1 ? 1 : calId);
            return insertCalendarEventsBK(activity, calId, eventId, title, location, description, startMillis, endMillis, allDay);
        } else {
            LogUtil.i(TAG, "addToCal: calId = " + calId);
            return insertOrUpdateCalEvents(activity, calId, eventId, title, location, description, startMillis, endMillis, allDay);
        }
    }

    private static boolean insertCalendarEventsBK(Activity activity, long calId, long eventId, String title, String location, String description, long startMillis, long endMillis, boolean allDay) {
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            ContentResolver cr = activity.getContentResolver();
            ContentValues values = putCalValues(calId, eventId, title, location, description, startMillis, endMillis, allDay);
            Uri uri = cr.insert(Events.CONTENT_URI, values);
            if (uri != null && uri.getLastPathSegment() != null) {
                ContentValues remindValues = new ContentValues();
                remindValues.put(Reminders.EVENT_ID, eventId);
                remindValues.put(Reminders.MINUTES, 1);
                remindValues.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                try {
                    Uri uri2 = activity.getContentResolver().insert(Reminders.CONTENT_URI, remindValues);
                    LogUtil.i(TAG, "uri2=" + uri2);
                    if (uri2 != null && uri2.getLastPathSegment() != null) {
                        return true;
                    }
                } catch (SQLiteException e) {
                    LogUtil.e(TAG, "SQLiteException:" + e.getMessage());
                }
            }
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return false;
    }

    private static boolean insertOrUpdateCalEvents(Activity activity, long calId, long eventId, String title, String location, String description, long startMillis, long endMillis, boolean allDay) {
        boolean success = false;
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            ContentResolver cr = activity.getContentResolver();
            ContentValues values = putCalValues(calId, eventId, title, location, description, startMillis, endMillis, allDay);
            Cursor cursor = cr.query(Events.CONTENT_URI, new String[]{"_id"}, "_id=?", new String[]{eventId + ""}, null);
            if (cursor != null) {
                success = cursor.getCount() == 1 ? updateCalendarEvents(activity, eventId, cr, values) : insertCalendarEvents(activity, eventId, cr, values);
                cursor.close();
            }
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return success;
    }

    private static boolean insertCalendarEvents(Activity activity, long eventId, ContentResolver cr, ContentValues values) {
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            Uri uri = cr.insert(Events.CONTENT_URI, values);
            if (uri != null && uri.getLastPathSegment() != null) {
                ContentValues remindValues = new ContentValues();
                remindValues.put(Reminders.EVENT_ID, eventId);
                remindValues.put(Reminders.MINUTES, 1);
                remindValues.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                try {
                    Uri uri2 = cr.insert(Reminders.CONTENT_URI, remindValues);
                    LogUtil.i(TAG, "uri2=" + uri2);
                    if (uri2 != null && uri2.getLastPathSegment() != null) {
                        return true;
                    }
                } catch (SQLiteException e) {
                    LogUtil.e(TAG, "SQLiteException:" + e.getMessage());
                }
            }
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return false;
    }

    private static boolean updateCalendarEvents(Activity activity, long eventId, ContentResolver cr, ContentValues values) {
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            Uri updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
            if (updateUri != null) {
                int rows = activity.getContentResolver().update(updateUri, values, null, null);
                LogUtil.i(TAG, "Rows updated: " + rows);
                if (rows > 0) {
                    return true;
                }
            }
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return false;
    }

    private static boolean deleteCaldarEvents(Activity activity, long eventID) {
        ContentResolver cr = activity.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);
        LogUtil.i(TAG, "Rows deleted: " + rows);
        if (rows > 0) {
            return true;
        }
        return false;
    }

    private static ContentValues putCalValues(long calId, long eventId, String title, String location, String description, long startMillis, long endMillis, boolean allDay) {
        ContentValues values = new ContentValues();
        values.put(Events._ID, eventId);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, title);
        if (allDay) {
            values.put(Events.ALL_DAY, true);
        }
        if (!TextUtils.isEmpty(location)) {
            values.put(Events.EVENT_LOCATION, location);
        }
        if (!TextUtils.isEmpty(description)) {
            values.put(Events.DESCRIPTION, description);
        }
        values.put(Events.CALENDAR_ID, calId);
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        return values;
    }

    /**
     * 添加日历账户
     *
     * @return int 日历ID
     */
    private static int initCalendars(Activity activity, int calId) {//插入成功
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put("_id", calId);
        value.put(Calendars.NAME, CAL_NAME);//LLDiary
        value.put(Calendars.ACCOUNT_NAME, CAL_ACCOUNT_NAME);
        value.put(Calendars.ACCOUNT_TYPE, CAL_ACCOUNT_TYPE);//"com.android.exchange"
        value.put(Calendars.CALENDAR_DISPLAY_NAME, activity.getString(R.string.app_name));
        value.put(Calendars.VISIBLE, 1);
        value.put(Calendars.CALENDAR_COLOR, activity.getResources().getColor(R.color.primaryColor));
        value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_EDITOR);
        value.put(Calendars.SYNC_EVENTS, 1);
        value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(Calendars.OWNER_ACCOUNT, CAL_ACCOUNT_NAME);

        Uri calendarUri = Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, CAL_ACCOUNT_NAME)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, CAL_ACCOUNT_TYPE)
                .build();
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            activity.getContentResolver().insert(calendarUri, value);
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return calId;
    }

    /**
     * 是否有本地的恋恋日记本日历账户,获取它的id
     *
     * @return 如果返回的Id为-1，则没有日记本账户，新建；
     */
    private static int getLocalCalendarId(Activity activity) {
        int calId = -1;
        alreadyExistdDiaryCalendars = false;
        if (PermissionUtils.checkPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR)) {
            LogUtil.i(TAG, "有日历权限");
            Cursor cursor = activity.getContentResolver().query(Calendars.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                LogUtil.i(TAG, "Count: " + cursor.getCount());
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)).equals(CAL_ACCOUNT_NAME) &&
                                cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_TYPE)).equals(CAL_ACCOUNT_TYPE)) {
                            calId = cursor.getInt(cursor.getColumnIndex("_id"));
                            alreadyExistdDiaryCalendars = true;
                            LogUtil.i(TAG, "cursor.moveToNext(): calId=" + calId);
                            break;
                        }
                    }
                } else {
                    calId = cursor.getCount() + 1;
                }
                cursor.close();
            }
        } else {
            PermissionUtils.requestPermission(activity, PermissionUtils.PERMISSION_READ_CALENDAR, PermissionUtils.PMS_CODE_READ_CALENDAR);
        }
        return calId;
    }

    /**
     * 获取本周的第一天(星期一)的日期
     *格式：yyyy年MM月dd日
     * @return String
     */
    public static String getWeekFirstDate() {
        int mondayPlus;
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            mondayPlus = 0;
        } else {
            mondayPlus = 1 - dayOfWeek;
        }
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return sdf.format(monday);
    }

    /**
     * 获取本月的第一天
     * @return
     */
    public static String getMonthFirstDate(){
        int monthPlus;
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfMonth = cd.get(Calendar.DAY_OF_MONTH) ; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfMonth == 1) {
            monthPlus = 0;
        } else {
            monthPlus = 1 - dayOfMonth;
        }
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, monthPlus);
        Date month = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return sdf.format(month);
    }

//    public static String getLastMonthFirstDate(){
//
//    }

}
