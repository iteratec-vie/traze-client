package iteratec.guiclient.traze;

class Brain {

    private static int xMax = 61;
    private static int yMax = 61;

    Brain(int xMax, int yMax) {
        Brain.xMax = xMax;
        Brain.yMax = yMax;
    }

    static String calculateNextDirection(String wantedDirection) {
        if (TrazeClient.my_current_course != null) {
            switch (TrazeClient.my_current_course) {
                case "N":
                    if (wantedDirection.equals("S")) {
                        return "N";
                    }
                    break;
                case "E":
                    if (wantedDirection.equals("W")) {
                        return "E";
                    }
                    break;
                case "S":
                    if (wantedDirection.equals("N")) {
                        return "S";
                    }
                    break;
                case "W":
                    if (wantedDirection.equals("E")) {
                        return "W";
                    }
                    break;
            }
        }
        return wantedDirection;
    }

    static void calculateNextDirection() {
        if (TrazeClient.my_current_location != null && TrazeClient.my_current_course != null && TrazeClient.my_trail != null) {
            TrazeClient.buildSteerMessage();
        }
    }

    private static boolean northIsFree() {
        try {
            return TrazeClient.grid.getTiles()[TrazeClient.my_current_location.x][TrazeClient.my_current_location.y + 1] == 0 && TrazeClient.my_current_location.y < yMax;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static boolean southIsFree() {
        try {
            return TrazeClient.grid.getTiles()[TrazeClient.my_current_location.x][TrazeClient.my_current_location.y - 1] == 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static boolean eastIsFree() {
        try {
            return TrazeClient.grid.getTiles()[TrazeClient.my_current_location.x + 1][TrazeClient.my_current_location.y] == 0 && TrazeClient.my_current_location.x < xMax;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static boolean westIsFree() {
        try {
            return TrazeClient.grid.getTiles()[TrazeClient.my_current_location.x - 1][TrazeClient.my_current_location.y] == 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
