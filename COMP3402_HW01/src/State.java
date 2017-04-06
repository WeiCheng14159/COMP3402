class State
{
<<<<<<< HEAD
	private String all_states [] = {"login", "register", "profile", "leader_board", "play_game", "info"};
=======
	private String all_states [] = {"login", "register", "profile", "leader_board", "play_game", "error"};
>>>>>>> d182c7046bcfdf0db09a333f92d4fb2daedda699
	private String current_state = all_states[0];
	
	public State(){};
	
	/*
	 * change current state
	 */
	public void set_state(String s){
		boolean tmp = false;
		for ( String ss : this.all_states ) {
			if ( ss == s ) 
				tmp = true;
		}
		if (tmp){
<<<<<<< HEAD
			System.out.println("Set state from " + current_state + " to " + s);
			this.current_state = s;
		}else{
			System.out.println("Set state fail");
=======
			System.out.println("Change state from " + current_state + " to " + s);
			this.current_state = s;
		}else{
			System.out.println("Change state fail");
>>>>>>> d182c7046bcfdf0db09a333f92d4fb2daedda699
		}
	}
	
	/*
	 * get current state
	 */
	public String get_state(){
		return current_state;
	}
	
	/*
	 * get all possible states
	 */
	public String[] all_states(){
		return all_states;
	}
	
	/*
	 * change state
	 */
	public void change_state(String dest){
<<<<<<< HEAD
		//login state -> profile state/register state/info state
		if( current_state == all_states[0] && dest != all_states[1] && dest != all_states[2] && dest != all_states[5] ){
			System.out.println("Invalid destion state (from login state): " +  dest );
			return;
		}
		//register state -> login state/profile state/info state
		if( current_state == all_states[1] && dest != all_states[0] && dest != all_states[2] && dest != all_states[5] ){
			System.out.println("Invalid destion state (from register state): " +  dest );
			return;
		}
		//profile state -> ...
		if (current_state == all_states[2] && dest != all_states[0] && dest != all_states[2] && dest != all_states[3] && dest != all_states[4]){
			System.out.println("Invalid destion state (from profile state): " + dest);
			return;
		}
		//leader board state -> 
		if (current_state == all_states[3] && dest != all_states[0] && dest != all_states[2] && dest != all_states[3] && dest != all_states[4]){
			System.out.println("Invalid destion state (from leader_board state): " + dest);
			return;
		}
		//play game state -> 
		if (current_state == all_states[4] && dest != all_states[0] && dest != all_states[2] && dest != all_states[3] && dest != all_states[4]){
			System.out.println("Invalid destion state (from play_game state): " + dest);
			return;
		}
		// info state -> 
		if(current_state == all_states[5] && dest != all_states[0] && dest != all_states[1]){
			System.out.println("Invalid destion state (from info state): " + dest);
			return;
		}
		
		System.out.println("Change state from " + current_state + " to " + dest);
		current_state = dest;
		
=======
		//login/register state -> profile state
		if( (current_state == all_states[0] || current_state == all_states[1]) && dest != all_states[2]){
			System.out.println("Invalid destion state " +  dest );
			return;
		}else{
			System.out.println("Change state from " + current_state + " to " + dest);
			current_state = all_states[2];
		}
		//profile state -> ...
		if (current_state == all_states[2] && dest != all_states[3] && dest != all_states[4] && dest != all_states[5]){
			System.out.println("Invalid destion state");
			return;
		}else{
			System.out.println("Change state from " + current_state + " to " + dest);
			current_state = dest;
		}
		
>>>>>>> d182c7046bcfdf0db09a333f92d4fb2daedda699
	}
}