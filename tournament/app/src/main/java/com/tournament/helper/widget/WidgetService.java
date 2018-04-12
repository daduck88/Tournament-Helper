package com.tournament.helper.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
  /*
   * So pretty simple just defining the Adapter of the listview
   * here Adapter is ListProvider
   * */

  @Override
  public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
    int appWidgetId = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);

    return (new ListProvider(this.getApplicationContext(), intent));
  }
}
