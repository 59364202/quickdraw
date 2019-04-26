package com.example.quickdraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class createRoom extends AppCompatActivity {

    Button btn_create;
    TextView tv_showRoom;
    EditText edit_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        btn_create = (Button)findViewById(R.id.btn_create);
        tv_showRoom = (TextView)findViewById(R.id.tv_showRoom);
        edit_text = (EditText)findViewById(R.id.edit_text);

        final String text = edit_text.getText().toString();


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_showRoom.setText(text);

                Toast toast = Toast.makeText(getApplicationContext(),"You created room.", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(createRoom.this, restRoom.class);
                intent.putExtra("topicName",edit_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }

}
