package iteratec.guiclient.traze;

import com.fasterxml.jackson.databind.ObjectMapper;
import iteratec.guiclient.traze.content.Bike;
import iteratec.guiclient.traze.content.Grid;
import iteratec.guiclient.traze.content.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.io.IOException;

public class TrazeClient {

    static Grid grid;
    static Player[] players;
    static String my_current_course;
    static Point my_current_location;
    static Point[] my_trail;
    private static String myPlayerToken;
    private static int myPlayerId;
    private static ObjectMapper objectMapper = new ObjectMapper();
    static float time;
    private static BrokerClient brokerClient = new BrokerClientImpl();

    public static void main(String[] args) throws SlickException {
        brokerClient.init("tcp://traze.iteratec.de:1883");
        TrazeGUIClient.startClient();
    }

    static void respawn() {
        if (!playerAlive()) {
            brokerClient.join();
        }
    }

    static void updateGrid(String gridString) {
        JSONObject gridJson = new JSONObject(gridString);
        JSONArray tilesJsonArray = (JSONArray) gridJson.get("tiles");

        int height = tilesJsonArray.length();
        int width = ((JSONArray) tilesJsonArray.get(0)).length();

        int[][] tiles = new int[width][height];

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tiles[x][y] = (int) ((JSONArray) tilesJsonArray.get(x)).get(y);
            }
        }

        JSONArray bikesJsonArray = (JSONArray) gridJson.get("bikes");
        Bike[] bikes = new Bike[bikesJsonArray.length()];

        for (int j = 0; j < bikesJsonArray.length(); ++j) {
            Bike bike;
            int currentPlayerId;
            Point currentLocation;
            String direction;
            Point[] trail;

            JSONObject bikeJson = (JSONObject) bikesJsonArray.get(j);
            currentPlayerId = (int) bikeJson.get("playerId");

            int x = (int) ((JSONArray) bikeJson.get("currentLocation")).get(0);
            int y = (int) ((JSONArray) bikeJson.get("currentLocation")).get(1);
            currentLocation = new Point(x, y);

            direction = (String) bikeJson.get("direction");
            int trailLength = ((JSONArray) bikeJson.get("trail")).length();
            trail = new Point[trailLength];

            for (int i = 0; i < trailLength; ++i) {
                JSONArray trailJson = (JSONArray) ((JSONArray) bikeJson.get("trail")).get(i);
                trail[i] = new Point((int) trailJson.get(0), (int) trailJson.get(1));
            }

            if (currentPlayerId == myPlayerId) {
                my_current_location = currentLocation;
                my_trail = trail;
            }

            bike = new Bike(currentPlayerId, currentLocation, direction, trail);
            bikes[j] = bike;
        }

        JSONArray spawnJsonArray = (JSONArray) gridJson.get("spawns");
        Point[] spawns = new Point[spawnJsonArray.length()];

        for (int k = 0; k < spawnJsonArray.length(); ++k) {
            JSONArray currentArray = (JSONArray) spawnJsonArray.get(k);
            spawns[k] = new Point((int) (currentArray.get(0)), (int) (currentArray.get(1)));
        }

        TrazeClient.grid = new Grid(height, width, tiles, bikes, spawns);
        Brain.calculateNextDirection();
    }

    static void updateAllPlayers(String playersString) {
        try {
            players = objectMapper.readValue(playersString, Player[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean playerAlive() {
        for (Player player : players) {
            if (player.getId() == myPlayerId)
                return true;
        }
        time = 0;
        return false;
    }

    static Point getCurrentBikeLocation() {
        for (Bike bike : grid.getBikes()) {
            if (bike.getPlayerId() == myPlayerId) {
                return bike.getCurrentLocation();
            }
        }
        return null;
    }

    static void initPlayer(String playerJsonString) {
        JSONObject player = new JSONObject(playerJsonString);
        myPlayerId = (int) player.get("id");
        myPlayerToken = player.get("secretUserToken").toString();
        System.out.println("Spieler " + player.get("name") + " erfolgreich registriert!");
    }

    static void buildSteerMessage() {
        if (myPlayerToken != null && my_current_course != null) {
            String messageString = " {\"course\":\"" + my_current_course + "\", \"playerToken\": \"" + myPlayerToken + "\" }";
            String topic = "traze/" + brokerClient.getInstanceId() + "/" + myPlayerId + "/steer";
            brokerClient.publishMessage(topic, messageString);
        }
    }
}
