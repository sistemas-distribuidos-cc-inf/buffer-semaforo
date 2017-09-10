package clients;

import clients.Client.Type;

public class Producer extends Client {

	public static void main(String[] args) {
		connectToServer(Type.PRODUCER);
	}
}
