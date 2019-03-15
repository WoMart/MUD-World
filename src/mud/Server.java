package mud;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Naming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class Server implements ServerInterface {

    private List<String> users = new ArrayList<>();
    private Map<String, MUD> mudMap= new HashMap<>();


    Server(int registry, int server) throws RemoteException{
        createServer(registry, server);
        this.serverMessage("Your server is running...");
    }

    private MUD getMUD(String name) {
        return mudMap.get(name);
    }

    private void createServer(int registry, int server) throws RemoteException {
        String host = "";
        String policy = "static/mud.policy";

        try {
            host = (InetAddress.getLocalHost()).getCanonicalHostName();
            System.out.println("Hello, " + host + "!");
        }
        catch(UnknownHostException e) {
            System.err.println("Exception caught on hostname: " + e);
        }

        this.serverMessage("Server created on port " + registry);

        System.setProperty("java.security.policy", policy);
        System.setSecurityManager(new SecurityManager());

        ServerInterface mud = (ServerInterface)UnicastRemoteObject.exportObject(this, server);

        String url = String.format("rmi://%s:%d/mud", host, registry);
        this.serverMessage("Server address: " + url);
        try {
            Naming.rebind(url, mud);
        }
        catch(MalformedURLException e){
            System.err.println("Something wrong with the url\n" + e.getMessage());
        }
    }

    public boolean createMUD(String name) throws RemoteException {
        if (mudMap.containsKey(name))
            return false;
        mudMap.put(name, new MUD());
        this.serverMessage("New MUD created: " + name + " by " + ".");
        return true;
    }

    public boolean joinMUD(String name) {
        if (!mudMap.containsKey(name))
            return false;
        return true;
    }

    public String listMUD() throws RemoteException{
        String list = "Available MUDs:\n";
        for(String s : mudMap.keySet())
            list += "\t" + s + "\n";
        return list;
    }

    public void addUser(String username) {
        this.users.add(username);
        this.serverMessage("User: '" + username + "' is now online.");
    }

    public void removeUser(String username) {
        this.users.remove(username);
        this.serverMessage("User: '" + username + "' is now offline.");
    }

    public String usersOnline() {
        String online = "\nUsers online:";
        for(String user : this.users)
            online += "\n\t*" + user;
        return online + "\n";
    }

    public String startLocation(String mud) {
        return this.getMUD(mud).startLocation();
    }

    public String commandLook(String mud, String loc) {
        return this.getMUD(mud).locationInfo(loc);
    }

    public String commandMove(String mud, String loc, String dir, String user) {
        return this.getMUD(mud).moveThing(loc, dir, user);
    }

    public boolean commandTake(String mud, String loc, String thing) {
        return this.getMUD(mud).takeThing(loc, thing);
    }

    public void commandDrop(String mud, String loc, String thing) {
        this.getMUD(mud).dropThing(loc, thing);
    }

    private void serverMessage(String msg) {
        DateFormat df = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
        Date cur_date = new Date();
        System.out.println( "[" + df.format(cur_date) + "] " + msg );
    }
}
