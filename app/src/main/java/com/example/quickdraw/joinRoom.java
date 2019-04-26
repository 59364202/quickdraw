package com.example.quickdraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class joinRoom extends AppCompatActivity {

    Button btn_join;
    EditText editText_nameRoom;
    EditText tv_playername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        btn_join = (Button)findViewById(R.id.btn_join);
        editText_nameRoom = (EditText)findViewById(R.id.editText_nameRoom);
        tv_playername = (EditText)findViewById(R.id.tv_nameplayer);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),"You joined room.", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(joinRoom.this, joinrestRoom.class);
                intent.putExtra("topicName",editText_nameRoom.getText().toString());
                intent.putExtra("playerName",tv_playername.getText().toString());
                startActivity(intent);
                finish();

            }
        });

    }

}
