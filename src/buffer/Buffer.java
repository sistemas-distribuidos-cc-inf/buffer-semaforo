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
import java.util.concurrent.*;

import clients.Client;

public class Buffer {	
	
	public static enum Response{ 		
		SUCCESS, 		
		FAILURE_FULL_BUFFER, 		
		FAILURE_INVALID_REQUEST, 		
		FAILURE_EMPTY_BUFFER, 		
		FAILURE 	
	}
	
	private static final int BUFFER_SIZE = 3;
	private static int index_buffer = -1;
	static String buffer[] = new String[BUFFER_SIZE];
	private static Semaphore sem = new Semaphore(1);
	
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
	
	static Response Store (String s) {
		if(index_buffer == BUFFER_SIZE-1)
			return Response.FAILURE_FULL_BUFFER;
		else {
			buffer[++index_buffer] = s;
			System.out.println("Armazenei " + s + " com sucesso");
			return Response.SUCCESS;
		}
	}
	
	static void Consume (PrintWriter output) {
		if(index_buffer == -1)
			output.println (Response.FAILURE_EMPTY_BUFFER.toString()) ;
		else {
			System.out.println("Retornei do buffer " + buffer[index_buffer]);
			output.println (Response.SUCCESS.toString()) ;
			output.println ( buffer[index_buffer--] );
		}
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
		        String type ;
		        while ((type =  input.readLine()) != null) {
			        String s = input.readLine();
			        sem.acquire();
			        System.out.println("Cliente " + id + " entrou na regiao crítica");
			        if (type.equals(Client.Type.PRODUCER.toString())){
			        	Buffer.Response response =  Buffer.Store(s);
			        	output.println("" + response.toString());
			        }
			        if (type.equals(Client.Type.CONSUMER.toString())){
			        	Buffer.Consume(output);
			        }
			        Thread.sleep(5000);
			        sem.release();
			        System.out.println("Cliente " + id + " saiu da regiao crítica");
		        }

				input.close();
		        output.close();
		        socket.close();
			} catch (IOException | InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println("Client " + id + " just left.");
			}

			System.out.println("Client " + id + " just left.");
			return;
		}
	}
	
}
