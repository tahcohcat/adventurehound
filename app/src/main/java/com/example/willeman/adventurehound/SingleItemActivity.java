package com.example.willeman.adventurehound;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by WILLEMAN on 8/5/2016.
 */
public class SingleItemActivity extends AppCompatActivity {

    public static final String TAG = "AH.FilterActivity";

    private String activityPosition;
    private TaskListDocument activityItem;

    Context context; //is this memory efficient?
    private ArrayList<String> userAttributesList;
    private ArrayAdapter userAttributesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitem_activity);

        activityItem = null;
        activityPosition=  Integer.toString(getResources().getInteger(R.integer.activity_position_unset));

        context = this;

        TextView textView = (TextView) findViewById(R.id.single_acitivity_details_edittext);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinksClickable(true);
        textView.setAutoLinkMask(Linkify.WEB_URLS);
        Linkify.addLinks(textView, Linkify.WEB_URLS);

        Switch isPriceKnown = (Switch)  findViewById(R.id.is_price_known);
        isPriceKnown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("[IsPriceKnown]Switch State=", ""+isChecked);

                EditText priceText = (EditText) findViewById(R.id.price_edit_box);
                TextView priceCurrencyText = (TextView) findViewById(R.id.price_currency_text);

                if (isChecked)
                {
                    priceText.setVisibility(View.VISIBLE);
                    priceCurrencyText.setVisibility(View.VISIBLE);
                }
                else
                {
                    priceText.setVisibility(View.GONE);
                    priceCurrencyText.setVisibility(View.GONE);
                }
            }

        });

        Switch isTimeRangeKnown = (Switch)  findViewById(R.id.is_timelenght_known);
        isTimeRangeKnown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("[isTimeRangeKnown]Switch State=", ""+isChecked);

                GridLayout timeLengthLayout = (GridLayout) findViewById(R.id.time_range_grid_layout);

                if (isChecked)
                {
                    timeLengthLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    timeLengthLayout.setVisibility(View.GONE);
                }
            }

        });


        Switch isSpecificDateSwitch = (Switch)  findViewById(R.id.is_specific_date_switch);
        isSpecificDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("[IsSpecificDate]Switch State=", ""+isChecked);

                DatePicker datePicker = (DatePicker) findViewById(R.id.activity_datepicker);

                if (isChecked)
                {
                    datePicker.setVisibility(View.VISIBLE);
                }
                else
                {
                    datePicker.setVisibility(View.GONE);
                }
            }

        });



        //Rating
        NumberPicker np = (NumberPicker) findViewById(R.id.rating_numberpicker);
        String[] nums = new String[6];
        for(int i = 0; i<nums.length; i++)
            if (i == 0)
            {
                nums[i]="-";
            }
            else
            {
                nums[i] = Integer.toString(i);
            }

        np.setMinValue(0);
        np.setMaxValue(5);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(0);

        //TimeRange - days
        np = (NumberPicker) findViewById(R.id.timelenght_days_numberpicker);
        nums = new String[31];
        for(int i = 0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }


        np.setMinValue(0);
        np.setMaxValue(30);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(0);

        //TimeRange - hours
        np = (NumberPicker) findViewById(R.id.timelenght_hours_numberpicker);
        nums = new String[24];
        for(int i = 0; i<nums.length; i++)
            nums[i] = Integer.toString(i);

        np.setMinValue(0);
        np.setMaxValue(23);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(0);

        //TimeRange - mins
        np = (NumberPicker) findViewById(R.id.timelenght_mins_numberpicker);
        nums = new String[4];
        nums[0]=Integer.toString(0);
        nums[1]=Integer.toString(15);
        nums[2]=Integer.toString(30);
        nums[3]=Integer.toString(45);

        np.setMinValue(0);
        np.setMaxValue(3);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.single_acitivity_apply_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sendUpdatedItemOnUI();
                } catch (Exception e) {
                    Log.e(TAG, "Error: Serialising of FilterCriteria to JSON", e);
                    Snackbar.make(view, "Activity edit apply failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        final GridView attributesGridView = (GridView) findViewById(R.id.attribute_gridview);

        //TODO: temp workaround so that gridview can scroll. but
        //better solution would be for the gridview to grow and
        //not be scrollable.
        attributesGridView.setOnTouchListener(new GridView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        Button attrAddButton = (Button) findViewById(R.id.add_attribute_button);
        this.userAttributesList= new ArrayList<String>();
        this.userAttributesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.userAttributesList);
        attributesGridView.setAdapter(this.userAttributesAdapter);

        attrAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText attrKeyText = (EditText) findViewById(R.id.attr_key_editbox);
                if (attrKeyText.getText().toString().isEmpty()) {
                    return;
                }

                EditText attrValueText = (EditText) findViewById(R.id.attr_value_editbox);
                if (attrValueText.getText().toString().isEmpty()) {
                    return;
                }

                if (!isKeyReservedWord(attrKeyText.getText().toString())) {
                    userAttributesList.add(attrKeyText.getText().toString() + ":" + attrValueText.getText().toString());
                    userAttributesAdapter.notifyDataSetChanged();
                    Toast.makeText(SingleItemActivity.this, "'" + attrKeyText.getText().toString() +
                            "' added", Toast.LENGTH_LONG).show();
                }
            }
        });


        String bundleTypeId = context.getResources().getString(R.string.activity_bundle);

        try {
            Gson gson = new GsonBuilder().create();

            Bundle temp_debug_bundle_extras = getIntent().getExtras();
            boolean hasKey = temp_debug_bundle_extras.containsKey(bundleTypeId);
            boolean hasKey2 = getIntent().hasExtra(bundleTypeId);
            String intent_string =getIntent().toString();

            //existing item - populate widgets
            if (getIntent().hasExtra(getResources().getString(R.string.activity_position)))
            {
                if (!getIntent().hasExtra(bundleTypeId)) {
                    throw new IllegalArgumentException("Activity cannot find extras " + bundleTypeId);
                }

                //get existing item
                this.activityPosition =
                        getIntent().getStringExtra(getResources().getString(R.string.activity_position));
                this.activityItem = gson.fromJson(getIntent().getStringExtra(bundleTypeId), TaskListDocument.class);
            }
            else
            {
                //create new item
                this.activityItem = new TaskListDocument(
                        "",
                        "",
                        "",
                        false,
                        new ArrayList<String>(),
                        new HashMap<String,String>());
            }

            populateActivity(activityItem);
        }
        catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }

        //TODO: can use this for del later on
        attributesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SingleItemActivity.this, "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO: refactor to util lib and class static
    private boolean isKeyReservedWord(String key) {
        try {
            AttributeTypes isOkay = AttributeTypes.valueOf(key);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private void sendUpdatedItemOnUI() throws Exception {

        Intent intent = new Intent(this.context, ActivityListView.class);

        Gson gson = new GsonBuilder().create();
        Bundle bundle = new Bundle();

        try {
            //TODO: could be changed to new_item_added bundletype?
            bundle.putString(
                    getResources().getString(R.string.activity_bundle_type),
                    getResources().getString(R.string.update_single_item_bundle));

            bundle.putString(
                    getResources().getString(R.string.activity_bundle),
                    gson.toJson(readItemFromUI()));
        }
        catch (Exception ex)
        {
            Log.e(TAG,"Error reading widgets on save ", ex.toString());
            Toast.makeText(SingleItemActivity.this, "Error: cannot save activity", Toast.LENGTH_LONG).show();
            return;
        }

        bundle.putString(
                getResources().getString(R.string.activity_position),
                this.activityPosition);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private String readCategories() {

        StringBuilder categories = new StringBuilder();
        readCheckbox(R.id.cat_sport_checkBox, R.string.cat_sport_label, categories);
        readCheckbox(R.id.cat_art_checkbox, R.string.cat_art_label, categories);
        readCheckbox(R.id.cat_entertainment_checkbox, R.string.cat_entertainment_label, categories);
        readCheckbox(R.id.cat_adventure_checkbox, R.string.cat_adventure_label, categories);
        readCheckbox(R.id.cat_food_checkbox, R.string.cat_food_label, categories);
        readCheckbox(R.id.cat_other_checkbox, R.string.cat_other_label, categories);
        return categories.toString();
    }

    private void readCheckbox(
            int checkboxId,
            int checkboxStringId,
            StringBuilder sb) {

        CheckBox check = (CheckBox) findViewById(checkboxId);
        if (check == null)
        {
            Log.e(TAG,"Null widget found in readCheckBox");
            return;
        }

        if (check.isChecked())
        {
            if (!sb.toString().isEmpty())
            {
                sb.append(",");
            }
            sb.append(getResources().getString(checkboxStringId));
        }
    }

    private TaskListDocument readItemFromUI() throws Exception {

        if (this.activityItem == null)
        {
            FloatingActionButton view = (FloatingActionButton) findViewById(R.id.single_acitivity_apply_button);
            Snackbar.make(view, "Activity edit apply failed - no item present", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }

        EditText activityName = (EditText) findViewById(R.id.single_acitivity_name_edittext);
        EditText activityDetails = (EditText) findViewById(R.id.single_acitivity_details_edittext);
        NumberPicker rating = (NumberPicker) findViewById(R.id.rating_numberpicker);
        EditText price = (EditText) findViewById(R.id.price_edit_box);

        this.activityItem.setActivity(activityName.getText().toString());
        this.activityItem.setDetails(activityDetails.getText().toString());
        this.activityItem.updateAttribute(AttributeTypes.category.toString(), readCategories());
        this.activityItem.updateAttribute(AttributeTypes.rating.toString(), Integer.toString(rating.getValue()));
        this.activityItem.updateAttribute(AttributeTypes.time_length.toString(),readTimeLength());
        this.activityItem.updateAttribute(AttributeTypes.date.toString(),readDate());
        this.activityItem.updateAttribute(AttributeTypes.price.toString(), readPrice());

        Switch hasBeenDone = (Switch)  findViewById(R.id.has_been_done);
        this.activityItem.setHasBeenDone(hasBeenDone.isChecked());

        readUserAttributes();

        return this.activityItem;
    }

    private void readUserAttributes() {
        GridView attrGrid = (GridView) findViewById(R.id.attribute_gridview);
        for(int i=0; i<attrGrid .getChildCount(); i++) {
            TextView attribute = (TextView)attrGrid .getChildAt(i);
            List<String> attrPair = new ArrayList<String>(Arrays.asList(attribute.getText().toString().split(":")));
            if (attrPair.size()==2) {
                this.activityItem.addAttribute(attrPair.get(0), attrPair.get(1));
            }
        }
    }

    private String readPrice() {
        Switch priceSwitch = (Switch) findViewById(R.id.is_price_known);
        if (!priceSwitch.isChecked()) {
            return Integer.toString(getResources().getInteger(R.integer.activity_price_unset));
        }
        EditText price = (EditText) findViewById(R.id.price_edit_box);
        return price.getText().toString();
    }

    private String readDate() {

        Switch isSpecificDate = (Switch) findViewById(R.id.is_specific_date_switch);
        if (!isSpecificDate.isChecked())
        {
            return "";
        }

        DatePicker datePicker = (DatePicker) findViewById(R.id.activity_datepicker);
        return Integer.toString(datePicker.getDayOfMonth()) + "/" +
            Integer.toString(datePicker.getMonth()) + "/" +
            Integer.toString(datePicker.getYear());
    }

    private String readTimeLength() {

        Switch timeKnownSwitch = (Switch) findViewById(R.id.is_timelenght_known);
        if (!timeKnownSwitch.isChecked())
        {
            return "";
        }
        NumberPicker dayPicker = (NumberPicker) findViewById(R.id.timelenght_days_numberpicker);
        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.timelenght_hours_numberpicker);
        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.timelenght_mins_numberpicker);

        String time =
            Integer.toString(dayPicker.getValue()) + ":" +
            Integer.toString(hourPicker.getValue()) + ":" +
            Integer.toString(minutePicker.getValue());

        return time;
    }

    private void populateActivity(TaskListDocument item) throws Exception {
        EditText activityName = (EditText) findViewById(R.id.single_acitivity_name_edittext);
        activityName.setText(item.getActivity());

        EditText activityDetails = (EditText) findViewById(R.id.single_acitivity_details_edittext);
        activityDetails.setText(item.getDetails());

        setCategory(item.getAttribute(AttributeTypes.category.toString()));
        setRating(item.getAttribute(AttributeTypes.rating.toString()));
        setPrice(item.getAttribute(AttributeTypes.price.toString()));

        Switch hasBeenDone = (Switch)  findViewById(R.id.has_been_done);
        hasBeenDone.setChecked(item.hasBeenDone());

        if (!item.getAttribute(AttributeTypes.price.toString()).isEmpty())
        {
            Switch priceSwitch = (Switch) findViewById(R.id.is_price_known);
            priceSwitch.setChecked(true);
            TextView currentText = (TextView) findViewById(R.id.price_currency_text);
            EditText currentEdit = (EditText) findViewById(R.id.price_edit_box);
            currentText.setVisibility(View.VISIBLE);
            currentEdit.setVisibility(View.VISIBLE);
        }

        if (!item.getAttribute("date").isEmpty())
        {
            try {
                Switch dateSwitch = (Switch) findViewById(R.id.is_specific_date_switch);
                dateSwitch.setChecked(true);

                String dateString = item.getAttribute(AttributeTypes.date.toString());

                List<String> list = new ArrayList<String>(Arrays.asList(dateString.split("/")));
                if (list.size() != 3)
                {
                    Log.e(TAG,"Invalid date string for activity");
                    return;
                }

                int dayOfMonth = Integer.parseInt(list.get(0));
                int month = Integer.parseInt(list.get(1));
                int year  = Integer.parseInt(list.get(2));

                DatePicker datePicker = (DatePicker) findViewById(R.id.activity_datepicker);
                datePicker.setVisibility(View.VISIBLE);
                datePicker.updateDate(year,month,dayOfMonth);
            }
            catch (Exception ex)
            {
                Log.e(TAG,"Invalid date found ",ex);
            }
        }

        setTime(item.getAttribute(AttributeTypes.time_length.toString()));

        for (Map.Entry<String,String> attrib : item.getAttributes().entrySet()) {
            if (!isKeyReservedWord(attrib.getKey())) {
                this.userAttributesList.add(attrib.getKey() + ":" + attrib.getValue());
                this.userAttributesAdapter.notifyDataSetChanged();
            }
        }

    }

    private void setTime(String timeLength) {

        if ((timeLength == null) || (timeLength.isEmpty()))
        {
            return;
        }

        Switch timeSwitch = (Switch) findViewById(R.id.is_timelenght_known);
        timeSwitch.setChecked(true);

        NumberPicker dayPicker = (NumberPicker) findViewById(R.id.timelenght_days_numberpicker);
        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.timelenght_hours_numberpicker);
        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.timelenght_mins_numberpicker);

        dayPicker.setVisibility(View.VISIBLE);
        hourPicker.setVisibility(View.VISIBLE);
        minutePicker.setVisibility(View.VISIBLE);

        try {
            List<String> list = new ArrayList<String>(Arrays.asList(timeLength.split(":")));
            if (list.size() != 3)
            {
                Log.e(TAG,"Invalid timeLength for activity");
                return;
            }

            int days  = Integer.parseInt(list.get(0));
            int hours = Integer.parseInt(list.get(1));
            int mins  = Integer.parseInt(list.get(2));

            if (days >= 0 && days < 31) {
                dayPicker.setValue(days);
            }
            if (hours>=0 && hours<24){
                hourPicker.setValue(hours);
            }
            if (mins>=0 && mins<60) {
                minutePicker.setValue(mins);
            }

        }
        catch (Exception ex)
        {
            Log.e(TAG,"",ex);
        }
    }

    private void setPrice(String priceString) {

        if (priceString == null)
        {
            return;
        }
        try {
            //check validity
            int price = Integer.parseInt(priceString);

            EditText priceText = (EditText) findViewById(R.id.price_edit_box);
            priceText.setText(priceString);
        }
        catch (Exception ex)
        {
            Log.e(TAG,"Error setting Price ", ex.toString());
            //what to do
        }
    }

    public void setCategory(String category) {
        setCheckbox(R.id.cat_sport_checkBox, R.string.cat_sport_label, category);
        setCheckbox(R.id.cat_art_checkbox, R.string.cat_art_label, category);
        setCheckbox(R.id.cat_entertainment_checkbox, R.string.cat_entertainment_label, category);
        setCheckbox(R.id.cat_adventure_checkbox, R.string.cat_adventure_label, category);
        setCheckbox(R.id.cat_food_checkbox, R.string.cat_food_label, category);
        setCheckbox(R.id.cat_other_checkbox, R.string.cat_other_label, category);
    }

    private void setCheckbox(
            int checkboxId,
            int checkboxLabel,
            String field) {

        CheckBox check = (CheckBox) findViewById(checkboxId);
        if (check == null)
        {
            Log.e(TAG,"Null widget found in readCheckBox");
            return;
        }

        if ((getResources().getString(checkboxLabel) != null)
            && (getResources().getString(checkboxLabel).equalsIgnoreCase(field)))
        {
            check.setChecked(true);
            return;
        }
        check.setChecked(false);
    }

    public void setRating(String ratingString) {

        NumberPicker ratingPicker = (NumberPicker) findViewById(R.id.rating_numberpicker);
        if (ratingPicker == null)
        {
            Log.e(TAG,"Null widget found in setRating");
            return;
        }

        try {
            if (ratingString == null)
            {
                assert(R.integer.activity_rating_unset >= ratingPicker.getMinValue());
                assert(R.integer.activity_rating_unset <= ratingPicker.getMaxValue());
                ratingPicker.setValue(getResources().getInteger(R.integer.activity_rating_unset));
                return;
            }
            int rating = Integer.parseInt(ratingString);

            if ((rating < ratingPicker.getMinValue()) &&
                (rating > ratingPicker.getMaxValue()))
            {
                Log.e(TAG,"Rating of " + rating + "out of bounds");
                return;
            }
            ratingPicker.setValue(rating);
        }
        catch (Exception ex)
        {
            Log.e(TAG,"Conversion error found in setRating");
            return;
        }

    }
}
