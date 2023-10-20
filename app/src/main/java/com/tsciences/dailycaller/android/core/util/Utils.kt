package com.tsciences.dailycaller.android.core.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.text.Html
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.view.Display
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.*
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.services.QueueDataProvider
import com.tsciences.dailycaller.android.ui.menu.ExpandedControlsActivity
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

inline fun <T> tryOrNull(
    block: () -> T
): T? = try {
    block()
} catch (t: Throwable) {
    t.printStackTrace()
    null
}

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}

fun isPermissionGranted(context: Context, permission: String): Boolean =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun getCategoryColor(categoryTag: String): Color {
    return when (categoryTag.uppercase()) {
        AppConstants.US -> colorUS
        AppConstants.TECH -> colorTECH
        AppConstants.ENTERTAINMENT -> colorENTERTAINMENT
        AppConstants.BUSINESS -> colorBUSINESS
        AppConstants.GUNS_GEAR -> colorGUNS_GEAR
        AppConstants.OPINION -> colorOPINION
        AppConstants.EDUCATION -> colorEDUCATION
        AppConstants.SPORTS -> colorSPORTS
        AppConstants.POLITICS -> colorPOLITICS
        AppConstants.MEDIA -> colorMEDIA
        AppConstants.INVESTIGATIVE_GROUP -> colorINVESTIGATIVE_GROUP
        AppConstants.WORLD -> colorWORLD
        AppConstants.ENERGY -> colorENERGY
        else -> colorDEFAULT
    }
}

@SuppressLint("SimpleDateFormat")
fun getTime(inputTime: String?, pattern: String?): String? {
    var time: String? = null
    var originalFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    if (inputTime != null) {
        val targetFormat: DateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
        val target2Format: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date: Date
        try {
            date = originalFormat.parse(inputTime) as Date
            time = if (DateUtils.isToday(date.time)) {
                targetFormat.format(date)
            } else {
                target2Format.format(date)

            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    } else {
        val date = originalFormat.format(Date())
        originalFormat =
            if (date.endsWith("1") && !date.endsWith("11")) SimpleDateFormat(AppConstants.FIRST_FORMAT) else if (date.endsWith(
                    "2"
                ) && !date.endsWith("12")
            ) SimpleDateFormat(AppConstants.SECOND_FORMAT) else if (date.endsWith("3") && !date.endsWith(
                    "13"
                )
            ) SimpleDateFormat(AppConstants.THIRD_FORMAT) else SimpleDateFormat(AppConstants.FOURTH_FORMAT)
        time = originalFormat.format(Date())
    }
    return time
}

fun getTimeFromSearchResp(resp: String?): String? {
    var tokens: StringTokenizer? = null
    tokens = StringTokenizer(resp, ".")
    return tokens.nextToken()
}

fun getLinkFromSearchItem(resp: String): String {
    return if (resp.contains("https")) {
        if (resp.contains("www")) {
            resp.replace("https://www.dailycaller.com", "")
        } else {
            resp.replace("https://dailycaller.com", "")
        }
    } else {
        if (resp.contains("www")) {
            resp.replace("http://www.dailycaller.com", "")
        } else {
            resp.replace("http://dailycaller.com", "")
        }
    }
}

fun getWishes(context: Context): String? {
    var wishes: String? = null
    val currentTime = Calendar.getInstance().time
    val df = SimpleDateFormat("HH")
    val hour = df.format(currentTime).toInt()
    if (hour >= 1 && hour <= 12) {
        wishes = context.resources.getString(R.string.good_morining)
    } else if (hour >= 12 && hour <= 16) {
        wishes = context.resources.getString(R.string.good_afternoon)
    } else if (hour >= 16 && hour <= 21) {
        wishes = context.resources.getString(R.string.good_evening)
    } else if (hour >= 21 && hour <= 24) {
        wishes = context.resources.getString(R.string.good_night)
    }
    return wishes
}

fun isDeviceTablet(context: Context): Boolean {
    return if (isTablet(context = context)) {
        getDevice5Inch(context = context)
    } else {
        false
    }
}

private fun getDevice5Inch(context: Context): Boolean {
    return try {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
        val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
        val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
        diagonalinch >= 7
    } catch (e: Exception) {
        false
    }
}

private fun isTablet(context: Context): Boolean {
    return context.getResources()
        .getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun share(context: Context, item: Item) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT, """${
            """
    Look at this news article from The Daily Caller : 
    ${item.title}
    """.trimIndent()
        } 
${Html.fromHtml(item.link, Html.FROM_HTML_MODE_LEGACY)}"""
    )
    sendIntent.type = "text/plain"
    context.startActivity(Intent.createChooser(sendIntent, "share using"))
}

fun showErrorDialog(context: Context?, errorString: String?) {
    AlertDialog.Builder(context).setTitle("Error")
        .setMessage(errorString)
        .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            public override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.cancel()
            }
        })
        .create()
        .show()
}

