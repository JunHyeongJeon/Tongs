package com.example.jaecheol.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaecheol.tongs.R;

import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 5. 15..
 */
public class CouponAdapter extends BaseAdapter  {
    // 문자열을 보관 할 ArrayList
    private ArrayList<String> couponList[] = new ArrayList[5];

    // 생성자
    public CouponAdapter() {

        for(int i=0; i<5; i++) {
            couponList[i] = new ArrayList<String>();
        }
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return couponList[0].size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return couponList[0].get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView title = null;
        TextView content = null;
        TextView time = null;

        StoreHolder holder = null;


        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_coupon, parent, false);

            title = (TextView)convertView.findViewById(R.id.id_couponTitle);
            content = (TextView)convertView.findViewById(R.id.id_couponContent);
            time = (TextView)convertView.findViewById(R.id.id_couponTime);

            // 홀더 생성 및 Tag로 등록
            holder = new StoreHolder();
            holder.m_title = title;
            holder.m_content = content;
            holder.m_time = time;
            convertView.setTag(holder);
        }
        else {
            holder = (StoreHolder)convertView.getTag();
            title = holder.m_title;
            content = holder.m_content;
            time = holder.m_time;
        }

        // Text 등록
        title.setText(couponList[2].get(position));
        content.setText(couponList[3].get(position));
        time.setText(couponList[4].get(position));

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : "+couponList[0].get(pos), Toast.LENGTH_SHORT).show();


                String couponId = couponList[0].get(pos);
                Toast.makeText(context, "Coupon Id : " + couponId + "Click", Toast.LENGTH_SHORT);
//                Intent intent = new Intent(context, CouponDetailActivity.class);
//                intent.putExtra("coupon_id", couponId);
//                context.startActivity(intent);
            }
        });

        // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+couponList[0].get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String id, String store,
                    String title, String content,
                    String time) {
        couponList[0].add(id);
        couponList[1].add(store);
        couponList[2].add(title);
        couponList[3].add(content);
        couponList[4].add(time);
        Log.d("HELLO", "ADD");
    }

    // 외부에서 아이템 삭제 요청 시 사용
    private void remove(int _position) {
        couponList[0].remove(_position);
        couponList[1].remove(_position);
        couponList[2].remove(_position);
        couponList[3].remove(_position);
        couponList[4].remove(_position);
    }

    public void removeList()    {
        int len = couponList[0].size();

        for(int i=0; i<len; i++)    {
            remove(i);
        }
    }


    private class StoreHolder {
        TextView m_title;
        TextView m_content;
        TextView m_time;
    }
}
