package com.example.willeman.adventurehound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willeman.adventurehound.utils.AttributeIcons;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by WILLEMAN on 8/9/2016.
 */
public class CustomSwipeAdapter extends BaseAdapter {

    public static final String TAG = "TTD.CustomSwipeAdapter";
    private static LayoutInflater inflater=null;

    private ListView lv;
    private Activity parent;

    private boolean mSwiping; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed; // Detects if user

    private int swipe_duration;
    private int move_duration;

    private Context context;
    private float mDownX;
    private boolean swiped;
    private int mSwipeSlop = -1;

    Map<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
    Map<Integer,TaskListDocument> activityItemMap;

    private int[][] attrIds = new int[2][5];

    public CustomSwipeAdapter(
            Context context,
            boolean isSwipingUp,
            boolean isItemPressed,
            int swipe_duration,
            int move_duration,
            Map<Integer,TaskListDocument> itemMap,
            ListView listview,
            Activity parent) {

        activityItemMap=itemMap;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.mSwiping = isSwipingUp;
        this.mItemPressed = isItemPressed;
        this.move_duration = move_duration;
        this.swipe_duration = swipe_duration;
        this.lv = listview;
        this.parent = parent;

        attrIds[0][0]=R.id.attribute_image00;
        attrIds[0][1]=R.id.attribute_image01;
        attrIds[0][2]=R.id.attribute_image02;
        attrIds[0][3]=R.id.attribute_image03;
        attrIds[0][4]=R.id.attribute_image04;

        attrIds[1][0]=R.id.attribute_image10;
        attrIds[1][1]=R.id.attribute_image11;
        attrIds[1][2]=R.id.attribute_image12;
        attrIds[1][3]=R.id.attribute_image13;
        attrIds[1][4]=R.id.attribute_image14;
    }

    public void remove(Object item) {

        try {
            TaskListDocument document = (TaskListDocument)item;
            //Toast.makeText(context, "Removing: " + document.getActivity(), Toast.LENGTH_LONG).show();
            //call couchbase remove and repopulate list

            ActivityListView listViewActivity = (ActivityListView) parent;
            listViewActivity.removeItem(document);
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error : ",ex);
            TaskListDocument document = (TaskListDocument)item;
            if (document == null)
            {
                assert(false);
                return;
            }
            Toast.makeText(context, "Removing: of " + document.getActivity() + "failed", Toast.LENGTH_LONG).show();
        }
    }

