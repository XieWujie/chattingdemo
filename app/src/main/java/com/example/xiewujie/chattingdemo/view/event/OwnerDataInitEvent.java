package com.example.xiewujie.chattingdemo.view.event;

import com.example.xiewujie.chattingdemo.model.user.Owner;

public class OwnerDataInitEvent {
    private Owner owner;

    public OwnerDataInitEvent(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
