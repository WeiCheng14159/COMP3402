import java.rmi.RemoteException;
import javax.swing.*;

public interface Communication {
	public boolean checking(String cmd_code, String u_name, String pw) throws RemoteException;
	public JPanel request(String cmd_code, String u_name, String pw) throws RemoteException;
}
