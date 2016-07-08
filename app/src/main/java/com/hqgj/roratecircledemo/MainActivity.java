package com.hqgj.roratecircledemo;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hqgj.roratecirclelibrary.model.SelectItem;
import com.hqgj.roratecirclelibrary.views.RotateCircleView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RotateCircleView rotateCircleView;
    private ArrayList<SelectItem> selectItems=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        rotateCircleView= (RotateCircleView) findViewById(R.id.rotateCircleView);

        SelectItem selectItem=new SelectItem();
        selectItem.setQue_img("http://7xrpiy.com1.z0.glb.clouddn.com/test_5.png");
        selectItem.setAge_id("1");
        selectItem.setId("1");
        selectItem.setType_name("ha");
        selectItems.add(selectItem);
        SelectItem selectItem2=new SelectItem();
        selectItem2.setQue_img("");
        selectItem2.setAge_id("1");
        selectItem2.setId("1");
        selectItem2.setType_name("ha");
        selectItems.add(selectItem2);
        SelectItem selectItem3=new SelectItem();
        selectItem3.setQue_img("http://7xrpiy.com1.z0.glb.clouddn.com/test_3.png");
        selectItem3.setAge_id("1");
        selectItem3.setId("1");
        selectItem3.setType_name("ha");
        selectItems.add(selectItem3);
        SelectItem selectItem4=new SelectItem();
        selectItem4.setQue_img("");
        selectItem4.setAge_id("1");
        selectItem4.setId("1");
        selectItem4.setType_name("ha");
        selectItems.add(selectItem4);
        SelectItem selectItem1=new SelectItem();
        selectItem1.setQue_img("http://7xrpiy.com1.z0.glb.clouddn.com/test_1.png");
        selectItem1.setAge_id("1");
        selectItem1.setId("1");
        selectItem1.setType_name("ha");
        selectItems.add(selectItem1);
        SelectItem selectItem6=new SelectItem();
        selectItem6.setQue_img("");
        selectItem6.setAge_id("1");
        selectItem6.setId("1");
        selectItem6.setType_name("ha");
        selectItems.add(selectItem6);



        new Runnable(){
            @Override
            public void run() {
                rotateCircleView.setSelectItems(MainActivity.this,selectItems);
            }
        }.run();

        rotateCircleView.setOnMyClickListener(new RotateCircleView.MyClickListener() {
            @Override
            public void onClick(String typeId, String typeName, String age) {
                Toast.makeText(MainActivity.this, "" + typeId + "-" + typeName + "-" + age, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(rotateCircleView!=null){
            rotateCircleView.destoryView();
        }
    }
}
