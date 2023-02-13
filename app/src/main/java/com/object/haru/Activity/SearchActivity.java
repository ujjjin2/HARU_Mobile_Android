package com.object.haru.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Adapter.SearchAdapter;
import com.object.haru.DTO.TestDTO;
import com.object.haru.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText editText_search;
    private ImageView back_btn;
    ArrayList<String> list;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText_search = findViewById(R.id.search_edittext);
        back_btn = findViewById(R.id.back_btn);

        list = new ArrayList<>();
        list.add("롯데리아");
        list.add("인천");
        list.add("서빙알바");
        list.add("단기알바");
        list.add("인하공전");
        list.add("올리브영");
        list.add("음식점");

        SearchAdapter searchAdapter = new SearchAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.search_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(searchAdapter);

        // 자동으로 키보드 올리는 코드

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // 자동으로 editText에 포커스 주는 코드
        editText_search.requestFocus();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                SearchActivity.super.onBackPressed();
            }
        });

        editText_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("searchWord", editText_search.getText().toString());
//                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    hideKeyboard();
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    // 키보드 숨기는 코드
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(back_btn.getWindowToken(), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        editText_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
