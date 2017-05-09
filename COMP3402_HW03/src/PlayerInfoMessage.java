
public class PlayerInfoMessage {
	String name;
	int win_game = 0;
	int played_game = 0;
	float win_rate = 0f;
	
	public PlayerInfoMessage(String _name ) {
		this.name = _name; 
	}
	
	public String to_String(){
		return name + "_(" + win_game + "/"+this.played_game + ")_" + this.win_rate; 
	}

}
