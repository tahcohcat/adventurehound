package com.example.willeman.adventurehound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class ListViewPagerAdapter extends FragmentStatePagerAdapter {

    public SearchTypes searchType = SearchTypes.EmptyList;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Intent intent;

    public ListViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    public ListViewPagerAdapter(FragmentManager supportFragmentManager, Intent intent) {
        super(supportFragmentManager);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {

        android.support.v4.app.Fragment fragment = null;
        if (i == 0) {
            fragment = new ListViewMainFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(ListViewMainFragment.ARG_OBJECT, i + 1);
            args.putString("SearchType", this.searchType.toString());
            fragment.setArguments(args);
        }
        else
        {
            fragment = new ListViewUpcomingFragment();
            Bundle args = new Bundle();
            args.putInt(ListViewUpcomingFragment.ARG_OBJECT, i + 1); //not used but for austerity since we should really be injecting items we need
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
        {
            return "Activity List";
        }
        if (position == 1)
        {
            return "Upcoming";
        }
        return "OBJECT " + (position + 1);
    }

    public void refreshPage(int position) {
        if (position == 1)
        {
           if (getRegisteredFragment(position)!=null)
           {
               ListViewUpcomingFragment f =
                       (ListViewUpcomingFragment) getRegisteredFragment(position);
               f.refreshItems();
           }
        }
    }
}
