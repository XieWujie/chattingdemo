package com.example.xiewujie.chattingdemo.view.event;

import org.json.JSONObject;

public class UpdateFromJsonEvent {
    private JSONObject object;

    public UpdateFromJsonEvent(JSONObject object) {
        this.object = object;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
