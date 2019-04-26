package com.example.quickdraw;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class restRoom extends AppCompatActivity {
    private Button btn_start;
    private TextView tv_ListPlayer;

    private String topicName = "";
    MqttHelper mqttHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_room);

        btn_start = (Button) findViewById(R.id.btn_start);
        tv_ListPlayer = (TextView) findViewById(R.id.tv_listplayer);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            topicName = bundle.getString("topicName");
        }

        startMqtt();

        tv_ListPlayer.setText("Player" + "\n" + mqttHelper.clientId);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sending = "startGame";
                try {
                    mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, sending.getBytes() , 0,false);
                } catch (MqttException ex) {
                    ex.printStackTrace();
                }
                Intent intent = new Intent(restRoom.this,drawPlayer.class);
                intent.putExtra("topicName",topicName);
                startActivity(intent);
                finish();
            }
        });
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
                String[] command = message.split(":::");
                String player = tv_ListPlayer.getText().toString();
                if(command[0].equalsIgnoreCase("listplayer")){
                    player = player +","+ command[1];
                    tv_ListPlayer.setText(player);
                    player = "Hlistplayer" + ":::" + player;
                    try {
                        mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic, player.getBytes() , 0, false);

                    } catch (MqttException ex) {
                        ex.printStackTrace();
                    }}
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
