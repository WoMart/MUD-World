package mud;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements ServerInterface {

    private List<String> users = new ArrayList<>();
    private Map<String, MUD> mudMap= new HashMap<>();
    private MUD cur_mud = null;


    Server(int registry, int server) throws RemoteException{
        createServer(registry, server);

        String mud1 = "MUD 1";
        String mud2 = "MUD 2";
        this.mudMap.put(mud1, new MUD());
        this.mudMap.put(mud2, new MUD());

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

    private void createMUD(String name) {
        mudMap.put(name, new MUD());
    }

    public void addUser(String username) {
        this.users.add(username);
        System.out.println("User: '" + username + "' is now online.");
    }

    public void removeUser(String username) {
        this.users.remove(username);
        System.out.println("User: '" + username + "' is now offline.");
    }

    public String usersOnline() {
        String online = "\nUsers online:";
        for(String user : this.users)
            online += "\n\t*" + user;
        return online + "\n";
    }

    public String startLocation() {
        return this.cur_mud.startLocation();
    }

    public String commandLook(String loc) {
        return this.cur_mud.locationInfo(loc);
    }

    public String commandMove(String loc, String dir, String user) {
        return this.cur_mud.moveThing(loc, dir, user);
    }

    public boolean commandTake(String loc, String thing) {
        return this.cur_mud.takeThing(loc, thing);
    }

    public void commandDrop(String loc, String thing) {
        this.cur_mud.dropThing(loc, thing);
    }


    public String test() { return "this is a server"; }
}
