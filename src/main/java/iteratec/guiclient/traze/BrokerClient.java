package iteratec.guiclient.traze;

public interface BrokerClient {
    /**
     * 1. Diese Methode soll eine eine Client ID generieren und mit Hilfe der url und der generierten Client ID eine
     * Verbindung zum Broker aufbauen.
     * 2. Über ein MqttCallback soll die Reaktion bei eingehenden Nachrichten definiert werden.
     * 3. Hier sollen auch verschiedene, relevante Topics abboniert werden.
     *
     * @param url Die URL vom MQTT Broker (wird in der Klasse TrazeClient mitgegeben).
     */
    void init(String url);

    /**
     * Diese Methode dient zum Versenden von Nachrichten an den MQTT Broker.
     *
     * @param topic         Das Topic, an welches die Nachricht gesendet werden soll.
     * @param messageString Die eigentliche Nachricht als String (Achtung: Beim Versenden muss der String in Bytes
     *                      umgewandelt werden).
     */
    void publishMessage(String topic, String messageString);

    /**
     * Eine eigene Methode, die den Spieler beim Broker registrieren soll. Das ist die einzige Methode, in welcher eine
     * Nachricht an den Broker geschickt werden muss (Tipp: Muss im init aufgerufen werden).
     * traze/<Instanz>/join
     */
    void join();

    /**
     * @return Gibt die Game Instanz zurück. Die Game Instanz ist eine eindeutige ID für einen Game Room auf dem Traze
     * Broker.
     */
    int getInstanceId();

    /**
     * @return Gibt die Client ID zurück.
     */
    String getClientId();
}
