package it.univr.wordautomata.stylings;

import java.lang.reflect.Method;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The FXWinUtil class provides utility methods for working with
 * Windows-specific features in JavaFX applications.
 */
public class FXWinUtil {

    private FXWinUtil() {
    }

    /**
     * Represents a handle to a window in the Windows operating system.
     * The handle is used to uniquely identify a window and interact with it.
     * 
     * @param stage the stage for which to get the native handle
     * @return the native handle for the specified stage
     */
    public static WinDef.HWND getNativeHandleForStage(Stage stage) {
        try {
            Method getPeer = Window.class.getDeclaredMethod("getPeer", (Class<?>[]) null);
            getPeer.setAccessible(true);
            Object tkStage = getPeer.invoke(stage);
            Method getRawHandle = tkStage.getClass().getMethod("getRawHandle");
            getRawHandle.setAccessible(true);
            Pointer pointer = new Pointer((Long) getRawHandle.invoke(tkStage));
            return new WinDef.HWND(pointer);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Sets the immersive dark mode for the specified stage.
     *
     * @param stage    the stage for which to set the immersive dark mode
     * @param darkMode true to enable dark mode, false to disable it
     */
    public static void setImmersiveDarkMode(Stage stage, boolean darkMode) {
        if (!Platform.isWindows()) {
            return;
        }

        HWND hwnd = FXWinUtil.getNativeHandleForStage(stage);
        Dwmapi dwmapi = Dwmapi.INSTANCE;
        WinDef.BOOLByReference darkModeRef = new WinDef.BOOLByReference(new WinDef.BOOL(darkMode));

        dwmapi.DwmSetWindowAttribute(hwnd, 20, darkModeRef, Native.getNativeSize(WinDef.BOOLByReference.class));
    }
}