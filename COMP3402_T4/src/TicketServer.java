import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

public class TicketServer extends UnicastRemoteObject implements TicketMachine {

	private int counter;
	
	public TicketServer() throws RemoteException {
		counter = 1000;
	}
	
	public int getTicket() throws RemoteException {
		return counter++;
	}
	
	public static void main(String [] args) throws RemoteException, MalformedURLException, NamingException {
		
		/*env setup*/
		Hashtable<String, String> env = new Hashtable<String, String>() ;
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
		
		/*Create a new context*/
		Context ctx = new InitialContext(env);
		
		/*Create a new object*/
		TicketMachine app = new TicketServer();
		
		/*Bind this object to the LDAP server*/
		// ctx.rebind("cn=TicketMachine", app);
		
		String rmiurl = "rmi://localhost/TicketMachine";
		Reference ref = new Reference("TicketMachine", new StringRefAddr("URL", rmiurl));
		ctx.rebind("cn=TicketMachine", ref);
		ctx.rebind(rmiurl, app);

		/*Confirm*/
		System.out.println("Service registered");
	}
}
