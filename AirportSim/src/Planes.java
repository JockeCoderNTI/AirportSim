import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Planes {
    private int x;
    private int y;
    private Color topColor;
    private Color bottomColor;
    private AirportDisplay assignedAirport;
    private ArrayList<String> destinations;
    private ArrayList<String> route;
    private int currentDestinationIndex;
    private int serviceTime;  // Service time in seconds
    private int flyTime;      // Fly time in seconds
    private int fuelTime;     // Fuel time in seconds
    private int currentTime;  // Current time for the plane
    private int takeoffTime;  // Time at which the plane took off

    // States for the plane
    private enum State {
        WAITING_FOR_TAKE_OFF_PERMIT,
        TAKE_OFF,
        FLYING,
        WAITING_FOR_LANDING_PERMIT,
        SERVICE
    }

    private State currentState;

    public Planes(int x, int y, int serviceTime, int flyTime, int fuelTime) {
        this.x = x;
        this.y = y;
        this.topColor = generateRandomColor();
        this.bottomColor = generateRandomColor();
        this.destinations = new ArrayList<>();
        this.route = new ArrayList<>();
        this.currentDestinationIndex = 0;
        this.assignedAirport = null;
        this.serviceTime = serviceTime;
        this.flyTime = flyTime;
        this.fuelTime = fuelTime;
        this.currentTime = 0;
        this.currentState = State.WAITING_FOR_TAKE_OFF_PERMIT;
        this.takeoffTime = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getTopColor() {
        return topColor;
    }

    public Color getBottomColor() {
        return bottomColor;
    }

    public AirportDisplay getAssignedAirport() {
        return assignedAirport;
    }

    public void setAssignedAirport(AirportDisplay assignedAirport) {
        this.assignedAirport = assignedAirport;
    }

    public void setDestinations(ArrayList<String> destinations) {
        this.destinations = destinations;
    }

    public void setRoute(ArrayList<String> route) {
        this.route = route;
    }

    public void setCurrentDestinationIndex(int index) {
        this.currentDestinationIndex = index;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public boolean hasTakenOff() {
        return currentState == State.FLYING || currentState == State.WAITING_FOR_LANDING_PERMIT;
    }

    public void move() {
        currentTime++;

        switch (currentState) {
            case WAITING_FOR_TAKE_OFF_PERMIT:
                if (currentTime >= serviceTime) {
                    currentState = State.TAKE_OFF;
                    currentTime = 0;
                }
                break;
            case TAKE_OFF:
                if (currentTime >= flyTime) {
                    currentState = State.FLYING;
                    takeoffTime = 0;
                    currentTime = 0;
                }
                break;
            case FLYING:
                if (assignedAirport != null && currentDestinationIndex < route.size()) {
                    String currentDestination = route.get(currentDestinationIndex);
                    Point destinationPoint = assignedAirport.getAirportPosition();

                    int deltaX = destinationPoint.x - x;
                    int deltaY = destinationPoint.y - y;
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    double speed = 100.0 / 60.0; // 100 pixels per 60 seconds

                    if (distance > speed) {
                        double ratio = speed / distance;
                        x += (int) (ratio * deltaX);
                        y += (int) (ratio * deltaY);
                    } else {
                        x = destinationPoint.x;
                        y = destinationPoint.y;
                        currentDestinationIndex++;
                    }
                } else if (currentDestinationIndex >= route.size()) {
                    currentState = State.WAITING_FOR_LANDING_PERMIT;
                    currentTime = 0;
                }
                break;
            case WAITING_FOR_LANDING_PERMIT:
                if (currentTime >= flyTime) {
                    currentState = State.SERVICE;
                    currentTime = 0;
                }
                break;
            case SERVICE:
                if (currentTime >= serviceTime) {
                    currentState = State.WAITING_FOR_TAKE_OFF_PERMIT;
                    currentTime = 0;
                    // Reset destination index for next take-off
                    currentDestinationIndex = 0;
                    // Generate a new random route
                    route = generateRandomRoute(assignedAirport);
                }
                break;
        }
    }

    private ArrayList<String> generateRandomRoute(AirportDisplay airport) {
        ArrayList<String> randomRoute = new ArrayList<>();
        int numAirports = airport.getAirports().size();

        Random random = new Random();
        for (int i = 0; i < numAirports - 1; i++) {
            AirportDisplay.Airport randomAirport;
            do {
                randomAirport = airport.getAirports().get(random.nextInt(numAirports));
            } while (randomAirport == assignedAirport);

            randomRoute.add("Airport" + randomAirport.getID());
        }

        return randomRoute;
    }


    private Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
