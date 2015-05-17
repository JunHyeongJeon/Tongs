package com.example.jaecheol.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaecheol.tongs.R;
import com.example.jaecheol.tongs.StoreViewActivity;

import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 5. 10..
 */
public class StoreAdapter extends BaseAdapter   {
    // 문자열을 보관 할 ArrayList
    private ArrayList<String> storeList[] = new ArrayList[5];

    Context context;

    // 생성자
    public StoreAdapter(Context _context) {
        context = _context;

        for(int i=0; i<5; i++) {
            storeList[i] = new ArrayList<String>();
        }
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return storeList[0].size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return storeList[0].get(position);
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
        TextView location = null;
        TextView introduction = null;
        Button number = null;

        StoreHolder holder = null;


        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_store, parent, false);

            title = (TextView)convertView.findViewById(R.id.title);
            location = (TextView)convertView.findViewById(R.id.location);
            introduction = (TextView)convertView.findViewById(R.id.introduction);
            number = (Button)convertView.findViewById(R.id.number);

            // 홀더 생성 및 Tag로 등록
            holder = new StoreHolder();
            holder.m_title = title;
            holder.m_location = location;
            holder.m_description = introduction;
            holder.m_number = number;
            convertView.setTag(holder);
        }
        else {
            holder = (StoreHolder)convertView.getTag();
            title = holder.m_title;
            location = holder.m_location;
            introduction = holder.m_description;
            number = holder.m_number;
        }

        // Text 등록
        title.setText(storeList[1].get(position));
        location.setText(storeList[2].get(position));
        introduction.setText(storeList[3].get(position));
        number.setText(storeList[4].get(position));

        // 버튼 이벤트 등록
        number.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, storeList[0].get(pos), Toast.LENGTH_SHORT).show();
            }
        });

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : "+storeList[0].get(pos), Toast.LENGTH_SHORT).show();


                String sid = storeList[0].get(pos);
                Intent intent = new Intent(context, StoreViewActivity.class);
                intent.putExtra("sid", sid);
                context.startActivity(intent);
            }
        });

        // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+storeList[0].get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String id, String title,
                    String location, String description,
                    String waitingNum) {
        storeList[0].add(id);
        storeList[1].add(title);
        storeList[2].add(location);
        storeList[3].add(description);
        storeList[4].add(waitingNum);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    private void remove(int _position) {
        storeList[0].remove(_position);
        storeList[1].remove(_position);
        storeList[2].remove(_position);
        storeList[3].remove(_position);
        storeList[4].remove(_position);
    }

    public void removeList()    {
        for(int i=0; i<5; i++) {
            storeList[i] = new ArrayList<String>();
        }
    }


    private class StoreHolder {
        TextView m_title;
        TextView m_location;
        TextView m_description;
        Button m_number;
    }
}
