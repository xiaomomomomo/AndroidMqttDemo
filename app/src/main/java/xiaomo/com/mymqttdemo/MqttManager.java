package xiaomo.com.mymqttdemo;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import xiaomo.com.mymqttdemo.interfaces.Imanager;
import xiaomo.com.mymqttdemo.interfaces.OnMqttAndroidConnectListener;
import xiaomo.com.mymqttdemo.io.MqttAndroidConnect;

/**
 * Created by xiaomo
 * Date on  2019/4/14 16:25.
 *
 * @Desc mqttdemo 长连接接收推送 模块 的启动,功能操作类
 */

public class MqttManager implements Imanager {
    private static final String TAG = "MqttManager";
    public static String APP_NAME = "MyMqttDemo";
    public static String ip;
    public static int port;
    public static Handler mHandler;
    public static Application mApp; //当前应用的Application

    private MqttAndroidConnect mMqttAndroidConnect;
    private static MqttManager mInstance;

    public static MqttManager getInstance() {
        synchronized (MqttManager.class) {
            if (mInstance == null) {
                mInstance = new MqttManager();
            }
        }
        return mInstance;
    }

    private MqttManager() {
        mMqttAndroidConnect = new MqttAndroidConnect();
    }

    /**
     * @param application 当前Application
     */
    public MqttManager init(Application application) {
        mApp = application;
        mHandler = new Handler(application.getMainLooper());
        return mInstance;
    }

    /**
     * @param serverIp 服务端的ip
     * @return
     */
    public MqttManager setServerIp(String serverIp) {
        ip = serverIp;
        return mInstance;
    }

    /**
     * @param serverPort 服务端的port
     * @return
     */
    public MqttManager setServerPort(int serverPort) {
        port = serverPort;
        return mInstance;
    }

    @Override
    public void connect() {
        if (mMqttAndroidConnect.isAlive()) {
            Log.e(TAG, "MqttManager connect thread has alive");
            return;
        }
        if (mMqttAndroidConnect.isConnected()) {
            Log.e(TAG, "MqttManager has connected");
            return;
        }
        mMqttAndroidConnect.start();
    }

    @Override
    public void disConnect() {
        if (mMqttAndroidConnect == null) {
            Log.e(TAG, "Wisepush should connect first");
            return;
        }
        mMqttAndroidConnect.disConnect();
    }

    /**
     * 需要订阅的模块的String
     */
    @Override
    public void regeisterServerMsg(OnMqttAndroidConnectListener listener) {
        mMqttAndroidConnect.regeisterServerMsg(listener);
    }

    @Override
    public void unRegeisterServerMsg(OnMqttAndroidConnectListener listener) {
        mMqttAndroidConnect.unRegeisterServerMsg(listener);
    }

    /**
     * @param topic 需要发送的模块的toppic
     */
    @Override
    public void sendMsg(String topic, String message) {
        mMqttAndroidConnect.sendMsg(topic, message);
    }

    /**
     * 判断是否正在连接
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (mMqttAndroidConnect == null) {
            return false;
        }
        if (mMqttAndroidConnect.isConnected()) {
            return true;
        }
        return false;
    }

}
