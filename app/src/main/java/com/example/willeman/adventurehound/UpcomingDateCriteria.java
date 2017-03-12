package com.example.willeman.adventurehound;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WILLEMAN on 7/20/2016.
 */
public class UpcomingDateCriteria {

    public static final String TAG = "AH.FilterCriteria";

    public static boolean PROPERTY_INCLUDED_BOOL = true;
    public static boolean PROPERTY_EXCLUDED_BOOL = false;

    public static int PROPERTY_INCLUDED_INT = 1;
    public static int PROPERTY_EXCLUDED_INT = 0;

    private GregorianCalendar currentDate = null;

    private HashMap<String, String> criteria;


    public UpcomingDateCriteria() {
        criteria = new HashMap<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String now = dateFormat.format(date); //31/06/2016

        try {
            List<String> list = new ArrayList<String>(Arrays.asList(now.split("/")));
            assert (list.size() == 3);

            int currentDayOfMonth = Integer.parseInt(list.get(0));
            int currentMonth = Integer.parseInt(list.get(1));
            int currentYear = Integer.parseInt(list.get(2));
            this.currentDate = new GregorianCalendar(currentYear, currentMonth, currentDayOfMonth);
        } catch (Exception ex) {
            Log.e(TAG, "UpdateDateCriteria - constructor failed setting date [" + now + "]");
        }
    }

    public Map<String, String> getCriteria() {
        return this.criteria;
    }

    public void addCriteria(int diffDays) {
        criteria.put("DayDifference", Integer.toString(diffDays));
    }


    public boolean isIncluded(String dateString) {
        if ((dateString == null) || (dateString.isEmpty())) {
            return false;
        }

        try {
            List<String> list = new ArrayList<String>(Arrays.asList(dateString.split("/")));
            if (list.size() != 3) {
                com.couchbase.lite.util.Log.e(TAG, "Invalid date string for activity");
                return false;
            }

            int dayOfMonth = Integer.parseInt(list.get(0));
            int month = Integer.parseInt(list.get(1))+1; //android datepicker and java date use base 0 and base 1 offsets..
            int year = Integer.parseInt(list.get(2));

            Calendar itemDate = new GregorianCalendar(year, month, dayOfMonth);
            int dayDifference = daysBetween(this.currentDate.getTime(),itemDate.getTime());
            if ((dayDifference >= 0) && (dayDifference <= 7)) {
                return true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: exception thrown " + ex.toString());
        }
        return false;
    }

    private int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}

