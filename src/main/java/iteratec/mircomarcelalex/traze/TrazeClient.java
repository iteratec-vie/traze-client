package iteratec.mircomarcelalex.traze;

import iteratec.mircomarcelalex.traze.content.Bike;
import iteratec.mircomarcelalex.traze.content.Coordination2D;
import iteratec.mircomarcelalex.traze.content.Grid;
import iteratec.mircomarcelalex.traze.content.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

public class TrazeClient extends BasicGame {

    private static BrokerClient bc;
    private static Grid grid;
    private static Player[] players;
    private static AppGameContainer appgc;

    public TrazeClient(String title) {
        super(title);
    }

    public static void main(String[] args) throws SlickException {
        bc = new BrokerClient();

//        appgc = new AppGameContainer(new TrazeClient("MMA Traze Client"));
//        appgc.setDisplayMode(800, 600, false);
//        appgc.start();

    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if (grid != null) {
            for (int x = 0; x < grid.getGridWidth(); x++) {
                for (int y = 0; y < grid.getGridHeight(); y++) {
                    g.setColor(Color.green);
                    g.draw(new Rectangle(x, y, 800 / grid.getGridWidth(), 600 / grid.getGridWidth()));
                }
            }
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        // TODO Auto-generated method stub

    }

    public static void setGrid(String gridString) {
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
            int playerId;
            Coordination2D currentLocation;
            String direction;
            Coordination2D[] trail;

            JSONObject bikeJson = (JSONObject) bikesJsonArray.get(j);
            playerId = (int) bikeJson.get("playerId");

            int x = (int) ((JSONArray) bikeJson.get("currentLocation")).get(0);
            int y = (int) ((JSONArray) bikeJson.get("currentLocation")).get(1);
            currentLocation = new Coordination2D(x, y);

            direction = (String) bikeJson.get("direction");
            int trailLength = ((JSONArray) bikeJson.get("trail")).length();
            trail = new Coordination2D[trailLength];

            for (int i = 0; i < trailLength; ++i) {
                JSONArray trailJson = (JSONArray) ((JSONArray) bikeJson.get("trail")).get(i);
                trail[i] = new Coordination2D((int) trailJson.get(0), (int) trailJson.get(1));
            }

            bike = new Bike(playerId, currentLocation, direction, trail);
            bikes[j] = bike;
        }

        JSONArray spawnJsonArray = (JSONArray) gridJson.get("spawns");
        Coordination2D[] spawns = new Coordination2D[spawnJsonArray.length()];

        for (int k = 0; k < spawnJsonArray.length(); ++k) {
            spawns[k] = new Coordination2D((int) spawnJsonArray.get(0), (int) spawnJsonArray.get(1));
        }

        grid = new Grid(tiles, bikes, spawns);
    }

    public static void setPlayers(String playersString) {
        JSONArray playersJsonArray = new JSONArray(playersString);
        System.out.println(playersJsonArray);

        for (int i = 0; i < playersJsonArray.length(); ++i) {
            JSONObject playerJson = (JSONObject) playersJsonArray.get(i);
            Player player = new Player();
            player.setId((int) playerJson.get("id"));
            player.setName((String) playerJson.get("name"));
            player.setColor((String) playerJson.get("color"));
            player.setFrags((int) playerJson.get("frags"));
            player.setColor((String) playerJson.get("owned"));
            players[i] = player;
        }
    }
}
