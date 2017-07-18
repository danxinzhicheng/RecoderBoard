package com.weibu.recoderboard.entity;

/**
 * Created by blueberry on 2017/7/14.
 */

public class CarNumberMsg {
    private String car_no;

    public CarNumberMsg(String car_no){
        this.car_no = car_no;
    }
    public String getMessage() {
        return car_no;
    }

    public void setMessage(String car_no) {
        this.car_no = car_no;
    }
}

