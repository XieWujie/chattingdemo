package com.example.xiewujie.chattingdemo.model.user;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


public class ChatUser extends DataSupport implements Serializable{
  private String userId;
  private String avatarUrl;
  private String name;
  private String RemarkName;
  private String location;
  private String backgroudUrl;
  private String gender;
  private int age;

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getBackgroudUrl() {
    return backgroudUrl;
  }

  public void setBackgroudUrl(String backgroudUrl) {
    this.backgroudUrl = backgroudUrl;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public ChatUser() {
    }

    public ChatUser(String userId, String avatarUrl, String name, String remarkName) {
    this.userId = userId;
    this.avatarUrl = avatarUrl;
    this.name = name;
    RemarkName = remarkName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRemarkName() {
    return RemarkName;
  }

  public void setRemarkName(String remarkName) {
    RemarkName = remarkName;
  }
}
