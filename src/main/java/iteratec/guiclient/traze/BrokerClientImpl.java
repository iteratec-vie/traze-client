package iteratec.guiclient.traze;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class BrokerClientImpl implements BrokerClient {
    private MqttClient client;
    private String generatedClientId;

    @Override
    public void init(String url) {
        try {
            generatedClientId = MqttClient.generateClientId();
            client = new MqttClient(url, generatedClientId);
            SimpleMqttCallback ourCallback = new SimpleMqttCallback(this);
            client.setCallback(ourCallback);
            client.connect();
            client.subscribe("traze/" + getInstanceId() + "/grid");
            join();
            client.subscribe("traze/" + getInstanceId() + "/player/" + generatedClientId);
            client.subscribe("traze/" + getInstanceId() + "/players");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishMessage(String topic, String messageString) {
        MqttMessage message = new MqttMessage();
        message.setPayload(messageString.getBytes());
        try {
            client.publish(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void join() {
        String topic = "traze/" + getInstanceId() + "/join";
        JSONObject joiningPlayer = new JSONObject("{\"name\": \"" + NameGenerator.generateHeroName() + "\",\"mqttClientName\": \"" + generatedClientId + "\"}");
        publishMessage(topic, joiningPlayer.toString());
    }

    @Override
    public int getInstanceId() {
        return 1;
    }

    @Override
    public String getClientId() {
        return generatedClientId;
    }
}
