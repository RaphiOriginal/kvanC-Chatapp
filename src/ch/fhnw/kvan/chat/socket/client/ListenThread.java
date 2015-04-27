package ch.fhnw.kvan.chat.socket.client;

import org.apache.log4j.Logger;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.utils.In;

public class ListenThread extends Thread{
	private Logger logger;
	private Client client;
	private In in;
	private ClientGUI clientGUI;
	
	public ListenThread(Client client, In in){
		this.client = client;
		this.in = in;
		clientGUI = this.client.getClientGUI();
		logger = Logger.getLogger(ListenThread.class);
	}
	
	public void run(){
		while(true){
			String msg = in.readLine();
			encodeMessage(msg);
		}
	}
	
	public void encodeMessage(String msg){
		logger.trace("Encoding Message: " + msg);
		String[] splittedMsg = msg.split("=");
		switch(splittedMsg[0]){
		case "message":
			clientGUI.addMessage(splittedMsg[1].split(";")[0]);
			break;
		case "add_topic":
			clientGUI.addTopic(splittedMsg[1]);
			break;
		case "remove_topic":
			clientGUI.removeTopic(splittedMsg[1]);
			break;
		case "topics":
			if(splittedMsg.length > 1){
				String[] topics = splittedMsg[1].split(";");
				clientGUI.updateTopics(topics);
			}
			break;
		case "add_participant":
			clientGUI.addParticipant(splittedMsg[1]);
			break;
		case "remove_participant":
			clientGUI.removeParticipant(splittedMsg[1]);
			break;
		case "participants":
			if(splittedMsg.length > 1){
				String[] participants = splittedMsg[1].split(";");
				clientGUI.updateParticipants(participants);
			}
			break;
		case "messages":
			if(splittedMsg.length > 1){
				String[] messages = splittedMsg[1].split(";");
				clientGUI.updateMessages(messages);
			}
			break;
		}
	}
}
