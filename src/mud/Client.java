package mud;

import java.rmi.Naming;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client implements ClientInterface {

    private ServerInterface server;
    private String hostname;
    private int port;

    private String username;
    private String location;

    public Client(String h, int p, String u) throws RemoteException {
        this.hostname = h;
        this.port = p;
        this.username = u;

        System.setProperty("java.security.policy", "static/mud.policy");
        System.setSecurityManager(new SecurityManager());

        this.connect();
    }

    public void connect() throws RemoteException {
        try {
            this.server = (ServerInterface) Naming.lookup(
                    String.format("rmi://%s:%d/mud", this.hostname, this.port));
        }
        catch (MalformedURLException | NotBoundException e) {
            System.err.println("Connection FAILED\n" + e);
        }
    }

    public void join() throws RemoteException {
        this.server.addUser(this.username);
        System.out.println("[" + this.username + "]Connected to " + this.hostname);
    }

    public void quit() throws RemoteException {
        this.server.removeUser(this.username);
        System.out.println("[" + this.username + "]Disconnected from " + this.hostname);
    }

    public void whoIsOnline() throws RemoteException {
        System.out.println(this.server.usersOnline());
    }

    public void play() throws RemoteException {
        this.location = this.server.startLocation();

        Scanner in = new Scanner(System.in);
        System.out.print("\nI desire to ");
        String action = in.nextLine().trim();
    }
    public void test() throws RemoteException {
        System.out.println((this.server).test());
    }
}
