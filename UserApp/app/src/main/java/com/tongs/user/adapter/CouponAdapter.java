package com.tongs.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongs.user.asynctask.ImageDownloaderTask;
import com.tongs.user.item.CouponItem;
import com.tongs.user.activity.R;

import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 5. 15..
 */
public class CouponAdapter extends BaseAdapter  {

    private ArrayList<CouponItem> listData;
    private LayoutInflater layoutInflater;

    // 생성자
    public CouponAdapter(Context context, ArrayList<CouponItem> _listData) {
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

        CouponHolder holder;
        if( convertView == null )   {
            convertView = layoutInflater.inflate(R.layout.item_coupon, null);
            holder = new CouponHolder();
            holder.titleView = (TextView)convertView.findViewById(R.id.id_couponTitle);
            holder.locationView = (TextView)convertView.findViewById(R.id.id_couponLocation);
            holder.descriptionView = (TextView)convertView.findViewById(R.id.id_couponDescription);
            holder.imageView = (ImageView)convertView.findViewById(R.id.id_couponImage);
            convertView.setTag(holder);
        }
        else    {
            holder = (CouponHolder)convertView.getTag();
        }

        CouponItem newItem = listData.get(position);
        holder.titleView.setText(newItem.getTitle());
        holder.locationView.setText(newItem.getLocation());
        holder.descriptionView.setText(newItem.getDescription());
//        holder.timeView.setText(newItem.getTime());

        if( holder.imageView != null )  {
            new ImageDownloaderTask(holder.imageView).execute(newItem.getUrl());
        }

        return convertView;
    }

    private class CouponHolder {

        TextView titleView;
        TextView locationView;
        TextView descriptionView;
        TextView timeView;

        ImageView imageView;
    }
}
