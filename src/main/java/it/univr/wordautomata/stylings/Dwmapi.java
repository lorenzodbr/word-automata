package it.univr.wordautomata.stylings;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;

/**
 * The Dwmapi interface provides access to functions in the Dwmapi.dll library,
 * which is used for Desktop Window Manager (DWM) composition.
 */
public interface Dwmapi extends Library {
    Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

    /**
     * Sets the value of a specified attribute for a window.
     *
     * @param hwnd The handle to the window.
     * @param dwAttribute The attribute to set.
     * @param pvAttribute The new value of the attribute.
     * @param cbAttribute The size, in bytes, of the attribute value.
     * @return If the function succeeds, it returns S_OK. Otherwise, it returns an HRESULT error code.
     */
    int DwmSetWindowAttribute(WinDef.HWND hwnd, int dwAttribute, PointerType pvAttribute, int cbAttribute);
}