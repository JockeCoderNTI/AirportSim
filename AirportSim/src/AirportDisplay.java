import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class AirportDisplay {
    private JPanel panel;
    private ArrayList<Airport> airports;
    private ArrayList<Planes> planes;
    private int airportIDCounter = 1;  // Counter for assigning IDs to airports

    private int maxPlanes = 12; // Maximum number of planes per airport

    private int secondsElapsed = 0; // Variable to keep track of elapsed seconds
    private Timer timer; // Timer for updating planes and tracking time

    private ArrayList<Object> allObjects; // New field to store all objects

    // Updated constructor to accept allObjects
    public AirportDisplay(ArrayList<Object> allObjects) {
        this.allObjects = allObjects;
        airports = new ArrayList<>();
        planes = new ArrayList<>();
    }

    public void addPlane(Planes plane) {
        planes.add(plane);
    }

    private void updatePlanes() {
        for (Planes plane : planes) {
            plane.move();
        }
        panel.repaint();
        secondsElapsed++; // Increment seconds on each update
    }

    public void startSimulation() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlanes();
            }
        });
    }

    public JPanel getDisplayComponent() {
        if (panel == null) {
            panel = new JPanel() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    // Display the timer at the top of the screen with white text
                    g.setColor(Color.WHITE);
                    g.drawString(formatTime(secondsElapsed), 10, 20);

                    for (Airport airport : airports) {
                        g.setColor(airport.getColor());
                        g.fillRect(airport.getX(), airport.getY(), 20, 20);
                        // Display number of planes at the airport
                        g.drawString(String.valueOf(airport.getNumberOfPlanes()), airport.getX() + 10, airport.getY() + 15);
                    }
                    for (Planes plane : planes) {
                        g.setColor(plane.getTopColor());
                        g.fillOval(plane.getX(), plane.getY(), 20, 10);
                        g.setColor(plane.getBottomColor());
                        g.fillOval(plane.getX(), plane.getY() + 10, 20, 10);
                    }
                }
            };

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    boolean canSpawn = true;
                    Airport clickedAirport = null;

                    if (airports.isEmpty()) {
                        // Start the timer when the first airport is spawned
                        startSimulation();
                        timer.start();
                    }

                    for (Airport airport : airports) {
                        if (Math.abs(airport.getX() - x) <= 100 && Math.abs(airport.getY() - y) <= 100) {
                            canSpawn = false;
                            clickedAirport = airport;
                            break;
                        }
                    }
                    if (canSpawn) {
                        Color color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
                        Airport airport = new Airport(x, y, color, airportIDCounter++);
                        airports.add(airport);
                        panel.repaint();
                    } else {
                        showAirportInfo(clickedAirport);
                    }
                }
            });

            panel.setFocusable(true);
            panel.requestFocusInWindow();
            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            increasePlanes();
                            break;
                        case KeyEvent.VK_DOWN:
                            decreasePlanes();
                            break;
                    }
                }
            });
        }

        return panel;
    }

    private void showAirportInfo(Airport airport) {
        StringBuilder message = new StringBuilder();

        // Display information about the airport
        message.append("Airport Name: ").append("Airport: ").append(airport.getID()).append("\n");

        // Increment the number of planes if it's the first plane in the airport
        if (airport.getNumberOfPlanes() == 0) {
            airport.incrementNumberOfPlanes();
        }

        message.append("Number of Planes: ").append(airport.getNumberOfPlanes()).append("\n");

        // Display information about the routes for each plane
        message.append("\nRoutes:\n");
        int numAirports = airports.size();
        for (int i = 1; i <= airport.getNumberOfPlanes(); i++) {
            message.append("Plane ").append(i).append(": ").append(generateRandomRoute(numAirports, airport)).append("\n");
        }

        JOptionPane.showMessageDialog(panel, message.toString(), "Airport Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private String generateRandomRoute(int numAirports, Airport currentAirport) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < numAirports - 1; i++) {
            Airport randomAirport;
            do {
                randomAirport = airports.get(random.nextInt(numAirports));
            } while (randomAirport == currentAirport);
            route.append("Airport").append(randomAirport.getID()).append(" ");
        }
        return route.toString().trim();
    }

    private void increasePlanes() {
        for (Airport airport : airports) {
            if (airport.isSelected() && airport.getNumberOfPlanes() < maxPlanes) {
                airport.incrementNumberOfPlanes();
                panel.repaint();
                break; // Only one airport can be selected at a time
            }
        }
    }

    private void decreasePlanes() {
        for (Airport airport : airports) {
            if (airport.isSelected() && airport.getNumberOfPlanes() > 0) {
                airport.decrementNumberOfPlanes();
                panel.repaint();
                break; // Assuming only one airport can be selected at a time
            }
        }
    }

    public class Airport {
        private int x;
        private int y;
        private Color color;
        private int numberOfPlanes;
        private boolean selected;
        private int id;

        public Airport(int x, int y, Color color, int id) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.numberOfPlanes = 5;
            this.selected = false;
            this.id = id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Color getColor() {
            return color;
        }

        public int getNumberOfPlanes() {
            return numberOfPlanes;
        }

        public void setNumberOfPlanes(int numberOfPlanes) {
            this.numberOfPlanes = numberOfPlanes;
        }

        public Point getAirportPosition() {
            return new Point(x, y);
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isSelected() {
            return selected;
        }

        public void incrementNumberOfPlanes() {
            numberOfPlanes++;
        }

        public void decrementNumberOfPlanes() {
            numberOfPlanes--;
        }

        public int getID() {
            return id;
        }
    }

    // Format seconds to HH:mm:ss
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
