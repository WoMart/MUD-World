package mud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientFactory {

    private static String getUsername() {
        BufferedReader in = new BufferedReader( new InputStreamReader(System.in) );
        System.out.print("My name is: ");
        String name = "";
        try { name = in.readLine().trim(); }
        catch (IOException e) { System.err.println(e + " in getUsername()"); }
        if (name.length() < 3 | name.length() > 12 | name.contains(" ")) {
            System.out.println("Hmm...\nBut nobody would remember that. Let me start again. [Max character name: 12 | No spaces]");
            return getUsername();
        }
        return name;

    }

    public static void main(String[] args) throws RemoteException
    {
        if(args.length < 2){
            System.err.println("Failed to create a client.\nRequired arguments: <hostname> <server port>\n");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.println("\nWhat is your name, adventurer? :^)");
        Client client = new Client(host, port, getUsername());

        client.connect();

    }
}
