package mud;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.rmi.RemoteException;

import java.io.File;


public class MUDWorldServer {

    private static int getPort(String s) {
        System.out.print(s + " port: ");
        try { return new Scanner( System.in ).nextInt(); }
        catch (InputMismatchException ignored)
        { return getPort(s); }
    }

    private static String getChoice(String staticFiles) {
        System.out.print("Would you like to change this directory? [Y/n] ");
        String c = new Scanner( System.in ).next().toLowerCase();

        if ( c.startsWith("y") )
            return getStatic();
        if ( c.startsWith("n") )
            return staticFiles;
        return getChoice(staticFiles);
    }

    private static String getStatic() {
        String dir;
        System.out.print("New path: ");
        dir = new Scanner( System.in ).nextLine();

        if ( new File( dir + "\\mud.policy").isFile() ) {
            if ( !dir.endsWith("\\") ) dir += '\\';
            return dir;
        }
        else {
            System.err.println("[ERROR]Invalid path");
            try { Thread.sleep(1); } // makes sure error gets printed before the "New path:"
            catch (InterruptedException ignored) {}
            return getStatic();
        }
    }

    public static void main(String[] args) throws RemoteException {

        int registry;
        int server;
        String staticFiles = System.getProperty("user.dir") + "\\static\\";
        try {
            registry = Integer.parseInt(args[0]);
            server = Integer.parseInt(args[1]);
        }
        catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            System.out.println();
            registry = getPort("Registry");
            server = getPort("Server");
        }
        System.out.println(
                "\nCurrent static directory: " + staticFiles +
                        "\n\nStatic directory contains the game files and the server's policy."
        );

        new Server(registry, server, getChoice(staticFiles));
    }
}
