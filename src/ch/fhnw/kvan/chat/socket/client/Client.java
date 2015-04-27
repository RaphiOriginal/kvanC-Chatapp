package ch.fhnw.kvan.chat.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;

public class Client implements IChatRoom{
	private String username;
	private ClientGUI clientGUI;
	private Socket socket;
	private Out out;
	private In in;
	private ListenThread thread;
	
	private static Logger logger;
	
	
	public Client(String username, String host, String port) throws NumberFormatException, UnknownHostException, IOException{
		logger = Logger.getLogger(Client.class);
		this.username = username;
		logger.trace("Try to connect to Host: " + host + " Port: " + port);
		socket = new Socket(host, Integer.parseInt(port));
		in = new In(socket);
		out = new Out(socket);
	}
	
	private void createGUIandListen() throws IOException{
		clientGUI = new ClientGUI(this, username);
		thread = new ListenThread(this, in);
		thread.start();
		initialStart();
	}
	
	public static void main(String[] args){
		try {
			Client client = new Client(args[0], args[1], args[2]);
			client.createGUIandListen();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean addParticipant(String name) throws IOException {
		out.println("name=" + name);
		return true;
	}

	@Override
	public boolean removeParticipant(String name) throws IOException {
		out.println("remove_name=" + name);
		return true;
	}

	@Override
	public boolean addTopic(String topic) throws IOException {
		out.println("add_topic=" + topic);
		return true;
	}

	@Override
	public boolean removeTopic(String topic) throws IOException {
		out.println("remove_topic=" + topic);
		return true;
	}

	@Override
	public boolean addMessage(String topic, String message) throws IOException {
		out.println("message=" + message + ";" + topic);
		return true;
	}

	@Override
	public String getMessages(String topic) throws IOException {
		out.println("action=getMessages;topic=" + topic);
		return "";
	}

	@Override
	public String refresh(String topic) throws IOException {
		return getMessages(topic);
	}
	public void initialStart() throws IOException {
		addParticipant(username);
		out.println("initial_topic");
		out.println("initial_name");
	}
	
	public ClientGUI getClientGUI(){
		return clientGUI;
	}
}
