package iteratec.guiclient.traze;

import iteratec.guiclient.traze.content.Bike;
import iteratec.guiclient.traze.content.Player;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrazeGUIClient extends BasicGame {

    private static final int WINDOW_WIDTH = 806;
    private static final int WINDOW_HEIGHT = 620;
    private static final float GRID_GRAPHIC_WIDTH = 13f;
    private static final float GRID_GRAPHIC_HEIGHT = 10f;
    private static final int Y_OFFSET = 1;
    private static final Color GRID_COLOR = new Color(176, 65, 167, 0.1f);
    private static boolean gameHasBeenStarted;

    private TrazeGUIClient(String title) {
        super(title);
    }

    static void startClient() throws SlickException {
        AppGameContainer appgc = new AppGameContainer(new TrazeGUIClient("MMA Traze Client"));
        appgc.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
        appgc.start();
        Logger.getLogger(TrazeGUIClient.class.getName()).setLevel(Level.OFF);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if (TrazeClient.grid != null) {
            for (int x = 0; x < TrazeClient.grid.getWidth(); x++) {
                for (int y = 0; y < TrazeClient.grid.getHeight(); y++) {
                    if (TrazeClient.grid.getSpawns() != null) {
                        for (Point c : TrazeClient.grid.getSpawns()) {
                            if (c.getX() == x && c.getY() == y) {
                                g.setColor(new Color(255, 255, 255, 0.85f));
                                g.fill(new Rectangle(x * GRID_GRAPHIC_WIDTH, WINDOW_HEIGHT - (y + Y_OFFSET) * GRID_GRAPHIC_HEIGHT, GRID_GRAPHIC_WIDTH, GRID_GRAPHIC_HEIGHT));
                            }
                        }
                        g.setColor(GRID_COLOR);
                        g.draw(new Rectangle(x * GRID_GRAPHIC_WIDTH, WINDOW_HEIGHT - (y + Y_OFFSET) * GRID_GRAPHIC_HEIGHT, GRID_GRAPHIC_WIDTH, GRID_GRAPHIC_HEIGHT));
                    }
                }
            }
        }

        if (TrazeClient.grid != null && TrazeClient.players != null && TrazeClient.grid.getBikes() != null) {
            for (Bike bike : TrazeClient.grid.getBikes()) {
                Color c = getColor(bike.getPlayerId());
                c.a = 0.7f;
                g.setColor(c);
                for (Point point : bike.getTrail()) {
                    g.fill(new Rectangle((int) point.getX() * GRID_GRAPHIC_WIDTH, WINDOW_HEIGHT - (int) (point.getY() + Y_OFFSET) * GRID_GRAPHIC_HEIGHT, 13f, 10f));
                }
                c = getColor(bike.getPlayerId());
                g.setColor(c);
                Player player = findPlayerByBike(bike);
                String nameString;
                if (player != null) {
                    nameString = player.getName();
                    g.setColor(Color.white);
                    if (nameString.length() > 15) {
                        nameString = nameString.substring(0, 16);
                    }
                    g.drawString(nameString, (int) bike.getCurrentLocation().getX() * GRID_GRAPHIC_WIDTH, WINDOW_HEIGHT - (int) bike.getCurrentLocation().getY() * GRID_GRAPHIC_HEIGHT);
                }
                c = getColor(bike.getPlayerId());
                g.setColor(c);
                g.fill(new Rectangle((int) bike.getCurrentLocation().getX() * GRID_GRAPHIC_WIDTH, WINDOW_HEIGHT - (int) (bike.getCurrentLocation().getY() + Y_OFFSET) * GRID_GRAPHIC_HEIGHT, 13f, 10f));
            }
        }

        renderTime(g);

    }

    private void renderTime(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Time alive: " + (int) TrazeClient.time / 1000 + " sec", 10, 30);
    }

    private Player findPlayerByBike(Bike bike) {
        for (Player player : TrazeClient.players) {
            if (bike != null && player != null) {
                if (bike.getPlayerId() == player.getId()) {
                    return player;
                }
            }
        }
        return null;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    }

    private static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16)
        );
    }

    private Color getColor(int playerId) {
        for (Player p : TrazeClient.players) {
            if (p != null) {
                if (playerId == p.getId()) {
                    return (hex2Rgb(p.getColor()));
                }
            }
        }
        return Color.red;
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (gameHasBeenStarted) {
            TrazeClient.time += delta;
        }

        if (container.getInput().isKeyPressed(Input.KEY_A)) {
            parseKeyInput("W");
        } else if (container.getInput().isKeyPressed(Input.KEY_S)) {
            parseKeyInput("S");
        } else if (container.getInput().isKeyPressed(Input.KEY_D)) {
            parseKeyInput("E");
        } else if (container.getInput().isKeyPressed(Input.KEY_W)) {
            parseKeyInput("N");
        } else if (container.getInput().isKeyPressed(Input.KEY_R)) {
            TrazeClient.respawn();
        }
    }

    private void parseKeyInput(String key) {
        TrazeClient.my_current_course = Brain.calculateNextDirection(key);
        TrazeClient.buildSteerMessage();
        if (!gameHasBeenStarted) {
            gameHasBeenStarted = true;
        }
    }

}
