class State
{
	private String all_states [] = {"login", "register", "profile", "leader_board", "play_game", "error"};
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
			System.out.println("Change state from " + current_state + " to " + s);
			this.current_state = s;
		}else{
			System.out.println("Change state fail");
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
		
	}
}