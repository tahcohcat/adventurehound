package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/6/2016.
 */
public class ActivityFilter {

    public static boolean isIncluded(
            TaskListDocument item,
            FilterCriteria criteria)
    {

        //item must match at least one category in filter
        return criteria.isIncluded(item.getCategory());
    }

    public static boolean isIncluded(
            TaskListDocument item,
            UpcomingDateCriteria criteria,
            String attribute)
    {
        //item must match at least one category in filter
        return criteria.isIncluded(item.getAttribute(attribute));
    }
}
