package com.object.haru.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.object.haru.R;
import com.object.haru.Search_register;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivityTest extends AppCompatActivity {

    private CalendarView calendarView;

    private EditText registerTitle, registerCategory, registerWorkDate, registerMoney, registerAddr, registerDetailAddr;
    private String token, firstDate, lastDate, result, data, gender;
    private Long kakaoId;
    private Button submit;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Spinner spinnerStartTime, spinnerEndTime, spinnerStartAge, spinnerEndAge;
    private ImageView backButton;
    final private int minMoney = 9620;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_register);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);

        actionBackButton();
        actionSubmitButton();
        selectCategory();
        selectWorkDate();
        selectWorkTime();
        selectMoney();
        selectAddress();
        selectGender();
    }

    private void selectGender() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.male);
        radioButtonFemale = findViewById(R.id.female);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = "남성";
                        radioButtonMale.setTextColor(Color.parseColor("#ffffff"));
                        radioButtonFemale.setTextColor(Color.parseColor("#07BC7D"));
                        break;
                    case R.id.female:
                        gender = "여성";
                        radioButtonMale.setTextColor(Color.parseColor("#07BC7D"));
                        radioButtonFemale.setTextColor(Color.parseColor("#ffffff"));
                        break;
                }
            }
        });
    }

    private void selectAddress() {
        registerAddr = findViewById(R.id.address);
        registerAddr.setFocusable(false);
        registerAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search_register.class);
                overridePendingTransition(0, 0);
                getSearchResult.launch(intent);
            }
        });
    }

    private void selectMoney() {
        registerMoney = findViewById(R.id.money);
        result = "";

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    registerMoney.setText(result);
                    registerMoney.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        registerMoney.addTextChangedListener(watcher);
    }

    private void selectWorkTime() {
        spinnerStartTime = findViewById(R.id.start_spinner);
        spinnerEndTime = findViewById(R.id.end_spinner);

        ArrayAdapter startTimeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.startTime, android.R.layout.simple_spinner_item);
        ArrayAdapter endTimeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.endTime, android.R.layout.simple_spinner_item);

        startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(startTimeAdapter);
        endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndTime.setAdapter(endTimeAdapter);
    }

    private void selectWorkDate() {
        calendarView = findViewById(R.id.calendar_view);
        registerWorkDate = findViewById(R.id.workDate);

        calendarView.setShowDaysOfWeekTitle(false);
        calendarView.setSelectionManager(new RangeSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                List< Calendar > selectedDates = calendarView.getSelectedDates();
                if (selectedDates.size() > 0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    firstDate = simpleDateFormat.format(selectedDates.get(0).getTime());
                    lastDate = simpleDateFormat.format(selectedDates.get(selectedDates.size() - 1).getTime());
                    String message = firstDate + " ~ " + lastDate;
                    registerWorkDate.setText(message);
                }
            }
        }));
    }

    private void selectCategory() {
        registerCategory = findViewById(R.id.category);
        registerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] kind = getResources().getStringArray(R.array.kind);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityTest.this);
                builder.setTitle("분야를 선택해주세요.");
                builder.setItems(kind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerCategory.setText(kind[which]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void actionSubmitButton() {
        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void actionBackButton() {
        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //Search_register로 부터의 결과 값을 받아 이곳으로 전달
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        data = result.getData().getStringExtra("data");
                        registerAddr.setText(data);
                    }
                }
            }
    );
}
