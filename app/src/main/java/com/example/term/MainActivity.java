package com.example.term;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;
public class MainActivity extends AppCompatActivity {
    private Microgear microgear = new Microgear(this);
    private String appid = "TemperatureMonitoring"; //APP_ID
    private String key = "qsl7mPMfXo4gbkG"; //KEY
    private String secret = "dGbb12DP6F3PngaDno0nEZkIe"; //SECRET
    private String alias = "android";
    private Button button1;
    private Button button2;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MicrogearCallBack callback = new MicrogearCallBack();
        microgear.connect(appid,key,secret,alias);
        microgear.setCallback(callback);
        microgear.subscribe("tester");

        button1 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //microgear.publish("esp32_test", "testaaa");
                microgear.chat("esp32_test","On");
            }
        });
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //microgear.publish("esp32_test", "testaaa");
                microgear.chat("esp32_test","Off");
            }
        });
    }
    class MicrogearCallBack implements MicrogearEventListener{
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Now I'm connected with netpie");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Connected","Now I'm connected with netpie");
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", topic+" : "+message);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Message",topic+" : "+message);
        }

        @Override
        public void onPresent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "New friend Connect :"+token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("present","New friend Connect :"+token);
        }

        @Override
        public void onAbsent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Friend lost :"+token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("absent","Friend lost :"+token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("disconnect","Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("exception","Exception : "+error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("info","Info : "+info);
        }
    }
}
