package com.object.haru.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;
import com.object.haru.UpdateDongListener;
import com.object.haru.UpdateListener;

import java.util.ArrayList;

public class DoAdapter extends RecyclerView.Adapter<DoAdapter.CustomViewHolder>{

    ArrayList<String> list;
    ArrayList<String> siArrayList = new ArrayList<>(); // 하위 리사이클러뷰에 넘겨줄 어레이리스트
    ArrayList<String> dongArrayList = new ArrayList<>();

    UpdateListener listener; // 하위 리사이클러뷰 업데이트를 위한 리스너

    UpdateDongListener listener2;

    private int selectedItemPosition = 0;

    public DoAdapter(ArrayList<String> list, UpdateListener listener) {
        this.list = list;
        this.listener = listener;
        this.listener2 = listener2;
    }

    @NonNull
    @Override
    public DoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.do_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoAdapter.CustomViewHolder holder, final int position) {

        final int item = position;

        holder.textView.setText(list.get(position));

        if (position == selectedItemPosition) {
            holder.layout.setBackgroundColor(Color.WHITE);
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        if (selectedItemPosition == 0) {
            callItems(0);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition = item;
                notifyDataSetChanged();
                callItems(item);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout layout;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_location);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public void callItems(int position) {
        if (siArrayList.size() > 0) {
            siArrayList.clear();
        }

        if (position == 0) { // 서울
            siArrayList.add("전체");
            siArrayList.add("강남구");
            siArrayList.add("강동구");
            siArrayList.add("강북구");
            siArrayList.add("강서구");
            siArrayList.add("관악구");
            siArrayList.add("광진구");
            siArrayList.add("구로구");
            siArrayList.add("금천구");
            siArrayList.add("노원구");
            siArrayList.add("도봉구");
            siArrayList.add("동대문구");
            siArrayList.add("동작구");
            siArrayList.add("마포구");
            siArrayList.add("서대문군");
            siArrayList.add("서초구");
            siArrayList.add("성동구");
            siArrayList.add("성북구");
            siArrayList.add("송파구");
            siArrayList.add("양천구");
            siArrayList.add("영등포구");
            siArrayList.add("용산구");
            siArrayList.add("은평구");
            siArrayList.add("종로구");
            siArrayList.add("중구");
            siArrayList.add("중랑구");
            dongArrayList.add("");

        } else if (position == 1) {
            siArrayList.add("전체");
            siArrayList.add("가평군");
            siArrayList.add("고양시 덕양구");
            siArrayList.add("고양시 일산동구");
            siArrayList.add("고양시 일산서구");
            siArrayList.add("과천시");
            siArrayList.add("광명시");
            siArrayList.add("광주시");
            siArrayList.add("구리시");
            siArrayList.add("군포시");
            siArrayList.add("김포시");
            siArrayList.add("남양주시");
            siArrayList.add("동두천시");
            siArrayList.add("부천시");
            siArrayList.add("성남시 분당구");
            siArrayList.add("성남시 수정구");
            siArrayList.add("성남시 중원구");
            siArrayList.add("수원시 권선구");
            siArrayList.add("수원시 영통구");
            siArrayList.add("수원시 장안구");
            siArrayList.add("수원시 팔달구");
            siArrayList.add("시흥시");
            siArrayList.add("안산시 단원구");
            siArrayList.add("안산시 상록구");
            siArrayList.add("안성시");
            siArrayList.add("안양시 동안구");
            siArrayList.add("안양시 만안구");
            siArrayList.add("양주시");
            siArrayList.add("양평군");
            siArrayList.add("여주시");
            siArrayList.add("연천군");
            siArrayList.add("오산시");
            siArrayList.add("용인시 기흥구");
            siArrayList.add("용인시 수지구");
            siArrayList.add("용인시 처인구");
            siArrayList.add("의왕시");
            siArrayList.add("의정부시");
            siArrayList.add("이천시");
            siArrayList.add("파주시");
            siArrayList.add("평택시");
            siArrayList.add("포천시");
            siArrayList.add("하남시");
            siArrayList.add("화성시");
            dongArrayList.add("");
        }

        if (listener != null) {
            listener.onUpdate(siArrayList);
        }
    }
}
