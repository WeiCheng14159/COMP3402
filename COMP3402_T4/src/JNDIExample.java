import javax.naming.* ;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/*
 * Important JNDI API:
 * javax.naming (Naming)
 * javax.naming.directory (Directory) 
 * javax.naming.spi (Service Provider Interface)
 */

import java.util.Hashtable ; 

/*This JNDI example is testing the connection to a local LDAP server running on (default port 10389) */

/*How to install LDAP server on Mac OSX http://directory.apache.org/apacheds/basic-ug/1.3-installing-and-starting.html */

/*
 * Start the LDAP server on MAC: sudo launchctl start org.apache.directory.server 
 * Stop the server on MAC : sudo launchctl stop org.apache.directory.server 
 */

public class JNDIExample {

	public static void main(String[] args) {
		
		 Hashtable<String, String> env = new Hashtable<String, String>();
		 /*Environmental property stored in a Hashtable object*/
		 
		 env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory") ;
		 /*LDAP provider from SUN*/
		 
		 env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com") ; 
		 /*LDAP service broadcasting on 10389, root naming context: dc=example, dc=com*/
		 
 		 env.put(Context.SECURITY_AUTHENTICATION, "simple") ;
		 env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system") ; 
		 env.put(Context.SECURITY_CREDENTIALS, "secret") ;
		 /*Provider configuration information*/
		 
		 try {
			 /*Creating initial context*/
			 Context ctx = new InitialContext(env) ;
			 /*For naming operations*/
			 	/*javax.naming.InitialContext class implements Context interface as the starting point into a name space.  
			 	 * Application must acquire an InitialContext object to access objects in the name space*/
			 
			 DirContext dir_ctx = new InitialDirContext(env); 
			 /*For directory operations*/
			 
			 String message = "Testing";
			 /*Object to be serialized, must be serializable => object class must implement java.io.Serializable interface*/
			 /*
			  * Example: 
			  * public class Student implements java.io.Serializable{
			  * 	private String name; 
			  * 	private String u_num;
			  * 	public Student( String nm, String un){
			  * 		name = nm;
			  * 		u_num = un;
			  * 	}
			  * 	public String toString(){
			  * 		return (name + " " + u_num);
			  * 	}
			  * }
			  * */
			 
			 ctx.rebind( "cn=test", message) ;
			 /*Client side: Storing a serialized object in LDAP directory*/
			 
			 /*Server side: Store a remote object to LDAP
			  *Hello.java : The Hello remote interface
			  *
			  *public interface RemoteTime extends Remote{
			  *		public String getTime() throws RemoteException;
			  *}
			  *
			  *HelloImpl.java : The Hello server implementation
			  *
			  *public class RemoteTimeImpl extends UnicastRemoteObject implements RemoteTime{
			  *		return ("Hello, the time is " + new java.util.Date() );
			  *}
			  * */
			 
			 String response_msg = (String) ctx.lookup( "cn=test") ;
			 System.out.println( response_msg) ;
		 } catch (NamingException e) {
			 e.printStackTrace() ; 
		 }	
	}

}
