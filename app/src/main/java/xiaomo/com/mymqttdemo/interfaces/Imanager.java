package xiaomo.com.mymqttdemo.interfaces;

/**
 * Created xiaomo
 * Date on  2019/4/14
 *
 * @Desc 网络管理的基础结构
 */

public interface Imanager {
    void connect();

    void disConnect();

    void regeisterServerMsg(OnMqttAndroidConnectListener listener);

    void unRegeisterServerMsg(OnMqttAndroidConnectListener listener);

    void sendMsg(String topic, String message);

    boolean isConnected();
}
