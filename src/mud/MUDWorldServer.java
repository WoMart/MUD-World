package mud;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.rmi.RemoteException;


public class MUDWorldServer {

    private static int getPort(String s) {
        System.out.print(s + " port: ");
        try { return new Scanner( System.in ).nextInt(); }
        catch (InputMismatchException ignored)
        { return getPort(s); }
    }

    public static void main(String[] args) throws RemoteException {

        int registry;
        int server;
        try {
            registry = Integer.parseInt(args[0]);
            server = Integer.parseInt(args[1]);
        }
        catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            System.out.println();
            registry = getPort("Registry");
            server = getPort("Server");
        }

        new Server(registry, server);
    }
}
