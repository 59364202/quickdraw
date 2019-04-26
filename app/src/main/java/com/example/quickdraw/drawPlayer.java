package com.example.quickdraw;

import android.content.Intent;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class drawPlayer extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 11000;
    DrawOS drawOS;

    private TextView tv_timeCounter;
    private TextView tv_word;
    private Button btn_finish;
    private Button btn_reset;

    private CountDownTimer countDownTimer;
    private long timemilis = START_TIME_IN_MILLIS;
    private String topicName;

    MqttHelper mqttHelper;
    String RandomWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_player);

        drawOS = (DrawOS) findViewById(R.id.drawOS);
        tv_timeCounter = (TextView) findViewById(R.id.tv_timeCount);
        tv_word = (TextView) findViewById(R.id.tv_randomword);
        btn_finish = (Button) findViewById(R.id.tv_finishButton);
        btn_reset = (Button) findViewById(R.id.tv_resetButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            topicName = bundle.getString("topicName");

        }


        Random rand = new Random();
        String[] ListRandomWorld = getResources().getStringArray(R.array.word_random);
        int n = rand.nextInt(ListRandomWorld.length);
        RandomWord = ListRandomWorld[n];
        tv_word.setText(RandomWord);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOS.setNewLine(new ArrayList<ArrayList<PointF>>());
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                countDownTimer.cancel();
            }
        });

        countDownTimer = new CountDownTimer(timemilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timemilis = millisUntilFinished;
                int second = (int) (timemilis/1000)%60;
                tv_timeCounter.setText(String.valueOf(second));
            }

            @Override
            public void onFinish() {

                sendingFinish();
            }
        }.start();

        startMqtt();


    }

    private void sendingFinish(){
        String sending = "Picture::::::::::";
        for(int i = 0;i<drawOS.listOfLine.size();i++){
            for (int j = 0;j<drawOS.listOfLine.get(i).size();j++){
                sending = sending + drawOS.listOfLine.get(i).get(j).x + ":" + drawOS.listOfLine.get(i).get(j).y + "::";
            }
            sending = sending + ":::";
        }
        try {
            mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes() , 0, false);

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
        Intent intent = new Intent(drawPlayer.this,WaitingForAnswer.class);
        intent.putExtra("topicName",topicName);
        intent.putExtra("Word",RandomWord);
        startActivity(intent);
        finish();

    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
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

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

}
