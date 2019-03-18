package mud;

import java.rmi.ConnectException;
import java.rmi.RemoteException;


public class MUDWorld {

    public static void main(String[] args) throws RemoteException
    {
        String host = "";
        int port = 0;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            System.err.println("Failed to start the game.\nRequired arguments: <hostname> <server port>\n");
            System.exit(0);
        }

        try {
            Client client = new Client(host, port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    client.shutdown();
                } catch (RemoteException ignored) {
                    System.err.println("[SERVER CONNECTION LOST] Game shutdown");
                }
            }));
        }
        catch (ConnectException ignored)
        { System.err.println("[SERVER CONNECTION LOST] Server is not responding"); }
    }
}
