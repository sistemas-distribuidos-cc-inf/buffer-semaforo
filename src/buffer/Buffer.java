package buffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import clients.Client;

public class Buffer {	
	
	public static enum ProducerResponse{
		FAILURE_FULL_BUFFER,
		SUCCESS,
		FAILURE_INVALID_REQUEST,
		FAILURE
	}
	
	public static enum ConsumerResponse{
		FAILURE_EMPTY_BUFFER,
		FAILURE,
		SUCCESS
	}
	
	
	private static final int BUFFER_SIZE = 10;
	static String buffer[] = new String[BUFFER_SIZE];
	
	public static void main(String[] args) throws Exception {
		
		ServerSocket server = null;
		Socket client = null;
		int port = 8080; // Default port

		while (true) {
			try {
				server = new ServerSocket(port, 5);
				break;
			} catch (IOException e) {
				port++;
			}
		}
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println(
				"Server up and running! \nWaiting for clients at ip " + ip.getHostAddress() + "-" + port + "\n");

		try {
			while (true) {
				try {
					client = server.accept();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					System.exit(1);
				} finally {
					new Conection(client).start();
				}
			}
		} finally {
			server.close(); // Closes server
		}			
	}
	
	static void Produce (String s) {
		//TODO
		// add this string to the buffer after handling concurrency
	}
	static String Consume () {
		// TODO
		// return a string from the buffer after handlind concurrency	
		return "";
	}

	private static class Conection extends Thread {
		private static int clients = 0;
		private int id;
		private Socket socket;
	    private BufferedReader input;
	    private PrintWriter output;
	    private OutputStream outputStream;
	    private BufferedInputStream in;

		public Conection(Socket socket ) {
			this.socket = socket;
			this.id = ++clients;
		}

		public void run() {
			System.out.println("Client " + id + " at IP: " + socket.getInetAddress() + " PORT: " + socket.getPort()
					+ " just entered");

			try {
				
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		        int type =  input.read() - '0';
		        
		        if (type==Client.Type.PRODUCER.ordinal()){
		        	Buffer.Produce("");
		        	/*TODO
		        	 * read a String from the producer to store it in the buffer
		        	 RETURN A CLIENT_RESPONSE TO THE PRODUCER
		        	 eg: output.print(Buffer.SUCCESS);
		        	 */
		        }
		        if (type==Client.Type.CONSUMER.ordinal()){
		        	Buffer.Consume();
		        	/*TODO
		        	 * return a string from the buffer to the client
		        	 RETURN A CLIENT_RESPONSE TO THE CONSUMER
		        	 eg: output.print(Buffer.EMPTY_BUFFER);
		        	 */
		        }

				input.close();
		        output.close();
		        socket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.out.println("Client " + id + " just left.");
			}
			System.out.println("Client " + id + " just left.");
			return;
		}
	}
	
}
