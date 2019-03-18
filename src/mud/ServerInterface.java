package mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void addUser(String username)
            throws RemoteException;
    void removeUser(String username)
            throws RemoteException;
    String usersOnline()
            throws RemoteException;

    boolean createMUD(String name)
            throws RemoteException;
    boolean joinMUD(String name)
            throws RemoteException;
    String listMUD()
            throws RemoteException;

    String startLocation(String mud)
            throws RemoteException;

    String commandLook(String mud, String loc)
            throws RemoteException;
    String commandMove(String mud, String loc, String dir, String user)
            throws RemoteException;
    boolean commandTake(String mud, String loc, String thing)
            throws RemoteException;
    void commandDrop(String mud, String loc, String thing)
            throws RemoteException;

}
