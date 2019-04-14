package xiaomo.com.mymqttdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import xiaomo.com.mymqttdemo.interfaces.OnMqttAndroidConnectListener;
import xiaomo.com.mymqttdemo.io.MqttAndroidConnect;

/**
 * Created by xiaomo
 * Date on  2019/4/14 16:15
 *
 * @Desc
 */
public class MainActivity extends AppCompatActivity {

    private TextView tv_message;
    private TextView tv_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message = findViewById(R.id.tv_message);
        tv_send = findViewById(R.id.tv_send);

        //初始化自定义WiseMqtt模块的配置,并开启长连接
        MqttManager.getInstance()
                .init(getApplication())
                .setServerIp(Api.IP_MQTT)                            //Ip
                .setServerPort(Api.PORT_MQTT)                        //port
                .connect();
        MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
            @Override
            public void onDataReceive(String message) {
                Log.e("收到消息了,内容是 ", message);
                tv_message.setText(message);
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击了", "点击了");
                MqttManager.getInstance().sendMsg(Api.TOPIC, "你好吗?我发一条消息试试");
            }
        });
    }
}
