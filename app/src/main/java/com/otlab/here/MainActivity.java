package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.common.Here;
import com.otlab.here.map.MapActivity;
import com.otlab.here.option.OptionActivity;
import com.otlab.here.setting.SettingActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mapImg:
                if (Here.checkGPSPermission(getApplicationContext(), this)) {
                    Here.requestGPSPermission(this);
                } else if (Here.checkLocationPermission(getApplicationContext())) {
                    Here.requestLocationPermission(this);
                } else {
                    startActivity(new Intent(getApplication(), MapActivity.class));
                    finish();
                }
                break;

            case R.id.optText:
                startActivity(new Intent(getApplication(), OptionActivity.class));
                finish();
                break;

            case R.id.setText:
                startActivity(new Intent(getApplication(), SettingActivity.class));
                break;
        }
    }

    private void init() {
        findViewById(R.id.mapImg).setOnClickListener(this);
        findViewById(R.id.optText).setOnClickListener(this);
        findViewById(R.id.setText).setOnClickListener(this);
    }
}
/*
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String key = new String(Base64.encode(md.digest(), 0));
            Log.d("Hash key:", "!!!!!!!" + key + "!!!!!!");
        }
    } catch (Exception e) {
        Log.e("name not found", e.toString());
    }*/