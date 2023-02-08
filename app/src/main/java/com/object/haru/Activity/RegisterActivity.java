package com.object.haru.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.object.haru.Activity.MainActivity;
import com.object.haru.R;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    Button category_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //툴바 생성
        Toolbar toolbar = findViewById(R.id.Register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//튀로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle("구인 등록");

        category_btn = findViewById(R.id.register_sp_category);
        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_Category();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
            return super.onOptionsItemSelected(item);
        }

        public void showDialog_Category(){
            String[] kind = getResources().getStringArray(R.array.kind);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("분야를 선택해주세요");

            //다이얼로그에 리스트 담기
            builder.setItems(kind, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (kind[i].equals("식당")){
                        String[] kind1 = getResources().getStringArray(R.array.kind1);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
                        builder1.setTitle("해당되는 일을 선택해주세요");
                        builder1.setItems(kind1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                category_btn.setText("식당/"+kind1[i]);
                            }
                        });
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.show();
                    }else if (kind[i].equals("카페")){
                        String[] kind2 = getResources().getStringArray(R.array.kind2);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(RegisterActivity.this);
                        builder2.setTitle("해당되는 일을 선택해주세요");
                        builder2.setItems(kind2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                category_btn.setText("카페/"+kind2[i]);
                            }
                        });
                        AlertDialog alertDialog2 = builder2.create();
                        alertDialog2.show();
                    }else if (kind[i].equals("편의점")){
                        String[] kind3 = getResources().getStringArray(R.array.kind3);
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(RegisterActivity.this);
                        builder3.setTitle("해당되는 일을 선택해주세요");
                        builder3.setItems(kind3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                category_btn.setText("편의점/"+kind3[i]);
                            }
                        });
                        AlertDialog alertDialog3 = builder3.create();
                        alertDialog3.show();
                    }else if (kind[i].equals("기타")){
                        final EditText et = new EditText(RegisterActivity.this);
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(RegisterActivity.this);
                        builder4.setTitle("해당되는 일을 선택해주세요").setMessage("일을 적어주세요");
                        builder4.setView(et);
                        builder4.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String value = et.getText().toString();
                                category_btn.setText("기타/"+value);
                            }
                        });

                        AlertDialog alertDialog4 = builder4.create();
                        alertDialog4.show();
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
}