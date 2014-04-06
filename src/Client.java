import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Client {

	JTextArea incoming_text;
	JTextField outgoing_text;
	BufferedReader read_text;
	PrintWriter write_text;
	Socket socket;

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

	public void start() {
		JFrame frame = new JFrame("KISS Chat");
		JPanel mainPanel = new JPanel();
		incoming_text = new JTextArea(16, 48);
		incoming_text.setLineWrap(true);
		incoming_text.setWrapStyleWord(true);
		incoming_text.setEditable(false);
		JScrollPane scroller = new JScrollPane(incoming_text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing_text = new JTextField(22);

		JButton send_button = new JButton("SEND");
		send_button.addActionListener(new send_listener());
		mainPanel.add(scroller);
		mainPanel.add(outgoing_text);
		mainPanel.add(send_button);

		setupNetwork();

		Thread read_text_thread = new Thread(new Incoming_Reader());
		read_text_thread.start();

		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(600, 400);
		frame.setVisible(true);

	}

	private void setupNetwork() {

		try {
			socket = new Socket("localhost", 4200);
			InputStreamReader reader = new InputStreamReader(
					socket.getInputStream());
			read_text = new BufferedReader(reader);
			write_text = new PrintWriter(socket.getOutputStream());
			System.out.println("Connnnnnnected!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public class send_listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				write_text.println(outgoing_text.getText());
				write_text.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			outgoing_text.setText("");
			outgoing_text.requestFocus();
		}
	}
	
	public class Incoming_Reader implements Runnable{
		public void run(){
			String msg;
			try{
				while((msg = read_text.readLine()) != null) {
					System.out.println("read " + msg);
					incoming_text.append(msg + "\n");
				}
			} catch(Exception ex) {ex.printStackTrace();}
		}
	}
}

