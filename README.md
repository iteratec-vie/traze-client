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

Die Klasse MqttClient wird verwendet, um eine Verbindung zum Broker zu erstellen, verschiedene Topics zu abbonnieren und Nachrichten an den Broker zu senden.
```
MqttClient client = new MqttClient(url, clientId)
client.connect()
client.subscribe(topic)
client.publish(topic, mqttMessage)
client.disconnect()
```

Um eine Client ID zu generieren, wird eine statische Methode vom MqttClient verwendet:
```
MqttClient.generateClientId()
```

Um Nachrichten zu empfangen und bearbeiten, muss die Schnittstelle MqttCallback implementiert werden. Dieses definiert folgende drei Methoden:
```
public interface MqttCallback {
    void connectionLost(Throwable var1);
    void messageArrived(String var1, MqttMessage var2) throws Exception;
    void deliveryComplete(IMqttDeliveryToken var1);
}
```
Die Implementierung der Schnittstelle muss auf dem MqttClient registriert sein.
```
client.setCallback(mqttCallback)
``` 
Schickt eine Nachricht vom Client an den Broker.
```
String message = "message";
MqttMessage mqttMessage = new MqttMessage(message.getBytes());
client.publish(topic, mqttMessage);
```
