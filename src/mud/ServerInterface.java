package mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void addUser(String username) throws RemoteException;
    void removeUser(String username) throws RemoteException;
    String usersOnline() throws RemoteException;

    String startLocation() throws RemoteException;

    String commandLook(String loc) throws RemoteException;
    String commandMove(String loc, String dir, String user) throws RemoteException;

    String test() throws RemoteException;
}
