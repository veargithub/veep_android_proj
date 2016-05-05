package com.example.ant_test.switch_perference;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2016/4/7.
 */
public class SwitchPreferenceActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id
                .switch_compat);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(this);

        SwitchCompat switchCompat2 = (SwitchCompat) findViewById(R.id
                .switch_compat2);
        switchCompat2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_compat:
                Log.i("switch_compat", isChecked + "");
                break;
            case R.id.switch_compat2:
                Log.i("switch_compat2", isChecked + "");
                break;
        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//            case R.id.switch_compat:
//                Log.i("switch_compat", isChecked + "");
//                break;
//            case R.id.switch_compat2:
//                Log.i("switch_compat2", isChecked + "");
//                break;
//        }
//
//    }
}
