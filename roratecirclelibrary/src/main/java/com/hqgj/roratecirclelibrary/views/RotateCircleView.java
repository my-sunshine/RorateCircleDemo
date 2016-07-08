package com.hqgj.roratecirclelibrary.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.hqgj.roratecirclelibrary.R;
import com.hqgj.roratecirclelibrary.model.SelectItem;
import com.hqgj.roratecirclelibrary.utils.AppUtil;
import com.hqgj.roratecirclelibrary.utils.DensityUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by mojo on 2016/5/8.
 * <p>
 * http://blog.chinaunix.net/uid-26885609-id-3479671.html
 *
 * http://trylovecatch.iteye.com/blog/1189452
 *
 */
public class RotateCircleView extends View {

    private ArrayList<SelectItem> selectItems = new ArrayList<>();
    //Item列表
    private ArrayList<Item> items = new ArrayList<>();
    //数目
    private int itemCount = 0;
    //圆心坐标
    private int pointX = 0, pointY = 0;
    //半径
    private int radius = 0;
    //每两个点间隔的角度
    private int degreeDelta;
    //measure方法是否已经被调用了
    private boolean flag = false;
    private boolean isOnMeasure = false;
    private boolean isFinish = false;
    //刷新
    private int REFRESH = 1;
    //每隔100ms旋转一次
    private static final int INTERVAL = 100000;
    //动画类
    private BarAnimation anim;
    //动画动画是否启动
    private boolean animInStart = false;
    private float startX;
    private float startY;
    private int defaultImg = R.drawable.ic_default;
    private float imgWidth = 148.0f;
    private float deviceWidth = 720.0f;
    private float scale=1.0f;
    private DisplayImageOptions options;

    private MyClickListener listener;

    public void setOnMyClickListener(MyClickListener listener) {
        this.listener = listener;
    }

    public interface MyClickListener {
        void onClick(String typeId, String typeName, String age);
    }

    class BarAnimation extends Animation {

