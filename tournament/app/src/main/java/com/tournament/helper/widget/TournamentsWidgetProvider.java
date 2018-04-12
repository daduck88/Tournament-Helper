package com.tournament.helper.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.tournament.helper.R;
import com.tournament.helper.detail.DetailTournamentActivity;

public class TournamentsWidgetProvider extends AppWidgetProvider {
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    final int N = appWidgetIds.length;
    for(int i = 0; i < N; ++i) {
      RemoteViews remoteViews = updateWidgetListView(context,
          appWidgetIds[i]);
      appWidgetManager.updateAppWidget(appWidgetIds[i],
          remoteViews);
    }
  }

  private RemoteViews updateWidgetListView(Context context,
                                           int appWidgetId) {
    //which layout to show on widget
    RemoteViews remoteViews = new RemoteViews(
        context.getPackageName(), R.layout.simple_widget);

    //RemoteViews Service needed to provide adapter for ListView
    Intent svcIntent = new Intent(context, WidgetService.class);
    //passing app widget id to that RemoteViews Service
    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    //setting a unique Uri to the intent
    //don't know its purpose to me right now
    svcIntent.setData(Uri.parse(
        svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
    //setting adapter to listview of the widget
    remoteViews.setRemoteAdapter(R.id.listViewWidget,
        svcIntent);
    //setting an empty view in case of no data
    remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

    //click
    Intent clickIntentTemplate = new Intent(context, DetailTournamentActivity.class);
    PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
        .addNextIntentWithParentStack(clickIntentTemplate)
        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setPendingIntentTemplate(R.id.listViewWidget, clickPendingIntentTemplate);
    return remoteViews;
  }
}
