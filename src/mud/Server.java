package mud;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.Naming;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

import java.util.*;


public class Server implements ServerInterface {

    private List<String> users = new ArrayList<>();
    private Map<String, MUD> mudMap= new HashMap<>();
    private boolean busy = false;


    Server(int registry, int server) throws RemoteException{
        LocateRegistry.createRegistry(registry);
        createServer(registry, server);
        this.serverMessage("Your server is running...\n");

        this.createMUD("default", "admin");
    }

    private void createServer(int registry, int server) throws RemoteException {
        String host = "";
        String policy = "static/mud.policy";

        try {
            host = (InetAddress.getLocalHost()).getCanonicalHostName();
            System.out.println();
            this.serverMessage("Hello, " + host + "!");
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

        try { Naming.rebind(url, mud); }
        catch(MalformedURLException e){
            System.err.println("Something wrong with the url\n" + e.getMessage());
        }
    }

    /**
     * Helper function to standardise the output messages
     * Format: "[Time] Message"
     */
    private void serverMessage(String msg) {
        DateFormat df = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
        System.out.println( "[" + df.format( new Date() ) + "] " + msg );
    }

    private void lock() {
        while(this.busy) {
            try { Thread.sleep(100); }
            catch (InterruptedException ignored)
            { System.err.println("An error occurred while waiting for the unlock"); }
        }
        this.busy = true;
    }

    private void unlock() {
        this.busy = false;
    }

    private MUD getMUD(String name) {
        return mudMap.get(name);
    }

    /* Client interactions */

    public boolean createMUD(String mud_name, String username) {
        boolean created;
        MUD newMUD = new MUD();
        this.lock();
        if (mudMap.containsKey(mud_name)) {
            created = false;
        }
        else {
            mudMap.put(mud_name, newMUD);
            this.serverMessage(
                    "New MUD " + mud_name + " created by " + username + ". "
                            + newMUD.getMapSize() + " locations");
            created = true;
        }
        this.unlock();
        return created;
    }

    public boolean joinMUD(String mud_name, String username) {
        if (!mudMap.containsKey(mud_name))
            return false;
        getMUD(mud_name).addPlayer(username);
        return true;
    }

    public void leaveMUD(String mud_name, String loc, String username) {
        this.getMUD(mud_name).removePlayer(loc, username);
    }

    public String listMUD() {
        StringBuilder list = new StringBuilder("Available MUDs:\n");
        for(String s : mudMap.keySet())
            list.append("\t").append(s).append("\n");
        return list.toString();
    }

    public void addUser(String username) {
        this.users.add(username);
        this.serverMessage("User: '" + username + "' is now online.");
    }

    public void removeUser(String username) {
        this.users.remove(username);
        this.serverMessage("User: '" + username + "' is now offline.");
    }

    public boolean isUser(String username) {
        return !this.users.isEmpty() && this.users.contains(username);
    }

    public String usersOnline() {
        StringBuilder online = new StringBuilder("\nUsers online:\n");
        for(String user : this.users)
            online.append("\t*").append(user).append("\n");
        return online.toString();
    }

    public String usersInWorld(String mud_name) {
        return this.getMUD(mud_name).listPlayers();
    }

    public String startLocation(String mud_name) {
        return this.getMUD(mud_name).getStartLocation();
    }

    public String commandLook(String mud_name, String loc) {
        return this.getMUD(mud_name).locationInfo(loc);
    }

    public String commandMove(String mud_name, String loc, String dir, String user) {
        return this.getMUD(mud_name).movePlayer(loc, dir, user);
    }

    public boolean commandTake(String mud_name, String loc, String thing) {
        this.lock();
        boolean taken = this.getMUD(mud_name).takeThing(loc, thing);
        this.unlock();
        return taken;
    }

    public void commandDrop(String mud_name, String loc, String thing) {
        this.getMUD(mud_name).dropThing(loc, thing);
    }
}