        public BarAnimation() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                resetItemsAngle(0.5f);
                computeCoordinates();
            } else {
                RotateCircleView.this.startAnimation(anim);
            }
            postInvalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFinish) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                RotateCircleView.this.clearAnimation();
                animInStart = false;
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!animInStart) {
                    RotateCircleView.this.startAnimation(anim);
                }
                confirmPointPosition(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (!animInStart) {
                    if (Math.abs(event.getX() - startX) > 5 || Math.abs(event.getY() - startY) > 5) {
                        RotateCircleView.this.startAnimation(anim);
                    }
                }
                break;
        }
        return true;
    }

    private void confirmPointPosition(float x, float y) {
        String typeId = "1";
        String typeName = "1";
        String ageId = "2";
        for (int index = 0; index < itemCount; index++) {
            float imgCenterX = items.get(index).x;
            float imgCenterY = items.get(index).y;
            if (items.get(index).bitmap == null) {
                break;
            }
            float imgCircle = items.get(index).bitmap.getWidth();
            double r = Math.sqrt(Math.pow(x - imgCenterX, 2) + Math.pow(y - imgCenterY, 2));
            if (r <= imgCircle / 2) {
                typeId = items.get(index).id;
                typeName = items.get(index).type;
                ageId = items.get(index).ageId;
                listener.onClick(typeId, typeName, ageId);
            }
        }
    }


    /**
     * 计算每个点的坐标
     */
    private void computeCoordinates() {
        Item stone;
        for (int index = 0; index < itemCount; index++) {
            stone = items.get(index);
            //angle * Math.PI / 180; //换算成弧度
            stone.x = pointX + (float) (radius * Math.cos(stone.angle * Math.PI / 180));
            stone.y = pointY + (float) (radius * Math.sin(stone.angle * Math.PI / 180));
        }
    }

    private void resetItemsAngle(float a) {
        float angle = computeCurrentAngle(a);
        for (int index = 0; index < itemCount; index++) {
            items.get(index).angle = angle;
            angle += degreeDelta;
            angle = angle % 360;
        }
    }

    private float computeCurrentAngle(float a) {
        return items.get(0).angle += a;
    }


    //不能在布局文件中使用,没有自定义属性,当在代码中创建对象时会被调用
    public RotateCircleView(Context context) {
        super(context, null);
    }

    //能在布局文件中使用,没有自定义属性,可以使用系统的属性,通过xml文件来创建一个view对象的时候调用
    public RotateCircleView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    //没有进去？
    //能在布局文件中使用,有自定义属性
    public RotateCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(!ImageLoader.getInstance().isInited()){
            AppUtil.initImgLoader(context);
        }

        //setBackgroundResource(R.drawable.start_test_bg);
        deviceWidth= DensityUtils.getDeviceMinWidth(context);

        defaultImg=R.drawable.ic_default;
        imgWidth = 148.0f;
        scale=1.0f;

        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.RotateCircleView,defStyleAttr,0);

        defaultImg=typedArray.getResourceId(R.styleable.RotateCircleView_defaultImg,R.drawable.ic_default);

        imgWidth=typedArray.getFloat(R.styleable.RotateCircleView_imgWidth, 148.0f);

        scale=typedArray.getFloat(R.styleable.RotateCircleView_scale,1.0f);

        typedArray.recycle();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default)
                .showImageForEmptyUri(R.drawable.ic_default)
                .showImageOnFail(R.drawable.ic_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("tag", "onMeasure");
        float width=getMeasuredWidth();
        pointX = getMeasuredWidth() / 2;
        pointY = getMeasuredHeight() / 2;
        scale= width/deviceWidth;
        radius = Math.min(pointX, pointY) * 2 / 3;
        flag = true;
        if (selectItems.size() > 0) {
            if (isOnMeasure) {
                return;
            }
            isOnMeasure = true;
            setUpItems();
        }
    }


    public void setScale(float scale){
        this.scale=scale;
    }

    public void setSelectItems(Context context,ArrayList<SelectItem> items) {

        if(!ImageLoader.getInstance().isInited()){
            AppUtil.initImgLoader(context);
        }

        this.selectItems = items;
        if (!flag) {
            return;
        }
        setUpItems();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH) {
                invalidate();
                isFinish = true;
                anim = new BarAnimation();
                anim.setDuration(INTERVAL);
                anim.setInterpolator(new LinearInterpolator());

                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        animInStart = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                RotateCircleView.this.startAnimation(anim);
            }
        }
    };

    /**
     * 初始化每个点
     */
    @SuppressLint("NewApi")
    private void setUpItems() {
        new Runnable() {
            @Override
            public void run() {
                itemCount = selectItems.size();
                if (itemCount <= 0) {
                    return;
                }
                int angle = 0;
                degreeDelta = 360 / itemCount;
                for (int index = 0; index < itemCount; index++) {
                    Item item = new Item();
                    item.angle = angle;
                    item.imgPath = selectItems.get(index).getQue_img();
                    angle += degreeDelta;
                    item.id = selectItems.get(index).getId();
                    item.ageId = selectItems.get(index).getAge_id();
                    item.type = selectItems.get(index).getType_name();
                    item.x = pointX + (float) (radius * Math.cos(item.angle * Math.PI / 180));
                    item.y = pointY + (float) (radius * Math.sin(item.angle * Math.PI / 180));
                    item.bitmap = ImageLoader.getInstance().loadImageSync(selectItems.get(index).getQue_img(), options);
                    if (item.bitmap == null) {
                        item.bitmap = resizeImage(BitmapFactory.decodeResource(getResources(), defaultImg));
                    } else {
                        imgWidth = item.bitmap.getWidth();
                        item.bitmap=resizeImage(item.bitmap);
                    }
                    items.add(item);
                }
                handler.sendEmptyMessage(REFRESH);
            }
        }.run();
    }

    /**Bitmap缩小的方法*/
    private  Bitmap resizeImage(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float scaleWidth = ( imgWidth) / bitmap.getWidth();
        matrix.postScale(scale*scaleWidth,scale*scaleWidth); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    @Override
    public void onDraw(Canvas canvas) {
        itemCount = items.size();
        if (itemCount < 1) {
            return;
        }
        for (int index = 0; index < itemCount; index++) {
            drawInCenter(canvas, items.get(index).bitmap, items.get(index).x, items.get(index).y);
        }
    }

    void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top) {
        if (bitmap == null) {
            return;
        }
        canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2, top - bitmap.getHeight() / 2, null);
    }

    class Item {
        //图片
        Bitmap bitmap;
        //角度
        float angle;
        //x坐标
        float x;
        //y坐标
        float y;
        String id;
        String type;
        String ageId;
        String imgPath;
    }

    public void destoryView() {
        if (items == null || items.size() == 0) {
            return;
        }
        for (int index = 0; index < items.size(); index++) {
            if (!items.get(index).bitmap.isRecycled()) {
                items.get(index).bitmap.recycle();
            }
        }
    }

}
