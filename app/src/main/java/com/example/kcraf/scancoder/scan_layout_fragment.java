package com.example.kcraf.scancoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kcraf on 2018/01/28.
 * 完全自定义的碎片，相应 “扫描” 功能
 */

public class scan_layout_fragment extends Fragment {
    private List<Record> recordlist = new ArrayList<>();//记录测试
    static int Count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState) {
        Log.d("主活动:", "活动编号 初始化 new Fragment()");
        View view = inflater.inflate(R.layout.scan_layout_fragment, container, false);

        //创建碎片的视图
        TextView Title = (TextView) view.findViewById(R.id.contentTitle);
        //获取到TextView实例
        /**
         Button save=(Button)view.findViewById(R.id.app_bar_save);//获取到界面上的实例
         save.setVisibility(View.INVISIBLE);//设置保存button不可见
         */
        //MainActivity.Button_Save.setVisibility(View.INVISIBLE);//失效


        MainActivity activity = (MainActivity) getActivity();//获取到主活动的实例
        Button save = activity.findViewById(R.id.app_bar_save);//通过活动的实例获取到界面元素
        save.setVisibility(View.INVISIBLE);//然后设置界面元素属性

        Title.setText("以下为扫码内容");
        Log.d("主活动:", "RecyclerView 开始创建  ");
        Record rrecord = new Record("Test2", "Test", "Test", 0);
        recordlist.add(rrecord);
        recordlist.add(new Record("Test3", "Test", "Test", 1));
        recordlist.add(new Record("Test4", "Test", "Test", 2));
        recordlist.add(new Record("Test5", "Test", "Test", 3));

        Log.d("主活动:", "活动编号 add->" + recordlist.size());
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);//首先获取到活动的实例
        Log.d("主活动:", "获取RecyclerView->" + recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(null);//新建一个界面管理器
        Log.d("主活动:", "LinearLayoutManager->" + layoutManager);
        recyclerView.setLayoutManager(layoutManager);//将界面管理器设置为当前应用的主管理器
        final ScanRecord scanRecord = new ScanRecord(recordlist);
        //建立新的RecyclerView的Adaptor，使用派生于建立新的RecyclerView的Adaptor我们自己的Adaptor
        recyclerView.setAdapter(scanRecord);//将Adaptor设置到recyclerView的实例中进行显示
        //recyclerView.notify(new Record("Test","Test","Test",1));

        TextView textView = (TextView) view.findViewById(R.id.contentTitle);//获取到界面上的TextView实例
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击TextView事件
                recordlist.add(2, new Record("Test", "Test", "Test", Count++));
                //给我们维护显示列表的容器添加一个条目
                //scanRecord.notifyItemRemoved(0);
                scanRecord.notifyItemInserted(0);//将条目插入到适配器的是定位置
                recyclerView.scrollToPosition(0);//显示控件的最顶端
                //scanRecord.notifyItemRangeChanged(0, recordlist.size());
                //recyclerView
            }
        });


        return view;
    }
}
