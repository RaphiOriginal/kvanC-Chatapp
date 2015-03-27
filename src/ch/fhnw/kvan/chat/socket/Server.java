package ch.fhnw.kvan.chat.socket;

	import java.net.ServerSocket;
import java.net.Socket;

import ch.fhnw.kvan.chat.general.ChatRoomDriver;

public class Server {
	private static ChatRoomDriver chatRoomDriver = new ChatRoomDriver();
	private static final String host = "localhost";
	private static final int port = 1234;
	private static final int CHAT_SIZE = 3;
	private static int chatParticipant = 0;
	private static ServerSocket server;
	
	public static void main(String[] args) throws Exception{
		chatRoomDriver.connect(host, port);
		server = new ServerSocket(port);
		ConnectionListener t = new ConnectionListener();
		t.start();
		while(chatParticipant < CHAT_SIZE){
			Socket s = server.accept();
			chatParticipant++;
			Thread t2 = new Thread(new ConnectionHandler(s, t));
			t2.start();
		}
	}
}
