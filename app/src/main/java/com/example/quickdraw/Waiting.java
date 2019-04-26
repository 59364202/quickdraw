package com.example.quickdraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Waiting extends AppCompatActivity {

    private String topicName;
    private String playerName;
    MqttHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            topicName = bundle.getString("topicName");
            playerName = bundle.getString("playerName");
        }
        startMqtt();
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
                String[] commad = message.split("::::::::::");
                if (commad[0].equalsIgnoreCase("Picture")){
                    String listOfline = commad[1];
                    Intent intent = new Intent(Waiting.this, AnswerField.class);
                    intent.putExtra("topicName",topicName);
                    intent.putExtra("playerName",playerName);
                    intent.putExtra("Picture",listOfline);
                    startActivity(intent);
                    finish();}
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
