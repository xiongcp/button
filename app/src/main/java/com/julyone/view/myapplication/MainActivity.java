package com.julyone.view.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private CircleButton main_button_one;
    private CircleButton main_button_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_button_one = (CircleButton) findViewById(R.id.main_button_one);
        main_button_two = (CircleButton) findViewById(R.id.main_button_two);
        main_button_one.setTouchColor(0XFFFFFF, 90);
        main_button_two.setTouchColor(Color.RED, 20);
        main_button_one.setOnClickListener(onClickListener);
        main_button_two.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_button_one:
                    Toast.makeText(MainActivity.this, "main_button_one", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_button_two:
                    Toast.makeText(MainActivity.this, "main_button_two", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
