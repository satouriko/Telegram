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
    public static final boolean ignoreBlocked = true;
    public static boolean forceTablet = false;
    public static boolean rearVideoMessages = false;
    public static int mapPreviewProvider = 0;
    public static boolean residentNotification = false;
    public static boolean saveCacheToPrivateDirectory = Build.VERSION.SDK_INT >= 24;
    public static boolean unlimitedFavedStickers = false;

    public static boolean showReport = false;
    public static boolean showPrPr = true;
    public static boolean showViewHistory = true;
    public static boolean showAdminActions = false;
    public static boolean showChangePermissions = false;
    public static boolean showDeleteDownloadedFile = false;
    public static boolean showTranslate = true;
    public static boolean mapDriftingFix = false;
    public static int translationProvider = 1;

    public static long lastSuccessfulCheckUpdateTime = 0;
    public static final int THEME_VERSION = 1;
    public static int themeVersion = 0;

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
                editor.putBoolean("forceTablet", forceTablet);
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
                editor.putBoolean("unlimitedFavedStickers", unlimitedFavedStickers);
                editor.putBoolean("rearVideoMessages", rearVideoMessages);
                editor.putInt("translationProvider", translationProvider);
                editor.putLong("lastSuccessfulCheckUpdateTime", lastSuccessfulCheckUpdateTime);
                editor.putInt("themeVersion", themeVersion);

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
            forceTablet = preferences.getBoolean("forceTablet", false);
            mapPreviewProvider = preferences.getInt("mapPreviewProvider", 0);
            residentNotification = preferences.getBoolean("residentNotification", false);
            saveCacheToPrivateDirectory = preferences.getBoolean("saveCacheToPrivateDirectory", Build.VERSION.SDK_INT >= 24);
            showReport = preferences.getBoolean("showReport", false);
            showPrPr = preferences.getBoolean("showPrPr", true);
            showViewHistory = preferences.getBoolean("showViewHistory", true);
            showAdminActions = preferences.getBoolean("showAdminActions", false);
            showChangePermissions = preferences.getBoolean("showChangePermissions", false);
            showDeleteDownloadedFile = preferences.getBoolean("showDeleteDownloadedFile", false);
            showTranslate = preferences.getBoolean("showTranslate", false);
            unlimitedFavedStickers = preferences.getBoolean("unlimitedFavedStickers", false);
            translationProvider = preferences.getInt("translationProvider", 1);
            rearVideoMessages = preferences.getBoolean("rearVideoMessages", false);
            lastSuccessfulCheckUpdateTime = preferences.getLong("lastSuccessfulCheckUpdateTime", 0);
            themeVersion = preferences.getInt("themeVersion", 0);
            mapDriftingFix = preferences.getBoolean("mapDriftingFix", false);
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

    public static void toggleForceTablet() {
        forceTablet = !forceTablet;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("forceTablet", forceTablet);
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

    public static void setThemeVersion(int tv) {
        themeVersion = tv;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("themeVersion", themeVersion);
        editor.commit();
    }

    public static void toggleRearVideoMessages() {
        rearVideoMessages = !rearVideoMessages;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("rearVideoMessages", rearVideoMessages);
        editor.commit();
    }

    public static void setLastSuccessfulCheckUpdateTime(long time) {
        lastSuccessfulCheckUpdateTime = time;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastSuccessfulCheckUpdateTime", lastSuccessfulCheckUpdateTime);
        editor.commit();
    }

    public static void toggleMapDriftingFix() {
        mapDriftingFix = !mapDriftingFix;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("mapDriftingFix", mapDriftingFix);
        editor.commit();
    }
}
