package com.example.kcraf.scancoder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * splash2Activity的出现时用于测试
 * 每次在启动MainActivity'的时候都会出现一次异常终止错误，但后续活动正常启动
 * 经过测试是由于主题的设置错误，改正后废弃此活动，但没有删除
 * 便于以后查询错误!
 */
public class splash2Activity extends ZJFActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash2);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(splash2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
