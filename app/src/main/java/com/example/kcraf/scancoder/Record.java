package com.example.kcraf.scancoder;

import android.util.Log;

/**
 * Created by kcraf on 2018/01/29.
 */

public class Record {
    private String username;
    public String code;
    private String time;
    private int image;

    public Record(String un, String cd, String tm, int mg) {
        Log.d("主活动:", "Record 构造 |" + un + " | " + cd + " | " + tm + " | " + mg);
        this.username = un;
        this.code = cd;
        this.time = tm;
        this.image = mg;
    }

    public int getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return code;
    }
}
