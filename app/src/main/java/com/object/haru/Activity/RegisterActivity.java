package com.object.haru.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.object.haru.R;

public class RegisterActivity extends AppCompatActivity {

    Button category_btn,register_sp_time1,register_sp_time2,Register_btn_age,register_btn_pay,Register_btn_career;
    Dialog dialogtime1,dialogtime2,dialogAddr;
    EditText year,month,day,addr,register_pt_age,register_pt_career,register_pt_pay;
    TextView dialogtime_title;
    Spinner hour,min;
    String strhour,strmin;
    private Handler handler;


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


        dialogtime1 = new Dialog(RegisterActivity.this);
        dialogtime1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogtime1.setContentView(R.layout.dialogtime);
        WindowManager.LayoutParams params1 = dialogtime1.getWindow().getAttributes();
        params1.width = WindowManager.LayoutParams.MATCH_PARENT;
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogtime1.getWindow().setAttributes(params1);


        register_sp_time1 = findViewById(R.id.register_sp_time1);
        register_sp_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogtime1.show();
                year = dialogtime1.findViewById(R.id.dialogtime_year);
                month = dialogtime1.findViewById(R.id.dialogtime_month);
                day = dialogtime1.findViewById(R.id.dialogtime_day);
                hour = dialogtime1.findViewById(R.id.dialogtime_hour);

                min = dialogtime1.findViewById(R.id.dialogtime_minute);

                ArrayAdapter hourAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.time_hour, android.R.layout.select_dialog_item);

                ArrayAdapter minAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.time_minute, android.R.layout.select_dialog_item);

                minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                min.setAdapter(minAdapter);

                hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hour.setAdapter(hourAdapter);

                Button btnok = dialogtime1.findViewById(R.id.button2);
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String stryear = year.getText().toString();
                        String strmonth = month.getText().toString();
                        String strday = day.getText().toString();
                        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                strhour = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                strmin = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        register_sp_time1.setText(stryear+"-"+strmonth+"-"+strday+"/"+strhour+":"+strmin);
                        dialogtime1.dismiss();
                    }
                });

            }
        });

        dialogtime2 = new Dialog(RegisterActivity.this);
        dialogtime2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogtime2.setContentView(R.layout.dialogtime);
        WindowManager.LayoutParams params = dialogtime2.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogtime2.getWindow().setAttributes(params);


        register_sp_time2 = findViewById(R.id.register_sp_time2);
        register_sp_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogtime2.show();
                dialogtime_title = dialogtime2.findViewById(R.id.dialogtime_title);
                dialogtime_title.setText("종료 날짜와 시간을 적어주세요");
                year = dialogtime2.findViewById(R.id.dialogtime_year);
                month = dialogtime2.findViewById(R.id.dialogtime_month);
                day = dialogtime2.findViewById(R.id.dialogtime_day);
                hour = dialogtime2.findViewById(R.id.dialogtime_hour);
                min = dialogtime2.findViewById(R.id.dialogtime_minute);



                Button btnok = dialogtime2.findViewById(R.id.button2);
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String stryear = year.getText().toString();
                        String strmonth = month.getText().toString();
                        String strday = day.getText().toString();
                        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (!hour.getItemAtPosition(i).equals("0시")) {
//                                    spinnerMonth.setEnabled(true);
                                    strhour = (String) hour.getSelectedItem();
                                } else {
//                                    spinnerMonth.setEnabled(false);
//                                    spinnerDay.setEnabled(false);
                                    strhour = "";
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                strmin = adapterView.getItemAtPosition(i).toString();
                                if (!min.getItemAtPosition(i).equals("0분")) {
//                                    spinnerMonth.setEnabled(true);
                                    strmin = (String)min.getSelectedItem();
                                } else {
//                                    spinnerMonth.setEnabled(false);
//                                    spinnerDay.setEnabled(false);
                                    strhour = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        register_sp_time2.setText(stryear+"-"+strmonth+"-"+strday+"/"+strhour+":"+strmin);
                        dialogtime2.dismiss();
                    }
                });

            }
        });

        dialogAddr = new Dialog(RegisterActivity.this);
        dialogAddr.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddr.setContentView(R.layout.activity_search_register);

        WindowManager.LayoutParams params2 = dialogAddr.getWindow().getAttributes();
        params2.width = WindowManager.LayoutParams.MATCH_PARENT;
        params2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogAddr.getWindow().setAttributes(params2);

        addr = findViewById(R.id.register_pt_storeinfo);
        addr.setFocusable(false);
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                dialogAddr.show();
                Intent intent = getIntent();
                getSearchResult.launch(intent);

            }
        });

        Register_btn_age = findViewById(R.id.Register_btn_age);
        Register_btn_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_age = findViewById(R.id.register_pt_age);
                register_pt_age.setText("연령무관");
            }
        });

        Register_btn_career = findViewById(R.id.Register_btn_career);
        Register_btn_career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_career = findViewById(R.id.register_pt_career);
                register_pt_career.setText("경력무관");
            }
        });

        register_btn_pay = findViewById(R.id.register_btn_pay);
        register_btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_pay = findViewById(R.id.register_pt_pay);
                register_pt_pay.setText("최저시급");
            }
        });


    }

//    private void init_webView() {
//        //webview 설정
//        webView = findViewById(R.id.webView);
//        //자바스크립트의 window.open허용
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        //자바스크립트 이벤트에 대응할 함수를 정의한 클래스를 붙여줌
//        webView.addJavascriptInterface(new AndroidBridge(),"roadSearch");
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl("http://10.0.0.2:8080/roadSearch.html");
//
//    }
//
//    private class AndroidBridge {
//        @JavascriptInterface
//        public void setAddress(final String arg1, final String arg2, final String arg3) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    addr.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
//
//                    // WebView를 초기화 하지않으면 재사용할 수 없음
//                    init_webView();
//                }
//            });
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
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

        private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //Search_register로 부터의 결과 값을 받아 이곳으로 전달
                    if (result.getResultCode() == RESULT_OK){
                        if (result.getData() != null){
                            String data = result.getData().getStringExtra("data");
                            addr.setText(data);
                        }
                    }
                }
        );
}