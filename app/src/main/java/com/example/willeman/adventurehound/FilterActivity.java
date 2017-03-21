package com.example.willeman.adventurehound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.couchbase.lite.util.Log;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WILLEMAN on 8/5/2016.
 */
public class FilterActivity extends AppCompatActivity {

    public static final String TAG = "AH.FilterActivity";

    Context context; //is this memory efficient?
    private int priceRangePrevMin = -1;
    private int priceRangePrevMax = -1;
    private int timeRangePrevMin = -1;
    private int timeRangePrevMax = -1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_main);
        context = this;

        //final RangeSeekBar priceRangeSeekBar = (RangeSeekBar) findViewById(R.id.filter_pricerange_seekbar);

        //priceRangeSeekBar.setRangeValues(0, 100);
        //priceRangeSeekBar.setNotifyWhileDragging(true);

        /*
        priceRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                int diff = maxValue - minValue;

                if (diff < 40) {

                    if (minValue != priceRangePrevMin) {
                        priceRangeSeekBar.setSelectedMinValue(priceRangePrevMin);
                    } else if (maxValue != priceRangePrevMax) {
                        priceRangeSeekBar.setSelectedMaxValue(priceRangePrevMax);
                    }
                } else {
                    priceRangePrevMin = minValue;
                    priceRangePrevMax = maxValue;
                }
            }
        });
        */

        final GridView keyWordsGridView = (GridView) findViewById(R.id.keyword_gridView);
        Button keyWordAddButton = (Button) findViewById(R.id.add_keyword_button);
        final List<String> items = new ArrayList<String>();

        final ArrayAdapter<String> keyWordAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        keyWordsGridView.setAdapter(keyWordAdapter);

        keyWordAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText keywordInputText = (EditText) findViewById(R.id.keyword_editbox);
                if (keywordInputText.getText().toString().isEmpty()) {
                    return;
                }
                //TODO: check for duplicates

                items.add(keywordInputText.getText().toString());
                keyWordAdapter.notifyDataSetChanged();
                Toast.makeText(FilterActivity.this, "'" + keywordInputText.getText().toString() +
                        "' added", Toast.LENGTH_LONG).show();
            }
        });
        keyWordsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FilterActivity.this, "Clicked", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.filter_apply_button);
        final CheckBox sport_checkbox = (CheckBox) findViewById(R.id.cat_sport_checkBox);
        final CheckBox art_checkbox = (CheckBox) findViewById(R.id.cat_art_checkbox);
        final CheckBox food_checkbox = (CheckBox) findViewById(R.id.cat_food_checkbox);
        final CheckBox adventure_checkbox = (CheckBox) findViewById(R.id.cat_adventure_checkbox);
        final CheckBox entertain_checkbox = (CheckBox) findViewById(R.id.cat_entertainment_checkbox);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Filter applied", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(context, ActivityListView.class);

                FilterCriteria filter = new FilterCriteria();
                filter.addCriteria(getResources().getString(R.string.cat_sport_label), Boolean.toString(sport_checkbox.isChecked()));
                filter.addCriteria(getResources().getString(R.string.cat_art_label), Boolean.toString(art_checkbox.isChecked()));
                filter.addCriteria(getResources().getString(R.string.cat_food_label), Boolean.toString(food_checkbox.isChecked()));
                filter.addCriteria(getResources().getString(R.string.cat_adventure_label), Boolean.toString(adventure_checkbox.isChecked()));
                filter.addCriteria(getResources().getString(R.string.cat_entertainment_label), Boolean.toString(entertain_checkbox.isChecked()));

                Bundle bundle = new Bundle();
                try {
                    Gson gson = new GsonBuilder().create();
                    bundle.putString(
                            getResources().getString(R.string.activity_bundle_type),
                            getResources().getString(R.string.filter_bundle));

                    bundle.putString(getResources().getString(R.string.filter_bundle), gson.toJson(filter));
                } catch (Exception e) {
                    Log.e(TAG, "Error: Serialising of FilterCriteria to JSON", e);
                }
                intent.putExtras(bundle);
                //startActivity(intent);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListFilter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.willeman.adventurehound/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListFilter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.willeman.adventurehound/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
