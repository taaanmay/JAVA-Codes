/**
 * 
 */

//Create a New Class for another END USER

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import tcdIO.*;

/**
 *
 * End User class
 * 
 * An instance accepts user input 
 *
 */
public class Broker extends Node {
	

	Terminal terminal;
	InetSocketAddress dstAddress;
	ArrayList<Integer> workerList = new ArrayList<Integer>();
	ArrayList<Object> workList = new ArrayList<Object>();

	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Broker(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * onReceipt method for the Broker class
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		StringContent content= new StringContent(packet);
		//this.notify();
		terminal.println(content.toString());


		int Port = packet.getPort();
		String message = content.toString();

		switch(Port) {


		case Constant.Worker1_PORT: 
			if(message.equalsIgnoreCase("busy")) {
				for(int i=0; i<workerList.size();i++) {
					if(workerList.get(i)==Port) {
						workerList.remove(i);
						terminal.println("Worker Removed");
					}
				}
			}else {
				boolean isPortThere = false;
				for(int i=0; i<workerList.size();i++) {
					if(workerList.get(i)==Port) {
						isPortThere = true;
					}
				}
				if(isPortThere==false) {
					workerList.add(Port);
					terminal.println("Worker Added");
				}
				
			}
			
			sendMessageToWorker();
		break;
		
		case Constant.Worker2_PORT:
			if(message.equalsIgnoreCase("busy")) {
				for(int i=0; i<workerList.size();i++) {
					if(workerList.get(i)==Port) {
						workerList.remove(i);
						terminal.println("Worker Removed");
					}
				}
			}else {
				boolean isPortThere = false;
				for(int i=0; i<workerList.size();i++) {
					if(workerList.get(i)==Port) {
						isPortThere = true;
					}
				}
				if(isPortThere==false) {
					workerList.add(Port);	
					terminal.println("Worker added");
				}
				
			}
			
			sendMessageToWorker();
			break;
			
		case Constant.CandC1_PORT:
			this.notify();
			workList.add(packet);
			terminal.println("Work Added");
			sendMessageToWorker();
			break;
			
		case Constant.CandC2_PORT:
			this.notify();
			workList.add(packet);
			terminal.println("Work Added");
			sendMessageToWorker();
			break;

		default: System.out.println("Port Not Found");	
		}

	}

	public void sendMessageToWorker() {
		if(!workerList.isEmpty() && !workList.isEmpty()) {
			//byte[] data= null;
			DatagramPacket packet= null;

			if(workList.get(0)!=null) {
				
			packet= (DatagramPacket) (workList.get(0));
			workList.remove(0);

			terminal.println("Sending packet...");
			
			InetSocketAddress dstAddress = new InetSocketAddress(Constant.DEFAULT_DST_NODE, workerList.get(0));
			workerList.remove(0);
			packet.setSocketAddress(dstAddress);
			//packet= new DatagramPacket(data, data.length, dstAddress);
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			terminal.println("Packet sent");
			//this.wait();
			}
		} return;
	}


	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {
		byte[] data= null;
		DatagramPacket packet= null;

		String userString =  terminal.readString("String to send: ");
		
		data= (userString.getBytes());

		terminal.println("Sending packet...");
		packet= new DatagramPacket(data, data.length, dstAddress);
		socket.send(packet);
		terminal.println("Packet sent");
		this.wait();
	}


	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {					
			Terminal terminal= new Terminal("Broker");		
			Broker C1 = (new Broker(terminal, Constant.DEFAULT_DST_NODE, Constant.Worker1_PORT, Constant.Broker_PORT));
			while(true) {
				C1.wait();
			}
			//			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	void run() {
		while (true) {
			try {
				this.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	 */
}