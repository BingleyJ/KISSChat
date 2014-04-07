import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Client {
	// change username
	JFrame nameFrame = new JFrame("Change Username");
	JPanel namePanel = new JPanel();
	JTextField nameTxt = new JTextField(12);
	JButton nameButton = new JButton("Name");
	//eoChange Username
	
	JTextArea incoming_text;
	JTextField outgoing_text;
	BufferedReader read_text;
	PrintWriter write_text;
	Socket socket;

	String name = "unknown user"; // users name

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

	public void getName() {

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
		JButton name_button = new JButton("Change Username");

		send_button.addActionListener(new send_listener());
		name_button.addActionListener(new name_listener());

		mainPanel.add(name_button);

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
			socket = new Socket("localhost", 5500);
			InputStreamReader reader = new InputStreamReader(
					socket.getInputStream());
			read_text = new BufferedReader(reader);
			write_text = new PrintWriter(socket.getOutputStream());
			System.out.println("Connnnnnnected!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public class name_listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {

				nameButton.addActionListener(new nameChange_listener());
				namePanel.add(nameButton);
				namePanel.add(nameTxt);
				nameFrame.getContentPane().add(BorderLayout.CENTER, namePanel);
				nameFrame.setSize(250, 100);
				nameFrame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			outgoing_text.setText("");
			outgoing_text.requestFocus();
		}
	}

	public class nameChange_listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				name = nameTxt.getText();
				write_text.flush();
				nameFrame.setVisible(false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public class send_listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				write_text.println("[" + name + "] " + outgoing_text.getText());
				write_text.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			outgoing_text.setText("");
			outgoing_text.requestFocus();
		}
	}

	public class Incoming_Reader implements Runnable {
		public void run() {
			String msg;
			try {
				while ((msg = read_text.readLine()) != null) {
					System.out.println("read " + msg);
					incoming_text.append(msg + "\n");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
