package com.example.kcraf.scancoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by kcraf on 2018/01/30.
 */

public class setting_layout_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState) {
        Log.d("主活动:", "活动编号 初始化 setting_layout_fragment new Fragment()");
        View view = inflater.inflate(R.layout.setting_layout_fragment, container, false);
        //TODO
        //创建碎片的视图
        MainActivity activity = (MainActivity) getActivity();//获取到主活动的实例
        Button save = activity.findViewById(R.id.app_bar_save);//通过活动的实例获取到界面元素
        save.setVisibility(View.VISIBLE);//然后设置界面元素属性
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });
        //Over
        return view;
    }
}
