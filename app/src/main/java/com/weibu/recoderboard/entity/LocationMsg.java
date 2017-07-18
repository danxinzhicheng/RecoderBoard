package com.weibu.recoderboard.entity;

/**
 * Created by user on 2017/7/17.
 */

public class LocationMsg {
    private String msg_location;
    private float msg_speed;

    public String getMsg_location() {
        return msg_location;
    }

    public float getMsg_speed() {
        return msg_speed;
    }

    public void setMsg_location(String msg_location) {
        this.msg_location = msg_location;
    }

    public void setMsg_speed(float msg_speed) {
        this.msg_speed = msg_speed;
    }

    @Override
    public String toString() {
        return "LocationMsg{" +
                "msg_location='" + msg_location + '\'' +
                ", msg_speed='" + msg_speed + '\'' +
                '}';
    }

}
