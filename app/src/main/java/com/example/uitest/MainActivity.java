package com.example.uitest;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import com.yml.ui.widget.gesture.GestureView;
import com.yml.ui.widget.gesture.callback.OnResultListener;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private GestureView gestureView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et);
        gestureView = findViewById(R.id.gv);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gestureView.setPassword(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        gestureView.setResultListener(new OnResultListener() {
            @Override
            public void onError(String result) {
                Toast.makeText(MainActivity.this, "input error: " + result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, "input success: " + result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
