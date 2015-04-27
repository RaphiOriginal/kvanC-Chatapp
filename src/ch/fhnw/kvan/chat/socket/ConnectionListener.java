package ch.fhnw.kvan.chat.socket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import ch.fhnw.kvan.chat.utils.Out;

public class ConnectionListener extends Thread{
	private ArrayList<ConnectionHandler> connections = new ArrayList<>();
	private Queue<String> buffer = new LinkedList<String>();
	private Logger logger = Logger.getLogger(ConnectionListener.class);
	public void run(){
		while(true){
			if(buffer.size() != 0){
				String msg = buffer.poll();
				logger.info("Send to All: " + msg);
				for(ConnectionHandler ch: connections){
					Out out = ch.getOutputStream();
					out.println(msg);
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void addHandler(ConnectionHandler connection){
		connections.add(connection);
	}
	public void addMessageToAll(String msg){
		buffer.add(msg);
	}
}
