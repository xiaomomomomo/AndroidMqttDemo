package xiaomo.com.mymqttdemo.bean;


import xiaomo.com.mymqttdemo.interfaces.OnMqttAndroidConnectListener;

/**
 * Created by xiaomo
 * Date on  2019/4/14
 *
 * @Desc 每个注册事件的回调
 */

public class MqttListenerData {
    private String event;
    private OnMqttAndroidConnectListener listener;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public OnMqttAndroidConnectListener getListener() {
        return listener;
    }

    public void setListener(OnMqttAndroidConnectListener listener) {
        this.listener = listener;
    }
}
