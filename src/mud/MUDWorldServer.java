package mud;

import java.rmi.RemoteException;

public class MUDWorldServer {

    public static void main(String[] args) throws RemoteException {
        if (args.length < 2) {
            System.out.println("Usage:\njava mudserver <registry port> <server port>");
            return;
        }

        int registry = Integer.parseInt(args[0]);
        int server = Integer.parseInt(args[1]);
        new Server(registry, server);
    }
}
