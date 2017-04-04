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

public class JNDIExample_exercise_1 {

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
			 
			 Message msg = new Message( "Tester", "hello world");
			 
			 /*Store the message object to the LDAP server*/
			 ctx.rebind( "cn=test", msg) ;
			 
			 /*Fetch the message object from the LDAP server*/
			 Message res_msg = (Message) ctx.lookup( "cn=test") ;
			 
			 String res_str = res_msg.toString();  
			 String ini_str = msg.toString();
			 
			 System.out.println( "Initial Message:  " + ini_str + "\n" + "Response Message: " + res_str + "\n");
			 
			 if ( res_str.equals(ini_str) ){
				 System.out.println( "Message Object Match !" );
			 }else{
				 System.out.println( "Message Object Mismatch !" );
			 }
			 
		 } catch (NamingException e) {
			 e.printStackTrace() ; 
		 }	
	}
}
