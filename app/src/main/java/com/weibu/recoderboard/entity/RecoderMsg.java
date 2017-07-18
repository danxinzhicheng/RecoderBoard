package com.weibu.recoderboard.entity;

/**
 * Created by user on 2017/7/18.
 */

public class RecoderMsg {

    private String Msg_durion;
    public RecoderMsg(String Msg_durion) {
        this.Msg_durion = Msg_durion;
    }

    public String getMsg_durion() {
        return Msg_durion;
    }

    public void setMsg_durion(String msg_durion) {
        Msg_durion = msg_durion;
    }

    @Override
    public String toString() {
        return "RecoderMsg{" +
                "Msg_durion='" + Msg_durion + '\'' +
                '}';
    }
}
