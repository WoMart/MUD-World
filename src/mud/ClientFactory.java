package mud;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientFactory {

    private static String getUsername() {
        Scanner in = new Scanner(System.in);
        System.out.print("My name is: ");
        String name = in.nextLine().trim();
        if(name.length() > 12){
            System.out.println("Hmm...\nBut nobody would remember that. Let me start again. [Max character name: 12]");
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

        System.out.println("\nWould you like to listen to my story? :^)");
        Client client = new Client(host, port, getUsername());

        client.join();
        client.whoIsOnline();
        client.quit();

    }
}
