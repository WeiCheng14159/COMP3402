import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoMessage {
	String name;
	int win_game = 0;
	int played_game = 0;
	float win_rate = 0f;
	double avg_time = 0.f;
	int rank = 0;
	
	public PlayerInfoMessage(){
		this.name = "N/a";
	}
	
	public PlayerInfoMessage(String _name, int _win, int _played, float _win_rate, double _time, int _rank) {
		this.name = _name; 
		this.win_game = _win;
		this.played_game = _played;
		this.win_rate = _win_rate;
		this.avg_time = _time;
		this.rank = _rank;
	}
	
	public String to_profile(){
		String s = "<html>";
		s += String.format( " User Name       : <b>%s</b><br>" , this.name);
		s += String.format( " Number of games : %d    <br>", this.played_game);
		s += String.format( " Number of wins  : %d    <br>", this.win_game);
		s += String.format( " Win rate		  : %3.2f <br>", this.win_rate );
		s += String.format( " Rank#           : %d    <br>", this.rank);
		s += "</html>";
			return s;
	}
	
	public String to_board(){
		String s = "<tr>";
		s += String.format("<td> %s </td>", this.name);
		s += String.format("<td> %d </td>", this.played_game);
		s += String.format("<td> %d </td>", this.win_game);
		s += String.format("<td> %3.2f </td>", this.win_rate);
		s += String.format("<td> %d </td>", this.rank);
		s += "</tr>";
		return s;
	}

	public String toString(){
		return this.name + "_Played Game " + this.played_game + " Wins " + this.win_game + 
				" Win rate " + this.win_rate + " Rank " + this.rank;
	}
}
