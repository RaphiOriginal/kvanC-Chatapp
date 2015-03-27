package ch.fhnw.kvan.chat.socket;

import java.net.Socket;
import java.util.Queue;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;

public class ConnectionHandler implements Runnable{
	private Socket s;
	private ConnectionListener cl;
	private Queue<String> buffer;
	public ConnectionHandler(Socket s, ConnectionListener cl){
		this.s = s;
		this.cl = cl;
	}
	public void run(){
		Out out = new Out(s);
		In in = new In(s);
		//building lot
//		in.
	}

}
