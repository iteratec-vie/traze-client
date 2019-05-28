package iteratec.guiclient.traze;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SimpleMqttCallback implements MqttCallback {
    private BrokerClient brokerClient;

    SimpleMqttCallback(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
        System.exit(0);
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        if (topic.equals("traze/" + brokerClient.getInstanceId() + "/grid")) {
            TrazeClient.updateGrid(new String(mqttMessage.getPayload()));
        } else if (topic.equals("traze/" + brokerClient.getInstanceId() + "/player/" + brokerClient.getClientId())) {
            TrazeClient.initPlayer(new String(mqttMessage.getPayload()));
        } else if (topic.equals("traze/" + brokerClient.getInstanceId() + "/players")) {
            TrazeClient.updateAllPlayers(new String(mqttMessage.getPayload()));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
