package com.object.haru.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.DTO.TestDTO;
import com.object.haru.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> {

    ArrayList<String> list;
    private Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemDeleted(String searchWord);
    }


    public SearchAdapter(ArrayList<String> list,OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.CustomViewHolder holder, int position) {
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView cancel_btn;

        public CustomViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.text_title);
            cancel_btn = itemView.findViewById(R.id.cancel_image_view);

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // SharedPreferences 액세스
                    String searchWord = tv.getText().toString();
//                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    // 리스트 불러오기
//                    Set<String> searchWordsSet = sharedPreferences.getStringSet("searchWords", new HashSet<>());
//                    ArrayList<String> searchWords = new ArrayList<>(searchWordsSet);
//
//                    // 값 삭제
//                    searchWords.remove(searchWord);
//
//                    // 수정된 값 다시 저장
//                    editor.putStringSet("searchWords", new HashSet<>(searchWords));
//                    editor.apply();

                    if (listener != null) {
                        listener.onItemDeleted(searchWord);
                    }

                }
            });
        }
    }
}
