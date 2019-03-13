package mud;

import java.rmi.RemoteException;

public class Server{

    public static void main(String[] args) throws RemoteException {

        if(args.length < 2){
            System.out.println("Usage:\njava mudserver <registry port> <server port>");
            return;
        }

        int registry = Integer.parseInt(args[0]);
        int server = Integer.parseInt(args[1]);
        new MUD();
        new ServerManager(registry, server);
        System.out.println("Server created successfully!");
    }


}
