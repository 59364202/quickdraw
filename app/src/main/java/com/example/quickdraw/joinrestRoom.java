package com.example.quickdraw;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.StringTokenizer;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class joinrestRoom extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 2000 ;
    private CountDownTimer countDownTimer;
    private long timemilis = START_TIME_IN_MILLIS;
    private TextView tv_player;

    private String topicName;
    private String playerName;
    MqttHelper mqttHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinrest_room);

        tv_player = (TextView) findViewById(R.id.tv_ListOfPlayer);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            topicName = bundle.getString("topicName");
            playerName = bundle.getString("playerName");
        }
        startMqtt();
        countDownTimer = new CountDownTimer(timemilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timemilis = millisUntilFinished;
                int second = (int) (timemilis/1000)%60;
//                tv_player.setText(String.valueOf(second));
            }

            @Override
            public void onFinish() {
                String sendingText = "listplayer"+":::"+mqttHelper.clientId;
                try {
                    mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic,sendingText.getBytes(),0,false);
                } catch (MqttException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();


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
                if(mqttMessage.toString().equalsIgnoreCase("startGame")){
                    Intent intent = new Intent(joinrestRoom.this, Waiting.class);
                    intent.putExtra("topicName",topicName);
                    intent.putExtra("playerName",playerName);
                    startActivity(intent);
                    finish();
                }
                String[] commad = mqttMessage.toString().split(":::");
                if(commad[0].equalsIgnoreCase( "Hlistplayer")){
                    tv_player.setText(commad[1]);}

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
