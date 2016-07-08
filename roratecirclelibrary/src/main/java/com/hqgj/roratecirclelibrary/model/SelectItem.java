package com.hqgj.roratecirclelibrary.model;

/**
 * Created by mojo on 2016/5/9.
 */
public class SelectItem {

    /**
     * id : 1
     * type_name : 体能测试
     * age_id : 1
     * que_img : http://7xrpiy.com1.z0.glb.clouddn.com/test_5.png
     */

    private String id;
    private String type_name;
    private String age_id;
    private String que_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getAge_id() {
        return age_id;
    }

    public void setAge_id(String age_id) {
        this.age_id = age_id;
    }

    public String getQue_img() {
        return que_img;
    }

    public void setQue_img(String que_img) {
        this.que_img = que_img;
    }
}
