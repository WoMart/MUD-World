package mud;

import java.rmi.RemoteException;
import java.rmi.Remote;


public interface ServerInterface extends Remote {

    void addUser(String username)
            throws RemoteException;
    void removeUser(String username)
            throws RemoteException;
    String usersOnline()
            throws RemoteException;

    boolean createMUD(String mud_name, String username)
            throws RemoteException;
    boolean joinMUD(String mud_name, String username)
            throws RemoteException;
    void leaveMUD(String mud_name, String loc, String username)
            throws RemoteException;
    String listMUD()
            throws RemoteException;
    String usersInWorld(String mud_name)
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

    void lock()
        throws RemoteException;
    void unlock()
        throws RemoteException;

}
