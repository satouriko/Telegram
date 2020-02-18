package tw.nekomimi.nekogram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

public class NekoConfig {

    private static final Object sync = new Object();
    public static boolean useIPv6 = false;
    public static boolean ignoreBlocked = false;
    public static boolean forceTablet = false;
    public static int nameOrder = 1;
    public static int mapPreviewProvider = 0;
    public static boolean residentNotification = false;
    public static boolean saveCacheToPrivateDirectory = Build.VERSION.SDK_INT >= 24;
    public static boolean unlimitedFavedStickers = false;

    public static boolean showReport = false;
    public static boolean showPrPr = false;
    public static boolean showViewHistory = true;
    public static boolean showAdminActions = true;
    public static boolean showChangePermissions = true;
    public static boolean showDeleteDownloadedFile = true;
    public static boolean showTranslate = true;

    public static int eventType = 0;
    public static int actionBarDecoration = 0;
    public static boolean newYear = false;

    public static int translationProvider = 1;

    private static boolean configLoaded;

    static {
        loadConfig();
    }


    public static void saveConfig() {
        synchronized (sync) {
            try {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("useIPv6", useIPv6);
                editor.putBoolean("ignoreBlocked", ignoreBlocked);
                editor.putBoolean("forceTablet", forceTablet);
                editor.putInt("nameOrder", nameOrder);
                editor.putInt("mapPreviewProvider", mapPreviewProvider);
                editor.putBoolean("residentNotification", residentNotification);
                editor.putBoolean("saveCacheToPrivateDirectory", saveCacheToPrivateDirectory);
                editor.putBoolean("showReport", showReport);
                editor.putBoolean("showPrPr", showPrPr);
                editor.putBoolean("showViewHistory", showViewHistory);
                editor.putBoolean("showAdminActions", showAdminActions);
                editor.putBoolean("showChangePermissions", showChangePermissions);
                editor.putBoolean("showDeleteDownloadedFile", showDeleteDownloadedFile);
                editor.putBoolean("showTranslate", showTranslate);
                editor.putBoolean("newYear", newYear);
                editor.putBoolean("unlimitedFavedStickers", unlimitedFavedStickers);
                editor.putInt("translationProvider", translationProvider);
                editor.putInt("eventType", eventType);
                editor.putInt("actionBarDecoration", actionBarDecoration);

                editor.commit();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static void loadConfig() {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }

            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
            useIPv6 = preferences.getBoolean("useIPv6", false);
            ignoreBlocked = preferences.getBoolean("ignoreBlocked", false);
            forceTablet = preferences.getBoolean("forceTablet", false);
            nameOrder = preferences.getInt("nameOrder", 1);
            mapPreviewProvider = preferences.getInt("mapPreviewProvider", 0);
            residentNotification = preferences.getBoolean("residentNotification", false);
            saveCacheToPrivateDirectory = preferences.getBoolean("saveCacheToPrivateDirectory", Build.VERSION.SDK_INT >= 24);
            showReport = preferences.getBoolean("showReport", false);
            showPrPr = preferences.getBoolean("showPrPr", false);
            showViewHistory = preferences.getBoolean("showViewHistory", true);
            showAdminActions = preferences.getBoolean("showAdminActions", true);
            showChangePermissions = preferences.getBoolean("showChangePermissions", true);
            showDeleteDownloadedFile = preferences.getBoolean("showDeleteDownloadedFile", true);
            showTranslate = preferences.getBoolean("showTranslate", true);
            eventType = preferences.getInt("eventType", 0);
            actionBarDecoration = preferences.getInt("actionBarDecoration", 0);
            newYear = preferences.getBoolean("newYear", false);
            unlimitedFavedStickers = preferences.getBoolean("unlimitedFavedStickers", false);
            translationProvider = preferences.getInt("translationProvider", 1);
            configLoaded = true;
        }
    }

    public static void toggleShowReport() {
        showReport = !showReport;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showReport", showReport);
        editor.commit();
    }


    public static void toggleShowViewHistory() {
        showViewHistory = !showViewHistory;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showViewHistory", showViewHistory);
        editor.commit();
    }

    public static void toggleShowPrPr() {
        showPrPr = !showPrPr;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showPrPr", showPrPr);
        editor.commit();
    }

    public static void toggleShowAdminActions() {
        showAdminActions = !showAdminActions;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showAdminActions", showAdminActions);
        editor.commit();
    }

    public static void toggleShowChangePermissions() {
        showChangePermissions = !showChangePermissions;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showChangePermissions", showChangePermissions);
        editor.commit();
    }

    public static void toggleShowDeleteDownloadedFile() {
        showDeleteDownloadedFile = !showDeleteDownloadedFile;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showDeleteDownloadedFile", showDeleteDownloadedFile);
        editor.commit();
    }

    public static void toggleIPv6() {
        useIPv6 = !useIPv6;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("useIPv6", useIPv6);
        editor.commit();
    }

    public static void toggleIgnoreBlocked() {
        ignoreBlocked = !ignoreBlocked;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ignoreBlocked", ignoreBlocked);
        editor.commit();
    }

    public static void toggleForceTablet() {
        forceTablet = !forceTablet;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("forceTablet", forceTablet);
        editor.commit();
    }

    public static void setNameOrder(int order) {
        nameOrder = order;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("nameOrder", nameOrder);
        editor.commit();
    }

    public static void setMapPreviewProvider(int provider) {
        mapPreviewProvider = provider;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("mapPreviewProvider", mapPreviewProvider);
        editor.commit();
    }

    public static void toggleResidentNotification() {
        residentNotification = !residentNotification;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("residentNotification", residentNotification);
        editor.commit();
        Intent duangIntent = new Intent(ApplicationLoader.applicationContext, DuangService.class);
        if (residentNotification) {
            ApplicationLoader.applicationContext.startService(duangIntent);
        } else {
            ApplicationLoader.applicationContext.stopService(duangIntent);
        }
    }

    public static void toggleSaveCacheToPrivateDirectory() {
        saveCacheToPrivateDirectory = !saveCacheToPrivateDirectory;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("saveCacheToPrivateDirectory", saveCacheToPrivateDirectory);
        editor.commit();
    }

    public static void setEventType(int type) {
        eventType = type;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("eventType", eventType);
        editor.commit();
    }

    public static void setActionBarDecoration(int decoration) {
        actionBarDecoration = decoration;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("actionBarDecoration", actionBarDecoration);
        editor.commit();
    }

    public static void toggleNewYear() {
        newYear = !newYear;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("newYear", newYear);
        editor.commit();
    }

    public static void toggleUnlimitedFavedStickers() {
        unlimitedFavedStickers = !unlimitedFavedStickers;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("unlimitedFavedStickers", unlimitedFavedStickers);
        editor.commit();
    }

    public static void toggleShowTranslate() {
        showTranslate = !showTranslate;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showTranslate", showTranslate);
        editor.commit();
    }

    public static void setTranslationProvider(int provider) {
        translationProvider = provider;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("translationProvider", translationProvider);
        editor.commit();
    }

}
