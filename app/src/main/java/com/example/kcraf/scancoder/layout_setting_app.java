package com.example.kcraf.scancoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kcraf on 2018/01/31.
 */

public class layout_setting_app extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState) {
        Log.d("主活动:", "活动编号 初始化 new Fragment()");
        View view = inflater.inflate(R.layout.layout_setting_app, container, false);
        return view;
    }
}
