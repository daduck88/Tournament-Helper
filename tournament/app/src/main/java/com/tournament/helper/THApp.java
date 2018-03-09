package com.tournament.helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by destevancardozoj on 2/21/18.
 */

public class THApp extends Application {
  public static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
  }
}
