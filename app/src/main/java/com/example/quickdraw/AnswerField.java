package com.example.quickdraw;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class AnswerField extends AppCompatActivity {

    private Button btn_send;
    private EditText et_answer;
    private TextView tv_answer;

    DrawOS drawOS;
    MqttHelper mqttHelper;
    private String topicName;
    private String playerName;
    private String ListOfline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_field);

        drawOS = findViewById(R.id.drawOS);
        btn_send = findViewById(R.id.btn_Send);
        et_answer = findViewById(R.id.tv_answer);
        tv_answer = (TextView) findViewById(R.id.tv_time);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            topicName = bundle.getString("topicName");
            playerName = bundle.getString("playerName");
            ListOfline = bundle.getString("Picture");
        }

        startMqtt();
        ArrayList<ArrayList<PointF>> Picture = StringListConverter(ListOfline);
        drawOS.setNewLine(Picture);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sending = mqttHelper.clientId;
                sending = sending + ":::" +et_answer.getText().toString();
                try {
                    mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes(), 0, false);
                }
                catch (MqttException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    private ArrayList<ArrayList<PointF>> StringListConverter(String listLinepicture){
        ArrayList<ArrayList<PointF>> picture =  new ArrayList<ArrayList<PointF>>();
        String[] Picture = listLinepicture.split(":::");
        for(int i = 0;i<Picture.length;i++){
            String[] Line = Picture[i].split("::");
            ArrayList<PointF> litLine = new ArrayList<PointF>();
            for (int j=0;j<Line.length;j++){
                String[] pointf = Line[j].split(":");
                if(pointf != null && pointf.length >= 2){
                    PointF pointF = new PointF();
                    pointF.x = Float.parseFloat(pointf[0]);
                    pointF.y = Float.parseFloat(pointf[1]);
                    litLine.add(pointF);}
            }
            picture.add(litLine);

        }
        return picture;
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext(),playerName);
        mqttHelper.TopicSet(topicName);
        mqttHelper.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String message = mqttMessage.toString();
                String[] commandText = message.split(":::");
                if(commandText[0].equalsIgnoreCase( "HOST")){
                    String resultText;
                    if(commandText[1].toLowerCase().equalsIgnoreCase(mqttHelper.clientId.toLowerCase())){
                        resultText = "YOU WON!!";
                    }
                    else {
                        resultText = "YOU LOSE!!";
                    }
                    Intent intent = new Intent(AnswerField.this, ResultOfGame.class);
                    intent.putExtra("Result",resultText);
                    startActivity(intent);
                    finish();

                }
                else if(commandText[0].equalsIgnoreCase("time")){
                    tv_answer.setText(commandText[1]);
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
