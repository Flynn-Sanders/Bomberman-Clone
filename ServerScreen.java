import javax.swing.*;

import java.awt.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerScreen extends JPanel {


	private JTextField textInput;
	private JTextArea chatArea;


	public ServerScreen(){


		this.setLayout(null);
		 
		chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBounds(10, 10, 300, 500);
        this.add(chatArea);

		this.setFocusable(true);

		
	}

	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	} 

	public void addMessage(String msg){
		chatArea.append(msg + "\n");
	}

	public void startServer() throws IOException {
		int portNumber = 1024;
		String message;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		
		while(true){
			System.out.println("Waiting for a connection...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Got connection!");

			Manager mg = new Manager(this);
		
			ServerThread st = new ServerThread(clientSocket, mg);
			mg.addThread(st);

			Thread thread = new Thread(st);
			thread.start();
			
		}
		
	}

}



