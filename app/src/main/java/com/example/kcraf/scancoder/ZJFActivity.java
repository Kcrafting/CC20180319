package com.example.kcraf.scancoder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by kcraf on 2018/01/25.
 * 用于修改所有活动的派生类，方便添加通用方法
 * 以下重写了 onCreate 和 onDestroy
 * 增加了ActivityCollector的控制
 * 当新建时添加到ActivityCollector的活动列表
 * 当销毁时在列表中删除
 */

public class ZJFActivity extends AppCompatActivity {//派生一个属于自己的Activity

    @Override
    protected void onCreate(Bundle saveInstanceState) {//重写了创建方法，创建后加入ActivityCollector的活动列表
        super.onCreate(saveInstanceState);
        Log.d("父类ZJFActivty :", getClass().getSimpleName());//输出一个当前正在活动的Activity名称
        ActivityCollector.addActivity(this);//活动创建时，将其加入到ActivityCollector的列表中
    }

    @Override
    protected void onDestroy() {//重写方法
        super.onDestroy();
        ActivityCollector.removeActivity(this);//当活动被销毁时，将活动从ActivityCollector的列表中移除
    }
}
