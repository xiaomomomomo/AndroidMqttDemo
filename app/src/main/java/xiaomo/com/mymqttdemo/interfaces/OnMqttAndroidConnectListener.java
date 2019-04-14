package xiaomo.com.mymqttdemo.interfaces;

/**
 * Created by xiaomo
 * Date on  2018/4/14
 *
 * @Desc Mqtt模块收到消息的回调
 */

public abstract class OnMqttAndroidConnectListener {
    public void connect() {

    }

    public void disConnect() {

    }

    public abstract void onDataReceive(String message);

    public void onConnectFail(String exception) {

    }
}
