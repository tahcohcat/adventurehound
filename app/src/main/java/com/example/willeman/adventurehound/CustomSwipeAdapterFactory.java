package com.example.willeman.adventurehound;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import java.util.Map;

/**
 * Created by WILLEMAN on 8/9/2016.
 */
public class CustomSwipeAdapterFactory {

    private boolean SHOULD_DETECT_SWIPING_UP = false; // detects if user is swiping on ACTION_UP
    private boolean SHOULD_DETECT_ITEMPRESSED = false; // Detects if user is currently holding down a view
    private static final int SWIPE_DURATION = 250; // needed for velocity implementation
    private static final int MOVE_DURATION = 150;

    public CustomSwipeAdapterFactory()
    {
        super();
    }

    public CustomSwipeAdapter create(
            Context context,
            Map<Integer,TaskListDocument> itemMap,
            ListView listView,
            Activity parent)
    {
        return new CustomSwipeAdapter(
                context,
                SHOULD_DETECT_SWIPING_UP,
                SHOULD_DETECT_ITEMPRESSED,
                SWIPE_DURATION,
                MOVE_DURATION,
                itemMap,
                listView,
                parent);
    }

    
}
