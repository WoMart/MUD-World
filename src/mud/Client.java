package mud;

import java.rmi.Naming;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements ClientInterface {

    private ServerInterface server;
    private String hostname;
    private int port;

    private String username;
    private String location;
    private List<String> inventory = new ArrayList<>();
    private boolean ingame = false;

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
        System.out.println("[" + this.username + "] You have connected to " + this.hostname);
    }

    private void quit() throws RemoteException {
        this.ingame = false;
        this.server.removeUser(this.username);
        System.out.println("[" + this.username + "]Disconnected from " + this.hostname);
    }

    private void whoIsOnline() throws RemoteException {
        System.out.println(this.server.usersOnline());
    }

    private void look(String loc) throws RemoteException {
        System.out.println(
                this.server.commandLook(loc)
        );
    }

    private void move(String dir) throws RemoteException {
        String loc = this.server.commandMove(
                this.location,
                dir,
                this.username );
        if (this.location.equals(loc))
            System.out.println("You bashed into a tree...");
        else
            this.location = loc;

        System.out.println("You are currently in " + this.location);
    }

    private void take(String thing) throws RemoteException {
        if (this.server.commandTake(this.location, thing)) {
            this.inventory.add(thing);
            System.out.println(" You have put " + thing + " in your inventory.");
        }
        else
            System.out.println(" Turns out " + thing + " was just an illusion.");
    }

    private void drop(String thing) throws RemoteException {
        if (!this.inventory.contains(thing)) {
            System.out.println("There is no " + thing + " in your inventory");
            return;
        }
        System.out.println(" You have dropped " + thing + " in " + this.location + ".");
        this.server.commandDrop(this.location, thing);
        this.inventory.remove(thing);

    }

    private void inventory() {
        if(this.inventory.isEmpty()) {
            System.out.println("\nYour inventory is currently empty. Lots of space for loot!\n");
            return;
        }

        String inv = "\nYour inventory:\n";
        for(String i : this.inventory)
            inv += "\t*" + i + "\n";
        System.out.println(inv);
    }

    private String[] getInput(){
        Scanner in = new Scanner(System.in);
        System.out.print("[" + this.username + "]>>> ");
        String[] input = in.nextLine().trim().split(" ", 2);
        String[] output = { "", "" };

        try { output[0] = input[0].toLowerCase(); }
        catch (IndexOutOfBoundsException ignored) { }

        try { output[1] = input[1]; }
        catch (IndexOutOfBoundsException ignored) { }

        return output;
    }

    public void menu() throws RemoteException {
        this.ingame = false;
        String message = "";
        while(!this.ingame) {
            System.out.println("\n\t\\\\\\ Welcome to the MUD World ///" +
                    "\nMenu ( input the command in [] to choose the option ):" +
                    "\n\t1. Join MUD\t\t[ join <mud_name> ]" +
                    "\n\t2. Create MUD\t[ create <new_name> ] // TODO" +
                    "\n\t3. List MUDs\t[ list ] // TODO" +
                    "\n\t4. Exit\t\t\t[ exit ]" +
                    "\n*********************************************************\n\n" +
                    message
            ); message = "";

            String[] input = this.getInput();
            String action = input[0];
            String attribute = input[1];

            if (action.startsWith("join") & !attribute.equals("")) {
                System.out.println(attribute);
            }
            else if (action.startsWith("create") & !attribute.equals("")) {
                if (this.server.createMUD(attribute))
                    message = "The MUD " + attribute + " has been created";
                else
                    message = "The MUD" + attribute + " already exists";
            }
            else if (action.equals("list")) {
                message = this.server.listMUD();
            }
            else if (action.equals("exit")){
                System.out.println("\nQuitting MUD World");
                this.quit();
                return;
            }
            else message = "What does he mean?";

        }
    }

    public void play() throws RemoteException {
        Scanner in = new Scanner(System.in);
        this.ingame = true;
        this.location = this.server.startLocation();

        System.out.println("\nType 'help' to see available commands");

        while (ingame) {
            System.out.print("[" + this.username + "]>>> ");
            String action = in.nextLine().trim().toLowerCase();

            if (action.startsWith("move")) {
                this.move(action.split(" ", 2)[1]);
            }

            else if (action.startsWith("take")) {
                this.take(action.split(" ", 2)[1]);
            }

            else if (action.startsWith("drop")) {
                this.drop(action.split(" ", 2)[1]);
            }

            else if (action.equals("i")
                    | action.equals("inv")
                    | action.equals("inventory")) {
                this.inventory();
            }

            else if (action.equals("look")) {
                this.look(this.location);
            }
            else if (action.equals("help")) {
                System.out.println("\thelp\t\tshows this menu\n\n"
                        + "\texit\n\tquit\t\tleaves the game\n\n"
                        + "\tuserlist\tsee who is online\n\n"
                        + "\tlook\t\tlook around you\n\n");

            }
            else if (action.equals("userlist")) {
                this.whoIsOnline();

            }
            else if (action.equals("exit") || action.equals("quit")) {
                System.out.print("Are you sure you want to exit the game?\nEnter 'yes' to confirm: ");
                if (in.nextLine().trim().toLowerCase().startsWith("yes"))
                    this.quit();
            }
            else {
                System.out.println("But to no avail...");
            }
        }
    }

    public void test() throws RemoteException {
        System.out.println((this.server).test());
    }
}
