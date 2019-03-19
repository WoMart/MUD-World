package mud;

import java.rmi.RemoteException;


public class MUDWorldServer {

    public static void main(String[] args) throws RemoteException {

        int registry = 0;
        int server = 0;
        try {
            registry = Integer.parseInt(args[0]);
            server = Integer.parseInt(args[1]);
        }
        catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            System.out.println("Failed to start the game.\nRequired arguments: <hostname> <server port>\n");
            System.exit(0);
        }

        new Server(registry, server);
    }
}
