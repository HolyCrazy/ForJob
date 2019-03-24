package com.example.leeduo.forjob.Tools;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by LeeDuo on 2019/3/10.
 */

public class ScreenTools {
    private static WindowManager windowManager;
    private static Display display;

    public static int getScreenWidth( Context context){
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }
    public static int getScreenHeight( Context context){
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

}
