import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.*;

/**
 * POKER INTERFACE FOR RMI SERVICE
 * @author chengwei
 *
 */
public interface Poker extends Remote{
	public boolean authenticate(String cmd_code, String u_name, String pw) throws RemoteException;
	public String request(String cmd_code, String u_name, String pw) throws RemoteException;
}
