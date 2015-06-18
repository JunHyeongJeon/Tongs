package com.tongs.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongs.user.tool.ImageDownloaderTask;
import com.tongs.user.item.StoreItem;
import com.tongs.user.activity.R;

import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 5. 10..
 */
public class StoreAdapter extends BaseAdapter   {

    private ArrayList<StoreItem> listData;
    private LayoutInflater layoutInflater;

    // 생성자
    public StoreAdapter(Context context, ArrayList<StoreItem> _listData) {
        listData = _listData;
        layoutInflater = LayoutInflater.from(context);
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return listData.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StoreHolder holder;
        if( convertView == null )   {
            convertView = layoutInflater.inflate(R.layout.item_store, null);
            holder = new StoreHolder();
            holder.nameView = (TextView)convertView.findViewById(R.id.id_storeName);
            holder.locationView = (TextView)convertView.findViewById(R.id.id_storeLocation);
            holder.descriptionView = (TextView)convertView.findViewById(R.id.id_storeDescription);
            holder.numberView = (TextView)convertView.findViewById(R.id.id_storeNumber);
            holder.imageView = (ImageView)convertView.findViewById(R.id.id_storeImageView);
            convertView.setTag(holder);
        }
        else    {
            holder = (StoreHolder)convertView.getTag();
        }

        StoreItem newItem = listData.get(position);
        holder.nameView.setText(newItem.getName());
        holder.locationView.setText(newItem.getLocation());
        holder.descriptionView.setText(newItem.getDescription());
        holder.numberView.setText(newItem.getWaitingNum());

        if( holder.imageView != null )  {
            new ImageDownloaderTask(holder.imageView).execute(newItem.getUrl());
        }

        return convertView;
    }

    private class StoreHolder {
        TextView nameView;
        TextView locationView;
        TextView descriptionView;
        TextView numberView;

        ImageView imageView;
    }
}
