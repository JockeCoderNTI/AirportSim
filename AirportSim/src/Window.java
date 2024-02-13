import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Window {
   // Define the GUI components
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JPanel controlPanel;
   private JLabel statusLabel;
   private GridBagConstraints gbc = new GridBagConstraints();

   // Define a static method to generate and show the window
   public static void generateWindow() {
      Window swingControlDemo = new Window();  
      swingControlDemo.prepareGUI();
      swingControlDemo.showEventDemo();       
   }

   // Define a method to initialize the GUI components
   private void prepareGUI(){
      mainFrame = new JFrame("Control in action: Button");
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Get the maximum bounds for a window on the screen
      Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
      
      // Set the size of the window to the maximum for each screen
      mainFrame.setSize(maxBounds.width, maxBounds.height);
      mainFrame.setLayout(new BorderLayout());
    

      
      headerLabel = new JLabel("", JLabel.CENTER);
      statusLabel = new JLabel("", JLabel.CENTER);
      
      
      controlPanel = new JPanel(new GridBagLayout());
   }

   // Define a method to show the window and set up the buttons
   private void showEventDemo(){
      headerLabel.setText("Menu for AirportSimulator Game"); 

      // Create the buttons and set their labels
      JButton playButton = new JButton("Play AirportSimulator");
      JButton howtoplayButton = new JButton("How to play");
      JButton creditsButton = new JButton("Credits");
      JButton exitButton = new JButton("Exit Game");

      // Set the action commands for each button
      playButton.setActionCommand("Play AirportSimulator");
      howtoplayButton.setActionCommand("How to play");
      creditsButton.setActionCommand("Credits");
      exitButton.setActionCommand("Exit Game");

      // Set the font size for the buttons
      playButton.setFont(new Font("Serif", Font.BOLD, 24));
      howtoplayButton.setFont(new Font("Serif", Font.BOLD, 24));
      creditsButton.setFont(new Font("Serif", Font.BOLD, 24));
      exitButton.setFont(new Font("Serif", Font.BOLD, 24));
      
      // Add the buttons to the control panel
      playButton.addActionListener(new ButtonClickListener()); 
      howtoplayButton.addActionListener(new ButtonClickListener()); 
      creditsButton.addActionListener(new ButtonClickListener()); 
      exitButton.addActionListener(new ButtonClickListener());
      
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 0.2;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      controlPanel.add(playButton, gbc);

      gbc.gridx = 1;
      gbc.weightx = 0.2;
      controlPanel.add(howtoplayButton, gbc);

      gbc.gridx = 2;
      gbc.weightx = 0.2;
      controlPanel.add(creditsButton, gbc);
      
      gbc.gridx = 3;
      gbc.weightx = 0.2;
      controlPanel.add(exitButton, gbc);

      // Add the components to the main frame
      mainFrame.add(headerLabel, BorderLayout.NORTH);
      mainFrame.add(controlPanel, BorderLayout.CENTER);
      mainFrame.add(statusLabel, BorderLayout.SOUTH);
      
      // Make the window visible
      mainFrame.setVisible(true);  
   }

   private class ButtonClickListener implements ActionListener{
       public void actionPerformed(ActionEvent e) {
           String command = e.getActionCommand();  
           
        // Inside the actionPerformed method
           if (command.equals("Play AirportSimulator")) {
               statusLabel.setText("Play AirportSimulator Button clicked.");
               JFrame airportFrame = new JFrame("Airport Display");
               airportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

               // Get the default screen device
               GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

               // Get the maximum bounds of the default screen device
               int maxWidth = defaultScreenDevice.getDisplayMode().getWidth();
               int maxHeight = defaultScreenDevice.getDisplayMode().getHeight();

               airportFrame.setSize(maxWidth, maxHeight);
               AirportDisplay airportDisplay = new AirportDisplay();
               
               // Set black background color
               airportDisplay.getDisplayComponent().setBackground(Color.BLACK);
               
               airportFrame.add(airportDisplay.getDisplayComponent(), BorderLayout.CENTER);
               airportFrame.setVisible(true);
           
            } else if( command.equals( "How to play" ) )  {
               statusLabel.setText("1:	Press ´Play AirportSimulator Button´" + " 2:	Press ESC to come to this menu" + (" 3.	Enjoy")); 
            } else if( command.equals( "Credits" ) ) {
               statusLabel.setText("Credits To; Joakim J. Sten K. Simon F.");
            } else if( command.equals( "Exit Game" ) ) {
               System.exit(0);
            }      
         }        
      }
}



