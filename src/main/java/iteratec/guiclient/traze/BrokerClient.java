package iteratec.guiclient.traze;

public interface BrokerClient {
    void init(String url);

    void publishMessage(String topic, String messageString);

    void join();

    int getInstanceId();

    String getClientId();
}
