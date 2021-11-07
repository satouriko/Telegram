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
    public static long lastSuccessfulCheckUpdateTime = 0;
    public static String lastUpdateName = "";

    private static boolean configLoaded;

    static {
        loadConfig();
    }

    public static void saveConfig() {
        synchronized (sync) {
            try {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("lastSuccessfulCheckUpdateTime", lastSuccessfulCheckUpdateTime);
                editor.putString("lastUpdateName", lastUpdateName);

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
            lastSuccessfulCheckUpdateTime = preferences.getLong("lastSuccessfulCheckUpdateTime", 0);
            lastUpdateName = preferences.getString("lastUpdateName", "");
            configLoaded = true;
        }
    }

    public static void setLastSuccessfulCheckUpdateTime(long time) {
        lastSuccessfulCheckUpdateTime = time;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastSuccessfulCheckUpdateTime", lastSuccessfulCheckUpdateTime);
        editor.commit();
    }

    public static void setLastUpdateName(String updateName) {
        lastUpdateName = updateName;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastUpdateName", lastUpdateName);
        editor.commit();
    }
}
