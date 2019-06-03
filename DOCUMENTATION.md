### Grid Information
If you want to write your own view client or an AI pilot you can do so by parsing the MQTT repersentation of the grid. It conains the dimensions of the grid, a «rendered» representation of the grid, as well as a list of bikes with their associated properties. spawns contains a list of spawn points where new players are about to join the grid.

Topic: `traze/{instanceName}/grid`

Retention: Yes

Action: `Subscribe`

Payload:
```
{
  "height":3,
  "width":3,
  "tiles":[
        [ 1, 0, 0 ],
        [ 1, 1, 2 ],
        [ 0, 2, 2 ]
    ],
  "bikes":[
      {
        "playerId":2,
        "currentLocation":[1,0],
        "direction":"W",
        "trail":[[2,0],[2,1]]
        }
   ],
   "spawns":[[2,2]]
}
```
Coordinates (tuples) are represented as JSON lists of two elements e.g. [x,y]. The coordinates have to be interpreted as shown in the illustration below. The tiles can be accessed accordingly like tiles[1][0] == 1 in the example.

coordinate sytem explained by example
This diagram explains how the rendered grid representation in tiles has to be interpreted.

The grid topic is published on every server tick. (4 times a Second)

### Player Information
In addition to the grid you can receive a list of currently active players.

Topic: `traze/{instanceName}/players`

Retention: Yes

Action: `Subscribe`

Payload:
```
[
   {
     "id": 1,
     "name": "player1",
     "color": "#28BA3C",
     "frags": 1,
     "owned": 2
   },
   {
     "id": 2,
     "name": "player2",
     "color": "#0A94FF",
     "frags": 2,
     "owned": 1
   }
]
```
The player topic is published every 5 seconds.

### Client Registration
You have to send a join request message to join the game. In return you’ll get a user token that allows you to control your bike. In this message you can choose a ingame nick name which will be visible in the players topic. You also have to provide a unique MQTT client name (MQTT Client Identifier) in order to receive your session token on your clients own topic. It is important that you specify this very client name in the MQTT connect call to the broker, otherwise you will not be able to receive messages on the traze/{instanceName}/player/{myClientName} topic due to the brokers access control list settings. In order to not be subject to a MQTT deauthentication attack you should choose a client name that can not be guessed. UUIDs are a good solution.
Please note that the MQTT client name is not the nick name which is being displayed ingame.

Topic: `traze/{instanceName}/join`

Retention: No

Action: `Publish`

Payload:

```
{
 "name": "myIngameNick",
 "mqttClientName": "myClientName"
}
```
If the server accepts your request you’ll receive a message communicating your initial position and a secret token to identify your steering messages. In addition you’ll get a player id (integer) from the game server. You will need the player id in the topic of the steering message.

Topic: `traze/{instanceName}/player/{myClientName}`

Retention: No

Action: `Subscribe`

Payload:
```
{
    "id": 1337,
    "name": "myIngameNick",
    "secretUserToken":"de37c1bc-d0e6-4c66-aaa3-911511f43d54",
    "position": [15,3]
}
```
Because the ingame nick is part of the topic your nickname may not include the following characters `#`, `+`, `/`.

