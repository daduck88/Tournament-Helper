package com.tournament.helper.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Pair;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tournament.helper.Contract;
import com.tournament.helper.R;
import com.tournament.helper.detail.matches.DetailTournamentMatchesFragment;

import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

  private Context context = null;
  List<Pair<String, String>> listItemList = new ArrayList<>();

  public ListProvider(Context context, Intent intent) {
    this.context = context;
    int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);

    populateListItem();
  }

  private void populateListItem() {
    Cursor cursor = context.getContentResolver().query(
        Contract.PATH_TOURNAMENTS_URI,
        null,
        null,
        null,
        null
    );
    try {
      while(cursor.moveToNext()) {
        listItemList.add(new Pair<>(cursor.getString(0), cursor.getString(1)));
      }
    } finally {
      cursor.close();
    }
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDataSetChanged() {

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public int getCount() {
    return listItemList.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  /*
   *Similar to getView of Adapter where instead of View
   *we return RemoteViews
   *
   */
  @Override
  public RemoteViews getViewAt(int position) {
    final RemoteViews remoteView = new RemoteViews(
        context.getPackageName(), R.layout.tournament_item_widget);
    Pair<String, String> listItem = listItemList.get(position);
    remoteView.setTextViewText(R.id.title, listItem.second);
    Intent fillInIntent = new Intent();
    fillInIntent.putExtra(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID, listItem.first);
    remoteView.setOnClickFillInIntent(R.id.itemClick, fillInIntent);
    return remoteView;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }
}
