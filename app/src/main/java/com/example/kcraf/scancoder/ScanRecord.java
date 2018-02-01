package com.example.kcraf.scancoder;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by kcraf on 2018/01/29.
 */

public class ScanRecord extends RecyclerView.Adapter<ScanRecord.ViewHolder> {
    private List<Record> mRrcordList;//建立一个保存记录的列表

    public static class ViewHolder extends RecyclerView.ViewHolder {//内部的视图类
        public ImageView RecordImage;
        public TextView RecordCode;
        public TextView RecordTime;
        public TextView RecordUserName;


        public ViewHolder(View view) {//构造函数
            super(view);
            Log.d("主活动:", "ScanRecord -> ViewHolder 构造");
            RecordImage = (ImageView) view.findViewById(R.id.r_image);//获取带视图上的各个实例
            RecordCode = (TextView) view.findViewById(R.id.r_code);
            RecordTime = (TextView) view.findViewById(R.id.r_time);
            RecordUserName = (TextView) view.findViewById(R.id.r_username);
        }
    }

    public ScanRecord(List<Record> RecordList) {//使用一个List来进行构造的构造函数
        Log.d("主活动:", "ScanRecord 复制构造");
        mRrcordList = RecordList;//初始化类内的列表
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("主活动:", "ScanRecord onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_layout, parent, false);//创建re的视图，
        ViewHolder holder = new ViewHolder(view);//创建新的行视图并返回
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("主活动:", "ScanRecord onBindViewHolder()");
        Record record = mRrcordList.get(position);//在列表中获取单条记录，并初始化视图
        //holder.RecordImage.setImageResource(record.getImage());
        holder.RecordCode.setText(record.getCode());
        holder.RecordTime.setText(record.getTime());
        holder.RecordUserName.setText(record.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "被点击的元素索引" + position, Toast.LENGTH_SHORT).show();
                mRrcordList.get(position).code = "被点的改变了！";
                notifyItemChanged(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mRrcordList.add(position, new Record("我是新来的！", "请多多指教！", "", 0));
                notifyItemInserted(position);
                notifyItemChanged(position);
                Toast.makeText(holder.itemView.getContext(), "Position:" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("主活动:", "ScanRecord getItemCount()" + mRrcordList.size());
        return mRrcordList.size();
    }
}
