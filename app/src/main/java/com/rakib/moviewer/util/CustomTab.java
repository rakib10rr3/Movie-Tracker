package com.rakib.moviewer.util;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by imran on 11/15/2017.
 */


/**
 * Usage DIrection :
 *
 * 1. Add this dependency in (build.gradle(Module:App))
 *      - compile 'com.android.support:customtabs:26.1.0'  // integer version number can be different read suggestio message if problem detected
 *
 *  2.call the open_link function with proper context and url
 *
 *  For Rakibul Huda,,
 *  Use below code in holder.itemView.SetOnclickListenr()
 *
 *  CustomTab ct=new CustomTab();
    ct.open_link(context,earthquake.getUrl());
 *
 *  comment everything else
 *
 *
 *
 */
public class CustomTab extends AppCompatActivity {
    private static final String TAG = "Information";
    String url;


    public CustomTab() {
    }

    public CustomTab(String url) {
        this.url = url;
    }

    public void open_link(Context mContext,String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor("#00E676"));
// set toolbar color and/or setting custom actions before invoking build()
// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
// and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(mContext, Uri.parse(url));

    }
}
