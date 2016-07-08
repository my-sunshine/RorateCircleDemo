package com.hqgj.roratecirclelibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.hqgj.roratecirclelibrary.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 项目名称：AiShang
 * 类描述：
 * 创建人：ly
 * 创建时间：2016/1/18 14:22
 * 修改人：ly
 * 修改时间：2016/1/18 14:22
 * 修改备注：
 */
public class AppUtil  {


    public static void initImgLoader(Context context) {

        //显示图片的配置
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_default)
                .showImageOnLoading(R.drawable.ic_default)
                .showImageOnFail(R.drawable.ic_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                /*.displayer(new RoundedBitmapDisplayer(20))*/
                .build();

        //图片加载器ImageLoader的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(5)
                .memoryCache(new WeakMemoryCache())
                .writeDebugLogs()
                .threadPriority(Thread.MIN_PRIORITY)
                .build();


        ImageLoader.getInstance().init(config);
    }

}