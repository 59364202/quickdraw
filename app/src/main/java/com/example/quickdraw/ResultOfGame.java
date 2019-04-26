package com.example.quickdraw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultOfGame extends AppCompatActivity {
    String resultText;

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_game);

        tv_result = (TextView) findViewById(R.id.tv_result);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            resultText = bundle.getString("Result");
        }

        tv_result.setText(resultText);

    }
}
