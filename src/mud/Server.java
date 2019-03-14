package mud;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {

    private List<String> users;
    private MUD mud;


    Server(int registry, int server) throws RemoteException{
        this.users = new ArrayList<>();
        createServer(registry, server);
        this.mud = new MUD();
        System.out.println(this.mud.toString());

    }

    private void createServer(int registry, int server) throws RemoteException {
        String host = "";
        String policy = "static/mud.policy";

        try {
            host = (InetAddress.getLocalHost()).getCanonicalHostName();
            System.out.println("Hello, " + host + "!\n");
        }
        catch(UnknownHostException e) {
            System.err.println("Exception caught on hostname: " + e);
        }

        System.out.println("Server created on port " + registry);

        System.setProperty("java.security.policy", policy);
        System.setSecurityManager(new SecurityManager());

        ServerInterface mud = (ServerInterface)UnicastRemoteObject.exportObject(this, server);

        String url = String.format("rmi://%s:%d/mud", host, registry);
        System.out.println("Server address: " + url);
        try {
            Naming.rebind(url, mud);
        }
        catch(MalformedURLException e){
            System.err.println("Something wrong with the url\n" + e.getMessage());
        }
    }

    public void addUser(String username) throws RemoteException {
        this.users.add(username);
        System.out.println("User: '" + username + "' is now online.");
    }

    public void removeUser(String username) throws RemoteException {
        this.users.remove(username);
        System.out.println("User: '" + username + "' is now offline.");
    }

    public String usersOnline() throws RemoteException {
        String online = "\nUsers online:";
        for(String user : this.users)
            online += "\n\t*" + user;
        return online + "\n";
    }

    public String test() { return "this is a server"; }
}
