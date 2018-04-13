package com.tournament.helper;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by destevancardozoj on 2/21/18.
 */

public class THApp extends Application {
  public static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
    MobileAds.initialize(this,
            getString(R.string.admob_id));
  }
}
