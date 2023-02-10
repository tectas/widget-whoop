package at.tectas.whoopwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
class WhoopWidget : AppWidgetProvider() {

    private val refreshClick = "at.tectas.whoopwidget.action.REFRESH_CLICK"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent!!.action.equals(refreshClick)) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, WhoopWidget::class.java))
            if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId)
                }
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val recoveryValue = Random.nextInt(0, 100);
        val strainValue = Random.nextInt(0, 21);
        val caloriesValue = Random.nextInt(1000, 2500);
        val hrvValue = Random.nextInt(50, 150);
        val currentDateTime = LocalDateTime.now();
        val dateFormatter = DateTimeFormatter.ofPattern("dd. MMM HH:mm")
        val currentDateTimeString = dateFormatter.format(currentDateTime)

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.whoop_widget)

        views.setProgressBar(R.id.recoveryProgressBar, 100, recoveryValue, false);
        views.setProgressBar(R.id.strainProgressBar, 21, strainValue, false);
        views.setTextViewText(R.id.caloriesValueTextView, caloriesValue.toString());
        views.setTextViewText(R.id.recoveryValueTextView, "$recoveryValue%");
        views.setTextViewText(R.id.strainValueTextView, strainValue.toString());
        views.setTextViewText(R.id.hrvValueTextView, hrvValue.toString());
        views.setTextViewText(R.id.lastUpdateTextView, "Updated $currentDateTimeString")

        views.setOnClickPendingIntent(R.id.updateButton, getPendingSelfIntent(context, refreshClick))

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent? {
        val intent = Intent(context, WhoopWidget::class.java) // An intent directed at the current class (the "self").
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}
