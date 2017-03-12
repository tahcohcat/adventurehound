package com.example.willeman.adventurehound;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by WILLEMAN on 8/19/2016.
 */
public class TabParentActivity extends FragmentActivity {
    // When requested, this adapter returns a ListViewMainFragment,
    // representing an object in the collection.
    ListViewPagerAdapter mListViewPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_listview_item);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mListViewPagerAdapter =
                new ListViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mListViewPagerAdapter);
    }
}
