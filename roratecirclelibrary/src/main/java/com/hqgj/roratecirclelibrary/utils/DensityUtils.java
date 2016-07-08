package com.hqgj.roratecirclelibrary.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * author: ly
 * data: 2016/5/12
 */
public class DensityUtils {

    public static int getDeviceMinWidth(Context context){
        WindowManager manager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outputSize=new Point();
        manager.getDefaultDisplay().getSize(outputSize);
        return outputSize.x<=outputSize.y?outputSize.x:outputSize.y;
    }

}
