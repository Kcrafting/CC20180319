package com.example.kcraf.scancoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kcraf on 2018/01/31.
 */

public class layout_setting_app extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState) {
        Log.d("主活动:", "活动编号 初始化 new Fragment()");
        View view = inflater.inflate(R.layout.layout_setting_app, container, false);
        MainActivity activity = (MainActivity) getActivity();//获取到主活动的实例
        Button save = activity.findViewById(R.id.app_bar_save);//通过活动的实例获取到界面元素
        save.setVisibility(View.VISIBLE);//然后设置界面元素属性
        return view;
    }
}
