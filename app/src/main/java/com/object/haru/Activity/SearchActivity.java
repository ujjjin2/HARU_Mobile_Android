package com.object.haru.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private EditText editText_search;
    private ImageView back_btn;
    ArrayList<String> list;

    private Long kakaoId;
    private String token;

    private static final int MAX_SEARCH_WORDS = 10; // 최대 저장 개수

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        kakaoId = intent.getLongExtra("kakaoId", 0);
        token = intent.getStringExtra("token");

        setContentView(R.layout.activity_search);
        editText_search = findViewById(R.id.search_edittext);
        back_btn = findViewById(R.id.back_btn);

        list = loadSearchWords(); // 저장된 SearchWord를 불러와서 list에 담음

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
                        intent.putExtra("kakaoId", kakaoId);
                        intent.putExtra("token", token);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        hideKeyboard();

                        // searchWord를 SharedPreferences에 저장
                        saveSearchWord(editText_search.getText().toString());

                        startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // 액티비티가 다시 활성화될 때 list를 업데이트
        list = loadSearchWords();
        SearchAdapter searchAdapter = new SearchAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setAdapter(searchAdapter);
    }

    // 키보드 숨기는 코드
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(back_btn.getWindowToken(), 0);
    }

    // searchWord를 SharedPreferences에 저장하는 메서드
    private void saveSearchWord(String searchWord) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> searchWordsSet = sharedPreferences.getStringSet("searchWords", new HashSet<>());
        ArrayList<String> searchWords = new ArrayList<>(searchWordsSet);

        searchWords.add(searchWord);

        if (searchWords.size() > MAX_SEARCH_WORDS) {
            searchWords.remove(0);
        }

        editor.putStringSet("searchWords", new HashSet<>(searchWords));
        editor.apply();
    }

    // 저장된 SearchWord를 불러와서 ArrayList로 반환하는 메서드
    private ArrayList<String> loadSearchWords() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Set<String> searchWordsSet = sharedPreferences.getStringSet("searchWords", new HashSet<>());
        return new ArrayList<>(searchWordsSet);
    }
}
