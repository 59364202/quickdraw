package com.example.quickdraw;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class WaitingForAnswer extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 31000;
    private CountDownTimer countDownTimer;
    private long timemilis = START_TIME_IN_MILLIS;

    MqttHelper mqttHelper;
    String topicName;
    String RandomWord;
    int numberOfanswer = 0;

    private TextView tv_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_answer);

        tv_answer = (TextView) findViewById(R.id.tv_answer);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            topicName = bundle.getString("topicName");
            RandomWord = bundle.getString("Word");

        }
        startMqtt();

        countDownTimer = new CountDownTimer(timemilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timemilis = millisUntilFinished;
                int second = (int) (timemilis/1000)%60;
                String sending = "time:::"+String.valueOf(second);
                try {
                    mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes() , 0, false);

                } catch (MqttException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                String sending = "HOST" + ":::" + "No One Answer";
                try {
                    mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes() , 0, false);

                } catch (MqttException ex) {
                    ex.printStackTrace();
                }
                Intent intent = new Intent(WaitingForAnswer.this, ResultOfGame.class);
                intent.putExtra("Result","Nobody Won");
                startActivity(intent);
                finish();
            }
        }.start();
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
                String message = mqttMessage.toString();
                numberOfanswer++;
                String[] commandText = message.split(":::");
                String answerText = new String();
                if(numberOfanswer < 5){
                    answerText = tv_answer.getText().toString() + "\n" + commandText[0] + " : " + commandText[1];
                }
                else  {
                    answerText = commandText[0] + " : " + commandText[1];
                    numberOfanswer = 0;
                }
                if(commandText[1].toLowerCase().equalsIgnoreCase(RandomWord.toLowerCase())){
                    String sending = "HOST" + ":::" + commandText[0];
                    try {
                        mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes() , 0, false);

                    } catch (MqttException ex) {
                        ex.printStackTrace();
                    }
                    Intent intent = new Intent(WaitingForAnswer.this, ResultOfGame.class);
                    intent.putExtra("Result",commandText[0] + " IS WINNER!!!");
                    startActivity(intent);
                    countDownTimer.cancel();
                    finish();
                }
                else if(!commandText[0].equalsIgnoreCase("time")){
                    tv_answer.setText(answerText);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
