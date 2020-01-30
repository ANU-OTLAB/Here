package com.otlab.here.setting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.otlab.here.common.model.Destination;
import com.otlab.here.common.Here;
import com.otlab.here.map.MapActivity;
import com.otlab.here.common.MessageThread;
import com.otlab.here.R;
import com.otlab.here.setting.model.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class SettingPopupActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Intent intent;
    SettingItem.ServiceType serviceType;
    SettingItem.DestinationType destinationType;
    int itemPosition;
    Destination[] destination;
    String destinationName;
    TextView nameText;
    TextView distanceText;
    TextView destinationText;
    Spinner destinationTypeSpinner;
    TextView validityText;
    TextView popupName;
    Button editBtn;
    Button okBtn;
    Button cancelBtn;
    Button dateBtn;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_setting);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                save();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.setValidityBtn:
                if (Here.checkVersion()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view2, year, month, day) -> {
                        validityText.setText(String.format("%d-%d-%d", year, ++month, day));
                    }, 2020, 2, 1);//초기상태
                    datePickerDialog.setMessage("날짜 선택");//다이얼로그 이름 선언
                    datePickerDialog.show();
                }
                break;
            case R.id.edit:
                goEdit();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0) destinationType = SettingItem.DestinationType.PERSON;
        else if (position == 1) destinationType = SettingItem.DestinationType.PLACE;
        goEdit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //view 불러 오기
    private void init() {
        appData = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE);

        //View 객체 불러오기
        nameText = findViewById(R.id.setName);
        distanceText = findViewById(R.id.setDistance);
        destinationText = findViewById(R.id.destinationText);
        destinationTypeSpinner = findViewById(R.id.destinationType);
        validityText = findViewById(R.id.setValidity);
        popupName = findViewById(R.id.popupName);
        editBtn = findViewById(R.id.edit);
        okBtn = findViewById(R.id.ok);
        cancelBtn = findViewById(R.id.cancel);
        dateBtn = findViewById(R.id.setValidityBtn);

        //intent 및 serviceType을 불러 옴
        intent = getIntent();
        serviceType = (SettingItem.ServiceType) intent.getSerializableExtra(getResources().getString(R.string.service));

        if (serviceType == SettingItem.ServiceType.UPDATE) {
            loadSettingInfo();
            okBtn.setText("저장");
            cancelBtn.setText("삭제");
            destinationTypeSpinner.setVisibility(View.INVISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            findViewById(R.id.edit).setOnClickListener(this);
        }
        if (serviceType == SettingItem.ServiceType.CREATE) {
            destinationTypeSpinner.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.INVISIBLE);
            setSpinner();
        }

        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.setValidityBtn).setOnClickListener(this);
        ((Spinner) findViewById(R.id.destinationType)).setOnItemSelectedListener(this);
    }


    private void save() {
        if (inputIsValidate()) {

            //데이터 전달하기 후 종료
            intent.putExtra(getResources().getString(R.string.settingName), nameText.getText().toString())
                    .putExtra(getResources().getString(R.string.distance), distanceText.getText().toString())
                    .putExtra(getResources().getString(R.string.destination), destination)
                    .putExtra(getResources().getString(R.string.destinationName), destinationText.getText().toString())
                    .putExtra(getResources().getString(R.string.validity), validityText.getText().toString())
                    .putExtra(getResources().getString(R.string.itemPosition), itemPosition);
            if (destination == null)
                intent.putExtra(getResources().getString(R.string.destinationList), appData.getString(getResources().getString(R.string.destinationList) + itemPosition, ""));
            //수락 대기 목록 디비에 넘기기
            try {
                ArrayList<String> sendmsg = new ArrayList<>();
                sendmsg.add("observer");
                sendmsg.add(appData.getString(getResources().getString(R.string.userId), ""));
                sendmsg.add("expiryDate");
                sendmsg.add(validityText.getText().toString());
                sendmsg.add("validity");
                sendmsg.add("REQUEST");
                sendmsg.add("numberOfTarget");
                sendmsg.add(destination.length + "");
                if (destination.length == 1) {
                    sendmsg.add("target");
                    sendmsg.add(destinationText.getText().toString());
                } else if (destination.length >= 2) {
                    for (int i = 0; i < destination.length; i++) {
                        sendmsg.add("target" + (i + 1));
                        sendmsg.add(destination[i].getDestinationName());
                    }
                }

                MessageThread send = new MessageThread(sendmsg, getResources().getString(R.string.addressAuthoritySave));
                String recvData = send.execute().get();

            } catch (Exception ignored) {
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            destinationTypeSpinner.setVisibility(View.INVISIBLE);
            editBtn.setVisibility(View.VISIBLE);

            destination = (Destination[]) data.getSerializableExtra("destination");
            destinationName = destination[0].getDestinationName() + (destination.length >= 2 ? "외 " + (destination.length - 1) + "명" : "");
            destinationText.setText(destinationName);
        } else if (resultCode == RESULT_CANCELED) {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    private void loadSettingInfo() {
        itemPosition = intent.getIntExtra(getResources().getString(R.string.itemPosition), 0);
        popupName.setText(intent.getStringExtra(getResources().getString(R.string.settingName)));
        nameText.setText(intent.getStringExtra(getResources().getString(R.string.settingName)));
        distanceText.setText(intent.getStringExtra(getResources().getString(R.string.distance)));
        destinationText.setText(intent.getStringExtra(getResources().getString(R.string.destinationName)));
        validityText.setText(intent.getStringExtra(getResources().getString(R.string.validity)));
    }

    private void setSpinner() {
        // 아이템이 들어갈 리스트 선언
        List<String> spinnerItem = new ArrayList<>();
        spinnerItem.add("친구 목록");
        spinnerItem.add("목적지");
        spinnerItem.add("");
        // 스피너에 들어갈 아이템과 스피너를 연결하는 어댑터 선언, android.R.layout.simple_spinner_item은 안드로이드에서 지원해주는 기본적인 형태
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem) {
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationTypeSpinner.setAdapter(spinnerAdapter);
        destinationTypeSpinner.setSelection(spinnerAdapter.getCount());
    }

    private void goEdit() {
        if (destinationType == SettingItem.DestinationType.PERSON) startActivityForResult(new Intent(getApplicationContext(), FriendSelectActivity.class), 1);
        if (destinationType == SettingItem.DestinationType.PLACE) startActivityForResult(new Intent(getApplicationContext(), MapActivity.class), 1);
    }

    private boolean inputIsValidate() {
        if (nameText != null && distanceText != null && destinationText != null && validityText != null
                && nameText.getText().toString().length() != 0 && distanceText.getText().toString().length() != 0 && destinationText.getText().toString().length() != 0 && validityText.getText().toString().length() != 0)
            return isNum(distanceText.getText().toString()) && destinationTypeSpinner.getSelectedItemPosition() < destinationTypeSpinner.getCount();
        return false;
    }

    private boolean isNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) > (int) '9' && (int) str.charAt(i) < (int) '0') return false;
        }
        return true;
    }

    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
    }
}