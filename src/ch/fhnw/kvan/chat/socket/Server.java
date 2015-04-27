package ch.fhnw.kvan.chat.socket;

import java.net.ServerSocket;
import java.net.Socket;

import ch.fhnw.kvan.chat.general.ChatRoomDriver;

public class Server {
	private static ChatRoomDriver chatRoomDriver = new ChatRoomDriver();
	private static final String host = "localhost";
	private static final int port = 1235;
	private static ServerSocket server;
	
	public static void main(String[] args) throws Exception{
		chatRoomDriver.connect(host, port);
		server = new ServerSocket(port);
		ConnectionListener t = new ConnectionListener();
		t.start();
		while(true){
			Socket s = server.accept();
			ConnectionHandler t2 = new ConnectionHandler(s, t);
			t2.start();
			t.addHandler(t2);
		}
	}
}
