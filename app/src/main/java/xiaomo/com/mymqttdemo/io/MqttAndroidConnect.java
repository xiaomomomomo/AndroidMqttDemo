package xiaomo.com.mymqttdemo.io;

import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import xiaomo.com.mymqttdemo.Api;
import xiaomo.com.mymqttdemo.MqttManager;
import xiaomo.com.mymqttdemo.util.BuildRandomNumber;

/**
 * Created by xiaomo
 * Date on  2019/4/14
 *
 * @Desc 使用AndroidClient的Mqtt模块, 用以替代使用MqttClient的模块
 */

public class MqttAndroidConnect extends BaseConnect {
    private MqttAndroidClient mqttAndroidClient;

    public MqttAndroidConnect() {
        TAG = "MqttAndroidConnect";
    }

    @Override
    protected void startConnect() {
        try {
            final long startTime = System.currentTimeMillis();
            String URL_FORMAT = "tcp://%s:%d";
            String randomId = BuildRandomNumber.createGUID();
            String clientId = String.format(FORMAT_CLIENT_ID, MqttManager.APP_NAME, randomId);
            showLog("clientId = " + clientId);
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            showLog(String.format(URL_FORMAT, MqttManager.ip, MqttManager.port));
            mqttAndroidClient = new MqttAndroidClient(MqttManager.mApp.getApplicationContext(),
                    String.format(URL_FORMAT, MqttManager.ip, MqttManager.port), clientId);
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.e(TAG, "connectComplete = " + reconnect);
                    if (reconnect) {
                        // Because Clean Session is true, we need to re-subscribe
                        showLog("It is reconnect = " + reconnect);
                    } else {
                        showLog("It is first connect...");
                    }
                    connectSuccessCallBack(reconnect);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "connectionLost");
                    showLog("connectionLost = " + cause.getMessage().toString());
                    disConnectCallBack();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    onDataReceiveCallBack(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
//                    showLog("deliveryComplete");
                }
            });

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(false);
//            mqttConnectOptions.setUserName("admin");
//            mqttConnectOptions.setPassword("admin".toCharArray());
            //设置true,不然重复发送
            mqttConnectOptions.setCleanSession(true);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "connectSuccess spend time = " + (System.currentTimeMillis() - startTime));
                    showLog("connectSuccess spend time = " + (System.currentTimeMillis() - startTime));
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    showLog("connect failure = " + exception.getMessage().toString());
                    connectFailCallBack(exception.getMessage().toString());
                    Log.e(TAG, "connect failure = " + exception.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception = " + e.toString());
            Toast.makeText(MqttManager.mApp.getApplicationContext(), "错误了", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void subscribeTopic() {
        try {
            mqttAndroidClient.subscribe(Api.TOPIC, 1);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "subscribe topic exception = " + e.toString());
        }
    }

    @Override
    public boolean isConnected() {
        if (mqttAndroidClient == null) {
            return false;
        }
        return mqttAndroidClient.isConnected();
    }

    @Override
    public void disConnect() {
        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect();
                mqttAndroidClient.close();
                disConnectCallBack();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void publish(String topic, String msg) throws Exception {
        MqttMessage mqttMsg = new MqttMessage();
        mqttMsg.setPayload(msg.getBytes());
        mqttAndroidClient.publish(topic, mqttMsg);
    }

}
