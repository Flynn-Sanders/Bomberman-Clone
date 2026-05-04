import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.*;


public class ClientScreen extends JPanel implements ActionListener {


    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton readyButton;


    
    Boolean isGuesser;
    String animal;

    public ClientScreen() {
        this.setLayout(null);

        readyButton = new JButton("Ready");
        readyButton.setBounds(300, 620, 100, 30);
        readyButton.addActionListener(this);
        this.add(readyButton);

        this.setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readyButton) {
            try {
                out.writeObject("READY");
                out.flush();
                readyButton.setEnabled(false); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void connect() {
        try {
            Socket socket = new Socket("localhost", 1024);
            
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to the chat server!");

            String username = JOptionPane.showInputDialog("Enter username:");
            out.writeObject("JOIN:" + username);

            while (true) {
                
            }
            
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
    
    


