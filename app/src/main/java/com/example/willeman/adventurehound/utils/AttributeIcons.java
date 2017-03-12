package com.example.willeman.adventurehound.utils;

import com.example.willeman.adventurehound.AttributeTypesExtra;
import com.example.willeman.adventurehound.R;

/**
 * Created by WILLEMAN on 8/22/2016.
 */
public class AttributeIcons {
    private static AttributeIcons ourInstance = new AttributeIcons();

    public static AttributeIcons getInstance() {
        return ourInstance;
    }

    public int getIconResource(AttributeTypesExtra name) {

        if (name.equalsName(AttributeTypesExtra.llama.toString()))
        {
            return R.drawable.ic_attrib_llama;
        }
        if (name.equalsName(AttributeTypesExtra.community.toString()))
        {
            return R.drawable.ic_attrib_uplift;
        }
        if (name.equalsName(AttributeTypesExtra.creative.toString()))
        {
            return R.drawable.ic_attrib_creative;
        }
        if (name.equalsName(AttributeTypesExtra.culture.toString()))
        {
            return R.drawable.ic_attrib_culture;
        }
        if (name.equalsName(AttributeTypesExtra.faith.toString()))
        {
            return R.drawable.ic_attrib_faith;
        }
        if (name.equalsName(AttributeTypesExtra.romantic.toString()))
        {
            return R.drawable.ic_attrib_rom;
        }
        if (name.equalsName(AttributeTypesExtra.day.toString()))
        {
            return R.drawable.ic_weather_sun;
        }

        return R.integer.activity_position_unset;
    }
}
