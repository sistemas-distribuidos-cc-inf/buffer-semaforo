package clients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.net.*;

public class Client {
	public static Scanner sc;
	public static Socket socket;
	public static PrintWriter output;
	public static BufferedReader input;
	private static ObjectOutputStream outputStream;

	public static void main(String[] args) {
		String serverHost = null;
		int serverPort = 0;

		serverHost = "localhost";
		serverPort = 8080;

		try {
		/* Creates a client socket */
		      socket = new Socket(serverHost, serverPort);
		      output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      sc = new Scanner(System.in);
			
			
			String s = sc.nextLine();
			output.println(s);
		
			sc.close();
			input.close();
			output.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}