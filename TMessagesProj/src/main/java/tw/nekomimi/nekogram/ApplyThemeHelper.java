package tw.nekomimi.nekogram;

import android.app.Activity;
import android.content.SharedPreferences;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;

import java.io.File;

public class ApplyThemeHelper implements NotificationCenter.NotificationCenterDelegate {
    public interface AfterApplyTheme {
        void afterApplyTheme(Theme.ThemeInfo themeInfo);
    }
    private String loadingThemeFileName;
    private String loadingThemeWallpaperName;
    private TLRPC.TL_wallPaper loadingThemeWallpaper;
    private Theme.ThemeInfo loadingThemeInfo;
    private TLRPC.TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private boolean loadingThemeNight;
    ActionBarLayout parentLayout;
    private AfterApplyTheme afterApplyTheme;

    public ApplyThemeHelper(final String theme, final boolean isNightTheme, ActionBarLayout parentLayout, AfterApplyTheme afterApplyTheme) {
        this.parentLayout = parentLayout;
        this.afterApplyTheme = afterApplyTheme;
        NotificationCenter.getInstance(UserConfig.selectedAccount).addObserver(this, NotificationCenter.fileDidLoad);
        loadingThemeNight = isNightTheme;
        TLRPC.TL_account_getTheme req = new TLRPC.TL_account_getTheme();
        req.format = "android";
        TLRPC.TL_inputThemeSlug inputThemeSlug = new TLRPC.TL_inputThemeSlug();
        inputThemeSlug.slug = theme;
        req.theme = inputThemeSlug;
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, (response, error) -> AndroidUtilities.runOnUIThread(() -> {
            if (response instanceof TLRPC.TL_theme) {
                TLRPC.TL_theme t = (TLRPC.TL_theme) response;
                if (t.settings != null) {
                    String key = Theme.getBaseThemeKey(t.settings);
                    Theme.ThemeInfo info = Theme.getTheme(key);
                    if (info != null) {
                        TLRPC.TL_wallPaper object;
                        if (t.settings.wallpaper instanceof TLRPC.TL_wallPaper) {
                            object = (TLRPC.TL_wallPaper) t.settings.wallpaper;
                            File path = FileLoader.getPathToAttach(object.document, true);
                            if (!path.exists()) {
                                loadingThemeAccent = true;
                                loadingThemeInfo = info;
                                loadingTheme = t;
                                loadingThemeWallpaper = object;
                                loadingThemeWallpaperName = FileLoader.getAttachFileName(object.document);
                                FileLoader.getInstance(UserConfig.selectedAccount).loadFile(object.document, object, 1, 1);
                                return;
                            }
                        }
                        applyTheme(info, isNightTheme);
                    }
                } else if (t.document != null) {
                    loadingThemeAccent = false;
                    loadingTheme = t;
                    loadingThemeFileName = FileLoader.getAttachFileName(loadingTheme.document);
                    FileLoader.getInstance(UserConfig.selectedAccount).loadFile(loadingTheme.document, t, 1, 1);
                }
            }
        }));
    }

    private void applyTheme(Theme.ThemeInfo applyingTheme, boolean nightTheme) {
        Theme.ThemeInfo previousTheme = Theme.getPreviousTheme();
        if (previousTheme == null) {
            return;
        }
        Theme.ThemeAccent accent = applyingTheme.getAccent(false);
        Theme.ThemeAccent previousAccent;
        if (previousTheme.prevAccentId >= 0) {
            previousAccent = previousTheme.themeAccentsMap.get(previousTheme.prevAccentId);
        } else {
            previousAccent = previousTheme.getAccent(false);
        }
        if (accent != null) {
            Theme.saveThemeAccents(applyingTheme, true, false, false, false);
            Theme.clearPreviousTheme();
            Theme.applyTheme(applyingTheme, nightTheme);
        } else {
            Theme.applyThemeFile(new File(applyingTheme.pathToFile), applyingTheme.name, applyingTheme.info, false);
            MessagesController.getInstance(applyingTheme.account).saveTheme(applyingTheme, null, false, false);
            SharedPreferences.Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", Activity.MODE_PRIVATE).edit();
            editor.putString("lastDayTheme", applyingTheme.getKey());
            editor.commit();
        }
        if (parentLayout != null) {
            parentLayout.rebuildAllFragmentViews(true, true);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didApplyNewTheme, previousTheme, previousAccent, true);
        if (afterApplyTheme != null) {
            afterApplyTheme.afterApplyTheme(applyingTheme);
        }
    }

    public void didReceivedNotification(int id, final int account, Object... args) {
        if (id == NotificationCenter.fileDidLoad) {
            if (loadingThemeFileName != null) {
                String path = (String) args[0];
                if (loadingThemeFileName.equals(path)) {
                    loadingThemeFileName = null;
                    File locFile = new File(ApplicationLoader.getFilesDirFixed(), "remote" + loadingTheme.id + ".attheme");
                    Theme.ThemeInfo themeInfo = Theme.fillThemeValues(locFile, loadingTheme.title, loadingTheme);
                    if (themeInfo != null) {
                        if (themeInfo.pathToWallpaper != null) {
                            File file = new File(themeInfo.pathToWallpaper);
                            if (!file.exists()) {
                                TLRPC.TL_account_getWallPaper req = new TLRPC.TL_account_getWallPaper();
                                TLRPC.TL_inputWallPaperSlug inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                                inputWallPaperSlug.slug = themeInfo.slug;
                                req.wallpaper = inputWallPaperSlug;
                                ConnectionsManager.getInstance(themeInfo.account).sendRequest(req, (response, error) -> AndroidUtilities.runOnUIThread(() -> {
                                    if (response instanceof TLRPC.TL_wallPaper) {
                                        TLRPC.TL_wallPaper wallPaper = (TLRPC.TL_wallPaper) response;
                                        loadingThemeInfo = themeInfo;
                                        loadingThemeWallpaperName = FileLoader.getAttachFileName(wallPaper.document);
                                        loadingThemeWallpaper = wallPaper;
                                        FileLoader.getInstance(themeInfo.account).loadFile(wallPaper.document, wallPaper, 1, 1);
                                    } else {
                                        applyTheme(themeInfo, loadingThemeNight);
                                        NotificationCenter.getInstance(UserConfig.selectedAccount).removeObserver(this, NotificationCenter.fileDidLoad);
                                    }
                                }));
                                return;
                            }
                        }
                        Theme.ThemeInfo finalThemeInfo = Theme.applyThemeFile(locFile, loadingTheme.title, loadingTheme, true);
                        if (finalThemeInfo != null) {
                            applyTheme(finalThemeInfo, loadingThemeNight);
                        }
                    }
                    NotificationCenter.getInstance(UserConfig.selectedAccount).removeObserver(this, NotificationCenter.fileDidLoad);
                }
            } else if (loadingThemeWallpaperName != null) {
                String path = (String) args[0];
                if (loadingThemeWallpaperName.equals(path)) {
                    loadingThemeWallpaperName = null;
                    File file = (File) args[1];
                    if (loadingThemeAccent) {
                        applyTheme(loadingThemeInfo, loadingThemeNight);
                    } else {
                        Theme.ThemeInfo info = loadingThemeInfo;
                        Utilities.globalQueue.postRunnable(() -> {
                            info.createBackground(file, info.pathToWallpaper);
                            AndroidUtilities.runOnUIThread(() -> {
                                if (loadingTheme == null) {
                                    return;
                                }
                                File locFile = new File(ApplicationLoader.getFilesDirFixed(), "remote" + loadingTheme.id + ".attheme");
                                Theme.ThemeInfo finalThemeInfo = Theme.applyThemeFile(locFile, loadingTheme.title, loadingTheme, true);
                                if (finalThemeInfo != null) {
                                    applyTheme(finalThemeInfo, loadingThemeNight);
                                }
                            });
                        });
                    }
                }
                NotificationCenter.getInstance(UserConfig.selectedAccount).removeObserver(this, NotificationCenter.fileDidLoad);
            }
        }
    }
}
