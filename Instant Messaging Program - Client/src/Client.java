import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	// constructor
	public Client(String host) {
		super("Instant Messaging Program - Client");
		
		serverIP = host;
		
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(500,350);
		setVisible(true);
		chatWindow.setEditable(false);		
	}
	
	// connect to server (Running the program)
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();			
		}catch(EOFException eofException) {
			showMessage("\n Client ended the connection!");	
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			close();
		}
	}
	
	// connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting connection... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to:" + connection.getInetAddress().getHostName());		
	}

	// set up streams to send and receive messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Your streams are now working! \n" );
	}
	
	// while chatting with the server
	private void whileChatting () throws IOException{
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n String Hack Error!");
			}
		}while(!message.equals("Server - END"));
	}
	
	// close the streams and sockets
	private void close() {
		showMessage("\n Closing the stream...");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	// send messages to server
	private void sendMessage(String message) {
		try {
			output.writeObject("Client - " + message);
			output.flush();
			showMessage("\n Client - " + message);
		}catch(IOException ioException) {
			chatWindow.append("\n Something went wrong! ");
		}
	}
	
	// update chatWindow
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(message);
					}
				}
			);
	}
	
	// gives user permission to type into the text box
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
			);
	}
}
