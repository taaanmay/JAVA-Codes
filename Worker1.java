/**
 * 
 */

//Create a New Class for another END USER

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import tcdIO.*;

/**
 *
 * End User class
 * 
 * An instance accepts user input 
 *
 */
public class Worker1 extends Node {
	

	Terminal terminal;
	InetSocketAddress dstAddress;

	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Worker1(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		StringContent content= new StringContent(packet);
		this.notify();
		terminal.println(content.toString());
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
			Terminal terminal= new Terminal("Worker 1");		
			Worker1 W1 = (new Worker1(terminal, Constant.DEFAULT_DST_NODE, Constant.Broker_PORT, Constant.Worker1_PORT));
			while(true) {
				W1.start();
			//	System.out.println("a");
			
				//W1.wait();
			//	System.out.println("b");
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