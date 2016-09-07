package com.example.ant_test.patch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2016/7/27.
 */
public class PatchActivity extends Activity implements View.OnClickListener {

    Button btnPatchExists, btnAddPatch, btnDeletePatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);
        btnPatchExists = (Button) findViewById(R.id.btnPatchExists);
        btnAddPatch = (Button) findViewById(R.id.btnAddPatch);
        btnDeletePatch = (Button) findViewById(R.id.btnDeletePatch);
        btnDeletePatch.setOnClickListener(this);
        btnAddPatch.setOnClickListener(this);
        btnPatchExists.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPatchExists:
                break;
            case R.id.btnAddPatch:
                break;
            case R.id.btnDeletePatch:
                break;
        }
    }
}
