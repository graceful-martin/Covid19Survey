package com.example.intent;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import java.util.ArrayList;

import static android.view.View.*;

public class NextActivity extends AppCompatActivity {
    private int result = 0;
    private final int cbCount = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("COVID19 Info");
        setContentView(R.layout.next_activity);

        Button btnFinish = findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<CheckBox> cbList = new ArrayList<>();
                String img;
                int resID;
                for(int size = 1; size < cbCount; size++) {
                    img = "checkBox" + size;
                    resID = getResources().getIdentifier(img, "id", "com.example.intent");
                    cbList.add((CheckBox)findViewById(resID));
                }
                for(CheckBox cb : cbList)
                    if(cb.isChecked())
                        result++;
                setResult(201, getIntent().putExtra("result", result));
                result = 0;
                finish();
            }
        });
    }
}