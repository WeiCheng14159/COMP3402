import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.*;

/**
 * CLIENT SERVER COMMUNICATION INTERFACE
 * @author chengwei
 *
 */
public interface PokerGameApp extends Remote{
	public boolean authenticate(String cmd_code, String u_name, String pw) throws RemoteException;
	public String request(String cmd_code, String u_name, String pw) throws RemoteException;
}
