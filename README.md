# Coding Dojo: IoT

Vorbedingungen:
- Java 8
- Maven
- IDE

---

In den Run/Debug Configurations folgenden VM Parameter setzen:
```
-Djava.library.path=<Pfad zum Repo>/native/<OS>  
```

Den Parameter OS durch das verwendete Betriebssystem ersetzen. Unterstützt werden:
- `windows`
- `macosx`
- `solaris`
- `linux`

---

Link zur Dokumentation:

https://traze.iteratec.de/

## Sprint 1

- Erstellen einer Verbindung vom Client zum Server
- Implementierung vom BrokerClient- und MqttCallback-Interface
- Tipp:
    - Macht Gebrauch von den Methoden `initPlayer`, `updateAllPlayers` und `updateGrid` in der Klasse `TrazeClient`
    - Relevante Topics:
        - `traze/<InstanzID>/grid`
        - `traze/<InstanzID>/join`
        - `traze/<InstanzID>/player/<ClientID>`
        - `traze/<InstanzID>/players`

#### Für besonders Schnelle
- Implementieren der Respawn-Funktion
- Tipp:
    - Macht Gebrauch von der Methode `playerAlive` in der Klasse `TrazeClient`

## Sprint 2

- Implementieren einer künstlichen Intelligenz (Bot)
- Tipp:
    - Erweitern der Methode `calculateNextDirection` in der Klasse `Brain`
    - Macht Gebrauch von den Methoden `<Himmelsrichtung>IsFree` in der Klasse `Brain`
    - Weitere Daten könnt ihr aus der Klasse `TrazeClient` beziehen

#### Für besonders Schnelle

- Schaut euch die Dokumentation an und implementiert zusätzliche Funktionen (Link s.o.)

## Cheatsheet

Dokumentation des MqttCallbacks:
```
public interface MqttCallback {
    void connectionLost(Throwable var1);
    void messageArrived(String var1, MqttMessage var2) throws Exception;
    void deliveryComplete(IMqttDeliveryToken var1);
}
```
Um eine Client ID zu generieren:
```
MqttClient.generateClientId()
```
Um einen MQTT Client zu initialisieren, muss eine Instanz erzeugt werden, indem die URL vom Broker und eine Client ID mitgegeben wird.
```
MqttClient client = new MqttClient(url, generatedClientId);
```
Jedem MQTT Client muss ein Callback mitgegeben werden, welcher eingehende Nachrichten bearbeitet.
```
client.setCallback(mqttCallback);
```
Verbindung zwischen Broker und Client herstellen.
```
client.connect();
```
Um ein Topic zu abonnieren muss folgende Funktion aufgerufen werden.
```
client.subscribe(topicAsString)
```
Schickt eine Nachricht vom Client an den Broker.
```
String message = "message";
MqttMessage mqttMessage = new MqttMessage(message.getBytes());
client.publish(topic, mqttMessage);
```
