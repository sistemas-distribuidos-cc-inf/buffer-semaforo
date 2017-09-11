package clients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import buffer.Buffer;

import java.net.*;

public class Client {
	public static enum Type {
		PRODUCER,
		CONSUMER
	}

	public static Scanner sc;
	public static Socket socket;
	public static PrintWriter output;
	public static BufferedReader input;
	
	public static void main (String[] args){
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
			
			boolean loop = true;
			
			while (loop) {
				System.out.println( (Type.PRODUCER.ordinal()) +  " - Produzir item aleat�rio");
				System.out.println( (Type.CONSUMER.ordinal()) +  " - Consome um item do buffer");
				int option = sc.nextInt();
				String s = option == Type.PRODUCER.ordinal() ? produce() : "" ;
				System.out.println(option + " " + s);
				output.print(option);
				output.println(s);
				String status = input.readLine();
				System.out.println(status);
				System.out.print("Status no cliente: " + status);
				if (option==Type.CONSUMER.ordinal()){
					//consume(status);
				}else if (option==Type.PRODUCER.ordinal()){
					//System.out.println(Buffer.Response.values()[status].toString());
				}
				
			}
			
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
	
	
	static void consume (int status) {
		try {			
			if (status==Buffer.Response.SUCCESS.ordinal()){
				String response = input.readLine();
				System.out.println("O item removido do buffer foi: " + response);
			}else if (status==Buffer.Response.FAILURE_EMPTY_BUFFER.ordinal()) {
				System.out.println("Bufffer estava vazio");
			}else if (status==Buffer.Response.FAILURE_INVALID_REQUEST.ordinal()) {
				System.out.println("A requisi��o possuia um formato incorreto. Siga o protocolo");
			}else if (status==Buffer.Response.FAILURE.ordinal()) {
				System.out.println("Alguma coisa deu errado");
			}		    
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

	
	static String produce () {
		StringBuilder sb = new StringBuilder (1000);
		Random randomGenerator = new Random();
		sb.append(adjectives[randomGenerator.nextInt(adjectives.length)]).append(" ");
		sb.append(verbs[randomGenerator.nextInt(verbs.length)]).append(" ");
		sb.append(nouns[randomGenerator.nextInt(nouns.length)]).append(" ");
		sb.append(ponctuation[randomGenerator.nextInt(ponctuation.length)]);
		return sb.toString();
	}
	
	private static String [] adjectives = {"hard",
			"boundless",
			"shrill",
			"bashful",
			"opposite",
			"fluffy",
			"dear",
			"astonishing",
			"eight",
			"sick",
			"placid",
			"ad hoc",
			"ambiguous",
			"irate",
			"ordinary",
			"numerous",
			"brawny",
			"harsh",
			"calm",
			"jumbled" 
	};
	private static String [] nouns = {"snails",
			"believe",
			"apparatus",
			"horn",
			"eggs",
			"desire",
			"snail",
			"fireman",
			"pets",
			"stocking",
			"curtain",
			"prose",
			"doctor",
			"expansion",
			"fish",
			"hammer",
			"tail",
			"profit",
			"grip",
			"regret"};
	private static String [] verbs = {"lock",
			"apologise",
			"knock",
			"advise",
			"scatter",
			"nest",
			"bomb",
			"roll",
			"decorate",
			"memorise",
			"name",
			"store",
			"harass",
			"remain",
			"last",
			"hop",
			"yell",
			"mug",
			"object",
			"weigh"};
	private static String [] ponctuation = { "?", "!" ,".", "...!"};
}