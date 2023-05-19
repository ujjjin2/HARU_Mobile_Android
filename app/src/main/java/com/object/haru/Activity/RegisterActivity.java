package com.object.haru.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.Search_register;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private EditText registerTitle, registerCategory, registerWorkDate, registerMoney, registerAddr, registerDetailAddr, registerCareer;
    private String token, firstDate, lastDate, result, data, gender, age;
    private Long kakaoId;
    private Button submit;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Spinner spinnerStartTime, spinnerEndTime, spinnerStartAge, spinnerEndAge;
    private ImageView backButton;
    private CheckBox irrelevantGender, irrelevantAge, irrelevantCareer, irrelevantMoney;

    private TextView waveMark;
    final private int minMoney = 9620;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);

        actionBackButton();
        actionSubmitButton();
        writeTitle();
        selectCategory();
        selectWorkDate();
        selectWorkTime();
        selectMoney();
        selectAddress();
        selectGender();
        selectAge();
        writeCareer();
    }

    private void writeTitle() {
        registerTitle = findViewById(R.id.title);
    }

    private void writeCareer() {
        registerCareer = findViewById(R.id.careerEdit);
        irrelevantCareer = findViewById(R.id.careerIrrelevantCheckbox);

        irrelevantCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (irrelevantCareer.isChecked()) {
                    registerCareer.setText("경력무관");
                    registerCareer.setEnabled(false);
                } else {
                    registerCareer.setEnabled(true);
                    registerCareer.setText("");
                }
            }
        });
    }

    private void selectAge() {
        spinnerStartAge = findViewById(R.id.age1);
        spinnerEndAge = findViewById(R.id.age2);
        irrelevantAge = findViewById(R.id.ageIrrelevantCheckbox);
        waveMark = findViewById(R.id.text2);

        ArrayAdapter startAgeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.startAge, android.R.layout.simple_spinner_item);
        ArrayAdapter endAgeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.endAge, android.R.layout.simple_spinner_item);

        irrelevantAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerStartAge.setEnabled(false);
                    spinnerEndAge.setEnabled(false);
                    waveMark.setTextColor(Color.parseColor("#D6D6D6"));
                    age = "연령무관";
                } else {
                    spinnerStartAge.setEnabled(true);
                    spinnerEndAge.setEnabled(true);
                    waveMark.setTextColor(Color.parseColor("#000000"));
                    age = "";
                    updateAgeValue();
                }
            }
        });

        startAgeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.startAge)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                    view.setOnClickListener(null);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }
        };

        endAgeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.endAge)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                    view.setOnClickListener(null);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }
        };

        startAgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartAge.setAdapter(startAgeAdapter);
        endAgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndAge.setAdapter(endAgeAdapter);

        spinnerStartAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAgeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerEndAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAgeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAgeValue() {
        String startAge = spinnerStartAge.getSelectedItem().toString();
        String endAge = spinnerEndAge.getSelectedItem().toString();
        age = startAge + " ~ " + endAge;
    }

    private void selectGender() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.male);
        radioButtonFemale = findViewById(R.id.female);
        irrelevantGender = findViewById(R.id.sexIrrelevantCheckbox);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = "남성우대";
                        radioButtonMale.setTextColor(Color.parseColor("#ffffff"));
                        radioButtonFemale.setTextColor(Color.parseColor("#07BC7D"));
                        break;
                    case R.id.female:
                        gender = "여성우대";
                        radioButtonMale.setTextColor(Color.parseColor("#07BC7D"));
                        radioButtonFemale.setTextColor(Color.parseColor("#ffffff"));
                        break;
                }
            }
        });

        irrelevantGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioGroup.clearCheck();
                    radioButtonFemale.setTextColor(Color.parseColor("#d6d6d6"));
                    radioButtonMale.setTextColor(Color.parseColor("#d6d6d6"));
                    radioButtonFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_button_disabled));
                    radioButtonMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_button_disabled));
                    radioButtonFemale.setEnabled(false);
                    radioButtonMale.setEnabled(false);
                    gender = "성별무관";
                } else {
                    radioButtonFemale.setEnabled(true);
                    radioButtonMale.setEnabled(true);
                    radioButtonMale.setTextColor(Color.parseColor("#07BC7D"));
                    radioButtonFemale.setTextColor(Color.parseColor("#07BC7D"));
                    radioButtonFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_button_selector));
                    radioButtonMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_button_selector));
                }
            }
        });
    }

    private void selectAddress() {
        registerAddr = findViewById(R.id.address);
        registerDetailAddr = findViewById(R.id.detailAddress);
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
        irrelevantMoney = findViewById(R.id.moneyMinCheckbox);

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

        irrelevantMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    registerMoney.setEnabled(false);
                    registerMoney.setText("9,620");
                } else {
                    registerMoney.setEnabled(true);
                    registerMoney.setText("");
                }
            }
        });
    }

    private void selectWorkTime() {
        spinnerStartTime = findViewById(R.id.start_spinner);
        spinnerEndTime = findViewById(R.id.end_spinner);

        ArrayAdapter startTimeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.startTime, android.R.layout.simple_spinner_item);
        ArrayAdapter endTimeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.endTime, android.R.layout.simple_spinner_item);

        startTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.startTime)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                    view.setOnClickListener(null);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }
        };

        endTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.endTime)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    view.setEnabled(false);
                    view.setOnClickListener(null);
                } else {
                    view.setEnabled(true);
                }
                return view;
            }
        };

        startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(startTimeAdapter);
        spinnerEndTime.setAdapter(endTimeAdapter);
    }

    private void selectWorkDate() {
        calendarView = findViewById(R.id.calendar_view);
        registerWorkDate = findViewById(R.id.workDate);
        registerWorkDate.setFocusable(false);
        registerWorkDate.setClickable(false);
        calendarView.setVisibility(View.GONE);

        registerWorkDate.setOnClickListener(new View.OnClickListener() {
            boolean isCalendarVisible = false;
            @Override
            public void onClick(View v) {
                if (isCalendarVisible) {
                    calendarView.setVisibility(View.GONE);
                    isCalendarVisible = false;
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    isCalendarVisible = true;
                }
            }
        });

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
        registerCategory.setFocusable(false);
        registerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] kind = getResources().getStringArray(R.array.kind);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

        final Geocoder geocoder = new Geocoder(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list = null;

                try {
                    list = geocoder.getFromLocationName(data+registerDetailAddr.getText().toString(), 10);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(RegisterActivity.this, "해당 주수정보의 위도 경도가 주어지지 않았습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Address address = list.get(0);
                        double lat = address.getLatitude();
                        double lon = address.getLongitude();

                        RecruitDTO recruitDTO = new RecruitDTO(data+registerDetailAddr.getText().toString(),
                                lastDate+" "+spinnerEndTime.getSelectedItem()+":00",
                                lat,
                                lon,
                                Integer.parseInt(registerMoney.getText().toString().replace(",", "")),
                                age,
                                registerCareer.getText().toString(),
                                gender,
                                firstDate+" "+spinnerStartTime.getSelectedItem()+":00",
                                registerCategory.getText().toString(),
                                registerTitle.getText().toString(),
                                kakaoId);
                        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().register(token, recruitDTO);
                        call.enqueue(new Callback<RecruitDTO>() {
                            @Override
                            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                                RecruitDTO recruitDTO = response.body();
                                Log.d("[성공]", "----------------------");
                                finish();
                            }

                            @Override
                            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                                Log.d("[실페]","================");
                            }
                        });
                    }
                }
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
