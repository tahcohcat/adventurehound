package com.example.willeman.adventurehound;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import com.couchbase.lite.*;

/**
 * Created by WILLEMAN on 7/20/2016.
 */
public class FilterCriteria {

    public static final String TAG = "AH.FilterCriteria";

    public static boolean PROPERTY_INCLUDED_BOOL = true;
    public static boolean PROPERTY_EXCLUDED_BOOL = false;

    public static int PROPERTY_INCLUDED_INT = 1;
    public static int PROPERTY_EXCLUDED_INT = 0;

    private HashMap<String, String> criteria;
    public FilterCriteria()
    {
        criteria = new HashMap<>();
    }

    private boolean includeAll = false;

    public FilterCriteria(boolean includeAll)
    {
        this.includeAll = includeAll;
        criteria = new HashMap<>();
    }

    public Map<String,String> getCriteria() { return this.criteria;}

    //for ex. <"sport","include">, <food,"exclude">
    public void addCriteria(String key, String value)
    {
        if (criteria.containsKey(key))
        {
            Log.e(TAG, "Error: Criteria [" + key +"] added multiple times to FilterCriteria");
        }
        criteria.put(key,value);
    }

    public boolean isIncluded(TaskListDocument document)
    {
        for (Map.Entry<String,String> attribute: document.getAttributes().entrySet())
        {
            if (isIncluded(attribute.getKey()) || isIncluded(attribute.getValue()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isIncluded(String propertyKey) {

        if (includeAll)
        {
            return true;
        }

        String value = criteria.get(propertyKey);
        if (value == null) {
            return false;
        }

        try {
            Integer intValue = Integer.parseInt(value);
            return intValue == PROPERTY_INCLUDED_INT;
        } catch (NumberFormatException e) {
            try {
                Boolean boolValue = Boolean.parseBoolean(value);
                return boolValue == PROPERTY_INCLUDED_BOOL;
            }
            catch (NumberFormatException ex2) {
                Log.e(TAG, "Invalid property type for key + " + propertyKey);
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: exception thrown " + ex.toString());
        }
        return false;
    }
}
