
public class Message implements java.io.Serializable{
	
	private String user; 
	private String object;
	private String content;
	
	public Message(String name, String content) {
		// TODO Auto-generated constructor stub
		this.user = name;
		this.object = super.toString();
		this.content = content;
	}
	
	public String toString(){
		  return "user:" + user + "/object:"+ object + "/content:"+ content; 
	}
	
}
