package edu.csula.cs470;

import java.io.DataOutputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Chat {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		List<Socket> sockets = new ArrayList<Socket>();
		final int port = Integer.parseInt(args[0]);
		OutputStream outStream = null;
		DataOutputStream outDataStream = null;
		String message = "";

		do {
			BufferedReader dis = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("Enter your Command: ");
			message = dis.readLine();
			if (message.equals("list")) {
				System.out.println("id: \t IP Address \t Port No.");
				for (int i = 0; i < sockets.size(); i++) {
					System.out.println((i + 1) + ": \t"
							+ sockets.get(i).getInetAddress() + "\t"
							+ sockets.get(i).getPort());
				}

			} else if (message.equals("myip")) {
				System.out.println(InetAddress.getLocalHost().toString());
			} else if (message.equals("myport")) {
				System.out.println(port);
			} else if (message.equals("help")) {
				dealwithelp();
			} else if (message.startsWith("connect")) {
				dealwithConnect(message, sockets, outStream, outDataStream,
						message);
			} else if (message.startsWith("send")) {
				dealwithsend(message, sockets, outStream, outDataStream,
						message);
			} else if (message.startsWith("terminate")) {
				dealwithterminate(message, sockets);
			} else if (message.startsWith("exit")) {
				System.out.print("Good Bye!");
				System.exit(0);
			} else {
				System.out.print("Invalid Command");
			}
		} while (!message.equals("exit"));
	}

	/**
	 * This method is used to deal with terminate command
	 * 
	 * @param message
	 * @param sockets
	 */
	private static void dealwithterminate(String message, List<Socket> sockets) {
		String serverDetail[] = message.split(" ");
		int socketId = Integer.parseInt(serverDetail[1]);

		if (socketId <= sockets.size() && socketId > 0) {
			sockets.remove(socketId - 1);
			System.out.print("Terminated!! \n"); 
		} else {
			System.out.print("Invalid Sender list id");
		}

	}

	/**
	 * This method is used to deal with send command
	 */

	private static void dealwithsend(String message, List<Socket> sockets,
			OutputStream outStream, DataOutputStream outDataStream, String port) {
		try {
			String messageDetail[] = message.split(" ");
			int socketId = Integer.parseInt(messageDetail[1]);

			message = "";
			for (int i = 2; i < messageDetail.length; i++) {
				message += messageDetail[i] + " ";
			}
			message = message.trim();

			if (socketId <= sockets.size() && socketId > 0) {
				outStream = sockets.get(socketId - 1).getOutputStream();
				outDataStream = new DataOutputStream(outStream);

				String messageBody = "Message recieved from "
						+ InetAddress.getLocalHost().toString();
				messageBody += "\n Sender's Port : " + port;
				messageBody += "\n Mesasage : " + message;
				outDataStream.writeUTF(messageBody);
			} else {
				System.out.print("Invalid Sender id.");
			}
		} catch (Exception e) {
			System.out.println("Some error occured. Please try again");
		}

	}

	/**
	 * This method is used to deal with help command Prints all the necessary
	 * messages on help command
	 */
	private static void dealwithelp() {
		System.out.println("1 ) myip - to see your ip address.");
		System.out.println("2 ) myport - to see your port number.");
		System.out.println("3 ) connect <ip> <port> - connect to peer.");
		System.out.println("4 ) list Command - list all the connected peer/peers.");
		System.out.println("5 ) send <id> - to send message to peer.");
		System.out.println("6 ) terminate <id> - terminate the connection");
		System.out.println("7 ) exit - exit the program.");
	}

	/**
	 * This method is used to deal with connect command
	 * 
	 * @param message
	 * @param sockets
	 * @param outStream
	 * @param outDataStream
	 * @param port
	 */

	private static void dealwithConnect(String message, List<Socket> sockets,
			OutputStream outStream, DataOutputStream outDataStream, String port) {

		String serverDetail[] = message.split(" ");
		String host = serverDetail[1];
		int secondPort = Integer.parseInt(serverDetail[2]);
		try {
			Socket sock = new Socket(host, secondPort);
			sock.getInetAddress();
			sockets.add(sock);
			System.out.println("Connection successfully established to : "
					+ host); // connecting to peer and printing
								// successful message.

			outStream = sock.getOutputStream();
			outDataStream = new DataOutputStream(outStream);
			String messageBody = "Connected: \n IP Address : "
					+ InetAddress.getLocalHost().toString() + "\n Port : "
					+ port;
			outDataStream.writeUTF(messageBody); 

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
};
