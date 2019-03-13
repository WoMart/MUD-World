package mud;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class ServerManager implements ServerInterface {

    private void createServer(int registry, int server) throws RemoteException {
        String host = "None";
        String url = "None";
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

        url = String.format("rmi://%s:%d/mud", host, registry);
        System.out.println("Server address: " + url);
        try {
            Naming.rebind(url, mud);
        }
        catch(MalformedURLException e){
            System.err.println("Something wrong with the url\n" + e.getMessage());
        }
    }

    ServerManager(int registry, int server) throws RemoteException{
        createServer(registry, server);
    }
}
