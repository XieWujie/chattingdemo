package com.example.xiewujie.chattingdemo.model.convert;

import com.avos.avoscloud.AVFile;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Contact extends DataSupport implements Serializable{


    private int age;
    private String sex;
    private AVFile backgroundImage;
    private String name;
    private String id;
    private String email;
    private AVFile header_image;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public AVFile getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(AVFile backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public AVFile getHeader_image() {
        return header_image;
    }

    public void setHeader_image(AVFile header_image) {
        this.header_image = header_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