/**
 * Formats time from milliseconds to hh:mm:ss string format.
 */
fun formatMillis(millisec: Int): String {
    var seconds: Int = (millisec / 1000)
    val hours: Int = seconds / (60 * 60)
    seconds %= (60 * 60)
    val minutes: Int = seconds / 60
    seconds %= 60
    val time: String = if (hours > 0) {
        String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
    }
    return time
}

/**
 * Returns `true` if and only if the screen orientation is portrait.
 */
fun isOrientationPortrait(context: Context): Boolean {
    return (context.resources.configuration.orientation
            == Configuration.ORIENTATION_PORTRAIT)
}

fun showQueuePopup(context: Context?, view: View?, mediaInfo: MediaInfo?) {
    val PRELOAD_TIME_S: Int = 20
    val utilCastExecutor: Executor = Executors.newSingleThreadExecutor();
    val castSession: CastSession? =
        CastContext.getSharedInstance(
            context!!,
            utilCastExecutor
        ).result.sessionManager.currentCastSession
    if (castSession == null || !castSession.isConnected) {
        return
    }
    val remoteMediaClient: RemoteMediaClient? = castSession.remoteMediaClient
    if (remoteMediaClient == null) {
        return
    }
    val provider: QueueDataProvider? = QueueDataProvider.Companion.getInstance(context)
    val popup = PopupMenu((context)!!, (view)!!)
    popup.menuInflater.inflate(
        if (provider!!.isQueueDetached || provider!!.count == 0) R.menu.detached_popup_add_to_queue else R.menu.popup_add_to_queue,
        popup.menu
    )
    val clickListener: PopupMenu.OnMenuItemClickListener =
        object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                val queueItem: MediaQueueItem =
                    MediaQueueItem.Builder((mediaInfo)!!).setAutoplay(
                        true
                    ).setPreloadTime(PRELOAD_TIME_S.toDouble()).build()
                val newItemArray: Array<MediaQueueItem> = arrayOf(queueItem)
                var toastMessage: String? = null
                if (provider?.count == 0) {
                    remoteMediaClient.queueLoad(
                        newItemArray, 0,
                        MediaStatus.REPEAT_MODE_REPEAT_OFF, JSONObject()
                    )
                } else {
                    val currentId: Int = provider!!.currentItemId
                    if (menuItem.itemId == R.id.action_play_now) {
                        remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, JSONObject())
                    } else if (menuItem.itemId == R.id.action_play_next) {
                        val currentPosition: Int = provider!!.getPositionByItemId(currentId)
                        if (currentPosition == provider!!.count - 1) {
                            //we are adding to the end of queue
                            remoteMediaClient.queueAppendItem(queueItem, JSONObject())
                        } else {
                            val nextItem: MediaQueueItem? =
                                provider.getItem(currentPosition + 1)
                            if (nextItem != null) {
                                val nextItemId: Int = nextItem.itemId
                                remoteMediaClient.queueInsertItems(
                                    newItemArray,
                                    nextItemId,
                                    JSONObject()
                                )
                            } else {
                                //remote queue is not ready with item; try again.
                                return false
                            }
                        }
                        toastMessage = context.getString(
                            R.string.queue_item_added_to_play_next
                        )
                    } else {
                        return false
                    }
                }
                if (menuItem.getItemId() == R.id.action_play_now) {
                    val intent: Intent = Intent(context, ExpandedControlsActivity::class.java)
                    context.startActivity(intent)
                }
                if (!TextUtils.isEmpty(toastMessage)) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
    popup.setOnMenuItemClickListener(clickListener)
    popup.show()
}

/**
 * Returns the screen/display size
 *
 */
fun getDisplaySize(context: Context): Point {
    val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display = wm.defaultDisplay
    val width: Int = display.width
    val height: Int = display.height
    return Point(width, height)
}
