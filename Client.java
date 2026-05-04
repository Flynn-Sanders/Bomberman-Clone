import java.io.*;
import javax.swing.JFrame;

public class Client {
    public static void main(String args[]) throws IOException {

        // Create a new JFrame window with title "Client"
        JFrame frame = new JFrame("Client");

        // Create an instance of ClientScreen (custom JPanel for rendering)
        ClientScreen sc = new ClientScreen();
        frame.add(sc);

        // Configure window to close the application when the close button is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Pack the window to fit the preferred size of its components
        frame.pack();

        // Make the window visible
        frame.setVisible(true);

        // Run the connection on a separate thread so the GUI doesn't freeze!
        new Thread(new Runnable() {
            public void run() {
                sc.connect();
            }
        }).start();
    }
}