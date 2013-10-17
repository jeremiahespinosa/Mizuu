package com.miz.widgets;

import com.miz.functions.MizLib;
import com.miz.mizuu.ShowDetails;
import com.miz.mizuu.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

@SuppressWarnings("deprecation")
public class ShowCoverWidgetProvider extends AppWidgetProvider {

	public static final String SHOW_COVER_WIDGET = "showCoverWidget";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (SHOW_COVER_WIDGET.equals(action)) {
			Intent openShow = new Intent();
			openShow.putExtra("showId", intent.getStringExtra("showId"));
			openShow.putExtra("isFromWidget", true);
			openShow.setClass(context, ShowDetails.class);
			openShow.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(openShow);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; ++i) {
			Intent intent = new Intent(context, ShowCoverWidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.movie_cover_widget);

			if (MizLib.hasICS())
				rv.setRemoteAdapter(R.id.widget_grid, intent);
			else
				rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_grid, intent);

			Intent toastIntent = new Intent(context, ShowCoverWidgetProvider.class);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			rv.setPendingIntentTemplate(R.id.widget_grid, PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT));

			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}