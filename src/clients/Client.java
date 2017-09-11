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
				System.out.println( "0 - " + Type.PRODUCER.toString());
				System.out.println( "1 - " + Type.CONSUMER.toString());
				int option = sc.nextInt();	
				String s = option == Type.PRODUCER.ordinal() ? produce() : "" ;
				output.println(Type.values()[option].toString());
				output.println(s);
				String status = input.readLine();
				
				if ( option==Type.CONSUMER.ordinal()){
					consume(status);
				}else if (option==Type.PRODUCER.ordinal()){
					System.out.println("Status da resposta " + status);
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
	
	
	static void consume (String status) {
		try {			
			if (status.equals( Buffer.Response.SUCCESS.toString())){
				String response = input.readLine();
				System.out.println("O item removido do buffer foi: " + response);
			}else if (status.equals( Buffer.Response.FAILURE_EMPTY_BUFFER.toString())){
				System.out.println("Bufffer estava vazio");
			}else if (status.equals( Buffer.Response.FAILURE_INVALID_REQUEST.toString())){
				System.out.println("A requisicaoo possuia um formato incorreto. Siga o protocolo");
			}else if (status.equals( Buffer.Response.FAILURE.toString())){
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