    // animates the removal of the view, also animates the rest of the view into position
    private void animateRemoval(final ListView listView, View viewToRemove)
    {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        final CustomSwipeAdapter adapter = (CustomSwipeAdapter)lv.getAdapter();
        for (int i = 0; i < listView.getChildCount(); ++i)
        {
            View child = listView.getChildAt(i);
            if (child != viewToRemove)
            {
                int position = firstVisiblePosition + i;
                long itemId = listView.getAdapter().getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }

        adapter.remove(adapter.getItem(listView.getPositionForView(viewToRemove)));

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw()
            {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i)
                {
                    final View child = listView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null)
                    {
                        if (startTop != top)
                        {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(move_duration).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable()
                                {
                                    public void run()
                                    {
                                        mSwiping = false;
                                        lv.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    }
                    else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(move_duration).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable()
                            {
                                public void run()
                                {
                                    mSwiping = false;
                                    lv.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return activityItemMap.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return position;
        return activityItemMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public void sortByAtoZ() {

        Map<Integer, TaskListDocument> sorted = sortByValue(activityItemMap);
        final CustomSwipeAdapter adapter = (CustomSwipeAdapter)lv.getAdapter();
        adapter.activityItemMap.clear();

        int itemCount=0;
        for (Map.Entry<Integer, TaskListDocument> entry : sorted.entrySet()) {
            Integer key = entry.getKey();
            adapter.activityItemMap.put(itemCount++, entry.getValue());
        }
        adapter.notifyDataSetChanged();
    }

    public void sortByRating() {
        Map<Integer, TaskListDocument> sorted = activityItemMap;
        List<TaskListDocument> sortedList = new ArrayList<TaskListDocument>(sorted.values());

        try{
            Collections.sort((List<TaskListDocument>) sortedList, new TaskListDocComparator());
        }
        catch (Exception ex)
        {
            Log.e(TAG,"",ex);
        }

        final CustomSwipeAdapter adapter = (CustomSwipeAdapter)lv.getAdapter();
        adapter.activityItemMap.clear();

        int itemCount=0;
        for (TaskListDocument item : sortedList) {
            adapter.activityItemMap.put(itemCount++, item);
        }
        adapter.notifyDataSetChanged();
    }

    static class TaskListDocComparator implements Comparator<TaskListDocument>
    {
        @Override
        public int compare(TaskListDocument t1, TaskListDocument t2)
        {
            String r1 = t1.getAttribute("rating");
            String r2 = t2.getAttribute("rating");

            if (r1 == null)
            {
                r1 = "0";
            }
            if (r2 == null)
            {
                r2 = "0";
            }
            return r2.compareTo(r1);
        }
    }

    public class Holder
    {
        int visibleIconCounter;
        TextView activityName;
        TextView activityDetails;
        ImageView img;

        ImageView[][] attrImages = new ImageView[2][5];

        private int getAttrIconRow() {
            assert(this. visibleIconCounter/5 < 2);
            return this. visibleIconCounter/5;
        }

        private int getAttrIconCol() {
            assert(this. visibleIconCounter % 5 < 5);
            return this. visibleIconCounter % 5;
        }

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder=new Holder();
        holder.visibleIconCounter=0;
        View rowView;
        try
        {
            rowView = inflater.inflate(R.layout.custom_listview_item, null);

            holder.img=(ImageView) rowView.findViewById(R.id.activityIcon);
            holder.activityName=(TextView) rowView.findViewById(R.id.activityText);
            holder.activityDetails=(TextView) rowView.findViewById(R.id.activitySubtext);

            for (int row =0; row < 2; ++row)
            {
                for (int col =0; col < 5; ++col)
                {
                    holder.attrImages[row][col] = (ImageView)rowView.findViewById(attrIds[row][col]);
                    holder.attrImages[row][col].setVisibility(View.GONE);
                }
            }

            //TODO: should be in separate function

            if (activityItemMap.get(position).getAttribute(AttributeTypes.rating.toString()) != null)
            {
                int rating = Integer.parseInt(activityItemMap.get(position).getAttribute(AttributeTypes.rating.toString()));
                for (int ratingCount = 0; ratingCount < rating; ++ratingCount)
                {
                    holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(R.drawable.ic_mustache_rating);
                    holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                    holder.visibleIconCounter++;
                }
            }

            if ((activityItemMap.get(position).getAttribute(AttributeTypes.price.toString()) != null) &&
                (!activityItemMap.get(position).getAttribute(AttributeTypes.price.toString()).isEmpty()))
            {
                try {
                    int price = Integer.parseInt(activityItemMap.get(position).getAttribute(AttributeTypes.price.toString()));
                    assert(price>=0);
                    if (price > context.getResources().getInteger(R.integer.price_expensive_threshold)) {
                        holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(R.drawable.pricing_icon_05);
                        holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                        holder.visibleIconCounter++;
                    }
                }
                catch (Exception ex)
                {
                    Log.e(TAG,"",ex);
                }
            }

            if ((activityItemMap.get(position).getAttribute(AttributeTypes.time_length.toString()) != null) &&
                    (!activityItemMap.get(position).getAttribute(AttributeTypes.time_length.toString()).isEmpty()))
            {
                try {
                    String timeLength = activityItemMap.get(position).getAttribute(AttributeTypes.time_length.toString());

                    List<String> list = new ArrayList<String>(Arrays.asList(timeLength.split(":")));
                    if (list.size() == 3) {
                        int days = Integer.parseInt(list.get(0));
                        int hours = Integer.parseInt(list.get(1));

                        if (days > context.getResources().getInteger(R.integer.long_timelength_days_threshold))
                        {
                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(R.drawable.ic_time_04);
                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                            holder.visibleIconCounter++;

                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(R.drawable.ic_time_04);
                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                            holder.visibleIconCounter++;
                        }
                        else if (hours > context.getResources().getInteger(R.integer.long_timelength_hours_threshold))
                        {
                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(R.drawable.ic_time_03);
                            holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                            holder.visibleIconCounter++;
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.e(TAG,"",ex);
                }
            }

            for (Map.Entry<String,String> attrib : activityItemMap.get(position).getAttributes().entrySet()) {
                int resourceId = getIconForAttr(attrib.getKey());

                if ((resourceId != -1) && (holder.visibleIconCounter < 10))
                {
                    holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setImageResource(resourceId);
                    holder.attrImages[holder.getAttrIconRow()][holder.getAttrIconCol()].setVisibility(View.VISIBLE);
                    holder.visibleIconCounter++;
                }
            }

            String category = "";
            if (activityItemMap.get(position).hasAttribute(AttributeTypes.category.toString()))
            {
                category = activityItemMap.get(position).getAttribute(AttributeTypes.category.toString());
            }

            //String category = activityItemMap.get(position).getCategory();
            //holder.img.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            if (category.toLowerCase().equalsIgnoreCase("default"))
            {
                //R.drawable.ic_art_activity_24dp,R.drawable.ic_activity_tech_24dp,R.drawable.ic_adventure_hound_icon,
                holder.img.setImageResource(R.drawable.ic_adventure_hound_icon);
            } else if (category.toLowerCase().contains("art"))
            {
                holder.img.setImageResource(R.drawable.ic_art_activity_24dp);
                //holder.img.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTagArt));
            }
            else if (category.toLowerCase().contains("sport")) {
                holder.img.setImageResource(R.drawable.ic_activity_active_24dp);
            }
            else if (category.toLowerCase().contains("adventure")) {
                holder.img.setImageResource(R.drawable.ic_activity_adventure_24dp);
            }
            else if (category.toLowerCase().contains("entertainment")) {
                holder.img.setImageResource(R.drawable.ic_activity_entertainment_24dp);
            } else if (category.toLowerCase().contains("food")) {
                holder.img.setImageResource(R.drawable.ic_activity_food_24dp);
            }
            else {
                holder.img.setImageResource(R.drawable.ic_activity_unknown);
                Log.e(TAG, "Unknown category [" + category + "]");
                //holder.img.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTagDefault));
            }

            holder.activityName.setText(activityItemMap.get(position).getActivity());
            holder.activityDetails.setText(activityItemMap.get(position).getDetails());

            rowView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    if (mSwipeSlop < 0) {
                        mSwipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (mItemPressed) {
                                // Doesn't allow swiping two items at same time
                                return false;
                            }
                            mItemPressed = true;
                            mDownX = event.getX();
                            swiped = false;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            v.setTranslationX(0);
                            mItemPressed = false;
                            break;
                        case MotionEvent.ACTION_MOVE: {
                            float x = event.getX() + v.getTranslationX();
                            float deltaX = x - mDownX;
                            float deltaXAbs = Math.abs(deltaX);

                            if (deltaX < 0)
                            {
                                // only want to allow swiping in one direction
                                // since we have a tabbed pane
                                return false;
                            }

                            if (!mSwiping) {
                                if (deltaXAbs > mSwipeSlop)
                                    // tells if user is actually swiping or just touching in sloppy manner
                                {
                                    mSwiping = true;
                                    lv.requestDisallowInterceptTouchEvent(true);
                                } else {
                                    //click
                                }
                            }
                            if (mSwiping && !swiped) // Need to make sure the user is both swiping and has not already completed a swipe action (hence mSwiping and swiped)
                            {
                                v.setTranslationX((x - mDownX)); // moves the view as long as the user is swiping and has not already swiped

                                if (deltaX > v.getWidth() / 3) // swipe to right
                                {

                                    v.setEnabled(false); // need to disable the view for the animation to run

                                    // stacked the animations to have the pause before the views flings off screen
                                    v.animate().setDuration(100).translationX(v.getWidth() / 3).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            v.animate().setDuration(100).alpha(0).translationX(v.getWidth()).withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mSwiping = false;
                                                    mItemPressed = false;
                                                    animateRemoval(lv, v);
                                                }
                                            });
                                        }
                                    });
                                    mDownX = x;
                                    swiped = true;
                                    return true;

                                }
                                /*else if (deltaX < -1 * (v.getWidth() / 3)) // swipe to left
                                {
                                    //mDownX = x;
                                    //swiped = true;
                                    //mSwiping = false;
                                    //mItemPressed = false;

                                    //v.animate().setDuration(300).translationX(v.getWidth() / 3); // could pause here if you want, same way as delete
                                    //TextView tv = (TextView) v.findViewById(R.id.activityText);

                                    //TODO: should toggle isFavourite
                                    //tv.setText("Swiped!");
                                    return false;
                                }*/
                            }

                        }
                        break;
                        case MotionEvent.ACTION_UP: {
                            if (mSwiping) // if the user was swiping, don't go to the and just animate the view back into position
                            {
                                v.animate().setDuration(300).translationX(0).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwiping = false;
                                        mItemPressed = false;
                                        lv.setEnabled(true);
                                    }
                                });
                            } else // user was not swiping; registers as a click
                            {
                                mItemPressed = false;
                                lv.setEnabled(true);

                                int i = lv.getPositionForView(v);

                                Toast.makeText(context, "" + activityItemMap.get(position).getActivity(), Toast.LENGTH_LONG).show();

                                Bundle bundle = new Bundle();

                                try {
                                    Gson gson = new GsonBuilder().create();
                                    bundle.putString(context.getResources().getString(R.string.activity_position), Integer.toString(position));
                                    bundle.putString(context.getResources().getString(R.string.activity_bundle), gson.toJson(activityItemMap.get(position)));
                                    Intent intent = new Intent(context, SingleItemActivity.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error: Parsing Activity JSON", e);
                                    Toast.makeText(context, "Error loading requested activity", Toast.LENGTH_LONG).show();
                                }

                                return false;
                            }
                        }
                        default:
                            return false;
                    }
                    return true;
                }

            });
            return rowView;
        }
        catch (Exception ex) {
            Log.e(TAG, "Error: ", ex);
        }

        return null;
    }

    private int getIconForAttr(String attribute) {
        AttributeTypesExtra attributeType = isKnownAttribute(attribute);
        if (attributeType!=AttributeTypesExtra.none)
        {
            return AttributeIcons.getInstance().getIconResource(attributeType);
        }
        return -1; //TODO: replace with resource int
    }

    private static AttributeTypesExtra isKnownAttribute(String key) {
        try {
            return AttributeTypesExtra.valueOf(key);
        } catch (IllegalArgumentException ex) {
            return AttributeTypesExtra.none;
        }
    }

}
