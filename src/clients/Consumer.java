package clients;

import clients.Client.Type;
public class Consumer extends Client{
	
	public static void main(String[] args) {
		connectToServer(Type.CONSUMER);
	}
}
