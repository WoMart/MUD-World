package mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void addUser(String username) throws RemoteException;
    void removeUser(String username) throws RemoteException;
    String usersOnline() throws RemoteException;
    String startLocation() throws RemoteException;
    String test() throws RemoteException;
}
