package ch.fhnw.kvan.chat.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;

public class ConnectionHandler extends Thread{
	private Socket s;
	private ConnectionListener cl;
	private ChatRoom chatRoom = ChatRoom.getInstance();
	private Out out;
	private In in;
	private Logger logger;
	
	
	public ConnectionHandler(Socket s, ConnectionListener cl){
		logger = Logger.getLogger(ConnectionHandler.class);
		this.s = s;
		this.cl = cl;
		out = new Out(s);
		in = new In(s);
	}
	public void run(){
		try {
			out.println(chatRoom.getParticipants());
			out.println(chatRoom.getTopics());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			String incomingMsg = in.readLine();
			if(incomingMsg != null){
				handleMessage(incomingMsg);
			} else {
				logger.error("Something is wrong, a null string received");
				break;
			}
		}
	}
	public void handleMessage(String msg){
		logger.trace("Handle message: " + msg);
		String[] msgArray = msg.split("=");
		
		switch (msgArray[0]){
		case "name":
			registerUsername(msgArray[1]);
			break;
		case "remove_name":
			removeUser(msgArray[1]);
		case "message":
			String[] outMsg = msgArray[1].split(";");
			sendMessage(outMsg[0], outMsg[1]);
			break;
		case "add_topic":
			addTopic(msgArray[1]);
			break;
		case "remove_topic":
			removeTopic(msgArray[1]);
			break;
		case "action":
			getMessages(msgArray[2]);
			break;
		case "initial_topic":
			getTopics();
			break;
		case "initial_name":
			getNames();
			break;
		}
		
	}
	public void sendMessage(String msg, String topic){
		try {
			chatRoom.addMessage(topic, msg);
			cl.addMessageToAll("message=" + msg + ";" + topic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerUsername(String name){
		try {
			chatRoom.addParticipant(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cl.addMessageToAll("add_participant=" + name);
	}
	public void removeUser(String name){
		try {
			chatRoom.removeParticipant(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cl.addMessageToAll("remove_participant=" + name);
	}
	public void getTopics(String topics){
		try {
			out.println(chatRoom.getTopics());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addTopic(String topic){
		try {
			chatRoom.addTopic(topic);
			cl.addMessageToAll("add_topic=" + topic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void removeTopic(String topic){
		try {
			chatRoom.removeTopic(topic);
			cl.addMessageToAll("remove_topic="+topic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getMessages(String topic){
		try {
			out.println(chatRoom.getMessages(topic));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getTopics(){
		try {
			out.println(chatRoom.getTopics());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getNames(){
		try {
			out.println(chatRoom.getParticipants());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Out getOutputStream(){
		return out;
	}

}
